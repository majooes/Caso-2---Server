package jungleattack.eventos;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoVictoriaTest {

    @Test
    void registra_el_ganador_y_la_duracion() {
        EventoVictoria evento = new EventoVictoria(90000, Constantes.ID_JUGADOR_2);
        Resumen resumen = new Resumen();

        evento.aplicarA(resumen);

        assertEquals(Constantes.ID_JUGADOR_2, resumen.getIdGanador());
        assertEquals(90000, resumen.getDuracionMs());
    }
}
