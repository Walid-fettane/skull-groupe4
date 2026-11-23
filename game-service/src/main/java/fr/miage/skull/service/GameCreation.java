package fr.miage.skull.service;

import fr.miage.skull.exception.ConfigurationPartieInvalideException;
import fr.miage.skull.model.Game;
import fr.miage.skull.model.joueur.IJoueur;
import fr.miage.skull.model.joueur.Joueur;
import fr.miage.skull.model.joueur.JoueurIA;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class GameCreation {

    private static final int NB_JOUEURS_MIN = 3;
    private static final int NB_JOUEURS_MAX = 6;

    private final AtomicInteger sequenceIA = new AtomicInteger();

    public Game createGame(IJoueur createur, int nbJoueursMax, int nbIA) throws ConfigurationPartieInvalideException {
        validerConfiguration(nbJoueursMax, nbIA);
        List<IJoueur> joueurs = new ArrayList<>();
        joueurs.add(createur);
        for (int i = 0; i < nbIA; i++) {
            joueurs.add(createIA());
        }
        return new Game(joueurs, nbJoueursMax);
    }

    public IJoueur createPlayer(int playerId, String username, long skinId) {
        return new Joueur(playerId, username, skinId);
    }

    public IJoueur createIA() {
        int numero = sequenceIA.incrementAndGet();
        return new JoueurIA(-numero, "IA " + numero);
    }

    private void validerConfiguration(int nbJoueursMax, int nbIA) throws ConfigurationPartieInvalideException {
        if (nbJoueursMax < NB_JOUEURS_MIN || nbJoueursMax > NB_JOUEURS_MAX) {
            throw new ConfigurationPartieInvalideException();
        }
        if (nbIA < 0 || nbIA > nbJoueursMax - 1) {
            throw new ConfigurationPartieInvalideException();
        }
    }
}