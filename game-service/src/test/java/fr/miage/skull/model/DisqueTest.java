package fr.miage.skull.model;

import fr.miage.skull.DisqueType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DisqueTest {

    @Test
    void unDisqueNeufNEstPasRevele() {
        Disque disque = new Disque(DisqueType.FLEUR);

        assertThat(disque.estRevele()).isFalse();
        assertThat(disque.estPasRevele()).isTrue();
    }

    @Test
    void revelerUnDisqueChangeSonEtatSansChangerSonType() {
        Disque disque = new Disque(DisqueType.SKULL);

        disque.setRevele(true);

        assertThat(disque.estRevele()).isTrue();
        assertThat(disque.estPasRevele()).isFalse();
        assertThat(disque.getType()).isEqualTo(DisqueType.SKULL);
    }

    @Test
    void cacherUnDisqueDejaReveleLeRemetPasRevele() {
        Disque disque = new Disque(DisqueType.FLEUR);
        disque.setRevele(true);

        disque.setRevele(false);

        assertThat(disque.estRevele()).isFalse();
        assertThat(disque.estPasRevele()).isTrue();
    }

    @Test
    void chaqueTypeDeDisqueEstConserve() {
        assertThat(new Disque(DisqueType.FLEUR).getType()).isEqualTo(DisqueType.FLEUR);
        assertThat(new Disque(DisqueType.SKULL).getType()).isEqualTo(DisqueType.SKULL);
    }
}
