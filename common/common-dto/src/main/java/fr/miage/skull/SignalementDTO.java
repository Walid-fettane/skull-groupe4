package fr.miage.skull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SignalementDTO(
        @NotNull(message = "joueurSignalerId est obligatoire")
        @Positive(message = "joueurSignalerId doit être un identifiant valide")
        int joueurSignalerId,

        @NotNull(message = "gameId est obligatoire")
        @Positive(message = "gameId doit être un identifiant valide")
        long gameId,

        @NotBlank(message = "raisonSignalement est obligatoire")
        @Size(max = 500, message = "raisonSignalement ne doit pas dépasser 500 caractères")
        String raisonSignalement) {
}
