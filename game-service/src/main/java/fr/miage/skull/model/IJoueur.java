package fr.miage.skull.model;

import fr.miage.skull.DisqueType;

import java.util.Random;

public interface IJoueur {

    void initialiserDisques();

    int getId();

    String getUsername();

    boolean estElimine();

    DisqueMain getDisqueMain();

    DisquePile getDisquePile();

    int getNbVictoire();

    Random getRand();

    void resetElimination();

    void recupererDisque();

    int getNbDisquePile();

    boolean revelerTouteSaPile();

    DisqueType revelerDisque();
}