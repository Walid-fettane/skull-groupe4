package fr.miage.skull.service;

import fr.miage.skull.SalonDTO;
import fr.miage.skull.exception.SalonNotFoundException;
import fr.miage.skull.model.Salon;
import fr.miage.skull.model.repos.SalonRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalonService {

    private final SalonRepository salonRepository;

    public SalonService(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    public List<SalonDTO> getAllSalons() {
        return salonRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SalonDTO getSalonById(Long id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException(id));
        return convertToDTO(salon);
    }

    public SalonDTO createSalon(SalonDTO salonDTO) {
        Salon salon = new Salon(
                salonDTO.getIdCreateur(),
                salonDTO.getNbMaxJoueur(),
                salonDTO.getNbAi(),
                salonDTO.getStatus()
        );
        Salon savedSalon = salonRepository.save(salon);
        return convertToDTO(savedSalon);
    }

    private SalonDTO convertToDTO(Salon salon) {
        return new SalonDTO(
                salon.getIdSalon(),
                salon.getIdCreateur(),
                salon.getNbMaxJoueur(),
                salon.getNbAi(),
                salon.getStatus()
        );
    }


}