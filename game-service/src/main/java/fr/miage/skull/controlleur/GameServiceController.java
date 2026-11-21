package fr.miage.skull.controlleur;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SignalementDTO;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.exception.JoueurElimineException;
import fr.miage.skull.service.GameEngineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameServiceController {

    @Autowired
    GameEngineService gameEngineService;

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDTO> getGameState(@PathVariable Long gameId) throws GameNotFoundException {
        return ResponseEntity.ok(gameEngineService.getEtat(gameId, 0));
    }

    @PostMapping("/{gameId}/moves")
    public ResponseEntity<GameStateDTO> playMove(@Valid @RequestBody CoupDTO unCoup, @PathVariable Long gameId) throws JoueurElimineException, GameNotFoundException {
        return ResponseEntity.ok(gameEngineService.jouerCoup(gameId, 0, unCoup));
    }

    @PostMapping("/{gameId}/signaler")
    public ResponseEntity<Void> signaler(@Valid @RequestBody SignalementDTO signalementDTO, @PathVariable Long gameId){
        return null;
    }
}
