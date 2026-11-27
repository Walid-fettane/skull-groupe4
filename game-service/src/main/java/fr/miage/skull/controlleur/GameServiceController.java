package fr.miage.skull.controlleur;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.CreerPartieDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.RejoindrePartieDTO;
import fr.miage.skull.SignalementDTO;
import fr.miage.skull.exception.ConfigurationPartieInvalideException;
import fr.miage.skull.exception.DebutDeLaPartieImpossibleException;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.exception.JoueurElimineException;
import fr.miage.skull.service.GameEngineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameServiceController {

    private static final String ENTETE_JOUEUR = "X-Player-Id";

    private final GameEngineService gameEngineService;

    public GameServiceController(GameEngineService gameEngineService) {
        this.gameEngineService = gameEngineService;
    }

    @PostMapping
    public ResponseEntity<GameStateDTO> createGame(@RequestHeader(ENTETE_JOUEUR) int playerId,
                                                  @Valid @RequestBody CreerPartieDTO creerPartieDTO)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException {
        GameStateDTO etat = gameEngineService.creerPartie(playerId, creerPartieDTO.username(),
                creerPartieDTO.skinId(), creerPartieDTO.nbJoueursMax(), creerPartieDTO.nbIA());
        return ResponseEntity.status(HttpStatus.CREATED).body(etat);
    }

    @PostMapping("/{gameId}/joindre")
    public ResponseEntity<GameStateDTO> joinGame(@RequestHeader(ENTETE_JOUEUR) int playerId,
                                                 @PathVariable Long gameId,
                                                 @Valid @RequestBody RejoindrePartieDTO rejoindrePartieDTO)
            throws GameNotFoundException, DebutDeLaPartieImpossibleException {
        return ResponseEntity.ok(gameEngineService.rejoindrePartie(gameId, playerId,
                rejoindrePartieDTO.username(), rejoindrePartieDTO.skinId()));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDTO> getGameState(@RequestHeader(ENTETE_JOUEUR) int playerId,
                                                     @PathVariable Long gameId) throws GameNotFoundException {
        return ResponseEntity.ok(gameEngineService.getEtat(gameId, playerId));
    }

    @PostMapping("/{gameId}/moves")
    public ResponseEntity<GameStateDTO> playMove(@RequestHeader(ENTETE_JOUEUR) int playerId,
                                                 @PathVariable Long gameId,
                                                 @Valid @RequestBody CoupDTO unCoup)
            throws JoueurElimineException, GameNotFoundException {
        return ResponseEntity.ok(gameEngineService.jouerCoup(gameId, playerId, unCoup));
    }

    @PostMapping("/{gameId}/signaler")
    public ResponseEntity<Void> signaler(@Valid @RequestBody SignalementDTO signalementDTO, @PathVariable Long gameId){
        return null;
    }
}
