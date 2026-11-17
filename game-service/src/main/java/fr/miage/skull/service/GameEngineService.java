package fr.miage.skull.service;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SignalementDTO;
import org.springframework.stereotype.Service;

@Service
public class GameEngineService {

    //Instanciation du gameRepository (faut je cherche comment faire)
    //Mapper
    //Les liens avec les autres services

    //Constructeur avec instanciation des objets notés au dessus
    public GameEngineService(){

    }

    public GameStateDTO getEtat(Long gameId, Integer playerId) {
        return null;
    }

    public GameStateDTO jouerCoup(Long gameId, Integer playerId, CoupDTO coup) {
        return null;
    }

    public void signaler(Long gameId, Integer playerId, SignalementDTO signalement) {
        return;
    }

    //Méthode pour trouver une partie

}
