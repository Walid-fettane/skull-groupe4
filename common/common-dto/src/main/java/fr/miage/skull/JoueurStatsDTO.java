package fr.miage.skull;

public class JoueurStatsDTO {
    private long idJoueur;
    private long nbVictoires;
    private long nbPartieJouees;
    private String email;
    private double ratioVictoires;

    public JoueurStatsDTO(Long idj , long nbVictoires, long nbPartieJouees, String email) {
        this.idJoueur = idj;
        this.nbVictoires = nbVictoires;
        this.nbPartieJouees = nbPartieJouees;
        this.email = email;
        this.ratioVictoires = nbPartieJouees > 0 ? (double) nbVictoires / nbPartieJouees : 0.0;
    }

    public long getIdJoueur() {
        return idJoueur;
    }

    public long getNbVictoires() {
        return nbVictoires;
    }

    public long getNbPartieJouees() {
        return nbPartieJouees;
    }

    public String getEmail() {
        return email;
    }

    public double getRatioVictoires() {
        return ratioVictoires;
    }

}
