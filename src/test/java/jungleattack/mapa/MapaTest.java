package jungleattack.mapa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.Test;

class MapaTest {

    private Mapa mapa;

    @BeforeEach
    void preparar() {
        mapa = new Mapa();
    }

    @Test
    void tiene_doce_zonas() {
        assertEquals(Constantes.TOTAL_ZONAS, mapa.getZonas().size());
    }

    @Test
    void cada_zona_tiene_entre_dos_y_cuatro_vecinas() {
        for (Zona z : mapa.getZonas()) {
            int vecinas = z.getVecinas().size();
            assertTrue(vecinas >= 2 && vecinas <= 4);
        }
    }

    @Test
    void las_esquinas_tienen_dos_vecinas_y_el_centro_cuatro() {
        assertEquals(2, mapa.getZona(0).getVecinas().size());
        assertEquals(4, mapa.getZona(5).getVecinas().size());
    }

    @Test
    void si_a_es_vecina_de_b_entonces_b_es_vecina_de_a() {
        for (Zona a : mapa.getZonas()) {
            for (Zona b : a.getVecinas()) {
                assertNotNull(b.getCaminoA(a));
            }
        }
    }

    @Test
    void get_zona_con_id_inexistente_devuelve_null() {
        assertNull(mapa.getZona(99));
    }

    @Test
    void zona_de_encuentra_la_zona_que_contiene_el_punto() {
        Zona zona = mapa.getZona(7);

        assertEquals(zona, mapa.zonaDe(zona.getCentro()));
        assertNull(mapa.zonaDe(new Punto(-10, -10)));
    }

    @Test
    void los_puntos_en_camino_siempre_estan_dentro_del_mapa() {
        Random azar = new Random(7);
        for (int i = 0; i < 100; i++) {
            Punto p = mapa.puntoAleatorioEnCamino(azar);
            assertNotNull(mapa.zonaDe(p));
        }
    }

    @Test
    void la_zona_aleatoria_es_siempre_una_zona_del_mapa() {
        Random azar = new Random(3);
        for (int i = 0; i < 50; i++) {
            assertNotNull(mapa.getZona(mapa.zonaAleatoria(azar).getId()));
        }
    }

    @Test
    void la_lista_de_zonas_no_se_puede_modificar_desde_afuera() {
        Executable intentoDeBorrar = new Executable() {
            @Override
            public void execute() {
                mapa.getZonas().clear();
            }
        };

        assertThrows(UnsupportedOperationException.class, intentoDeBorrar);
    }
}
