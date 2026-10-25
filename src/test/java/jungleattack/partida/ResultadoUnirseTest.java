package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class ResultadoUnirseTest {

    @Test
    void ok_no_tiene_texto_de_error() {
        assertEquals("", ResultadoUnirse.OK.getTexto());
    }

    @Test
    void todos_los_errores_tienen_un_texto_para_el_jugador() {
        for (ResultadoUnirse r : ResultadoUnirse.values()) {
            if (r != ResultadoUnirse.OK) {
                assertFalse(r.getTexto().isEmpty(), r.name());
            }
        }
    }
}
