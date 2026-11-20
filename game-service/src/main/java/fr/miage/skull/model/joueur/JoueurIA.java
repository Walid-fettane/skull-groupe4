package fr.miage.skull.model.joueur;

import fr.miage.skull.CoupDTO;

public class JoueurIA extends AbstractJoueur {

    public JoueurIA(int id, String username) {
        super(id, username);
    }

    public CoupDTO genererCoup(){
        return null;
    }
}