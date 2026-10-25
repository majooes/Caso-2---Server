package jungleattack.eventos;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ResumenTest {

    @Test
    void acumula_las_estadisticas_de_la_partida() {
        Resumen r = new Resumen();

        r.sumarBotEliminado();
        r.sumarBotEliminado();
        r.sumarAnimalPerdido(Constantes.ID_JUGADOR_1);
        r.sumarAnimalPerdido(Constantes.ID_JUGADOR_1);
        r.sumarAnimalPerdido(Constantes.ID_JUGADOR_2);
        r.sumarTaza(Constantes.ID_JUGADOR_1);
        r.sumarTaza(Constantes.ID_JUGADOR_2);
        r.terminar(Constantes.ID_JUGADOR_2, 90000);

        assertEquals(2, r.getBotsEliminados());
        assertEquals(2, r.getAnimalesPerdidosA());
        assertEquals(1, r.getAnimalesPerdidosB());
        assertEquals(1, r.getTazasA());
        assertEquals(1, r.getTazasB());
        assertEquals(90000, r.getDuracionMs());
        assertEquals(Constantes.ID_JUGADOR_2, r.getIdGanador());
    }
}
