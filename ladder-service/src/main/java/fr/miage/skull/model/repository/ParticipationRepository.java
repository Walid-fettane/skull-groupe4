package fr.miage.skull.model.repository;

import fr.miage.skull.model.Participation;
import fr.miage.skull.model.ParticipationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRepository  extends JpaRepository<Participation, ParticipationId> {
    List<Participation> findByJoueur_IdJoueurOrderByPartie_DateFinDesc(Long idJoueur);
}
