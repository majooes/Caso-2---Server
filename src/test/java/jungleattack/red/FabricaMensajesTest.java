package jungleattack.red;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import jungleattack.eventos.Resumen;
import jungleattack.modelo.Animal;
import jungleattack.modelo.AnimalBot;
import jungleattack.modelo.AnimalJugador;
import jungleattack.modelo.Especie;
import jungleattack.modelo.Manada;
import jungleattack.modelo.Punto;
import org.junit.jupiter.api.Test;

class FabricaMensajesTest {

    @Test
    void error_lleva_el_texto() {
        Mensaje m = FabricaMensajes.error("Algo salió mal");

        assertEquals(TipoMensaje.ERROR, m.getTipo());
        assertEquals("Algo salió mal", m.getCarga().get("mensaje").getAsString());
    }

    @Test
    void partida_creada_y_unida_llevan_el_codigo() {
        assertEquals("A7K2", FabricaMensajes.partidaCreada("A7K2").getCarga().get("codigo").getAsString());
        assertEquals(TipoMensaje.PARTIDA_UNIDA, FabricaMensajes.partidaUnida("A7K2").getTipo());
    }

    @Test
    void seleccion_lista_los_nombres_de_las_especies_libres() {
        List<Especie> libres = new ArrayList<>();
        libres.add(Especie.JAGUAR);
        libres.add(Especie.TUCAN);

        JsonArray disponibles = FabricaMensajes.seleccion(libres).getCarga().getAsJsonArray("disponibles");

        assertEquals(2, disponibles.size());
        assertEquals("JAGUAR", disponibles.get(0).getAsString());
    }

    @Test
    void partida_iniciada_lleva_la_zona() {
        assertEquals(5, FabricaMensajes.partidaIniciada(5).getCarga().get("zona").getAsInt());
    }

    @Test
    void animal_incluye_todos_los_datos_que_lee_el_cliente() {
        Manada bots = new Manada(Especie.RANA, true, 1);
        Animal bot = new AnimalBot(4, new Punto(0, 0), 10, bots);

        JsonObject json = FabricaMensajes.animal(bot, false, new Punto(120, 80));

        assertEquals(4, json.get("id").getAsInt());
        assertEquals("RANA", json.get("especie").getAsString());
        assertFalse(json.get("esMio").getAsBoolean());
        assertTrue(json.get("esBot").getAsBoolean());
        assertEquals(10, json.get("energia").getAsInt());
        assertEquals(120.0, json.get("x").getAsDouble(), 0.0001);
        assertEquals(80.0, json.get("y").getAsDouble(), 0.0001);
    }

    @Test
    void objeto_lleva_tipo_y_posicion() {
        JsonObject json = FabricaMensajes.objeto("TAZA", new Punto(10, 20));

        assertEquals("TAZA", json.get("tipo").getAsString());
        assertEquals(20.0, json.get("y").getAsDouble(), 0.0001);
    }

    @Test
    void estado_incluye_las_estadisticas_de_la_manada_propia() {
        Manada propia = new Manada(Especie.JAGUAR, false, 2);
        propia.registrarTaza();
        propia.registrarBotEliminado();
        propia.agregarAnimal(new AnimalJugador(1, new Punto(0, 0), 20, propia));
        List<Integer> zonas = new ArrayList<>();
        zonas.add(3);

        Mensaje m = FabricaMensajes.estado(3, zonas, propia, new JsonArray(), new JsonArray());

        assertEquals(TipoMensaje.ACTUALIZACION_ESTADO, m.getTipo());
        assertEquals(3, m.getCarga().get("zona").getAsInt());
        assertEquals(1, m.getCarga().getAsJsonArray("zonasConAnimales").size());
        assertEquals(3, m.getCarga().get("capacidadDanio").getAsInt());
        assertEquals(1, m.getCarga().get("tazasConsumidas").getAsInt());
        assertEquals(1, m.getCarga().get("botsEliminados").getAsInt());
    }

    @Test
    void estado_sin_manada_propia_no_falla() {
        Mensaje m = FabricaMensajes.estado(0, new ArrayList<Integer>(), null, new JsonArray(), new JsonArray());

        assertFalse(m.getCarga().has("capacidadDanio"));
    }

    @Test
    void fin_lleva_el_resumen_completo() {
        Resumen r = new Resumen();
        r.sumarBotEliminado();
        r.sumarTaza(1);
        r.sumarAnimalPerdido(2);
        r.terminar(1, 90000);

        JsonObject carga = FabricaMensajes.fin(r, "Ana", "Luis", "Ana").getCarga();

        assertEquals(1, carga.get("botsEliminados").getAsInt());
        assertEquals(1, carga.get("tazasA").getAsInt());
        assertEquals(1, carga.get("animalesPerdidosB").getAsInt());
        assertEquals(90000, carga.get("duracionMs").getAsLong());
        assertEquals("Ana", carga.get("apodoA").getAsString());
        assertEquals("Ana", carga.get("ganador").getAsString());
    }
}
