package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

public class Disque {

    private final DisqueType type;
    private boolean revele;


    public Disque(DisqueType type){
        this.type = type;
    }

    public DisqueType getType() {
        return type;
    }

    public boolean isRevele() {
        return revele;
    }

    public void setRevele(boolean revele) {
        this.revele = revele;
    }
}