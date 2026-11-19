package fr.miage.skull.controlleur;

import fr.miage.skull.SalonDTO;
import fr.miage.skull.service.SalonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salons")
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
    public ResponseEntity<SalonDTO> createSalon(@RequestBody SalonDTO salonDTO) {
        SalonDTO createdSalon = salonService.createSalon(salonDTO);
        return ResponseEntity.ok(createdSalon);
    }
}