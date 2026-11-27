package fr.miage.skull.controlleur;

import fr.miage.skull.FinPartieDTO;
import fr.miage.skull.JoueurStatsDTO;
import fr.miage.skull.PartieDetailDTO;
import fr.miage.skull.service.LadderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class Ladder {
    private final LadderService ladderService;

    public Ladder(LadderService ladderService) {
        this.ladderService = ladderService;
    }

    @GetMapping("/ladder")
    public ResponseEntity<List<JoueurStatsDTO>> getLeaderboard() {
        List<JoueurStatsDTO> leaderboard = ladderService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
    @GetMapping("/historique/{joueurId}")
    public ResponseEntity <?> getPlayerHistory(@PathVariable Long joueurId) {
        return ladderService.getHistoriqueByJoueur(joueurId)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Joueur non trouvé avec l'id : "+joueurId));
    }
    @GetMapping("/historique/games/{gamesid}")
    public ResponseEntity<?> getGameHistory(@PathVariable Long gamesid){
        return ladderService.getHistoriqueByPartie(gamesid)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Partie non trouvée avec l'id : " + gamesid));
    }
    @PostMapping("/historique/games")
    public ResponseEntity<PartieDetailDTO> enregistrerFinPartie(@Valid @RequestBody FinPartieDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ladderService.enregistrerFinPartie(dto));
    }
}