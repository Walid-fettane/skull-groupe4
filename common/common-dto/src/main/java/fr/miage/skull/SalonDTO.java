package fr.miage.skull;

public class SalonDTO {
    private Long idSalon;
    private Long idCreateur;
    private long nbMaxJoueur;
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
