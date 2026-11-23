package fr.miage.skull;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PositiveOrZero;

public record DisqueDTO(
        @PositiveOrZero
        int disquePosition,

        @Nullable
        DisqueType disqueType,

        boolean revele) {
}
