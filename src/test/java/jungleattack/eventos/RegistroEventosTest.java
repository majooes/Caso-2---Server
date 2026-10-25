package jungleattack.eventos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistroEventosTest {

    private RegistroEventos registro;

    @BeforeEach
    void preparar() {
        registro = new RegistroEventos();
    }

    @Test
    void un_registro_vacio_da_un_resumen_en_cero() {
        Resumen r = registro.construirResumen();

        assertEquals(0, r.getBotsEliminados());
        assertEquals(0, r.getDuracionMs());
        assertEquals(Constantes.ID_SIN_JUGADOR, r.getIdGanador());
    }

    @Test
    void las_muertes_de_bots_y_de_animales_se_cuentan_por_separado() {
        registro.agregar(new EventoMuerte(100, true, Constantes.ID_SIN_JUGADOR));
        registro.agregar(new EventoMuerte(200, true, Constantes.ID_SIN_JUGADOR));
        registro.agregar(new EventoMuerte(300, false, Constantes.ID_JUGADOR_1));
        registro.agregar(new EventoMuerte(400, false, Constantes.ID_JUGADOR_2));
        registro.agregar(new EventoMuerte(500, false, Constantes.ID_JUGADOR_2));

        Resumen r = registro.construirResumen();

        assertEquals(2, r.getBotsEliminados());
        assertEquals(1, r.getAnimalesPerdidosA());
        assertEquals(2, r.getAnimalesPerdidosB());
    }

    @Test
    void las_tazas_se_cuentan_por_jugador() {
        registro.agregar(new EventoTaza(100, Constantes.ID_JUGADOR_1));
        registro.agregar(new EventoTaza(200, Constantes.ID_JUGADOR_1));
        registro.agregar(new EventoTaza(300, Constantes.ID_JUGADOR_2));

        Resumen r = registro.construirResumen();

        assertEquals(2, r.getTazasA());
        assertEquals(1, r.getTazasB());
    }

    @Test
    void la_victoria_guarda_el_ganador_y_la_duracion() {
        registro.agregar(new EventoVictoria(90000, Constantes.ID_JUGADOR_2));

        Resumen r = registro.construirResumen();

        assertEquals(Constantes.ID_JUGADOR_2, r.getIdGanador());
        assertEquals(90000, r.getDuracionMs());
    }

    @Test
    void los_eventos_quedan_en_orden() {
        registro.agregar(new EventoTaza(10, 1));
        registro.agregar(new EventoVictoria(20, 1));

        assertEquals(2, registro.getEventos().size());
        assertTrue(registro.getEventos().get(0) instanceof EventoTaza);
    }
}
