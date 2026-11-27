package fr.miage.skull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record RejoindrePartieDTO(
        @NotBlank(message = "username est obligatoire")
        String username,

        @PositiveOrZero(message = "skinId doit être un identifiant valide")
        long skinId) {
}
