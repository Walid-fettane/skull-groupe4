package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

class DisqueMainTest {

    private static final long GRAINE = 42L;

    private final DisqueMain disqueMain = new DisqueMain();

    @Test
    void uneMainNeuveEstVide() {
        assertThat(disqueMain.estVide()).isTrue();
        assertThat(disqueMain.getNbDisque()).isZero();
        assertThat(disqueMain.getDisques()).isEmpty();
    }

    @Test
    void ajouterDisqueAugmenteLeNombreDeDisques() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));

        assertThat(disqueMain.getNbDisque()).isEqualTo(2);
        assertThat(disqueMain.getDisques()).hasSize(2);
        assertThat(disqueMain.estVide()).isFalse();
    }

    @Test
    void possedeTypeEstVraiUniquementPourLesTypesPresents() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));

        assertThat(disqueMain.possedeType(DisqueType.FLEUR)).isTrue();
        assertThat(disqueMain.possedeType(DisqueType.SKULL)).isFalse();
    }

    @Test
    void supprimerDisqueEnleveUnSeulExemplaireDuType() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));

        disqueMain.supprimerDisque(DisqueType.FLEUR);

        assertThat(disqueMain.getNbDisque()).isEqualTo(2);
        assertThat(disqueMain.possedeType(DisqueType.FLEUR)).isTrue();
        assertThat(disqueMain.possedeType(DisqueType.SKULL)).isTrue();
    }

    @Test
    void supprimerDisqueDUnTypeAbsentNeChangeRienALaMain() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));

        disqueMain.supprimerDisque(DisqueType.SKULL);

        assertThat(disqueMain.getNbDisque()).isEqualTo(1);
        assertThat(disqueMain.possedeType(DisqueType.FLEUR)).isTrue();
    }

    @Test
    void supprimerLeDernierDisqueRendLaMainVide() {
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));

        disqueMain.supprimerDisque(DisqueType.SKULL);

        assertThat(disqueMain.estVide()).isTrue();
        assertThat(disqueMain.possedeType(DisqueType.SKULL)).isFalse();
    }

    @Test
    void supprimerDisqueSurUneMainVideNeFaitRien() {
        disqueMain.supprimerDisque(DisqueType.FLEUR);

        assertThat(disqueMain.estVide()).isTrue();
    }

    @Test
    void enleverDisqueAleatoirementRetireUnSeulDisque() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));

        disqueMain.enleverDisqueAleatoirement(new Random(GRAINE));

        assertThat(disqueMain.getNbDisque()).isEqualTo(2);
    }

    @Test
    void enleverDisqueAleatoirementSurUneMainVideNeFaitRien() {
        disqueMain.enleverDisqueAleatoirement(new Random(GRAINE));

        assertThat(disqueMain.estVide()).isTrue();
    }

    @Test
    void enleverDisqueAleatoirementVideLaMainDisqueParDisque() {
        disqueMain.ajouterDisque(new Disque(DisqueType.FLEUR));
        disqueMain.ajouterDisque(new Disque(DisqueType.SKULL));
        Random random = new Random(GRAINE);

        disqueMain.enleverDisqueAleatoirement(random);
        disqueMain.enleverDisqueAleatoirement(random);

        assertThat(disqueMain.estVide()).isTrue();
    }
}
