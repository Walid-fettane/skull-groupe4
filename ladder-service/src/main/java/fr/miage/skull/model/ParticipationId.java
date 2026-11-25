package fr.miage.skull.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ParticipationId implements Serializable {
    private Long idJoueur;
    private Long idPartie;

    public ParticipationId() {}
    public ParticipationId(Long idJoueur, Long idPartie) {
        this.idJoueur = idJoueur;
        this.idPartie = idPartie;
    }

    public Long getIdJoueur() {
        return idJoueur;
    }

    public void setIdJoueur(Long idJoueur) {
        this.idJoueur = idJoueur;
    }

    public Long getIdPartie() {
        return idPartie;
    }

    public void setIdPartie(Long idPartie) {
        this.idPartie = idPartie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParticipationId that)) return false;
        return Objects.equals(idJoueur, that.idJoueur) && Objects.equals(idPartie, that.idPartie);
    }

    @Override
    public int hashCode() { return Objects.hash(idJoueur, idPartie); }
}