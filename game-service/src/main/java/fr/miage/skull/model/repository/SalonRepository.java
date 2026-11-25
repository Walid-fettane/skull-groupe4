package fr.miage.skull.model.repository;

import fr.miage.skull.model.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long> {
    List<Salon> findByStatus(String status);
}