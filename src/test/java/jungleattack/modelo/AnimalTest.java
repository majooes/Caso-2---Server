package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Prueba la lógica común de Animal usando AnimalJugador. */
class AnimalTest {

    private Manada manadaA;
    private Manada manadaB;
    private Animal atacante;
    private Animal objetivo;

    @BeforeEach
    void preparar() {
        manadaA = new Manada(Especie.JAGUAR, false, 2);
        manadaB = new Manada(Especie.TUCAN, false, 2);
        atacante = new AnimalJugador(1, new Punto(0, 0), 20, manadaA);
        objetivo = new AnimalJugador(2, new Punto(10, 0), 20, manadaB);
    }

    @Test
    void recibir_danio_baja_la_energia() {
        objetivo.recibirDanio(5);

        assertEquals(15, objetivo.getEnergia());
        assertTrue(objetivo.estaVivo());
    }

    @Test
    void la_energia_nunca_baja_de_cero_y_muere_al_llegar_a_cero() {
        objetivo.recibirDanio(500);

        assertEquals(0, objetivo.getEnergia());
        assertFalse(objetivo.estaVivo());
    }

    @Test
    void ganar_energia_no_pasa_del_maximo() {
        objetivo.recibirDanio(2);

        objetivo.ganarEnergia(10, Constantes.MAX_ENERGIA_JUGADOR);

        assertEquals(Constantes.MAX_ENERGIA_JUGADOR, objetivo.getEnergia());
    }

    @Test
    void atacar_en_rango_quita_el_danio_de_la_manada() {
        boolean ataco = atacante.atacar(objetivo, 0);

        assertTrue(ataco);
        assertEquals(18, objetivo.getEnergia());
    }

    @Test
    void atacar_fuera_de_rango_no_hace_nada() {
        objetivo.setPosicion(new Punto(500, 500));

        assertFalse(atacante.atacar(objetivo, 0));
        assertEquals(20, objetivo.getEnergia());
    }

    @Test
    void solo_hay_un_ataque_por_segundo() {
        assertTrue(atacante.atacar(objetivo, 0));
        assertFalse(atacante.atacar(objetivo, 500));
        assertTrue(atacante.atacar(objetivo, Constantes.ENFRIAMIENTO_ATAQUE_MS));

        assertEquals(16, objetivo.getEnergia());
    }

    @Test
    void no_puede_atacar_un_animal_muerto() {
        atacante.recibirDanio(20);

        assertFalse(atacante.atacar(objetivo, 0));
    }

    @Test
    void camina_a_velocidad_constante_hacia_el_destino() {
        List<Punto> ruta = new ArrayList<>();
        ruta.add(new Punto(1000, 0));
        atacante.asignarRuta(ruta);

        atacante.actualizar(1000);

        assertEquals(Constantes.VELOCIDAD_ANIMAL, atacante.getPosicion().getX(), 0.0001);
        assertEquals(0.0, atacante.getPosicion().getY(), 0.0001);
        assertTrue(atacante.tieneRuta());
    }

    @Test
    void el_sobrante_del_paso_sigue_al_siguiente_punto_de_la_ruta() {
        List<Punto> ruta = new ArrayList<>();
        ruta.add(new Punto(10, 0));
        ruta.add(new Punto(10, 1000));
        atacante.asignarRuta(ruta);

        atacante.actualizar(1000); // avanza 80: 10 hacia x y 70 hacia y

        assertEquals(10.0, atacante.getPosicion().getX(), 0.0001);
        assertEquals(Constantes.VELOCIDAD_ANIMAL - 10, atacante.getPosicion().getY(), 0.0001);
    }

    @Test
    void al_llegar_al_final_de_la_ruta_se_detiene() {
        List<Punto> ruta = new ArrayList<>();
        ruta.add(new Punto(5, 0));
        atacante.asignarRuta(ruta);

        atacante.actualizar(1000);

        assertEquals(5.0, atacante.getPosicion().getX(), 0.0001);
        assertFalse(atacante.tieneRuta());
    }

    @Test
    void un_animal_muerto_no_se_mueve() {
        List<Punto> ruta = new ArrayList<>();
        ruta.add(new Punto(100, 0));
        atacante.asignarRuta(ruta);
        atacante.recibirDanio(20);

        atacante.actualizar(1000);

        assertEquals(0.0, atacante.getPosicion().getX(), 0.0001);
    }
}
