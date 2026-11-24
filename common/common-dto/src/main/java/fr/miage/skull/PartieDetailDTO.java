package fr.miage.skull;

import java.time.LocalDateTime;
import java.util.List;

public record PartieDetailDTO (Long idPartie, LocalDateTime dateFin, List<ParticipantDTO> participants) {

}
