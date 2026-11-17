package fr.miage.skull.controlleur;

import fr.miage.skull.CoupDTO;
import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SignalementDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/games")
public class GameServiceController {

    @GetMapping("/{gameId}")
    public ResponseEntity<GameStateDTO> getGameState(@PathVariable Long gameId) {
        return null;
    }

    @PostMapping("/{gameId}/moves")
    public ResponseEntity<GameStateDTO> playMove(@Valid @RequestBody CoupDTO unCoup, @PathVariable Long gameId){
        return null;
    }

    @PostMapping("/{gameId}/signaler")
    public ResponseEntity<Void> signaler(@Valid @RequestBody SignalementDTO signalementDTO, @PathVariable Long gameId){
        return null;
    }
}
