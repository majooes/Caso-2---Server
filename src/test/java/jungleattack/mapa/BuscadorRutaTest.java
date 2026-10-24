package jungleattack.mapa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import jungleattack.modelo.Punto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuscadorRutaTest {

    private Mapa mapa;
    private BuscadorRuta buscador;

    @BeforeEach
    void preparar() {
        mapa = new Mapa();
        buscador = new BuscadorRuta();
    }

    @Test
    void de_una_zona_a_si_misma_la_ruta_es_solo_esa_zona() {
        Zona z = mapa.getZona(4);

        List<Zona> zonas = buscador.buscarZonas(z, z);

        assertEquals(1, zonas.size());
        assertEquals(z, zonas.get(0));
    }

    @Test
    void entre_zonas_vecinas_la_ruta_tiene_dos_zonas() {
        List<Zona> zonas = buscador.buscarZonas(mapa.getZona(0), mapa.getZona(1));

        assertEquals(2, zonas.size());
    }

    @Test
    void de_una_esquina_a_la_opuesta_cruza_la_cantidad_minima_de_zonas() {
        // De la zona 0 (arriba-izquierda) a la 11 (abajo-derecha): 3 pasos a la derecha + 2 abajo
        List<Zona> zonas = buscador.buscarZonas(mapa.getZona(0), mapa.getZona(11));

        assertEquals(6, zonas.size());
        assertEquals(mapa.getZona(0), zonas.get(0));
        assertEquals(mapa.getZona(11), zonas.get(5));
    }

    @Test
    void cada_zona_de_la_ruta_es_vecina_de_la_anterior() {
        List<Zona> zonas = buscador.buscarZonas(mapa.getZona(3), mapa.getZona(8));

        for (int i = 0; i < zonas.size() - 1; i++) {
            assertNotNull(zonas.get(i).getCaminoA(zonas.get(i + 1)));
        }
    }

    @Test
    void todas_las_zonas_se_pueden_alcanzar_desde_cualquier_otra() {
        for (Zona a : mapa.getZonas()) {
            for (Zona b : mapa.getZonas()) {
                assertTrue(buscador.buscarZonas(a, b).size() >= 1);
            }
        }
    }

    @Test
    void la_ruta_en_la_misma_zona_es_solo_el_destino() {
        Zona z = mapa.getZona(2);
        Punto destino = z.getCentro();

        List<Punto> ruta = buscador.construirRuta(z, z, destino);

        assertEquals(1, ruta.size());
        assertEquals(destino, ruta.get(0));
    }

    @Test
    void la_ruta_entre_vecinas_pasa_por_la_puerta_y_termina_en_el_destino() {
        Zona a = mapa.getZona(0);
        Zona b = mapa.getZona(1);
        Punto destino = b.getCentro();

        List<Punto> ruta = buscador.construirRuta(a, b, destino);

        assertEquals(2, ruta.size());
        assertEquals(a.getCaminoA(b).getPuerta(), ruta.get(0));
        assertEquals(destino, ruta.get(1));
    }

    @Test
    void la_ruta_larga_incluye_el_centro_de_las_zonas_intermedias() {
        Zona a = mapa.getZona(0);
        Zona c = mapa.getZona(2);
        Punto destino = c.getCentro();

        List<Punto> ruta = buscador.construirRuta(a, c, destino);

        // puerta 0->1, centro de la 1, puerta 1->2, destino
        assertEquals(4, ruta.size());
        Punto centroIntermedio = mapa.getZona(1).getCentro();
        assertEquals(centroIntermedio.getX(), ruta.get(1).getX(), 0.0001);
        assertEquals(centroIntermedio.getY(), ruta.get(1).getY(), 0.0001);
        assertEquals(destino, ruta.get(3));
    }
}
