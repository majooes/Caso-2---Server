package jungleattack.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConstantesTest {

    @Test
    void elCodigoTieneCuatroCaracteres() {
        assertEquals(4, Constantes.LONGITUD_CODIGO);
    }

    @Test
    void laEnergiaInicialNoSuperaElMaximo() {
        assertTrue(Constantes.ENERGIA_JUGADOR <= Constantes.MAX_ENERGIA_JUGADOR);
    }

    @Test
    void hayCincoBotsParaSieteEspeciesYDosJugadores() {
        assertEquals(5, Constantes.TOTAL_ESPECIES - 2);
    }
}
