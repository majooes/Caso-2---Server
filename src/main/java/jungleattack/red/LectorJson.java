package jungleattack.red;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee datos de un mensaje del cliente. Si falta un campo o trae un tipo equivocado
 * devuelve el valor por defecto, así un cliente mal hecho no puede romper el servidor.
 */
public final class LectorJson {

    private LectorJson() {
        // Solo métodos estáticos.
    }

    public static String leerTexto(JsonObject objeto, String clave, String defecto) {
        JsonElement e = objeto.get(clave);
        if (e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isString()) {
            return e.getAsString();
        }
        return defecto;
    }

    public static int leerEntero(JsonObject objeto, String clave, int defecto) {
        JsonElement e = objeto.get(clave);
        if (e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber()) {
            return e.getAsInt();
        }
        return defecto;
    }

    public static double leerDouble(JsonObject objeto, String clave, double defecto) {
        JsonElement e = objeto.get(clave);
        if (e != null && e.isJsonPrimitive() && e.getAsJsonPrimitive().isNumber()) {
            return e.getAsDouble();
        }
        return defecto;
    }

    /** Lista de enteros; los elementos que no sean números se ignoran. */
    public static List<Integer> leerEnteros(JsonObject objeto, String clave) {
        List<Integer> numeros = new ArrayList<>();
        JsonElement e = objeto.get(clave);
        if (e != null && e.isJsonArray()) {
            for (JsonElement item : e.getAsJsonArray()) {
                if (item.isJsonPrimitive() && item.getAsJsonPrimitive().isNumber()) {
                    numeros.add(item.getAsInt());
                }
            }
        }
        return numeros;
    }
}
