package fr.miage.skull.service;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SignalementDTO;
import fr.miage.skull.exception.AutoSignalementException;
import fr.miage.skull.exception.ConfigurationPartieInvalideException;
import fr.miage.skull.exception.DebutDeLaPartieImpossibleException;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.exception.JoueurElimineException;
import fr.miage.skull.exception.JoueurNonDansPartieException;
import fr.miage.skull.exception.SignalementIAException;
import fr.miage.skull.model.Game;
import fr.miage.skull.model.joueur.IJoueur;
import fr.miage.skull.model.joueur.Joueur;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GameEngineService {

    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private final AtomicLong sequenceGame = new AtomicLong();
    private final GameCreation gameCreation;

    public GameEngineService(GameCreation gameCreation) {
        this.gameCreation = gameCreation;
    }

    public GameStateDTO creerPartie(int playerId, String username, long skinId, int nbJoueursMax, int nbIA)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException {
        //On créé un joueur (le créateur de la partie)
        IJoueur createur = gameCreation.createPlayer(playerId, username, skinId);
        //On créé une partie en passant le créateur, un nb joueur max et un nombre d'IA souhaité
        Game game = gameCreation.createGame(createur, nbJoueursMax, nbIA);
        game.setId(sequenceGame.incrementAndGet());
        //On l'ajoute dans la mémoire
        games.put(game.getId(), game);
        //Si la partie est complète (un seul joueurs + seulement des IA) alors la partie peu commencer
        if (game.estComplete()) {
            game.commencerJeu();
        }
        //On envoi l'état de la partie au créateur
        return game.getEtatPourJoueurId(playerId);
    }

    public GameStateDTO rejoindrePartie(long gameId, int playerId, String username, long skinId)
            throws GameNotFoundException, DebutDeLaPartieImpossibleException {
        //On cherche la partie (exception not found géré par trouverPartie()
        Game game = trouverPartie(gameId);
        //On ajoute le joueur à la partie
        game.ajouterJoueur(gameCreation.createPlayer(playerId, username, skinId));
        if (game.estComplete()) {
            //Si la partie devient complète alors on peut lancer le jeu
            game.commencerJeu();
        }
        //On envoi le nouvel état au joueur ayant rejoint la partie
        return game.getEtatPourJoueurId(playerId);
    }

    public GameStateDTO getEtat(long gameId, int playerId) throws GameNotFoundException {
        Optional<Game> game = Optional.ofNullable(games.get(gameId));
        if(game.isEmpty()){
            //Si aucune partie n'existe sous cette id --> Exception
            throw new GameNotFoundException();
        }

        return game.get().getEtatPourJoueurId(playerId);
    }

    public GameStateDTO jouerCoup(long gameId, int playerId, CoupDTO coup) throws GameNotFoundException, JoueurElimineException {
        //On cherche la partie avec l'id renseigné
        Optional<Game> game = Optional.ofNullable(games.get(gameId));
        if(game.isEmpty()){
            //Si aucune partie n'existe sous cette id --> Exception
            throw new GameNotFoundException();
        }
        //Si la partie est trouvé, on joue le coup, puis on renvoie le nouvel état de la partie
        game.get().jouerCoup(coup, playerId);
        return game.get().getEtatPourJoueurId(playerId);
    }

    public void signaler(long gameId, int playerId, SignalementDTO signalement) throws GameNotFoundException {
        Optional<Game> game = Optional.ofNullable(games.get(gameId));
        if(game.isEmpty()){
            //Si aucune partie n'existe sous cette id --> Exception
            throw new GameNotFoundException();
        }
        //On verif que le joueur qui signale est bien dans la partie
        if(!game.get().estJoueur(playerId)){
            throw new JoueurNonDansPartieException();
        }

        //On vérif que le joueur ne se signale pas lui-même
        if(playerId==signalement.joueurSignalerId()){
            throw new AutoSignalementException();
        }

        //On verif que le joueur signalé est bien dans la partie et est un Joueur (pour exclure JoueurIA)
        IJoueur joueurTarget = game.get().getJoueurParId(signalement.joueurSignalerId());
        if(joueurTarget == null){
            throw new JoueurNonDansPartieException();
        }
        if(!(joueurTarget instanceof Joueur)){
            throw new SignalementIAException();
        }

        //Appel service signalement
    }

    private Game trouverPartie(long gameId) throws GameNotFoundException {
        return Optional.ofNullable(games.get(gameId))
                .orElseThrow(GameNotFoundException::new);
    }
}