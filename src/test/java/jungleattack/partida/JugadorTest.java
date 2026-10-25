package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import jungleattack.modelo.Especie;
import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

class JugadorTest {

    @Test
    void al_inicio_no_tiene_id_ni_especie() {
        Jugador j = new Jugador("Ana", new CanalGuardado());

        assertEquals("Ana", j.getApodo());
        assertEquals(Constantes.ID_SIN_JUGADOR, j.getId());
        assertNull(j.getEspecie());
    }

    @Test
    void guarda_id_especie_y_zona_vista() {
        Jugador j = new Jugador("Ana", new CanalGuardado());

        j.setId(2);
        j.setEspecie(Especie.RANA);
        j.setZonaVista(7);

        assertEquals(2, j.getId());
        assertEquals(Especie.RANA, j.getEspecie());
        assertEquals(7, j.getZonaVista());
    }

    @Test
    void enviar_manda_el_mensaje_por_su_canal() {
        CanalGuardado canal = new CanalGuardado();
        Jugador j = new Jugador("Ana", canal);

        j.enviar(new Mensaje(TipoMensaje.ERROR));

        assertEquals(1, canal.getMensajes().size());
        assertEquals(TipoMensaje.ERROR, canal.getMensajes().get(0).getTipo());
    }
}
