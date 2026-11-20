package fr.miage.skull.model;


import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.GameStatus;
import fr.miage.skull.Phase;
import fr.miage.skull.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {

    private long id;
    private GameStatus status;
    private Phase statusCoup;
    //coupNumero est un peu comme une version, ça servira à savoir pour le client
    //S'il y a une différence entre un Etat reçu ou un autre
    private long coupNumero;
    private List<IJoueur> listeJoueurs;
    private int idJoueurCourant;
    private Tour tour;
    private long tourId;
    private List<Signalement> signalements;
    private int indexJoueurCourant = 0;
    private int vainqueurId = -1;

    //tableau des ids des joueurs ayant passé l'enchère dans le tour courrant
    private List<Integer> playerIdEncherePasse;

    public void commencerJeu() throws DebutDeLaPartieImpossibleException {
        this.initJoueursStatus();
        this.validerDebutPartie();
        this.status = GameStatus.EN_COURS;
        this.coupNumero = 0;
        this.idJoueurCourant = listeJoueurs.getFirst().getId();
        this.initTour();
        this.playerIdEncherePasse = new ArrayList<>();
    }

    public void jouerCoup(CoupDTO coup, int playerId) throws JoueurElimineException {
        valideJoueur(playerId);
        if(tour != null){
            tour.jouerCoup(playerId, coup);
        }
    }

    //On valide que le joueur est dans la partie et que c'est à lui de jouer
    public void valideJoueur(int playerId) throws JoueurElimineException {
        //On valide que le joueur n'est pas éliminé
        validerJoueurPasElimine(playerId);

        //Qu'il est bien dans la partie
        Joueur joueur = getJoueurParId(playerId);
        if(joueur == null){
            throw new JoueurNonDansPartieException();
        }

        //Que c'est bien à lui de jouer
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

    public void validerJoueurPasElimine(int playerId) throws JoueurElimineException {
        if(getJoueurParId(playerId).estElimine()){
            throw new JoueurElimineException();
        }
    }

    public void initJoueursStatus(){
        for(IJoueur unJoueur : this.listeJoueurs){
            unJoueur.initialiserDisques();
        }
    }

    public void initTour(){
        tour = new Tour(0L, this);
    }

    private void commencerNouveauTour() {
        //On met tour à null pour éviter les coup de s'éxecuter ou autre problèmes..
        this.tour = null;
        this.playerIdEncherePasse = null;
        for(IJoueur unJoueur : this.listeJoueurs){
            unJoueur.resetElimination();
            unJoueur.recupererDisque();
        }
        this.tour = new Tour(++this.tourId, this);
        this.playerIdEncherePasse = new ArrayList<>();
    }

    //@param skullRetourneJoueurId = Le joueur à qui la skull s'est fait retourné si le challenge est perdu..
    public void appliquerResultatChallenge(boolean challengeGagne, int skullRetourneJoueurId, int challengerId){
        if(challengeGagne){
            Joueur joueur = getJoueurParId(challengerId);
            joueur.ajouterVictoire();
            if(joueur.getNbVictoire() == 2){
                finirJeu(joueur.getId()); //TODO à définir
            }

        }
    }

    public void finirJeu(int vainqueurId) {
        this.vainqueurId=vainqueurId;
        this.status = GameStatus.TERMINE;
    }

    public List<IJoueur> getJoueursActif() {
        return this.listeJoueurs;
    }

    public GameStateDTO getEtatPourJoueurId(Integer playerId) {
        return null;
    }

    public void setGagnant(Integer playerId) {

    }

    public boolean estJoueur(int playerId) {
        return this.listeJoueurs.stream().anyMatch(x -> x.getId()==playerId);
    }

    public Joueur getJoueurParId(Integer playerId) {
        return null;
    }

    public void ajouterIA() {
    }

    public void encherePasse(int playerId){
        this.playerIdEncherePasse.add(playerId);
    }

    public void prochainJoueur() {
        this.coupNumero++;
        this.indexJoueurCourant = (this.indexJoueurCourant + 1) % this.listeJoueurs.size();
        IJoueur prochainJoueur =  this.listeJoueurs.get(this.indexJoueurCourant);
        this.idJoueurCourant = prochainJoueur.getId();
        if(prochainJoueur instanceof JoueurIA){
            //Si le prochain joueur est instance de JoueurIA donc s'il est une IA, on demande de trouver un coup
            trouverCoup((JoueurIA) prochainJoueur);
        }

        //Si c'est une phase d'enchere
        if(this.tour.getPhase()==Phase.ENCHERE){
            //On prend le nombre de joueurs non elimine qu'on compare au nombre d'id dans la liste de joueur qui ont passé
            //Si un seul joueur n'a pas passé, alors le challenge peut commencer
            if((this.getNbPlayerNonElimine()-this.getListIdPlayerPasserEnchere().size())==1){
                //On cherche le joueur non éliminé qui à son id dans listeJoueur mais pas dans playerIdEncherePasse
                //AKA note challengeur
                int playerIdChallenge = listeJoueurs.stream()
                        .filter(joueur -> !joueur.estElimine())
                        .map(IJoueur::getId)
                        .filter(id -> !playerIdEncherePasse.contains(id))
                        .findFirst()
                        .orElseThrow();
                //On indique à l'objet du tour que le challenge peut commencer avec le challenger en id
                this.tour.commencerChallenge(playerIdChallenge);
            }
            if(this.playerIdEncherePasse.contains(prochainJoueur.getId())){
                prochainJoueur();
            }
        }
    }

    public void trouverCoup(JoueurIA ai){
        try {
            //On joue un coup trouvé par l'IA
            jouerCoup(ai.genererCoup(), this.idJoueurCourant);
        } catch (JoueurElimineException e){
            //Si l'IA est éliminé, alors, on passe au prochain joueur
            prochainJoueur();
        }
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

    public List<IJoueur> getListeJoueurs() {
        return listeJoueurs;
    }

    public void setListeJoueurs(List<IJoueur> listeJoueurs) {
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

    public long getNbPlayerNonElimine(){
        return this.listeJoueurs.stream().filter(x -> !x.estElimine()).count();
    }

    public List<Integer> getListIdPlayerPasserEnchere(){
        return this.playerIdEncherePasse;
    }

}