package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

class GeneradorCodigoTest {

    @Test
    void el_codigo_tiene_cuatro_caracteres_validos() {
        Random azar = new Random(1);
        for (int i = 0; i < 200; i++) {
            String codigo = GeneradorCodigo.generar(new HashSet<String>(), azar);

            assertEquals(Constantes.LONGITUD_CODIGO, codigo.length());
            for (int j = 0; j < codigo.length(); j++) {
                assertTrue(Constantes.CARACTERES_CODIGO.indexOf(codigo.charAt(j)) >= 0);
            }
        }
    }

    @Test
    void con_la_misma_semilla_sale_el_mismo_codigo() {
        String a = GeneradorCodigo.generar(new HashSet<String>(), new Random(5));
        String b = GeneradorCodigo.generar(new HashSet<String>(), new Random(5));

        assertEquals(a, b);
    }

    @Test
    void nunca_repite_un_codigo_que_ya_se_usa() {
        Set<String> usados = new HashSet<>();
        usados.add(GeneradorCodigo.generar(new HashSet<String>(), new Random(5)));

        String nuevo = GeneradorCodigo.generar(usados, new Random(5));

        assertNotEquals(usados.iterator().next(), nuevo);
    }

    @Test
    void mil_codigos_seguidos_son_todos_distintos() {
        Set<String> usados = new HashSet<>();
        Random azar = new Random(9);
        for (int i = 0; i < 1000; i++) {
            usados.add(GeneradorCodigo.generar(usados, azar));
        }

        assertEquals(1000, usados.size());
    }
}
