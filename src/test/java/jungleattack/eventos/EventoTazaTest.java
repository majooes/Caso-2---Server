package jungleattack.eventos;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventoTazaTest {

    @Test
    void suma_una_taza_al_jugador() {
        EventoTaza evento = new EventoTaza(1200, Constantes.ID_JUGADOR_2);
        Resumen resumen = new Resumen();

        assertEquals(1200, evento.getTiempoMs());
        evento.aplicarA(resumen);

        assertEquals(0, resumen.getTazasA());
        assertEquals(1, resumen.getTazasB());
    }
}
