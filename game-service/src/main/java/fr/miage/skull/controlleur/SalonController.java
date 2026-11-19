package fr.miage.skull.controlleur;

import fr.miage.skull.SalonDTO;
import fr.miage.skull.service.SalonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SalonController {

    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }

    @GetMapping("/salons")
    public ResponseEntity<List<SalonDTO>> getAllSalons() {
        List<SalonDTO> salons = salonService.getAllSalons();
        return ResponseEntity.ok(salons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long id) {
        SalonDTO salonDTO = salonService.getSalonById(id);
        return ResponseEntity.ok(salonDTO);
    }

    @PostMapping("/salons")
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        SalonDTO createdSalon = salonService.createSalon(salonDTO);
        return ResponseEntity.ok(createdSalon);
    }

    @PostMapping("/{id}/joindre")
    public ResponseEntity<SalonDTO> joindreSalon (@PathVariable Long id, @RequestParam Long idJoueur) {
        return ResponseEntity.ok(salonService.joindreSalon(id,idJoueur));
    }

    @PostMapping("/{id}/quitter")
    public ResponseEntity<?> quitterSalon(@PathVariable Long id, @RequestParam Long idJoueur) {
        boolean remplaceParAI= salonService.quitterSalon(id,idJoueur);
        if(remplaceParAI){
            return ResponseEntity.ok("Joueur remplacé par IA");
        }
        return ResponseEntity.noContent().build();
    }
}