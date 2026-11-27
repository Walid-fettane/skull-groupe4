package fr.miage.skull.model;


import fr.miage.skull.*;
import fr.miage.skull.exception.*;
import fr.miage.skull.mapper.Mapper;
import fr.miage.skull.model.joueur.IJoueur;
import fr.miage.skull.model.joueur.Joueur;
import fr.miage.skull.model.joueur.JoueurIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Game {

    private static final int MAX_COUPS_IA = 100;

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
    private DisqueFinChallenge dFinChallenge;
    private final int nbJoueursMax;
    private int vainqueurId = -1;
    private Long idSalon;

    //tableau des ids des joueurs ayant passé l'enchère dans le tour courrant
    private List<Integer> playerIdEncherePasse;

    public Game(List<IJoueur> listeJoueurs, int nbJoueursMax) {
        this.listeJoueurs = new ArrayList<>(listeJoueurs);
        this.nbJoueursMax = nbJoueursMax;
        this.status = GameStatus.EN_ATTENTE_DE_JOUEURS;
        this.signalements = new ArrayList<>();
        this.playerIdEncherePasse = new ArrayList<>();
    }

    public void ajouterJoueur(IJoueur joueur) {
        if (status != GameStatus.EN_ATTENTE_DE_JOUEURS) {
            throw new PartieDejaCommenceeException();
        }
        if (estJoueur(joueur.getId())) {
            throw new JoueurDejaDansPartieException();
        }
        if (estComplete()) {
            throw new PartiePleineException();
        }
        listeJoueurs.add(joueur);
    }

    public boolean estComplete() {
        return listeJoueurs.size() >= nbJoueursMax;
    }


    public void commencerJeu() throws DebutDeLaPartieImpossibleException {
        this.initJoueursStatus();
        this.validerDebutPartie();
        this.status = GameStatus.EN_COURS;
        this.coupNumero = 0;
        this.initTour();
        this.playerIdEncherePasse = new ArrayList<>();
        this.setJoueurCourant(listeJoueurs.getFirst().getId());
        this.jouerCoupsIA();
    }

    public void jouerCoup(CoupDTO coup, int playerId) throws JoueurElimineException {
        valideJoueur(playerId);
        if(tour != null){
            tour.jouerCoup(playerId, coup);
            jouerCoupsIA();
        }
    }

    //On valide que le joueur est dans la partie et que c'est à lui de jouer
    public void valideJoueur(int playerId) throws JoueurElimineException {
        //On valide qu'il est bien dans la partie
        valideJoueurDansLaPartie(playerId);

        //Qu'il n'est pas éliminé
        validerJoueurPasElimine(playerId);

        //Que c'est bien à lui de jouer
        if(!(Objects.equals(getIdJoueurCourant(), playerId))){
            throw new PasLeTourDuJoueurException();
        }
    }

    public void valideJoueurDansLaPartie(int playerId){
        IJoueur joueur = getJoueurParId(playerId);
        if(joueur == null){
            throw new JoueurNonDansPartieException();
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

    public void finSuppressionDisque(){
        this.commencerNouveauTour(this.dFinChallenge.getDisqueOwner());
    }

    private void commencerNouveauTour(int premierJoueurId) {
        //On met tour à null pour éviter les coup de s'éxecuter ou autre problèmes..
        this.tour = null;
        this.playerIdEncherePasse = null;
        this.dFinChallenge = null;
        for(IJoueur unJoueur : this.listeJoueurs){
            unJoueur.recupererDisque();
            unJoueur.resetElimination();
            if(unJoueur.getNbDisquesMain()==0){
                unJoueur.setElimine(true);
            }
        }
        if(finirJeuSiPlusAssezDeJoueurs()){
            return;
        }
        this.tour = new Tour(++this.tourId, this);
        this.playerIdEncherePasse = new ArrayList<>();
        this.setJoueurCourant(premierJoueurId);
        if(!peutJouer(getJoueurCourant())){
            this.prochainJoueur();
        }
    }

    //@param skullRetourneJoueurId = Le joueur à qui la skull s'est fait retourné si le challenge est perdu..
    public void appliquerResultatChallenge(boolean challengeGagne, int skullRetourneJoueurId, int challengerId){
        IJoueur joueur = getJoueurParId(challengerId);
        if(challengeGagne){
            joueur.ajouterVictoire();
            if(joueur.getNbVictoire() == 2){
                finirJeu(joueur.getId());
                return;
            }
            this.commencerNouveauTour(challengerId);
        } else {
            //Si le challenge est perdu
            //On incrémente le coupNumero pour signifier au client qu'il faut jouer
            //Le joueur qui doit jouer est celui qui à retourné le skull, si c'est le challenger, la logique est la même
            this.coupNumero++;
            joueur.recupererDisque();
            this.dFinChallenge=new DisqueFinChallenge(joueur.getDisqueMain().getDisques(), challengerId);
            this.tour.setPhase(Phase.FIN_CHALLENGE);
            this.setJoueurCourant(skullRetourneJoueurId);
        }
    }

    private boolean finirJeuSiPlusAssezDeJoueurs(){
        List<IJoueur> joueursAvecDisques = this.listeJoueurs.stream()
                .filter(joueur -> joueur.getNbDisquesMain() > 0)
                .toList();
        if(joueursAvecDisques.size() > 1){
            return false;
        }
        finirJeu(joueursAvecDisques.isEmpty() ? -1 : joueursAvecDisques.getFirst().getId());
        return true;
    }

    public void finirJeu(int vainqueurId) {
        this.setGagnant(vainqueurId);
        this.status = GameStatus.TERMINE;
    }

    public int getVainqueurId() {
        return this.vainqueurId;
    }

    public List<IJoueur> getJoueursActif() {
        return this.listeJoueurs;
    }

    public DisqueFinChallenge getdFinChallenge() {
        return dFinChallenge;
    }

    public GameStateDTO getEtatPourJoueurId(int playerId) {
        valideJoueurDansLaPartie(playerId);
        Phase phase = this.tour == null ? null : this.tour.getPhase();
        int enchereEnCours = this.tour == null ? 0 : this.tour.getEnchereEnCours();
        return new GameStateDTO(this.id,
                this.getStatus(),
                phase,
                this.getCoupNumero(),
                this.getIdJoueurCourant(),
                enchereEnCours,
                getDisquesDTO(playerId),
                Mapper.disqueFinChallengeToDTO(this.getdFinChallenge(), playerId),
                getPlayersDTO(playerId),
                this.vainqueurId == -1 ? null : this.vainqueurId
        );
    }

    public Long getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(Long idSalon) {
        this.idSalon = idSalon;
    }

    public FinPartieDTO getFinPartieDTO() {
        List<ParticipantDTO> participants = this.listeJoueurs.stream()
                .filter(joueur -> !joueur.estIA())
                .map(joueur -> new ParticipantDTO(
                        (long) joueur.getId(),
                        joueur.getNbVictoire(),
                        joueur.getNbDisquesMain() + joueur.getNbDisquePile(),
                        joueur.getId() == this.vainqueurId))
                .toList();
        return new FinPartieDTO(this.id, this.idSalon, participants);
    }

    public void setGagnant(int playerId) {
        this.vainqueurId=playerId;
    }

    public boolean estJoueur(int playerId) {
        return this.listeJoueurs.stream().anyMatch(x -> x.getId()==playerId);
    }

    public IJoueur getJoueurParId(Integer playerId) {
        return this.listeJoueurs.stream().filter(x -> x.getId()==playerId).findFirst().orElse(null);
    }

    public void encherePasse(int playerId){
        this.playerIdEncherePasse.add(playerId);
    }

    public void prochainJoueur() {
        this.coupNumero++;

        if(this.tour.getPhase()==Phase.REVELATION){
            return;
        }

        //Si c'est une phase d'enchere
        if(this.tour.getPhase()==Phase.ENCHERE){
            //On prend le nombre de joueurs non elimine qu'on compare au nombre d'id dans la liste de joueur qui ont passé
            //Si un seul joueur n'a pas passé, alors le challenge peut commencer
            if((this.getNbPlayerNonElimine()-this.getListIdPlayerPasserEnchere().size())==1){
                int playerIdChallenge = trouverChallengerId();
                //On indique à l'objet du tour que le challenge peut commencer avec le challenger en id
                this.tour.commencerChallenge(playerIdChallenge);
                this.setJoueurCourant(playerIdChallenge);
                return;
            }
        }

        this.setJoueurCourant(prochainJoueurJouable().getId());
    }

    //On cherche le joueur non éliminé qui à son id dans listeJoueur mais pas dans playerIdEncherePasse
    //AKA note challengeur
    private int trouverChallengerId(){
        return this.listeJoueurs.stream()
                .filter(joueur -> !joueur.estElimine())
                .map(IJoueur::getId)
                .filter(id -> !this.playerIdEncherePasse.contains(id))
                .findFirst()
                .orElseThrow();
    }

    private IJoueur prochainJoueurJouable(){
        int index = this.indexJoueurCourant;
        for(int i = 0; i < this.listeJoueurs.size(); i++){
            index = (index + 1) % this.listeJoueurs.size();
            IJoueur joueur = this.listeJoueurs.get(index);
            if(peutJouer(joueur)){
                return joueur;
            }
        }
        return getJoueurCourant();
    }

    private boolean peutJouer(IJoueur joueur){
        if(joueur.estElimine()){
            return false;
        }
        return !(this.tour.getPhase()==Phase.ENCHERE && this.playerIdEncherePasse.contains(joueur.getId()));
    }

    private void setJoueurCourant(int playerId){
        this.indexJoueurCourant = indexDuJoueur(playerId);
        this.idJoueurCourant = playerId;
    }

    private int indexDuJoueur(int playerId){
        return IntStream.range(0, this.listeJoueurs.size())
                .filter(index -> this.listeJoueurs.get(index).getId()==playerId)
                .findFirst()
                .orElseThrow(JoueurNonDansPartieException::new);
    }

    private IJoueur getJoueurCourant(){
        return this.listeJoueurs.get(this.indexJoueurCourant);
    }

    private void jouerCoupsIA(){
        int nbCoupsIA = 0;
        while(this.status==GameStatus.EN_COURS && this.tour!=null && nbCoupsIA < MAX_COUPS_IA
                && getJoueurCourant() instanceof JoueurIA ia){
            jouerCoupIA(ia);
            nbCoupsIA++;
        }
    }

    private void jouerCoupIA(JoueurIA ia){
        try {
            //On joue un coup trouvé par l'IA
            this.tour.jouerCoup(ia.getId(), genererCoupIA(ia));
        } catch (RuntimeException e){
            if(this.tour.getPhase()==Phase.ENCHERE){
                this.tour.encherePasse(ia.getId());
            } else {
                prochainJoueur();
            }
        }
    }

    private CoupDTO genererCoupIA(JoueurIA ia){
        return ia.genererCoup(this.tour.getPhase(), this.tour.getEnchereEnCours(),
                this.listeJoueurs.stream().collect(Collectors.toMap(IJoueur::getId, IJoueur::getNbDisquesPilePasRevele)));
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

    public MesDisquesDTO getDisquesDTO(int playerId) {
        //On trouve le joueur
        Optional<IJoueur> joueur = getListeJoueurs().stream().filter(x -> x.getId()==playerId).findFirst();
        if(joueur.isPresent()){
            IJoueur leJoueur = joueur.get();
            return new MesDisquesDTO(leJoueur.getNbDisquesMain(), leJoueur.getDisqueMain().possedeType(DisqueType.SKULL));
        }
        return null;
    }

    public List<PlayerGameDTO> getPlayersDTO(int playerId){
        List<PlayerGameDTO> retourAttendu = new ArrayList<>();
        //On cherche tous les joueurs sauf le demandeur
        List<IJoueur> joueurs = getListeJoueurs().stream().filter(x -> x.getId() != playerId).toList();

        for(IJoueur unJoueur : joueurs){
            retourAttendu.add(new PlayerGameDTO(
                    unJoueur.getId(),
                    unJoueur.getUsername(),
                    unJoueur.getskinId(),
                    unJoueur.getNbDisquesMain(),
                    unJoueur.getDisquesDTO(),
                    unJoueur.getNbVictoire()
                    ));
        }

        return retourAttendu;
    }
}