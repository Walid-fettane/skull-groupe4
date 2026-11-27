package fr.miage.skull.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Salon")
public class Salon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSalon;
    private Long idCreateur;
    private long nbMaxJoueur;
    private long nbAi;
    private String status;
    private Long idGame;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "salon_joueurs", joinColumns = @JoinColumn(name = "id_salon"))
    @Column(name = "id_joueur")
    private Set<Long> joueurIds = new HashSet<>();

    public Salon() {}

    public Salon(Long idCreateur, long nbMaxJoueur, long nbAi, String status) {
        this.idCreateur = idCreateur;
        this.nbMaxJoueur = nbMaxJoueur;
        this.nbAi = nbAi;
        this.status = status;
    }

    public Long getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(Long idSalon) {
        this.idSalon = idSalon;
    }

    public Long getIdCreateur() {
        return idCreateur;
    }

    public void setIdCreateur(Long idCreateur) {
        this.idCreateur = idCreateur;
    }

    public long getNbMaxJoueur() {
        return nbMaxJoueur;
    }

    public void setNbMaxJoueur(long nbMaxJoueur) {
        this.nbMaxJoueur = nbMaxJoueur;
    }

    public long getNbAi() {
        return nbAi;
    }

    public void setNbAi(long nbAi) {
        this.nbAi = nbAi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getIdGame() {
        return idGame;
    }

    public void setIdGame(Long idGame) {
        this.idGame = idGame;
    }
    public Set<Long> getJoueurIds() { return joueurIds; }
    public boolean estPlein() {
        return joueurIds.size() + nbAi >= nbMaxJoueur;
    }
}