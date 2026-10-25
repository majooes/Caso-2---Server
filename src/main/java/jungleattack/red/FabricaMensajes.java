package jungleattack.red;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.List;
import jungleattack.eventos.Resumen;
import jungleattack.modelo.Animal;
import jungleattack.modelo.Especie;
import jungleattack.modelo.Manada;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;

/** Patrón Factory: arma los mensajes que el servidor le manda al cliente. */
public final class FabricaMensajes {

    private FabricaMensajes() {
        // Solo métodos estáticos.
    }

    public static Mensaje error(String texto) {
        JsonObject carga = new JsonObject();
        carga.addProperty(Constantes.JSON_MENSAJE, texto);
        return new Mensaje(TipoMensaje.ERROR, carga);
    }

    public static Mensaje partidaCreada(String codigo) {
        return conCodigo(TipoMensaje.PARTIDA_CREADA, codigo);
    }

    public static Mensaje partidaUnida(String codigo) {
        return conCodigo(TipoMensaje.PARTIDA_UNIDA, codigo);
    }

    /** Las especies que todavía no ha elegido nadie. */
    public static Mensaje seleccion(List<Especie> libres) {
        JsonArray disponibles = new JsonArray();
        for (Especie e : libres) {
            disponibles.add(e.name());
        }
        JsonObject carga = new JsonObject();
        carga.add(Constantes.JSON_DISPONIBLES, disponibles);
        return new Mensaje(TipoMensaje.SELECCION_ANIMALES, carga);
    }

    public static Mensaje partidaIniciada(int zona) {
        JsonObject carga = new JsonObject();
        carga.addProperty(Constantes.JSON_ZONA, zona);
        return new Mensaje(TipoMensaje.PARTIDA_INICIADA, carga);
    }

    /** Un animal visible. Su posición va relativa a la zona que ve el jugador. */
    public static JsonObject animal(Animal animal, boolean esMio, Punto local) {
        JsonObject json = new JsonObject();
        json.addProperty(Constantes.JSON_ID, animal.getId());
        json.addProperty(Constantes.JSON_ESPECIE, animal.getManada().getEspecie().name());
        json.addProperty(Constantes.JSON_ES_MIO, esMio);
        json.addProperty(Constantes.JSON_ES_BOT, animal.getManada().isEsBot());
        json.addProperty(Constantes.JSON_ENERGIA, animal.getEnergia());
        json.addProperty(Constantes.JSON_X, local.getX());
        json.addProperty(Constantes.JSON_Y, local.getY());
        return json;
    }

    /** Un objeto visible (taza o bandera), con posición relativa a la zona vista. */
    public static JsonObject objeto(String tipo, Punto local) {
        JsonObject json = new JsonObject();
        json.addProperty(Constantes.JSON_TIPO_OBJETO, tipo);
        json.addProperty(Constantes.JSON_X, local.getX());
        json.addProperty(Constantes.JSON_Y, local.getY());
        return json;
    }

    /** Foto del juego para un jugador. propia puede ser null antes de empezar. */
    public static Mensaje estado(int zona, List<Integer> zonasConAnimales, Manada propia,
                                 JsonArray animales, JsonArray objetos) {
        JsonArray zonas = new JsonArray();
        for (Integer id : zonasConAnimales) {
            zonas.add(id);
        }
        JsonObject carga = new JsonObject();
        carga.addProperty(Constantes.JSON_ZONA, zona);
        carga.add(Constantes.JSON_ZONAS_CON_ANIMALES, zonas);
        if (propia != null) {
            carga.addProperty(Constantes.JSON_CAPACIDAD_DANIO, propia.getCapacidadDanio());
            carga.addProperty(Constantes.JSON_TAZAS, propia.getTazasConsumidas());
            carga.addProperty(Constantes.JSON_BOTS_ELIMINADOS, propia.getBotsEliminados());
        }
        carga.add(Constantes.JSON_ANIMALES, animales);
        carga.add(Constantes.JSON_OBJETOS, objetos);
        return new Mensaje(TipoMensaje.ACTUALIZACION_ESTADO, carga);
    }

    public static Mensaje fin(Resumen r, String apodoA, String apodoB, String ganador) {
        JsonObject carga = new JsonObject();
        carga.addProperty(Constantes.JSON_BOTS_ELIMINADOS, r.getBotsEliminados());
        carga.addProperty(Constantes.JSON_ANIMALES_PERDIDOS_A, r.getAnimalesPerdidosA());
        carga.addProperty(Constantes.JSON_ANIMALES_PERDIDOS_B, r.getAnimalesPerdidosB());
        carga.addProperty(Constantes.JSON_TAZAS_A, r.getTazasA());
        carga.addProperty(Constantes.JSON_TAZAS_B, r.getTazasB());
        carga.addProperty(Constantes.JSON_DURACION_MS, r.getDuracionMs());
        carga.addProperty(Constantes.JSON_APODO_A, apodoA);
        carga.addProperty(Constantes.JSON_APODO_B, apodoB);
        carga.addProperty(Constantes.JSON_GANADOR, ganador);
        return new Mensaje(TipoMensaje.FIN_PARTIDA, carga);
    }

    private static Mensaje conCodigo(TipoMensaje tipo, String codigo) {
        JsonObject carga = new JsonObject();
        carga.addProperty(Constantes.JSON_CODIGO, codigo);
        return new Mensaje(tipo, carga);
    }
}
