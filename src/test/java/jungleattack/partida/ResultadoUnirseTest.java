package jungleattack.partida;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultadoUnirseTest {

    @Test
    void tiene_los_cuatro_resultados() {
        assertEquals(4, ResultadoUnirse.values().length);
        assertEquals(ResultadoUnirse.PARTIDA_LLENA, ResultadoUnirse.valueOf("PARTIDA_LLENA"));
    }

    @Test
    void ok_no_tiene_mensaje_y_los_errores_si() {
        assertTrue(ResultadoUnirse.OK.getMensaje().isEmpty());
        assertFalse(ResultadoUnirse.CODIGO_INVALIDO.getMensaje().isEmpty());
        assertFalse(ResultadoUnirse.PARTIDA_LLENA.getMensaje().isEmpty());
        assertFalse(ResultadoUnirse.ESPECIE_TOMADA.getMensaje().isEmpty());
    }
}
