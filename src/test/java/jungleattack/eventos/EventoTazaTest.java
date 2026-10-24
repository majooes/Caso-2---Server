package jungleattack.eventos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoTazaTest {

    @Test
    void guarda_sus_datos() {
        EventoTaza e = new EventoTaza(1200, 4, Evento.JUGADOR_B);

        assertEquals(1200, e.getTiempoMs());
        assertEquals(4, e.getIdTaza());
        assertEquals(Evento.JUGADOR_B, e.getDuenio());
    }

    @Test
    void no_genera_mensaje_para_el_cliente() {
        assertNull(new EventoTaza(0, 1, Evento.JUGADOR_A).aMensaje());
    }
}
