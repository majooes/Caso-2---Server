package jungleattack.eventos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistroEventosTest {

    private RegistroEventos registro;

    @BeforeEach
    void preparar() {
        registro = new RegistroEventos();
    }

    @Test
    void obtener_nuevos_entrega_cada_evento_una_sola_vez() {
        registro.agregar(new EventoTaza(100, 1, Evento.JUGADOR_A));
        registro.agregar(new EventoTaza(200, 2, Evento.JUGADOR_B));

        assertEquals(2, registro.obtenerNuevos().size());
        assertTrue(registro.obtenerNuevos().isEmpty());

        registro.agregar(new EventoTaza(300, 3, Evento.JUGADOR_A));
        assertEquals(1, registro.obtenerNuevos().size());
        assertEquals(3, registro.getEventos().size());
    }

    @Test
    void resumen_cuenta_bots_animales_perdidos_y_tazas() {
        registro.agregar(new EventoMuerte(100, 1, true, Evento.SIN_JUGADOR));
        registro.agregar(new EventoMuerte(200, 2, true, Evento.SIN_JUGADOR));
        registro.agregar(new EventoMuerte(300, 3, false, Evento.JUGADOR_A));
        registro.agregar(new EventoMuerte(400, 4, false, Evento.JUGADOR_B));
        registro.agregar(new EventoMuerte(500, 5, false, Evento.JUGADOR_B));
        registro.agregar(new EventoTaza(600, 1, Evento.JUGADOR_A));
        registro.agregar(new EventoTaza(700, 2, Evento.JUGADOR_A));
        registro.agregar(new EventoTaza(800, 3, Evento.JUGADOR_B));
        registro.agregar(new EventoAtaque(900, 1, 2, 2));

        Resumen r = registro.construirResumen();

        assertEquals(2, r.getBotsEliminados());
        assertEquals(1, r.getAnimalesPerdidosA());
        assertEquals(2, r.getAnimalesPerdidosB());
        assertEquals(2, r.getTazasA());
        assertEquals(1, r.getTazasB());
        assertEquals(900, r.getDuracionMs());
    }

    @Test
    void resumen_sin_eventos_esta_en_cero() {
        Resumen r = registro.construirResumen();

        assertEquals(0, r.getBotsEliminados());
        assertEquals(0, r.getDuracionMs());
    }
}
