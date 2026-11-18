package fr.miage.skull.model.repos;

import fr.miage.skull.model.Game;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GameRepository {

    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private long gameIdIncrement = 0;

    public Game saveGame(Game game) {
        if (game.getId() == null) {
            gameIdIncrement++;
            game.setId(gameIdIncrement);
        }
        games.put(game.getId(), game);
        return game;
    }

    public Optional<Game> findGameById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(games.get(id));
    }

    public List<Game> findAllGames() {
        return List.copyOf(games.values());
    }

    public boolean existsGameById(Long id) {
        return id != null && games.containsKey(id);
    }

    public void deleteGameById(Long id) {
        if (id != null) {
            games.remove(id);
        }
    }

    public long countGames() {
        return games.size();
    }
}