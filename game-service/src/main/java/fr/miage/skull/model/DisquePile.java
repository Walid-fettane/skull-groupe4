package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

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

    public DisqueType revelerPremierDisque() {
        nbDisque--;
        return disques.remove(nbDisque).getType();
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

    public Map<Integer, Disque> getDisques(){
        return this.disques;
    }

    public boolean revelerTouteLaPile(){
        boolean hasSkull = false;
        for(Disque unDisque : this.disques.values()){
            unDisque.setRevele(true);
            if(unDisque.getType()==DisqueType.SKULL){
                hasSkull = true;
            }
        }
        return hasSkull;
    }
}