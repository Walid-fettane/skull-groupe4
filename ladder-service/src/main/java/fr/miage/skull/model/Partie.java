package fr.miage.skull.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "PARTIE")
public class Partie{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPartie;
    @Column(name = "idSalon", nullable = false)
    private Long idSalon;
    private LocalDateTime dateFin;
    @OneToMany(mappedBy = "partie", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Participation> participations = new ArrayList<>();
    public Partie(){}

    public Long getIdPartie() {
        return idPartie;
    }

    public void setIdPartie(Long idPartie) {
        this.idPartie = idPartie;
    }

    public Long getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(Long idSalon) {
        this.idSalon = idSalon;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }
}



