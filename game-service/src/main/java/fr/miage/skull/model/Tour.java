package fr.miage.skull.model;


import fr.miage.skull.CoupDTO;
import fr.miage.skull.CoupType;
import fr.miage.skull.DisqueType;
import fr.miage.skull.Phase;
import fr.miage.skull.exception.*;

import java.util.Objects;

public class Tour {

    private long tourId;
    private int idJoueurCourrant;
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


    public void jouerCoup(int playerId, CoupDTO coup){
        valideCoup(playerId, coup);
        switch(coup.coupType()){
            case CoupType.PLACER_DISQUE -> placerDisque(playerId, coup.disqueType());
            case CoupType.ENCHERIR -> encherir(playerId, coup.encherirNb());
            case CoupType.REVELER -> reveler(playerId, coup.revelerPlayerId());
        }
    }

    //Avant cela, on aura déjà verifier que c'est bien à ce joueur de jouer..
    public void placerDisque(int playerId, DisqueType disqueType) {
        if(this.phase != Phase.PLACEMENT){
            throw new RegleMetierException();
        }

        Joueur joueur = gameAssocTour.getJoueurParId(playerId);

        if(!joueur.possedeDisqueType(disqueType)){
            throw new DisqueIndisponibleException();
        }

        joueur.placerDisque(disqueType);
    }

    public void encherir(int playerId, int enchereNb) {
        if (!peutEncherir(playerId)) {
            throw new CoupNonAutoriseException();
        }
        if (this.phase == Phase.PLACEMENT) {
            initEnchere();
        }

        this.enchere.encherir(playerId, enchereNb);
    }

    public boolean peutEncherir(int playerId){
        Joueur joueur = this.gameAssocTour.getJoueurParId(playerId);
        //Si c'est le tour du joueur et qu'il à déjà un disque dans sa pile, alors il peut enchérir sinon non
        return joueur.getNbDisquePile() >= 1;
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
    }

    // On valide que le coup qu'il joue est valide avec le status de la partie
    public void valideCoup(int playerId, CoupDTO coup) {
        boolean valide = switch (coup.coupType()) {
            case PLACER_DISQUE -> this.phase == Phase.PLACEMENT;
            case REVELER -> this.phase == Phase.REVELATION;
            //On fera d'avantage de check plus tard pour encherir..
            case ENCHERIR -> this.phase == Phase.PLACEMENT || this.phase == Phase.ENCHERE;

        };

        if (!valide) {
            throw new CoupNonAutoriseException();
        }
    }

    public void initEnchere(){
        this.enchere = new Enchere(this.enchereId++);
        this.phase = Phase.ENCHERE;
    }

    public void initChallenge(int playerId, int challengeNb, int challengeId){
        this.challenge = new Challenge(playerId, challengeNb, challengeId++);
        this.phase = Phase.CHALLENGE;
    }
}