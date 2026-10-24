package jungleattack.mapa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ZonaTest {

    private Zona zona;

    @BeforeEach
    void preparar() {
        zona = new Zona(5, new Punto(800, 600));
    }

    @Test
    void contiene_los_puntos_de_su_rectangulo() {
        assertTrue(zona.contiene(new Punto(900, 700)));
        assertFalse(zona.contiene(new Punto(100, 100)));
        assertFalse(zona.contiene(new Punto(800 + Constantes.ANCHO_ZONA, 700)));
    }

    @Test
    void el_centro_queda_en_la_mitad() {
        assertEquals(800 + Constantes.ANCHO_ZONA / 2, zona.getCentro().getX(), 0.0001);
        assertEquals(600 + Constantes.ALTO_ZONA / 2, zona.getCentro().getY(), 0.0001);
    }

    @Test
    void los_puntos_aleatorios_siempre_caen_dentro_de_la_zona() {
        Random azar = new Random(42);
        for (int i = 0; i < 100; i++) {
            assertTrue(zona.contiene(zona.puntoAleatorio(azar)));
        }
    }

    @Test
    void camino_a_devuelve_null_si_no_son_vecinas() {
        Zona otra = new Zona(6, new Punto(1600, 600));

        assertNull(zona.getCaminoA(otra));
    }

    @Test
    void vecinas_sale_de_los_caminos_agregados() {
        Zona otra = new Zona(6, new Punto(1600, 600));
        zona.agregarCamino(new Camino(zona, otra, new Punto(1600, 900)));

        assertEquals(1, zona.getVecinas().size());
        assertEquals(otra, zona.getVecinas().get(0));
        assertEquals(otra, zona.getCaminoA(otra).getDestino());
    }
}
