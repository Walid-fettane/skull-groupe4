package fr.miage.skull.model;


import fr.miage.skull.CoupDTO;
import fr.miage.skull.CoupType;
import fr.miage.skull.DisqueType;
import fr.miage.skull.Phase;
import fr.miage.skull.exception.*;
import fr.miage.skull.model.joueur.IJoueur;

import java.util.Map;

public class Tour {

    private long tourId;
    private Phase phase;
    private Enchere enchere;
    private int enchereId=0;
    private Challenge challenge;
    private int challengeId = 0;
    private Game gameAssocTour;

    public Tour(long tourId, Game game){
        this.tourId = tourId;
        this.phase = Phase.PLACEMENT;
        this.enchere = null;
        this.challenge = null;
        this.gameAssocTour = game;
    }


    public void jouerCoup(int playerId, CoupDTO coup) {
        valideCoup(playerId, coup);

        switch(coup.coupType()){
            case CoupType.PLACER_DISQUE -> placerDisque(playerId, coup.disqueType());
            case CoupType.ENCHERIR -> encherir(playerId, coup.encherirNb());
            case CoupType.ENCHERE_PASSE -> encherePasse(playerId);
            case CoupType.REVELER -> reveler(playerId, coup.revelerPlayerId());
            case CoupType.CHOIX_DISQUE_A_ENLEVER -> enleverDisque(playerId, coup.slotDisque());
        }
    }

    //Avant cela, on aura déjà verifier que c'est bien à ce joueur de jouer..
    public void placerDisque(int playerId, DisqueType disqueType) {
        IJoueur joueur = gameAssocTour.getJoueurParId(playerId);
        if(!joueur.possedeDisqueType(disqueType)){
            throw new DisqueIndisponibleException();
        }

        joueur.placerDisque(disqueType);
        this.prochainJoueur();
    }

    public void enleverDisque(int playerId, int slot){
        IJoueur joueur = this.gameAssocTour.getJoueurParId(playerId);
        Disque disque = this.gameAssocTour.getdFinChallenge().getDisquePosition().get(slot);
        if(disque == null){
            throw new DisqueIndisponibleException();
        }
        joueur.supprimerDisque(disque.getType());
        this.gameAssocTour.finSuppressionDisque();
    }

    public void encherir(int playerId, int enchereNb) {
        if (!peutEncherir(playerId, enchereNb)) {
            throw new CoupNonAutoriseException();
        }
        if (this.phase == Phase.PLACEMENT) {
            initEnchere();
        }

        this.enchere.encherir(playerId, enchereNb);
        this.prochainJoueur();
    }

    public boolean peutEncherir(int playerId, int enchereNb){
        IJoueur joueur = this.gameAssocTour.getJoueurParId(playerId);
        int maxEnchere = 0;
        for(IJoueur unJoueur : this.gameAssocTour.getListeJoueurs()){
            maxEnchere+=unJoueur.getNbDisquePile();
        }
        //Si c'est le tour du joueur et qu'il à déjà un disque dans sa pile, alors il peut enchérir sinon non
        //On verifie aussi que l'enchère est égale ou en dessous du nombre de disque dans les piles de tous les joueurs cumulés
        return joueur.getNbDisquePile() >= 1 && enchereNb <= maxEnchere;
    }

    public void commencerChallenge(int playerId) {
        if(!peutCommencerChallenge(playerId)){
            throw new CoupNonAutoriseException();
        }

        initChallenge(playerId, this.enchere.getWinnerEnchereNB(), challengeId);
    }

    public boolean peutCommencerChallenge(int playerId) {
        return this.enchere.getWinnerId()==playerId && this.phase == Phase.ENCHERE;
    }

    public void reveler(int playerId, int playerTargetId) {
        //On verifi la phase en cours
        if(phase!=Phase.REVELATION){
            throw new CoupNonAutoriseException();
        }

        //Si le voulant réveler n'est pas le challenger --> Exception
        if(!(this.challenge.getPlayerChallengerId()==playerId)){
            throw new PasLeTourDuJoueurException();
        }

        //Si le joueur veut se révèler lui-même alors il révèle sa pile
        if(playerId==playerTargetId){
            this.challenge.revelerSaPile();
            return;
        }

        //Le joueur révèle un autre joueur
        this.challenge.revelerLePremierDisque(playerTargetId);
        this.prochainJoueur();
    }

    // On valide que le coup qu'il joue est valide avec le status de la partie
    public void valideCoup(int playerId, CoupDTO coup) throws CoupNonAutoriseException {
        boolean valide = switch (coup.coupType()) {
            case PLACER_DISQUE -> this.phase == Phase.PLACEMENT;
            //On ne met pas de check sur la phase Challenge, car elle s'active d'elle même
            //Les phases challenge et revelation sont un peu ambigue, on utilisera seulement REVELATION
            case REVELER -> this.phase == Phase.REVELATION;
            //On fera d'avantage de check plus tard pour encherir..
            case ENCHERIR -> this.phase == Phase.PLACEMENT || this.phase == Phase.ENCHERE;
            case ENCHERE_PASSE -> this.phase == Phase.ENCHERE;
            case CHOIX_DISQUE_A_ENLEVER -> this.phase == Phase.FIN_CHALLENGE;
        };

        if (!valide) {
            throw new CoupNonAutoriseException();
        }
    }

    public void initEnchere(){
        this.enchere = new Enchere(this.enchereId++, this);
        this.phase = Phase.ENCHERE;
    }

    public void initChallenge(int playerId, int challengeNb, int challengeId){
        this.challenge = new Challenge(playerId, challengeNb, challengeId++, this.gameAssocTour.getListeJoueurs(), this);
        this.phase = Phase.REVELATION;
    }

    public void resultatChallenge(boolean challengeGagne, int skullRetourneJoueurId, int challengerId){
        this.gameAssocTour.appliquerResultatChallenge(challengeGagne, skullRetourneJoueurId, challengerId);
    }

    public void encherePasse(int playerId){
        this.gameAssocTour.encherePasse(playerId);
        this.prochainJoueur();
    }

    public void prochainJoueur(){
        this.gameAssocTour.prochainJoueur();
    }

    public Phase getPhase(){
        return this.phase;
    }

    public void setPhase(Phase newPhase){this.phase=newPhase;}

    public int getEnchereEnCours(){
        return this.enchere == null ? 0 : this.enchere.getCurrentEnchere();
    }
}