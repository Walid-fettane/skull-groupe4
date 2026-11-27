package fr.miage.skull.model.joueur;

import fr.miage.skull.DisqueDTO;
import fr.miage.skull.DisqueType;
import fr.miage.skull.model.DisqueMain;
import fr.miage.skull.model.DisquePile;

import java.util.List;
import java.util.Random;

public interface IJoueur {

    void initialiserDisques();

    int getId();

    String getUsername();

    boolean estElimine();

    boolean estIA();

    DisqueMain getDisqueMain();

    DisquePile getDisquePile();

    int getNbVictoire();

    Random getRand();

    void resetElimination();

    void recupererDisque();

    int getNbDisquePile();

    boolean revelerTouteSaPile();

    DisqueType revelerDisque();

    int getNbDisquesMain();

    long getskinId();

    List<DisqueDTO> getDisquesDTO();

    long getNbDisquesPilePasRevele();

    void supprimerDisque(DisqueType type);

    void setElimine(boolean elimine);

    void ajouterVictoire();

    boolean possedeDisqueType(DisqueType disqueType);

    void placerDisque(DisqueType disqueType);
}