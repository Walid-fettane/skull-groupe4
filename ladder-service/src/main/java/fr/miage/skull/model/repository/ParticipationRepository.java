package fr.miage.skull.model.repository;

import fr.miage.skull.model.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository  extends JpaRepository<Participation, Long> {
    List<Participation> findByJoueurAndIdJoueurOrderByPartieAndDateFinDesc(Long idJoueur);
}
