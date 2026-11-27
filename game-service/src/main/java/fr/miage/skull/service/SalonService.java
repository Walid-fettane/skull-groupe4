package fr.miage.skull.service;

import fr.miage.skull.GameStateDTO;
import fr.miage.skull.SalonDTO;
import fr.miage.skull.exception.ConfigurationPartieInvalideException;
import fr.miage.skull.exception.DebutDeLaPartieImpossibleException;
import fr.miage.skull.exception.GameNotFoundException;
import fr.miage.skull.exception.SalonNotFoundException;
import fr.miage.skull.model.Salon;
import fr.miage.skull.model.repository.SalonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalonService {

    private final SalonRepository salonRepository;
    private final GameEngineService gameEngineService;
    private static final String EN_ATTENTE = "EN_ATTENTE";
    private static final String PLEIN = "PLEIN";

    public SalonService(SalonRepository salonRepository, GameEngineService gameEngineService) {
        this.salonRepository = salonRepository;
        this.gameEngineService = gameEngineService;
    }
    @Transactional(readOnly = true)
    public List<SalonDTO> getAllSalons() {
        return salonRepository.findByStatus(EN_ATTENTE).stream()
                .map(this::convertToDTO)
                .toList();
    }
    @Transactional(readOnly = true)
    public SalonDTO getSalonById(Long id) {
        Salon salon = salonRepository.findById(id)
                .orElseThrow(() -> new SalonNotFoundException(id));
        return convertToDTO(salon);
    }
    @Transactional
    public SalonDTO createSalon(SalonDTO salonDTO)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException, GameNotFoundException {
        if (salonDTO.getNbAi() + 1 > salonDTO.getNbMaxJoueur()) {
            throw new IllegalArgumentException("Trop d'IA pour la capacité du salon");
        }
        Salon salon = new Salon(
                salonDTO.getIdCreateur(),
                salonDTO.getNbMaxJoueur(),
                salonDTO.getNbAi(),
                EN_ATTENTE
        );
        salon.getJoueurIds().add(salonDTO.getIdCreateur());
        Salon savedSalon = salonRepository.save(salon);
        if (savedSalon.estPlein()) {
            savedSalon.setStatus(PLEIN);
            savedSalon.setIdGame(demarrerPartie(savedSalon));
            savedSalon = salonRepository.save(savedSalon);
        }
        return convertToDTO(savedSalon);
    }

    private SalonDTO convertToDTO(Salon salon) {
        SalonDTO salonDTO = new SalonDTO(
                salon.getIdSalon(),
                salon.getIdCreateur(),
                salon.getNbMaxJoueur(),
                salon.getNbAi(),
                salon.getStatus()
        );
        salonDTO.setIdGame(salon.getIdGame());
        return salonDTO;
    }

    private Long demarrerPartie(Salon salon)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException, GameNotFoundException {
        GameStateDTO etat = gameEngineService.creerPartie(
                salon.getIdCreateur().intValue(),
                nomParDefaut(salon.getIdCreateur()),
                0L,
                (int) salon.getNbMaxJoueur(),
                (int) salon.getNbAi(),
                salon.getIdSalon());
        for (Long idJoueur : salon.getJoueurIds()) {
            if (!idJoueur.equals(salon.getIdCreateur())) {
                gameEngineService.rejoindrePartie(etat.gameId(), idJoueur.intValue(), nomParDefaut(idJoueur), 0L);
            }
        }
        return etat.gameId();
    }

    private String nomParDefaut(Long idJoueur) {
        return "Joueur " + idJoueur;
    }

    @Transactional
    public SalonDTO joindreSalon(Long idSalon,Long idJoueur)
            throws ConfigurationPartieInvalideException, DebutDeLaPartieImpossibleException, GameNotFoundException {
        Salon salon = salonRepository.findById(idSalon).orElseThrow(() -> new SalonNotFoundException(idSalon));
        if(!EN_ATTENTE.equals(salon.getStatus())) {
            throw new IllegalStateException("Ce salon est rempli et n'accepte plus de joueurs");
        }
        if(salon.getJoueurIds().contains(idJoueur)) {
            throw new IllegalStateException("Joueur est deja present dans le salon");
        }
        if(salon.estPlein()){
            throw new IllegalStateException("Ce salon est plein, essayez un autre");
        }
        salon.getJoueurIds().add(idJoueur);
        if (salon.estPlein()) {
            salon.setStatus(PLEIN);
            salon.setIdGame(demarrerPartie(salon));
        }

        return convertToDTO(salonRepository.save(salon));
    }
    @Transactional
    public boolean quitterSalon(Long idSalon,Long idJoueur) {
        Salon salon = salonRepository.findById(idSalon).orElseThrow(() -> new SalonNotFoundException(idSalon));
        if (!salon.getJoueurIds().remove(idJoueur)) {
            throw new IllegalArgumentException("Ce joueur n'est pas present dans le salon");
        }
        if(EN_ATTENTE.equals(salon.getStatus())) { //salon vide = delete salon
            if (salon.getJoueurIds().isEmpty()){
                salonRepository.delete(salon);
            }
            return false;
        }
        salon.setNbAi(salon.getNbAi() + 1);
        salonRepository.save(salon);
        return true;

    }


}