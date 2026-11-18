package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

import java.util.List;

public class DisqueMain {

    private List<Disque> disques;

    public void ajouterDisque(Disque unDisque) {
    }

    public void supprimerDisque(DisqueType disqueType) {
    }

    public boolean possedeType(DisqueType disqueType) {
        return disques.stream().anyMatch(x -> x.getType().equals(disqueType));
    }

    public boolean estVide() {
        return false;
    }

    public void enleverDisqueAleatoirement() {
    }

    public int getNbDisque(){
        return this.disques.size();
    }
}