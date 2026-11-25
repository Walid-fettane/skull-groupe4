package fr.miage.skull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SalonDTO {
    private Long idSalon;
    @NotNull
    private Long idCreateur;
    @Min(3) @Max(6)
    private long nbMaxJoueur;
    @Min(0)
    private long nbAi;
    private String status;

    public SalonDTO(Long idSalon, Long idCreateur, long nbMaxJoueur, long nbAi, String status) {
        this.idSalon = idSalon;
        this.idCreateur = idCreateur;
        this.nbMaxJoueur = nbMaxJoueur;
        this.nbAi = nbAi;
        this.status = status;
    }

    public SalonDTO(){}

    public Long getIdSalon() {
        return idSalon;
    }
    public Long getIdCreateur() {
        return idCreateur;
    }
    public long getNbMaxJoueur() {
        return nbMaxJoueur;
    }
    public long getNbAi() {
        return nbAi;
    }
    public String getStatus() {
        return status;
    }


}
