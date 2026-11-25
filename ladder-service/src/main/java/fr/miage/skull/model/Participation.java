package fr.miage.skull.model;

import jakarta.persistence.*;

@Entity
@Table(name = "historique_participation")
public class Participation {
    @EmbeddedId
    private ParticipationId id = new ParticipationId();

    @MapsId("idPartie")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idPartie")
    private Partie partie;

    @MapsId("idJoueur")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "idJoueur")
    private Joueur joueur;
    private int score;
    @Column(name = "est_gagnant")
    private boolean vainqueur;

    public Participation() {}

    public ParticipationId getId() {
        return id;
    }

    public void setId(ParticipationId id) {
        this.id = id;
    }

    public Partie getPartie() {
        return partie;
    }

    public void setPartie(Partie partie) {
        this.partie = partie;
    }

    public Joueur getJoueur() {
        return joueur;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isVainqueur() {
        return vainqueur;
    }

    public void setVainqueur(boolean vainqueur) {
        this.vainqueur = vainqueur;
    }
}
