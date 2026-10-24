package jungleattack.eventos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoAtaqueTest {

    @Test
    void guarda_sus_datos() {
        EventoAtaque e = new EventoAtaque(500, 3, 8, 2);

        assertEquals(500, e.getTiempoMs());
        assertEquals(3, e.getIdAtacante());
        assertEquals(8, e.getIdObjetivo());
        assertEquals(2, e.getDanio());
    }

    @Test
    void no_genera_mensaje_para_el_cliente() {
        assertNull(new EventoAtaque(0, 1, 2, 1).aMensaje());
    }
}
