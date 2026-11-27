package fr.miage.skull.service;

import fr.miage.skull.FinPartieDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class LadderClient {

    private static final String CHEMIN_FIN_PARTIE = "/api/v1/historique/games";

    private final RestClient restClient;

    public LadderClient(@Value("${skull.ladder-service.url}") String urlLadderService) {
        this.restClient = RestClient.create(urlLadderService);
    }

    public void enregistrerFinPartie(FinPartieDTO finPartie) {
        try {
            restClient.post()
                    .uri(CHEMIN_FIN_PARTIE)
                    .body(finPartie)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
        }
    }
}
