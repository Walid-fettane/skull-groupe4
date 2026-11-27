package fr.miage.skull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreerPartieDTO(
        @NotBlank(message = "username est obligatoire")
        String username,

        @PositiveOrZero(message = "skinId doit être un identifiant valide")
        long skinId,

        @Min(value = 3, message = "une partie compte entre 3 et 6 joueurs")
        @Max(value = 6, message = "une partie compte entre 3 et 6 joueurs")
        int nbJoueursMax,

        @PositiveOrZero(message = "nbIA ne peut pas être négatif")
        @Max(value = 5, message = "une partie compte au plus 5 IA")
        int nbIA) {
}
