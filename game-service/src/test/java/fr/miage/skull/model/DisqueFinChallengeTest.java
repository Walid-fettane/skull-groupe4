package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DisqueFinChallengeTest {

    private static final int ALICE = 1;

    @Test
    void lesDisquesSontProposesAuxPositionsUnAN() {
        List<Disque> disques = List.of(new Disque(DisqueType.FLEUR), new Disque(DisqueType.SKULL));

        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(disques, ALICE);

        assertThat(disqueFinChallenge.getDisquePosition()).containsOnlyKeys(1, 2);
    }

    @Test
    void auPlusQuatreDisquesSontProposes() {
        List<Disque> disques = List.of(
                new Disque(DisqueType.FLEUR), new Disque(DisqueType.FLEUR),
                new Disque(DisqueType.FLEUR), new Disque(DisqueType.SKULL),
                new Disque(DisqueType.FLEUR), new Disque(DisqueType.FLEUR));

        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(disques, ALICE);

        assertThat(disqueFinChallenge.getDisquePosition()).containsOnlyKeys(1, 2, 3, 4);
    }

    @Test
    void uneListeVideNeProposeAucunDisque() {
        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(List.of(), ALICE);

        assertThat(disqueFinChallenge.getDisquePosition()).isEmpty();
    }

    @Test
    void uneListeNulleNeProposeAucunDisque() {
        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(null, ALICE);

        assertThat(disqueFinChallenge.getDisquePosition()).isEmpty();
    }

    @Test
    void leProprietaireDesDisquesEstConserve() {
        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(List.of(new Disque(DisqueType.FLEUR)), ALICE);

        assertThat(disqueFinChallenge.getDisqueOwner()).isEqualTo(ALICE);
    }

    @Test
    void lesDisquesProposesSontCeuxDeLaListeDOrigine() {
        Disque fleur = new Disque(DisqueType.FLEUR);
        Disque skull = new Disque(DisqueType.SKULL);

        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(List.of(fleur, skull), ALICE);

        assertThat(disqueFinChallenge.getDisquePosition().values()).containsExactlyInAnyOrder(fleur, skull);
    }

    @Test
    void laListeDOrigineNEstPasModifiee() {
        List<Disque> disques = new ArrayList<>(List.of(
                new Disque(DisqueType.FLEUR), new Disque(DisqueType.SKULL)));
        List<Disque> copie = List.copyOf(disques);

        new DisqueFinChallenge(disques, ALICE);

        assertThat(disques).containsExactlyElementsOf(copie);
    }

    @Test
    void lesPositionsNeSontPasModifiables() {
        DisqueFinChallenge disqueFinChallenge = new DisqueFinChallenge(List.of(new Disque(DisqueType.FLEUR)), ALICE);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> disqueFinChallenge.getDisquePosition().put(2, new Disque(DisqueType.SKULL)));
    }
}
