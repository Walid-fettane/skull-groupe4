package fr.miage.skull;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record GameStateDTO(
        @NotNull
        @Positive
        long gameId,

        @NotNull
        GameStatus status,

        Phase statusCoup,

        @PositiveOrZero
        long coupNumero,

        @PositiveOrZero
        int idJoueurCourant,

        @PositiveOrZero
        int enchereEnCours,

        @NotNull
        @Valid
        MesDisquesDTO mesDisques,

        @Nullable
        DisqueFinChallengeDTO dFinChallengeDTO,

        @NotNull
        List<@Valid PlayerGameDTO> playerGames,

        @Nullable
        Integer vainqueurId) {
}
