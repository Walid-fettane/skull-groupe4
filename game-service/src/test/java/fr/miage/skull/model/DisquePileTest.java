package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import fr.miage.skull.exception.RevelationInvalideException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class DisquePileTest {

    private final DisquePile disquePile = new DisquePile();

    @Test
    void unePileNeuveEstVide() {
        assertThat(disquePile.estVide()).isTrue();
        assertThat(disquePile.getNbDisque()).isZero();
        assertThat(disquePile.getDisques()).isEmpty();
        assertThat(disquePile.getNbDisquePasRevele()).isZero();
    }

    @Test
    void ajouterDisqueEmpileLesDisquesAvecUnePositionCroissante() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.SKULL));
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));

        assertThat(disquePile.getNbDisque()).isEqualTo(3);
        assertThat(disquePile.estVide()).isFalse();
        assertThat(disquePile.getDisques()).containsOnlyKeys(0, 1, 2);
        assertThat(disquePile.getDisques().get(1).getType()).isEqualTo(DisqueType.SKULL);
    }

    @Test
    void revelerPremierDisqueRevelLeDernierDisquePoseSansLeRetirerDeLaPile() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.SKULL));

        DisqueType type = disquePile.revelerPremierDisque();

        assertThat(type).isEqualTo(DisqueType.SKULL);
        assertThat(disquePile.getNbDisque()).isEqualTo(2);
        assertThat(disquePile.getDisques().get(1).estRevele()).isTrue();
        assertThat(disquePile.getDisques().get(0).estRevele()).isFalse();
    }

    @Test
    void revelerPremierDisqueDescendDuSommetVersLeBasDeLaPile() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.SKULL));
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));

        assertThat(disquePile.revelerPremierDisque()).isEqualTo(DisqueType.FLEUR);
        assertThat(disquePile.revelerPremierDisque()).isEqualTo(DisqueType.SKULL);
        assertThat(disquePile.revelerPremierDisque()).isEqualTo(DisqueType.FLEUR);
        assertThat(disquePile.getNbDisquePasRevele()).isZero();
    }

    @Test
    void revelerPremierDisqueSurUnePileVideEstInvalide() {
        assertThatExceptionOfType(RevelationInvalideException.class)
                .isThrownBy(disquePile::revelerPremierDisque);
    }

    @Test
    void revelerPremierDisqueSurUnePileEntierementReveleeEstInvalide() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.revelerPremierDisque();

        assertThatExceptionOfType(RevelationInvalideException.class)
                .isThrownBy(disquePile::revelerPremierDisque);
        assertThat(disquePile.getNbDisque()).isEqualTo(1);
    }

    @Test
    void revelerTouteLaPileSignaleLaPresenceDUnSkull() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.SKULL));

        boolean hasSkull = disquePile.revelerTouteLaPile();

        assertThat(hasSkull).isTrue();
        assertThat(disquePile.getNbDisquePasRevele()).isZero();
        assertThat(disquePile.getNbDisque()).isEqualTo(2);
    }

    @Test
    void revelerTouteLaPileSansSkullNeSignaleAucunSkull() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));

        boolean hasSkull = disquePile.revelerTouteLaPile();

        assertThat(hasSkull).isFalse();
        assertThat(disquePile.getNbDisquePasRevele()).isZero();
    }

    @Test
    void revelerTouteLaPileSurUnePileVideNeSignaleAucunSkull() {
        assertThat(disquePile.revelerTouteLaPile()).isFalse();
    }

    @Test
    void getNbDisquePasReveleDecroitAuFilDesRevelations() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));

        assertThat(disquePile.getNbDisquePasRevele()).isEqualTo(2);

        disquePile.revelerPremierDisque();

        assertThat(disquePile.getNbDisquePasRevele()).isEqualTo(1);
    }

    @Test
    void reinitialiserPileVideLaPile() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.revelerPremierDisque();

        disquePile.reinitialiserPile();

        assertThat(disquePile.estVide()).isTrue();
        assertThat(disquePile.getNbDisque()).isZero();
        assertThat(disquePile.getDisques()).isEmpty();
    }

    @Test
    void ajouterDisqueApresReinitialisationRepartDeLaPositionZero() {
        disquePile.ajouterDisque(new Disque(DisqueType.FLEUR));
        disquePile.reinitialiserPile();

        disquePile.ajouterDisque(new Disque(DisqueType.SKULL));

        assertThat(disquePile.getDisques()).containsOnlyKeys(0);
        assertThat(disquePile.getDisques().get(0).getType()).isEqualTo(DisqueType.SKULL);
    }
}
