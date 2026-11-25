package fr.miage.skull.service;

import fr.miage.skull.*;
import fr.miage.skull.model.Joueur;
import fr.miage.skull.model.Participation;
import fr.miage.skull.model.Partie;
import fr.miage.skull.model.repository.JoueurRepository;
import fr.miage.skull.model.repository.ParticipationRepository;
import fr.miage.skull.model.repository.PartieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LadderService {
    private final JoueurRepository joueurRepository;
    private final PartieRepository partieRepository;
    private final ParticipationRepository participationRepository;

    public LadderService(JoueurRepository joueurRepository,
                         PartieRepository partieRepository,
                         ParticipationRepository participationRepository) {
        this.joueurRepository = joueurRepository;
        this.partieRepository = partieRepository;
        this.participationRepository = participationRepository;
    }
    public List<JoueurStatsDTO> getLeaderboard(){
        return joueurRepository.findAllByOrderByNbVictoiresDesc()
                .stream()
                .map(j -> new JoueurStatsDTO(
                j.getIdJoueur(),
                j.getNbVictoires(),
                j.getNbParties(),
                j.getEmail()

        ))
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public Optional <List<HistoriqueDTO>> getHistoriqueByJoueur(Long idJoueur) {
        if (!joueurRepository.existsById(idJoueur)) {
            return Optional.empty();
        }
        List<HistoriqueDTO> historique = participationRepository
                .findByJoueur_IdJoueurOrderByPartie_DateFinDesc(idJoueur)
                .stream()
                .map(p -> new HistoriqueDTO(
                        p.getPartie().getIdPartie(),
                        p.getPartie().getDateFin(),
                        p.getScore(),
                        p.getTuilesRestantes(),
                        p.isVainqueur()))
                .toList();
        return Optional.of(historique);

    }
    @Transactional(readOnly = true)
    public Optional<PartieDetailDTO> getHistoriqueByPartie(Long gameId) {
        return partieRepository.findById(gameId).map(this::toDetailDTO);
    }

    private PartieDetailDTO toDetailDTO(Partie partie) {
        return new PartieDetailDTO(
                partie.getIdPartie(),
                partie.getDateFin(),
                partie.getParticipations().stream()
                        .map(p -> new ParticipantDTO(
                                p.getJoueur().getIdJoueur(),
                                p.getScore(),
                                p.getTuilesRestantes(),
                                p.isVainqueur()))
                        .toList());
    }

    @Transactional
    public PartieDetailDTO enregistrerFinPartie(FinPartieDTO dto) {
        long distincts = dto.participants().stream().map(ParticipantDTO::idJoueur).distinct().count();
        if (distincts != dto.participants().size()) {
            throw new IllegalArgumentException("Un joueur apparaît plusieurs fois");
        }
        //verifi si joueur à 0 tuilles
        for (ParticipantDTO p : dto.participants()) {
            if (p.vainqueur() && p.tuilesRestantes() == 0) {
                throw new IllegalArgumentException("Un joueur éliminé ne peut pas être vainqueur : " + p.idJoueur());
            }
        }
        long nbVainqueurs = dto.participants().stream().filter(ParticipantDTO::vainqueur).count();
        if (nbVainqueurs > 1) {//si une IA gagne aucun humain n'est vainqueur = 0
            throw new IllegalArgumentException("Une partie ne peut avoir qu'un seul vainqueur");
        }

        if (partieRepository.existsByGameServiceId(dto.gameServiceId())) {
            throw new IllegalStateException("La partie " + dto.gameServiceId() + " a déjà été enregistrée.");
        }
        Partie partie = new Partie();
        partie.setIdSalon(dto.idSalon());
        partie.setGameServiceId(dto.gameServiceId());
        partie.setDateFin(LocalDateTime.now());

        for (ParticipantDTO p : dto.participants()) {
            Joueur joueur = joueurRepository.findById(p.idJoueur())
                    .orElseThrow(() -> new IllegalArgumentException("Joueur inconnu : " + p.idJoueur()));

            Participation participation = new Participation();
            participation.setPartie(partie);
            participation.setJoueur(joueur);
            participation.setScore(p.score());
            participation.setVainqueur(p.vainqueur());
            partie.getParticipations().add(participation);

            joueur.setNbParties(joueur.getNbParties() + 1);
            if (p.vainqueur()) {
                joueur.setNbVictoires(joueur.getNbVictoires() + 1);
            }
        }

        Partie saved = partieRepository.save(partie);
        return toDetailDTO(saved);
    }


}
