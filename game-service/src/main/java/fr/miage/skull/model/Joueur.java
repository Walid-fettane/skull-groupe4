package fr.miage.skull.model;


import fr.miage.skull.DisqueType;

public class Joueur {

    private Integer id;
    private String username;
    private Long skinId;
    private boolean elimine;
    private DisqueMain disqueMain;
    private DisquePile disquePile;
    //private AIStrategyType aiStrategyType;
    private int nbVictoire;

    public boolean possedeDisqueType(DisqueType type) {
        return disqueMain.possedeType(type);
    }

    public boolean doitParier() {
        return false;
    }

    public void ajouterVictoire() {
    }

    public void perteAleatoireDisque() {
    }

    public void recupererDisque() {
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
        disqueMain = new DisqueMain();
        disquePile = new DisquePile();

        //On ajoute 3 fleurs, 1 skull
        for (int i = 0; i < 3; i++) {
            disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        }
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));
    }

    public int getId(){
        return this.id;
    }

}