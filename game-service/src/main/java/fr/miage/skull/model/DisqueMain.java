package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class DisqueMain {

    private final List<Disque> disques = new ArrayList<>();

    public void ajouterDisque(Disque unDisque) {
        disques.add(unDisque);
    }

    public void supprimerDisque(DisqueType disqueType) {
        disques.stream()
                .filter(unDisque -> unDisque.getType() == disqueType)
                .findFirst()
                .ifPresent(disques::remove);
    }

    public boolean possedeType(DisqueType disqueType) {
        return disques.stream().anyMatch(x -> x.getType().equals(disqueType));
    }

    public boolean estVide() {
        return disques.isEmpty();
    }

    public void enleverDisqueAleatoirement(Random random) {
        if (disques.isEmpty()) {
            return; //N'a pas de disque
        }
        int index = random.nextInt(disques.size());
        disques.remove(index);
    }

    public List<Disque> getDisques() {
        return disques;
    }

    public int getNbDisque(){
        return this.disques.size();
    }
}