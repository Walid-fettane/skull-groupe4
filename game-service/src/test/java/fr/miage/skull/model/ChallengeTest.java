package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import fr.miage.skull.exception.CoupNonAutoriseException;
import fr.miage.skull.exception.JoueurNonDansPartieException;
import fr.miage.skull.exception.RevelationInvalideException;
import fr.miage.skull.model.joueur.IJoueur;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChallengeTest {

    private static final int ALICE = 1;
    private static final int BOB = 2;
    private static final int CAROL = 3;
    private static final int INCONNU = 99;
    private static final int CHALLENGE_ID = 7;

    @Mock
    private Tour tour;

    @Mock
    private IJoueur alice;

    @Mock
    private IJoueur bob;

    @Mock
    private IJoueur carol;

    @Test
    void getPlayerChallengerIdRenvoieLIdPasseAuConstructeur() {
        Challenge challenge = spy(new Challenge(ALICE, 2, CHALLENGE_ID, List.of(alice), tour));

        assertThat(challenge.getPlayerChallengerId()).isEqualTo(ALICE);
    }

    @Test
    void revelerSaPileAvecUnChallengerSansSkullEtObjectifNonAtteint() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        Challenge challenge = spy(new Challenge(ALICE, 3, CHALLENGE_ID, List.of(alice, bob), tour));

        challenge.revelerSaPile();

        verify(tour, never()).resultatChallenge(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    void revelerSaPileAvecUnChallengerSansSkullEtObjectifAtteint() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(2);
        Challenge challenge = spy(new Challenge(ALICE, 2, CHALLENGE_ID, List.of(alice, bob), tour));

        challenge.revelerSaPile();

        verify(tour).resultatChallenge(true, -1, ALICE);
    }

    @Test
    void revelerSaPileAvecUnChallengerAvecSkull() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(true);
        Challenge challenge = spy(new Challenge(ALICE, 1, CHALLENGE_ID, List.of(alice, bob), tour));

        challenge.revelerSaPile();

        verify(tour).resultatChallenge(false, ALICE, ALICE);
    }

    @Test
    void revelerSaPileAvecUnChallengerAbsentDeLaListe() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        Challenge challenge = spy(new Challenge(INCONNU, 1, CHALLENGE_ID, List.of(alice, bob), tour));

        challenge.revelerSaPile();

        verifyNoInteractions(tour);
    }

    @Test
    void revelerSaPileAvecUneListeDeJoueursVide() {
        Challenge challenge = spy(new Challenge(ALICE, 1, CHALLENGE_ID, List.of(), tour));

        challenge.revelerSaPile();

        verifyNoInteractions(tour);
    }

    @Test
    void revelerSaPileAvecLeChallengerEnFinDeListe() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(carol.getId()).thenReturn(CAROL);
        when(carol.revelerTouteSaPile()).thenReturn(false);
        when(carol.getNbDisquePile()).thenReturn(2);
        Challenge challenge = spy(new Challenge(CAROL, 2, CHALLENGE_ID, List.of(alice, bob, carol), tour));

        challenge.revelerSaPile();

        verify(tour).resultatChallenge(true, -1, CAROL);
    }

    @Test
    void revelerLePremierDisqueSansAvoirReveleSaPile() {
        Challenge challenge = spy(new Challenge(ALICE, 2, CHALLENGE_ID, List.of(alice, bob), tour));

        assertThatExceptionOfType(CoupNonAutoriseException.class)
                .isThrownBy(() -> challenge.revelerLePremierDisque(BOB));
        verifyNoInteractions(tour, alice, bob);
    }

    @Test
    void revelerLePremierDisqueDUnJoueurAbsentDeLaListe() {
        when(alice.getId()).thenReturn(ALICE);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        Challenge challenge = spy(new Challenge(ALICE, 3, CHALLENGE_ID, List.of(alice), tour));
        challenge.revelerSaPile();

        assertThatExceptionOfType(JoueurNonDansPartieException.class)
                .isThrownBy(() -> challenge.revelerLePremierDisque(INCONNU));
        verify(tour, never()).resultatChallenge(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    void revelerLePremierDisqueDUnJoueurSansDisqueDansSaPile() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        when(bob.getNbDisquePile()).thenReturn(0);
        Challenge challenge = spy(new Challenge(ALICE, 3, CHALLENGE_ID, List.of(alice, bob), tour));
        challenge.revelerSaPile();

        assertThatExceptionOfType(RevelationInvalideException.class)
                .isThrownBy(() -> challenge.revelerLePremierDisque(BOB));
        verify(bob, never()).revelerDisque();
    }

    @Test
    void revelerLePremierDisqueQuiEstUnSkull() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        when(bob.getNbDisquePile()).thenReturn(1);
        when(bob.revelerDisque()).thenReturn(DisqueType.SKULL);
        Challenge challenge = spy(new Challenge(ALICE, 3, CHALLENGE_ID, List.of(alice, bob), tour));
        challenge.revelerSaPile();

        challenge.revelerLePremierDisque(BOB);

        verify(tour).resultatChallenge(false, BOB, ALICE);
    }

    @Test
    void revelerLePremierDisqueQuiEstUneFleurSansAtteindreLObjectif() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        when(bob.getNbDisquePile()).thenReturn(1);
        when(bob.revelerDisque()).thenReturn(DisqueType.FLEUR);
        Challenge challenge = spy(new Challenge(ALICE, 3, CHALLENGE_ID, List.of(alice, bob), tour));
        challenge.revelerSaPile();

        challenge.revelerLePremierDisque(BOB);

        verify(tour, never()).resultatChallenge(anyBoolean(), anyInt(), anyInt());
    }

    @Test
    void revelerLePremierDisqueQuiEstUneFleurEtAtteintLObjectif() {
        when(alice.getId()).thenReturn(ALICE);
        when(bob.getId()).thenReturn(BOB);
        when(alice.revelerTouteSaPile()).thenReturn(false);
        when(alice.getNbDisquePile()).thenReturn(1);
        when(bob.getNbDisquePile()).thenReturn(1);
        when(bob.revelerDisque()).thenReturn(DisqueType.FLEUR);
        Challenge challenge = spy(new Challenge(ALICE, 2, CHALLENGE_ID, List.of(alice, bob), tour));
        challenge.revelerSaPile();

        challenge.revelerLePremierDisque(BOB);

        verify(tour).resultatChallenge(true, -1, ALICE);
    }

    @Test
    void finChallengeDelegueLeResultatAuTour() {
        Challenge challenge = spy(new Challenge(ALICE, 2, CHALLENGE_ID, List.of(alice), tour));

        challenge.finChallenge(false, BOB);

        verify(tour).resultatChallenge(false, BOB, ALICE);
    }
}
