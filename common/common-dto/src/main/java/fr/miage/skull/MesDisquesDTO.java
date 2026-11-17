package fr.miage.skull;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.PositiveOrZero;

public record MesDisquesDTO(
        @PositiveOrZero
        @Max(4)
        int nbDisquesEnMain,

        boolean possedeSkull) {
}
