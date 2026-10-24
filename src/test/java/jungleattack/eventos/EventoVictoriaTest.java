package jungleattack.eventos;

import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoVictoriaTest {

    @Test
    void se_convierte_en_fin_partida_con_el_resumen() {
        Resumen resumen = new Resumen(4, 3, 5, 2, 1, 90000);
        EventoVictoria e = new EventoVictoria(90000, "Ana", "Ana", "Luis", resumen);

        Mensaje m = e.aMensaje();

        assertEquals(TipoMensaje.FIN_PARTIDA, m.getTipo());
        assertEquals(4, m.getCarga().get("botsEliminados").getAsInt());
        assertEquals(5, m.getCarga().get("animalesPerdidosB").getAsInt());
        assertEquals(2, m.getCarga().get("tazasA").getAsInt());
        assertEquals(90000, m.getCarga().get("duracionMs").getAsLong());
        assertEquals("Luis", m.getCarga().get("apodoB").getAsString());
        assertEquals("Ana", m.getCarga().get("ganador").getAsString());
    }

    @Test
    void guarda_ganador_y_resumen() {
        Resumen resumen = new Resumen(0, 0, 0, 0, 0, 10);
        EventoVictoria e = new EventoVictoria(10, "Luis", "Ana", "Luis", resumen);

        assertEquals("Luis", e.getGanador());
        assertEquals(resumen, e.getResumen());
    }
}
