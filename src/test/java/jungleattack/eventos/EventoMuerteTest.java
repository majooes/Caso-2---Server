package jungleattack.eventos;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoMuerteTest {

    @Test
    void una_muerte_de_bot_suma_un_bot_eliminado() {
        EventoMuerte evento = new EventoMuerte(900, true, Constantes.ID_SIN_JUGADOR);
        Resumen resumen = new Resumen();

        assertEquals(900, evento.getTiempoMs());
        evento.aplicarA(resumen);

        assertEquals(1, resumen.getBotsEliminados());
        assertEquals(0, resumen.getAnimalesPerdidosA());
    }

    @Test
    void una_muerte_de_jugador_suma_un_animal_perdido() {
        EventoMuerte evento = new EventoMuerte(0, false, Constantes.ID_JUGADOR_1);
        Resumen resumen = new Resumen();

        evento.aplicarA(resumen);

        assertEquals(0, resumen.getBotsEliminados());
        assertEquals(1, resumen.getAnimalesPerdidosA());
    }
}
