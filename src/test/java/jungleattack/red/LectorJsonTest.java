package jungleattack.red;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LectorJsonTest {

    private JsonObject objeto;

    @BeforeEach
    void preparar() {
        objeto = new JsonObject();
        objeto.addProperty("texto", "hola");
        objeto.addProperty("numero", 7);
        objeto.addProperty("decimal", 2.5);
    }

    @Test
    void lee_los_valores_cuando_son_del_tipo_correcto() {
        assertEquals("hola", LectorJson.leerTexto(objeto, "texto", "x"));
        assertEquals(7, LectorJson.leerEntero(objeto, "numero", -1));
        assertEquals(2.5, LectorJson.leerDouble(objeto, "decimal", 0), 0.0001);
    }

    @Test
    void si_falta_el_campo_devuelve_el_valor_por_defecto() {
        assertEquals("x", LectorJson.leerTexto(objeto, "nada", "x"));
        assertEquals(-1, LectorJson.leerEntero(objeto, "nada", -1));
        assertEquals(9.0, LectorJson.leerDouble(objeto, "nada", 9.0), 0.0001);
    }

    @Test
    void si_el_tipo_es_equivocado_devuelve_el_valor_por_defecto() {
        assertEquals("x", LectorJson.leerTexto(objeto, "numero", "x"));
        assertEquals(-1, LectorJson.leerEntero(objeto, "texto", -1));
        assertEquals(0.0, LectorJson.leerDouble(objeto, "texto", 0.0), 0.0001);
    }

    @Test
    void la_lista_de_enteros_ignora_lo_que_no_es_numero() {
        JsonArray arreglo = new JsonArray();
        arreglo.add(3);
        arreglo.add("raro");
        arreglo.add(8);
        objeto.add("ids", arreglo);

        List<Integer> ids = LectorJson.leerEnteros(objeto, "ids");

        assertEquals(2, ids.size());
        assertEquals(8, ids.get(1));
    }

    @Test
    void una_lista_que_no_existe_o_no_es_arreglo_queda_vacia() {
        assertEquals(0, LectorJson.leerEnteros(objeto, "nada").size());
        assertEquals(0, LectorJson.leerEnteros(objeto, "texto").size());
    }
}
