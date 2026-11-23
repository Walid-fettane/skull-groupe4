package fr.miage.skull.model;

import fr.miage.skull.exception.EnchereInvalideException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class EnchereTest {

    private static final int ENCHERE_ID = 3;
    private static final int ALICE = 1;

    private final Tour tour = mock(Tour.class);

    @Spy
    private Enchere enchere = new Enchere(ENCHERE_ID, tour);

    @Test
    void uneEnchereNeuveNAPasDEncherisseurNiDeMontant() {
        assertThat(enchere.getCurrentEnchere()).isZero();
        assertThat(enchere.getWinnerEnchereNB()).isZero();
        assertThat(enchere.getWinnerId()).isEqualTo(-1);
    }

    @Test
    void encherirAuDessusDeLEnchereCouranteEnregistreLEnchere() {
        enchere.encherir(ALICE, 2);

        assertThat(enchere.getCurrentEnchere()).isEqualTo(2);
        assertThat(enchere.getWinnerEnchereNB()).isEqualTo(2);
        assertThat(enchere.getWinnerId()).isEqualTo(ALICE);
    }

    @Test
    void encherirAvecLIdDeLEncherisseurCourantEstInvalide() {
        assertThatExceptionOfType(EnchereInvalideException.class)
                .isThrownBy(() -> enchere.encherir(-1, 2));
        assertThat(enchere.getCurrentEnchere()).isZero();
        assertThat(enchere.getWinnerId()).isEqualTo(-1);
    }

    @Test
    void encherirEnDessousOuEgalALEnchereCouranteEstInvalide() {
        assertThatExceptionOfType(EnchereInvalideException.class)
                .isThrownBy(() -> enchere.encherir(ALICE, 0));
        assertThat(enchere.getCurrentEnchere()).isZero();
        assertThat(enchere.getWinnerId()).isEqualTo(-1);
    }
}
