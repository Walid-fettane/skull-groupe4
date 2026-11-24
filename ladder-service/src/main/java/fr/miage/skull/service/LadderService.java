package fr.miage.skull.service;

import fr.miage.skull.HistoriqueDTO;
import fr.miage.skull.JoueurStatsDTO;
import fr.miage.skull.PartieDetailDTO;
import fr.miage.skull.model.repository.JoueurRepository;
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
    }//todo apres creation des class partie et partipation
    public Optional <List<HistoriqueDTO>> getHistoriqueByJoueur(Long idJoueur) {
        return Optional.empty();
        //todo apres creation des class parite et partiipation
    }

    public Optional<PartieDetailDTO> getHistoriqueByPartie(Long gameId) {
        return Optional.empty();
    }


}
