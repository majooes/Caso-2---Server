package jungleattack.eventos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumenTest {

    @Test
    void guarda_todos_los_numeros() {
        Resumen r = new Resumen(4, 3, 5, 2, 1, 90000);

        assertEquals(4, r.getBotsEliminados());
        assertEquals(3, r.getAnimalesPerdidosA());
        assertEquals(5, r.getAnimalesPerdidosB());
        assertEquals(2, r.getTazasA());
        assertEquals(1, r.getTazasB());
        assertEquals(90000, r.getDuracionMs());
    }
}
