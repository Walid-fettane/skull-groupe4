package fr.miage.skull.model;

import java.util.HashMap;
import java.util.Map;

public class DisquePile {

    private Map<Integer, Disque> disques;
    private int nbDisque;

    public DisquePile() {
        this.disques = new HashMap<>();
        this.nbDisque = 0;
    }

    public void ajouterDisque(Disque unDisque) {
        disques.put(nbDisque++, unDisque);
    }

    public Disque revelerPremierDisque() {
        nbDisque--;
        return disques.remove(nbDisque);
    }

    public void reinitialiserPile() {
        disques.clear();
        nbDisque = 0;
    }

    public boolean estVide() {
        return nbDisque == 0;
    }

    public int getNbDisque() {
        return nbDisque;
    }
}