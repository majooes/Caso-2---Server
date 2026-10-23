package jungleattack.util;

/**
 * Valores fijos del juego. No se instancia: todo es static final.
 */
public final class Constantes {

    // Red
    public static final int PUERTO = 5000;
    public static final int LONGITUD_CODIGO = 4;
    public static final String CARACTERES_CODIGO = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Mapa y partida
    public static final int TOTAL_ZONAS = 12;
    public static final int TOTAL_ESPECIES = 7;
    public static final int TOTAL_TAZAS = 10;
    public static final int MS_POR_TICK = 50;

    // Manada de jugador
    public static final int MIEMBROS_JUGADOR = 10;
    public static final int ENERGIA_JUGADOR = 20;
    public static final int DANIO_JUGADOR = 2;
    public static final int MAX_ENERGIA_JUGADOR = 20;

    // Manada de bot
    public static final int MIEMBROS_BOT = 4;
    public static final int ENERGIA_BOT = 10;
    public static final int DANIO_BOT = 1;

    // Reglas de juego
    public static final double RADIO_ENCUENTRO = 30.0;
    public static final double RADIO_TAZA = 30.0;
    public static final double RADIO_BANDERA = 30.0;
    public static final double VELOCIDAD_ANIMAL = 80.0;
    public static final long ENFRIAMIENTO_ATAQUE_MS = 1000;
    public static final int BOTS_POR_BONO_ENERGIA = 2;
    public static final int ID_SIN_JUGADOR = 0;

    private Constantes() {
        // Evita que se creen objetos de esta clase.
    }
}
