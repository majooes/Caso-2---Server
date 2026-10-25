package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

    @Test
    void buscar_encuentra_la_especie_o_devuelve_null() {
        assertEquals(Especie.TUCAN, Especie.buscar("TUCAN"));
        assertNull(Especie.buscar("DRAGON"));
        assertNull(Especie.buscar(null));
    }
}
