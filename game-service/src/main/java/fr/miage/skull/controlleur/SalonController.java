package fr.miage.skull.controlleur;

import fr.miage.skull.SalonDTO;
import fr.miage.skull.exception.ConfigurationPartieInvalideException;
import fr.miage.skull.exception.DebutDeLaPartieImpossibleException;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.service.SalonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salons")
public class SalonController {

    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping
    public ResponseEntity<List<SalonDTO>> getAllSalons() {
        List<SalonDTO> salons = salonService.getAllSalons();
        return ResponseEntity.ok(salons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long id) {
        SalonDTO salonDTO = salonService.getSalonById(id);
        return ResponseEntity.ok(salonDTO);
    }

    @PostMapping
    public ResponseEntity<SalonDTO> createSalon(@Valid @RequestBody SalonDTO salonDTO)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException, GameNotFoundException {
        SalonDTO createdSalon = salonService.createSalon(salonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSalon);
    }

    @PostMapping("/{id}/joindre")
    public ResponseEntity<SalonDTO> joindreSalon (@PathVariable Long id, @RequestParam Long idJoueur)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException, GameNotFoundException {
        return ResponseEntity.ok(salonService.joindreSalon(id,idJoueur));
    }

    @DeleteMapping("/{id}/quitter")
    public ResponseEntity<?> quitterSalon(@PathVariable Long id, @RequestParam Long idJoueur) {
        boolean remplaceParAI= salonService.quitterSalon(id,idJoueur);
        if(remplaceParAI){
            return ResponseEntity.ok("Joueur remplacé par IA");
        }
        return ResponseEntity.noContent().build();
    }
}