package fr.miage.skull.controlleur;
import fr.miage.skull.JoueurStatsDTO;
import fr.miage.skull.service.LadderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity <?> getPlayerHistory(@PathVariable Long idJoueur) {
        return ladderService.getHistoriqueByJoueur(idJoueur)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Joueur non trouvé avec l'id : "+idJoueur));
    }
    @GetMapping("/historique/games/{gamesid}")
    public ResponseEntity<?> getGameHistory(@PathVariable Long gameid){
        return ladderService.getHistoriqueByPartie(gameid)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Partie non trouvée avec l'id : " + gameid));
    }
}
