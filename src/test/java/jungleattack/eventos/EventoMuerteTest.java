package jungleattack.eventos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoMuerteTest {

    @Test
    void guarda_sus_datos() {
        EventoMuerte e = new EventoMuerte(900, 5, true, Evento.SIN_JUGADOR);

        assertEquals(900, e.getTiempoMs());
        assertEquals(5, e.getIdAnimal());
        assertTrue(e.isEsBot());
        assertEquals(Evento.SIN_JUGADOR, e.getDuenio());
    }

    @Test
    void no_genera_mensaje_para_el_cliente() {
        assertNull(new EventoMuerte(0, 1, false, Evento.JUGADOR_A).aMensaje());
    }
}
