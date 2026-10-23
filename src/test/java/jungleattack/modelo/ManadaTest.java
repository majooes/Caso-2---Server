package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ManadaTest {

    private Manada manada;
    private Animal vivo;
    private Animal muerto;

    @BeforeEach
    void preparar() {
        manada = new Manada(Especie.JAGUAR, false, 2);
        vivo = new AnimalJugador(1, new Punto(0, 0), 20, manada);
        muerto = new AnimalJugador(2, new Punto(0, 0), 20, manada);
        muerto.recibirDanio(20);
        manada.agregarAnimal(vivo);
        manada.agregarAnimal(muerto);
    }

    @Test
    void sumar_danio_sube_la_capacidad_de_toda_la_manada() {
        manada.sumarDanio();

        assertEquals(3, manada.getCapacidadDanio());
    }

    @Test
    void registrar_taza_cuenta_la_taza_y_sube_el_danio() {
        manada.registrarTaza();

        assertEquals(1, manada.getTazasConsumidas());
        assertEquals(3, manada.getCapacidadDanio());
    }

    @Test
    void registrar_bot_eliminado_suma_uno() {
        manada.registrarBotEliminado();
        manada.registrarBotEliminado();

        assertEquals(2, manada.getBotsEliminados());
    }

    @Test
    void obtener_vivos_y_contar_muertos() {
        assertEquals(1, manada.obtenerVivos().size());
        assertTrue(manada.obtenerVivos().contains(vivo));
        assertEquals(1, manada.contarMuertos());
    }

    @Test
    void al_inicio_no_tiene_jugador_asignado() {
        assertEquals(Constantes.ID_SIN_JUGADOR, manada.getIdJugador());

        manada.setIdJugador(2);

        assertEquals(2, manada.getIdJugador());
    }
}
