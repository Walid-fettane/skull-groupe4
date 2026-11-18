package fr.miage.skull.model;


import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.GameStatus;
import fr.miage.skull.Phase;
import fr.miage.skull.exception.DebutDeLaPartieImpossibleException;
import fr.miage.skull.exception.JoueurNonDansPartieException;
import fr.miage.skull.exception.PasLeTourDuJoueurException;

import java.util.List;
import java.util.Objects;

public class Game {

    private Long id;
    private GameStatus status;
    private Phase statusCoup;
    //TODO : penser à voir quand incrémenter le coupNumero
    private long coupNumero;
    private List<Joueur> listeJoueurs;
    //TODO : penser à voir quand changer idJoueurCourrant
    private Integer idJoueurCourant;
    private Tour tour;
    private Long tourId;
    private List<Signalement> signalements;

    public void commencerJeu() throws DebutDeLaPartieImpossibleException{
        this.initJoueursStatus();
        this.validerDebutPartie();
        this.status = GameStatus.EN_COURS;
        this.coupNumero = 0;
        this.idJoueurCourant = listeJoueurs.getFirst().getId();
        this.initTour();
    }

    public void jouerCoup(CoupDTO coup, int playerId){
        valideJoueur(playerId);
        if(tour != null){
            tour.jouerCoup(playerId, coup);
        }
    }

    //On valide que le joueur est dans la partie et que c'est à lui de jouer
    public void valideJoueur(int playerId){
        Joueur joueur = getJoueurParId(playerId);
        if(joueur == null){
            throw new JoueurNonDansPartieException();
        }

        if(!(Objects.equals(getIdJoueurCourant(), playerId))){
            throw new PasLeTourDuJoueurException();
        }
    }

    public void validerDebutPartie() throws DebutDeLaPartieImpossibleException {
        int nbParticipants = this.getListeJoueurs().size();
        if(nbParticipants < 3 || nbParticipants > 6) {
            throw new DebutDeLaPartieImpossibleException();
        }

        //Si le status n'est pas EN ATTENTE DE JOUEURS alors ERREUR
        if(!(this.status == GameStatus.EN_ATTENTE_DE_JOUEURS)){
            throw new DebutDeLaPartieImpossibleException();
        }
    }

    public void initJoueursStatus(){
        for(Joueur unJoueur : this.listeJoueurs){
            unJoueur.initialiserDisques();
        }
    }

    public void initTour(){
        tour = new Tour(0L, this);
    }

    public void finirJeu() {
    }

    public void appliquerCoup() {
    }

    public List<Joueur> getJoueursActif() {
        return null;
    }

    public GameStateDTO getEtatPourJoueurId(Integer playerId) {
        return null;
    }

    public void setGagnant(Integer playerId) {
    }

    public boolean estJoueur(Integer playerId) {
        return false;
    }

    public Joueur getJoueurParId(Integer playerId) {
        return null;
    }

    public void ajouterIA() {
    }

    private boolean valideCoup(CoupDTO unCoup) {
        return false;
    }

    private void finTour() {
    }

    private void commencerNouveauTour() {
    }

    private void prochainJoueur() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Signalement> getSignalements() {
        return signalements;
    }

    public void setSignalements(List<Signalement> signalements) {
        this.signalements = signalements;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public Integer getIdJoueurCourant() {
        return idJoueurCourant;
    }

    public void setIdJoueurCourant(Integer idJoueurCourant) {
        this.idJoueurCourant = idJoueurCourant;
    }

    public long getCoupNumero() {
        return coupNumero;
    }

    public void setCoupNumero(long coupNumero) {
        this.coupNumero = coupNumero;
    }

    public List<Joueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public void setListeJoueurs(List<Joueur> listeJoueurs) {
        this.listeJoueurs = listeJoueurs;
    }

    public Phase getStatusCoup() {
        return statusCoup;
    }

    public void setStatusCoup(Phase statusCoup) {
        this.statusCoup = statusCoup;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}