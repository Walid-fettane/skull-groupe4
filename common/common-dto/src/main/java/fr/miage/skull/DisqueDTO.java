package fr.miage.skull;

import jakarta.validation.constraints.PositiveOrZero;

public record DisqueDTO(
        @PositiveOrZero
        int disquePosition,

        DisqueType disqueType,

        boolean revele) {
}
