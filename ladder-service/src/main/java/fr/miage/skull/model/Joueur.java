package fr.miage.skull.model;

import jakarta.persistence.*;

@Entity
@Table(name="JOUEUR")
public class Joueur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJoueur;
    @Column(nullable=false, unique=true)
    private String email;
    @Column(nullable=false)
    private int nbVictoires;
    @Column(nullable=false)
    private int nbParties;

    public Joueur() {}

    public Joueur( String email, int nbVictoires, int nbParties) {
        this.email = email;
        this.nbVictoires = nbVictoires;
        this.nbParties = nbParties;
    }

    public Long getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(Long idJoueur) {
        this.idJoueur = idJoueur;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNbVictoires() {
        return nbVictoires;
    }

    public void setNbVictoires(int nbVictoires) {
        this.nbVictoires = nbVictoires;
    }

    public int getNbParties() {
        return nbParties;
    }

    public void setNbParties(int nbParties) {
        this.nbParties = nbParties;
    }
}
