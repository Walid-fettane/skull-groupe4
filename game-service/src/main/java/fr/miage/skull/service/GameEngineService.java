package fr.miage.skull.service;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SignalementDTO;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.exception.JoueurElimineException;
import fr.miage.skull.model.Game;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameEngineService {

    //Instanciation du gameRepository (faut je cherche comment faire)
    private final Map<Long, Game> games = new ConcurrentHashMap<>();

    //Mapper
    //Les liens avec les autres services

    //Constructeur avec instanciation des objets notés au dessus
    public GameEngineService(){

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

    public void signaler(long gameId, int playerId, SignalementDTO signalement) {
        return;
    }

    //Méthode pour trouver une partie

}
