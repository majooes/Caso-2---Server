package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

class EspecieTest {

    @Test
    void hayTantasEspeciesComoDiceLaConstante() {
        assertEquals(Constantes.TOTAL_ESPECIES, Especie.values().length);
    }

    @Test
    void cadaEspecieTieneNombreVisible() {
        assertEquals("Jaguar", Especie.JAGUAR.getNombre());
    }
}
