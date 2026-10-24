package jungleattack.partida;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneradorCodigoTest {

    @Test
    void el_codigo_tiene_la_longitud_y_solo_letras_mayusculas_o_numeros() {
        String codigo = GeneradorCodigo.generar(new HashSet<>(), new Random(1));

        assertEquals(Constantes.LONGITUD_CODIGO, codigo.length());
        assertTrue(codigo.matches("[A-Z0-9]+"));
    }

    @Test
    void no_repite_un_codigo_que_ya_se_usa() {
        // Con la misma semilla, el primer intento sale igual: hay que conseguir otro
        String primero = GeneradorCodigo.generar(new HashSet<>(), new Random(7));
        Set<String> usados = new HashSet<>();
        usados.add(primero);

        String nuevo = GeneradorCodigo.generar(usados, new Random(7));

        assertNotEquals(primero, nuevo);
    }

    @Test
    void con_la_misma_semilla_sale_el_mismo_codigo() {
        String a = GeneradorCodigo.generar(new HashSet<>(), new Random(3));
        String b = GeneradorCodigo.generar(new HashSet<>(), new Random(3));

        assertEquals(a, b);
    }
}
