package jungleattack.partida;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Patrón Singleton: hay un solo gestor en todo el servidor. Guarda las partidas activas
 * indexadas por su código y se encarga de que no haya dos con el mismo.
 */
public class GestorPartidas {

    private static final GestorPartidas INSTANCIA = new GestorPartidas();

    private final Map<String, Partida> partidas = new ConcurrentHashMap<>();
    private final Random azar = new Random();

    private GestorPartidas() {
        // Privado: solo se usa obtenerInstancia().
    }

    public static GestorPartidas obtenerInstancia() {
        return INSTANCIA;
    }

    /** Crea una partida nueva con un código único y mete a su creador como primer jugador. */
    public synchronized Partida crearPartida(Jugador creador) {
        String codigo = GeneradorCodigo.generar(partidas.keySet(), azar);
        if (codigo == null) {
            return null; // ya no quedan códigos libres
        }
        Partida partida = new Partida(codigo, new Random());
        partida.unirJugador(creador);
        partidas.put(codigo, partida);
        return partida;
    }

    /** Busca una partida por código, sin importar mayúsculas. Devuelve null si no existe. */
    public Partida buscarPartida(String codigo) {
        if (codigo == null) {
            return null;
        }
        return partidas.get(codigo.trim().toUpperCase());
    }

    public void eliminarPartida(String codigo) {
        partidas.remove(codigo);
    }

    public int cantidadPartidas() {
        return partidas.size();
    }
}
