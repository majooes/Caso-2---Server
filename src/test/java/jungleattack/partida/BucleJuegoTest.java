package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jungleattack.modelo.Especie;
import org.junit.jupiter.api.Test;

class BucleJuegoTest {

    private Partida partidaEnCurso(GestorPartidas gestor) {
        Partida partida = gestor.crearPartida(new Jugador("Ana", new CanalGuardado()));
        Jugador luis = new Jugador("Luis", new CanalGuardado());
        partida.unirJugador(luis);
        partida.elegirEspecie(partida.getJugadores().get(0), Especie.JAGUAR);
        partida.elegirEspecie(luis, Especie.TUCAN);
        return partida;
    }

    @Test
    void con_una_partida_terminada_el_bucle_sale_enseguida_y_la_quita_del_gestor() {
        GestorPartidas gestor = GestorPartidas.obtenerInstancia();
        Partida partida = partidaEnCurso(gestor);
        partida.jugadorDesconectado(partida.getJugadores().get(0));

        new BucleJuego(partida, gestor).run();

        assertNull(gestor.buscarPartida(partida.getCodigo()));
    }

    @Test
    void mientras_corre_hace_avanzar_el_tiempo_de_la_partida() throws Exception {
        GestorPartidas gestor = GestorPartidas.obtenerInstancia();
        Partida partida = partidaEnCurso(gestor);
        BucleJuego bucle = new BucleJuego(partida, gestor);
        Thread hilo = new Thread(bucle);

        hilo.start();
        Thread.sleep(300);
        bucle.detener();
        hilo.join(2000);

        assertTrue(partida.getTiempoMs() > 0);
        assertNull(gestor.buscarPartida(partida.getCodigo()));
    }
}
