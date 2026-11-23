package fr.miage.skull;

import jakarta.validation.constraints.Positive;

import java.util.Map;

public record DisqueFinChallengeDTO(
            Map<Integer, DisqueDTO> disques
    ){}
