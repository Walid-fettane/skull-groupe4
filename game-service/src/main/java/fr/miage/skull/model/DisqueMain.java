package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

import java.util.List;
import java.util.Random;

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

    public void enleverDisqueAleatoirement(Random random) {
        if (disques.isEmpty()) {
            return; //N'a pas de disque
        }
        int index = random.nextInt(disques.size());
        disques.remove(index);
    }

    public int getNbDisque(){
        return this.disques.size();
    }
}