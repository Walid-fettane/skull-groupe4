package fr.miage.skull;

import java.time.LocalDateTime;

public record HistoriqueDTO(Long idPartie, LocalDateTime dateFin, int score,
                            int tuilesRestantes, boolean vainqueur) {}


