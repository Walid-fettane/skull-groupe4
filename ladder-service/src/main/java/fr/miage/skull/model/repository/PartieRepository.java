package fr.miage.skull.model.repository;

import fr.miage.skull.model.Partie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartieRepository extends JpaRepository<Partie, Long> {
    boolean existsByGameServiceId(Long gameServiceId);
}
