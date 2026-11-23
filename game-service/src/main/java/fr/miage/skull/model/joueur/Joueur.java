package fr.miage.skull.model.joueur;

public class Joueur extends AbstractJoueur {

    private long skinId;

    public Joueur(int id, String username, long skinId) {
        super(id, username, skinId);
    }

    public Joueur(){
        super();
    }

}