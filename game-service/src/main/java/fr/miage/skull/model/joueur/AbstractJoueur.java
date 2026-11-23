package fr.miage.skull.model.joueur;

import fr.miage.skull.DisqueDTO;
import fr.miage.skull.DisqueType;
import fr.miage.skull.model.Disque;
import fr.miage.skull.model.DisqueMain;
import fr.miage.skull.model.DisquePile;

import java.util.*;

public class AbstractJoueur implements IJoueur {

    private int id;
    private String username;
    private boolean elimine;
    private DisqueMain disqueMain;
    private DisquePile disquePile;
    private int nbVictoire;
    private Random rand;
    private long skinId;

    protected AbstractJoueur() {
        this.rand = new Random();
        this.distribuerDisquesInitiaux();
    }

    protected AbstractJoueur(int id, String username) {
        this();
        this.id = id;
        this.username = username;
    }

    protected AbstractJoueur(int id, String username, long skinId) {
        this(id, username);
        this.skinId = skinId;
    }

    @Override
    public boolean possedeDisqueType(DisqueType type) {
        return disqueMain.possedeType(type);
    }

    //Si le joueur n'a plus de disque, et que c'est à son tour de jouer, il doit pariter
    public boolean doitParier() {
        return this.disqueMain.estVide();
    }

    @Override
    public void ajouterVictoire() {
        this.nbVictoire++;
    }

    @Override
    public DisqueType revelerDisque(){
        return this.disquePile.revelerPremierDisque();
    }

    @Override
    public int getNbDisquesMain() {
        return this.disqueMain.getNbDisque();
    }

    @Override
    public long getskinId() {
        return this.skinId;
    }

    @Override
    public List<DisqueDTO> getDisquesDTO() {
        List<DisqueDTO> dto = new ArrayList<>();
        List<Disque> disques = getDisquePile().getDisques().values().stream().toList();
        for(int unePoseDisque : getDisquePile().getDisques().keySet()){
            Disque disque = disques.get(unePoseDisque);
            //On regarde si un disque est révélé, si oui, on peut mettre le type, si non, on le met en null pour éviter la triche
            dto.add(new DisqueDTO(unePoseDisque, disque.estRevele() ? disque.getType() : null, disque.estRevele()));
        }

        return dto;
    }

    @Override
    public long getNbDisquesPilePasRevele() {
        return this.disquePile.getNbDisquePasRevele();
    }

    @Override
    public void supprimerDisque(DisqueType type) {
        this.disqueMain.supprimerDisque(type);
        if(this.disqueMain.estVide()&&this.disquePile.estVide()){
            this.setElimine(true);
        }
    }

    @Override
    public void setElimine(boolean elimine) {
        this.elimine = elimine;
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

    @Override
    public void placerDisque(DisqueType disqueType) {
        this.disqueMain.supprimerDisque(disqueType);
        this.disquePile.ajouterDisque(new Disque(disqueType));
    }

    @Override
    public void initialiserDisques() {
        this.distribuerDisquesInitiaux();
    }

    private void distribuerDisquesInitiaux() {
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
