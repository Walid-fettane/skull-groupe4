package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

import java.util.Random;

public class AbstractJoueur implements IJoueur {

    private int id;
    private String username;
    private boolean elimine;
    private DisqueMain disqueMain;
    private DisquePile disquePile;
    private int nbVictoire;
    private Random rand;

    protected AbstractJoueur() {
        this.rand = new Random();
    }

    protected AbstractJoueur(int id, String username) {
        this();
        this.id = id;
        this.username = username;
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

    public void perteAleatoireDisque() {
        this.disqueMain.enleverDisqueAleatoirement(this.rand);
        if(this.disqueMain.estVide()){
            this.elimine = true;
        }
    }

    @Override
    public DisqueType revelerDisque(){
        return this.disquePile.revelerPremierDisque();
    }

    @Override
    public void recupererDisque() {
        for(Disque unDisque : this.disquePile.getDisques().values()){
            this.disqueMain.ajouterDisque(unDisque);
        }
        this.disquePile.reinitialiserPile();
    }

    public int getNbDisqueMain() {
        return this.disqueMain.getNbDisque();
    }

    @Override
    public int getNbDisquePile() {
        return this.disquePile.getNbDisque();
    }

    public void placerDisque(DisqueType disqueType) {
        this.disquePile.ajouterDisque(new Disque(disqueType));
    }

    @Override
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

    @Override
    public void resetElimination(){
        this.elimine = false;
    }

    @Override
    public boolean revelerTouteSaPile(){
        return this.disquePile.revelerTouteLaPile();
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean estElimine(){
        return this.elimine;
    }

    @Override
    public DisqueMain getDisqueMain() {
        return this.disqueMain;
    }

    @Override
    public DisquePile getDisquePile() {
        return this.disquePile;
    }

    @Override
    public int getNbVictoire(){
        return this.nbVictoire;
    }

    @Override
    public Random getRand() {
        return this.rand;
    }

}
