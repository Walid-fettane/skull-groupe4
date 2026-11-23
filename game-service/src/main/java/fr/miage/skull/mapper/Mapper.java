package fr.miage.skull.mapper;

import fr.miage.skull.DisqueDTO;
import fr.miage.skull.DisqueFinChallengeDTO;
import fr.miage.skull.model.Disque;
import fr.miage.skull.model.DisqueFinChallenge;

import java.util.Map;
import java.util.TreeMap;

public class Mapper {

    public static DisqueFinChallengeDTO disqueFinChallengeToDTO(DisqueFinChallenge disqueFinChallenge, int playerId) {
        if (disqueFinChallenge == null) {
            return null;
        }

        boolean estOwner = disqueFinChallenge.getDisqueOwner() == playerId;

        Map<Integer, DisqueDTO> positions = new TreeMap<>(); // positions triées 1 → 4
        for (Map.Entry<Integer, Disque> entry : disqueFinChallenge.getDisquePosition().entrySet()) {
            int position = entry.getKey();
            DisqueDTO dto = estOwner ? disqueToDTO(entry.getValue(), position) : new DisqueDTO(position, null, false); // disque caché : position seule
            positions.put(position, dto);
        }

        return new DisqueFinChallengeDTO(positions);
    }

    public static DisqueDTO disqueToDTO(Disque disque, int position) {
        return new DisqueDTO(position, disque.getType(), disque.estRevele());
    }
}
