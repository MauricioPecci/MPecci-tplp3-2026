package py.edu.uc.lp3.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import py.edu.uc.lp3.exceptions.ArmaInvalidaException;

class ArmaTest {

    @Test
    void constructorSimpleDejaElFusilEnEstadoLegal() {
        Fusil fusil = new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true);

        assertThat(fusil.getNombre()).isEqualTo("AK-47");
        assertThat(fusil.isSilenciador()).isFalse();
        assertThat(fusil.getMira()).isEqualTo(1);
        assertThat(fusil.getRetroceso()).isZero();
    }

    @Test
    void constructorSobrecargadoDelFusilConservaLosAccesorios() {
        Fusil fusil = new Fusil("M4A4", 2, 3100, 33, 66, 3.1, 585, false, 4, 2, true);

        assertThat(fusil.getMira()).isEqualTo(4);
        assertThat(fusil.getRetroceso()).isEqualTo(2);
        assertThat(fusil.isSilenciador()).isTrue();
    }

    @Test
    void elDominioRechazaUnPrecioNegativo() {
        assertThatThrownBy(() -> new Humo("Humo de cortina", 8, -1))
                .isInstanceOf(ArmaInvalidaException.class)
                .hasMessageContaining("precio");
    }

    @Test
    void elDominioRechazaPrecisionFueraDeRango() {
        assertThatThrownBy(() -> new Fusil("AK-47", 1, 2700, 36, 150, 2.4, 600, true))
                .isInstanceOf(ArmaInvalidaException.class)
                .hasMessageContaining("precisión");
    }

    @Test
    void elSetterNoDejaElArmaEnEstadoIlegal() {
        Fusil fusil = new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true);

        assertThatThrownBy(() -> fusil.setDano(-5))
                .isInstanceOf(ArmaInvalidaException.class);
        assertThat(fusil.getDano()).isEqualTo(36);
    }

    @Test
    void sobrecargaDeCalcularDanoSegunDistanciaYCritico() {
        Fusil fusil = new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true);

        assertThat(fusil.calcularDano()).isEqualTo(36);
        assertThat(fusil.calcularDano(100)).isEqualTo(26);
        assertThat(fusil.calcularDano(100, true)).isEqualTo(52);
        assertThat(fusil.calcularDano(500)).isEqualTo(1);
    }

    @Test
    void sobrecargaDeAreaCoberturaDelHumo() {
        Humo humo = new Humo("Humo de cortina", 8, 300);

        assertThat(humo.areaCobertura()).isCloseTo(Math.PI * 36.0, org.assertj.core.data.Offset.offset(0.001));
        assertThat(humo.areaCobertura(2.0)).isCloseTo(Math.PI * 64.0, org.assertj.core.data.Offset.offset(0.001));
    }

    @Test
    void sobreescrituraDeDescribirEnLasDosClasesHijasIndependientes() {
        Arma fusil = new Fusil("AK-47", 1, 2700, 36, 71, 2.4, 600, true, 2, 3, false);
        Arma humo = new Humo("Humo de cortina", 8, 300);

        assertThat(fusil.describir()).startsWith("Fusil[").contains("silenciador=false");
        assertThat(humo.describir()).startsWith("Humo[").contains("radio=6.0");
        assertThat(fusil.describir()).isNotEqualTo(humo.describir());
    }

    @Test
    void laJerarquiaSostieneLaRelacionEsUn() {
        Arma francotirador = new Francotirador("AWP", 9, 4750, 115, 85, 3.7, 340,
                false, 8, 5, true, 80.0, 4);

        assertThat(francotirador).isInstanceOf(Fusil.class).isInstanceOf(Arma.class);
        assertThat(francotirador.describir()).startsWith("Francotirador[");
    }
}
