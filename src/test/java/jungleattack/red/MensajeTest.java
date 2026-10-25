package jungleattack.red;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

class MensajeTest {

    @Test
    void ida_y_vuelta_conserva_tipo_y_carga() {
        JsonObject carga = new JsonObject();
        carga.addProperty("apodo", "Ana");
        Mensaje original = new Mensaje(TipoMensaje.CREAR_PARTIDA, carga);

        Mensaje copia = Mensaje.desdeJson(original.aJson());

        assertEquals(TipoMensaje.CREAR_PARTIDA, copia.getTipo());
        assertEquals("Ana", copia.getCarga().get("apodo").getAsString());
    }

    @Test
    void cada_tipo_de_mensaje_sobrevive_a_la_ida_y_vuelta() {
        for (TipoMensaje tipo : TipoMensaje.values()) {
            Mensaje copia = Mensaje.desdeJson(new Mensaje(tipo).aJson());

            assertNotNull(copia, tipo.name());
            assertEquals(tipo, copia.getTipo());
        }
    }

    @Test
    void json_invalido_devuelve_null() {
        assertNull(Mensaje.desdeJson("esto no es json"));
        assertNull(Mensaje.desdeJson(null));
        assertNull(Mensaje.desdeJson("[1,2,3]"));
    }

    @Test
    void tipo_desconocido_devuelve_null() {
        assertNull(Mensaje.desdeJson("{\"type\":\"INVENTADO\",\"payload\":{}}"));
    }

    @Test
    void sin_carga_usa_objeto_vacio() {
        Mensaje m = Mensaje.desdeJson("{\"type\":\"ERROR\"}");

        assertNotNull(m.getCarga());
        assertEquals(0, m.getCarga().size());
    }

    @Test
    void buscar_devuelve_null_si_el_tipo_no_existe() {
        assertNull(TipoMensaje.buscar("NADA"));
        assertEquals(TipoMensaje.ATACAR, TipoMensaje.buscar("ATACAR"));
    }
}
