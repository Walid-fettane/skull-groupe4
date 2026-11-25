package fr.miage.skull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record FinPartieDTO(@NotNull Long gameServiceId, @NotNull Long idSalon,
                           @NotEmpty List<@Valid ParticipantDTO> participants) {
}
