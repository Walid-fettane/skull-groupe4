package fr.miage.skull.model.repository;

import fr.miage.skull.model.Joueur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JoueurRepository  extends JpaRepository<Joueur, Long> {
    List<Joueur> findAllByOrderByNbVictoiresDesc();


}
