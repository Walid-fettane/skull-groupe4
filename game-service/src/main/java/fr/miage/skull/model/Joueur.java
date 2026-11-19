package fr.miage.skull.model;


import fr.miage.skull.DisqueType;

import java.util.Random;

public class Joueur {

    private Integer id;
    private String username;
    private Long skinId;
    private boolean elimine;
    private DisqueMain disqueMain;
    private DisquePile disquePile;
    //private AIStrategyType aiStrategyType;
    private int nbVictoire;
    private Random rand;

    public Joueur(){
        this.rand = new Random();
    }

    public boolean possedeDisqueType(DisqueType type) {
        return disqueMain.possedeType(type);
    }

    //Si le joueur n'a plus de disque, et que c'est à son tour de jouer, il doit pariter
    public boolean doitParier() {
        return this.disqueMain.estVide();
    }


    public void ajouterVictoire() {
        this.nbVictoire++;
    }

    public int getNbVictoire(){
        return this.nbVictoire;
    }

    public void perteAleatoireDisque() {
        this.disqueMain.enleverDisqueAleatoirement(this.rand);
        if(this.disqueMain.estVide()){
            this.elimine = true;
        }
    }

    public DisqueType revelerDisque(){
        return this.disquePile.revelerPremierDisque();
    }

    public void recupererDisque() {
        for(Disque unDisque : this.disquePile.getDisques().values()){
            this.disqueMain.ajouterDisque(unDisque);
        }
        this.disquePile.reinitialiserPile();
    }

    public int getNbDisqueMain() {
        return this.disqueMain.getNbDisque();
    }
    public int getNbDisquePile() {
        return this.disquePile.getNbDisque();
    }

    public void placerDisque(DisqueType disqueType) {
        this.disquePile.ajouterDisque(new Disque(disqueType));
    }

    public void initialiserDisques() {
        //On créer une nouvelle Pile / disque en main donc vide
        this.disqueMain = new DisqueMain();
        this.disquePile = new DisquePile();

        //On ajoute 3 fleurs, 1 skull
        for (int i = 0; i < 3; i++) {
            this.disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        }
        this.disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));
    }

    public int getId(){
        return this.id;
    }

    public boolean estElimine(){
        return this.elimine;
    }

    public void resetElimination(){
        this.elimine = false;
    }

    public boolean revelerTouteSaPile(){
        return this.disquePile.revelerTouteLaPile();
    }

}