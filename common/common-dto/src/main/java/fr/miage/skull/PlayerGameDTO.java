package fr.miage.skull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record PlayerGameDTO(
        @NotNull
        @Positive
        Integer playerId,

        @NotBlank
        String username,

        Long skinId,

        @PositiveOrZero
        @Max(4)
        int disqueMain,

        @NotNull
        List<@Valid DisqueDTO> disques,

        @PositiveOrZero
        @Max(2)
        int manchesGagnees) {
}
