package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import fr.miage.skull.exception.CoupNonAutoriseException;
import fr.miage.skull.exception.JoueurNonDansPartieException;

import java.util.ArrayList;
import java.util.List;

public class Challenge {

    private int playerChallengerId;
    private int challengeNb;
    private int fleursRevelesId;
    private int challengeId;
    private boolean aReveleSaPile;
    private List<Joueur> joueurs;
    private Tour tour;

    public Challenge(int playerChallengerId, int challengeNb, int challengeId, List<Joueur> joueurs, Tour tour){
        this.playerChallengerId = playerChallengerId;
        this.challengeNb = challengeNb;
        this.challengeId = challengeId;
        this.aReveleSaPile = false;
        this.fleursRevelesId = 0;
        this.joueurs  = joueurs;
        this.tour = tour;
    }

    public void revelerSaPile() {
        //On cherche le challenger avec son id dans la liste des joueurs
        Joueur challenger = null;
        for(Joueur joueur : this.joueurs){
            if(joueur.getId()==this.playerChallengerId){
                challenger = joueur;
            }
        }
        if(challenger!=null){
            //On révèle sa pile, on cherche à savoir si un skull se trouvait dans sa pile
            boolean hasSkull = challenger.revelerTouteSaPile();
            if(hasSkull){
                //Si le joueur à retourné un skull dans sa pile, alors il perd le challenge
                finChallenge(false, challenger.getId());
            } else {
                fleursRevelesId+=challenger.getNbDisquePile();
            }
        }


        this.aReveleSaPile=true;
    }

    //Le nom est un peu trompeur, cette méthode sert à réveler les disques des autres en général
    public void revelerLePremierDisque(int targetPlayerId) {
        //Si le challenger relève un disque d'un autre avant sa pile --> Exception
        if(!aReveleSaPile){
            throw new CoupNonAutoriseException();
        }

        Joueur joueur = this.joueurs.stream()
                .filter(x -> x.getId() == targetPlayerId)
                .findFirst()
                .orElseThrow(JoueurNonDansPartieException::new);

        DisqueType dt = joueur.revelerDisque();
        if(dt == DisqueType.SKULL){
            finChallenge(false, joueur.getId());
        } else {
            fleursRevelesId++;
        }

    }

    public void finChallenge(boolean challengeGagne, int playerSkullRetourneId) {
        tour.resultatChallenge(challengeGagne, playerSkullRetourneId, this.playerChallengerId);
    }

    public int getPlayerChallengerId(){
        return this.playerChallengerId;
    }
}