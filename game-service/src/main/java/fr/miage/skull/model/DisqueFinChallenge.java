package fr.miage.skull.model;

import java.util.*;

public class DisqueFinChallenge {

    private Map<Integer, Disque> disquePosition = new HashMap<>();
    //Le disque owner est celui qui possède les disques (celui qui voit leurs valeurs)
    private int disqueOwner;

    public DisqueFinChallenge(List<Disque> disques, int disqueOwner) {
        this.disqueOwner = disqueOwner;
        if (disques == null || disques.isEmpty()) return;

        // Copie pour ne pas modifier la liste d'origine
        List<Disque> melange = new ArrayList<>(disques);
        Collections.shuffle(melange);

        int nb = Math.min(melange.size(), 4); // 4 disques max
        for (int i = 0; i < nb; i++) {
            disquePosition.put(i + 1, melange.get(i)); // positions 1 à 4
        }
    }

    public int getDisqueOwner() {
        return disqueOwner;
    }

    public Map<Integer, Disque> getDisquePosition() {
        return Collections.unmodifiableMap(disquePosition);
    }




}
