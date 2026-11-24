package fr.miage.skull.service;

import fr.miage.skull.JoueurStatsDTO;
import fr.miage.skull.model.repository.JoueurRepository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LadderService {
    private final JoueurRepository joueurRepository;

    public LadderService(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }
    public List<JoueurStatsDTO> getLeaderboard(){
        return joueurRepository.findAllByOrderByNbVictoiresDesc()
                .stream()
                .map(j -> new JoueurStatsDTO(
                j.getIdJoueur(),
                j.getNbVictoires(),
                j.getNbParties(),
                j.getEmail()

        ))
                .collect(Collectors.toList());
    }
    public Optional <Object> getHistoriqueByJoueur(Long idJoueur) {
        return Optional.empty();
    }

    public Optional<Object> getHistoriqueByPartie(Long gameId) {
        return Optional.empty();
    }


}
