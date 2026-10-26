package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class GestorPartidasTest {

    private final GestorPartidas gestor = GestorPartidas.obtenerInstancia();
    private final List<String> creadas = new ArrayList<>();

    /** Crea una partida y anota su código para borrarla al terminar la prueba. */
    private Partida crear(String apodo) {
        Partida p = gestor.crearPartida(new Jugador(apodo, new CanalGuardado()));
        creadas.add(p.getCodigo());
        return p;
    }

    @AfterEach
    void limpiar() {
        for (String codigo : creadas) {
            gestor.eliminarPartida(codigo);
        }
    }

    @Test
    void es_un_singleton() {
        assertSame(GestorPartidas.obtenerInstancia(), GestorPartidas.obtenerInstancia());
    }

    @Test
    void crear_partida_mete_al_creador_como_jugador_uno() {
        Partida p = crear("Ana");

        assertEquals(1, p.getJugadores().size());
        assertEquals(1, p.getJugadores().get(0).getId());
        assertEquals(EstadoPartida.ESPERANDO_JUGADOR, p.getEstado());
    }

    @Test
    void se_puede_buscar_por_codigo_sin_importar_mayusculas_ni_espacios() {
        Partida p = crear("Ana");

        assertSame(p, gestor.buscarPartida(p.getCodigo()));
        assertSame(p, gestor.buscarPartida(" " + p.getCodigo().toLowerCase() + " "));
    }

    @Test
    void un_codigo_que_no_existe_devuelve_null() {
        assertNull(gestor.buscarPartida("ZZZZ"));
        assertNull(gestor.buscarPartida(null));
    }

    @Test
    void dos_partidas_nunca_tienen_el_mismo_codigo() {
        Partida a = crear("Ana");
        Partida b = crear("Luis");

        assertNotEquals(a.getCodigo(), b.getCodigo());
        assertNotNull(gestor.buscarPartida(a.getCodigo()));
        assertNotNull(gestor.buscarPartida(b.getCodigo()));
    }

    @Test
    void eliminar_quita_la_partida_del_gestor() {
        Partida p = crear("Ana");
        int antes = gestor.cantidadPartidas();

        gestor.eliminarPartida(p.getCodigo());

        assertNull(gestor.buscarPartida(p.getCodigo()));
        assertEquals(antes - 1, gestor.cantidadPartidas());
    }
}
