package fr.miage.skull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ParticipantDTO(@NotNull Long idJoueur,
                             @Min(0) int score,
                             @Min(0) @Max(4) int tuilesRestantes,
                             boolean vainqueur) {}