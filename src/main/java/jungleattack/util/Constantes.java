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
    public static final double MS_POR_SEGUNDO = 1000.0;
    public static final int COLUMNAS_MAPA = 4;
    public static final int FILAS_MAPA = 3;
    public static final double ANCHO_ZONA = 800.0;
    public static final double ALTO_ZONA = 600.0;
    public static final double MARGEN_ZONA = 60.0;
    public static final double TRAYECTO_MIN = 0.1;
    public static final double TRAYECTO_MAX = 0.9;

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
    public static final int BONO_ENERGIA = 1;

    // Claves del JSON (deben coincidir con las del cliente)
    public static final String JSON_TIPO = "type";
    public static final String JSON_CARGA = "payload";
    public static final String JSON_APODO = "apodo";
    public static final String JSON_CODIGO = "codigo";
    public static final String JSON_MENSAJE = "mensaje";
    public static final String JSON_ESPECIE = "especie";
    public static final String JSON_DISPONIBLES = "disponibles";
    public static final String JSON_ZONA = "zona";
    public static final String JSON_ZONAS_CON_ANIMALES = "zonasConAnimales";
    public static final String JSON_CAPACIDAD_DANIO = "capacidadDanio";
    public static final String JSON_TAZAS = "tazasConsumidas";
    public static final String JSON_BOTS_ELIMINADOS = "botsEliminados";
    public static final String JSON_ANIMALES = "animales";
    public static final String JSON_OBJETOS = "objetos";
    public static final String JSON_ID = "id";
    public static final String JSON_ES_MIO = "esMio";
    public static final String JSON_ES_BOT = "esBot";
    public static final String JSON_ENERGIA = "energia";
    public static final String JSON_X = "x";
    public static final String JSON_Y = "y";
    public static final String JSON_TIPO_OBJETO = "tipo";
    public static final String JSON_ANIMALES_PERDIDOS_A = "animalesPerdidosA";
    public static final String JSON_ANIMALES_PERDIDOS_B = "animalesPerdidosB";
    public static final String JSON_TAZAS_A = "tazasA";
    public static final String JSON_TAZAS_B = "tazasB";
    public static final String JSON_APODO_A = "apodoA";
    public static final String JSON_APODO_B = "apodoB";
    public static final String JSON_DURACION_MS = "duracionMs";
    public static final String JSON_GANADOR = "ganador";
    public static final String JSON_ANIMAL_IDS = "animalIds";
    public static final String JSON_DESTINO_X = "targetX";
    public static final String JSON_DESTINO_Y = "targetY";
    public static final String JSON_OBJETIVO_ID = "objetivoId";
    public static final String TIPO_OBJETO_TAZA = "TAZA";
    public static final String TIPO_OBJETO_BANDERA = "BANDERA";

    // Jugadores
    public static final int ID_JUGADOR_1 = 1;
    public static final int ID_JUGADOR_2 = 2;
    public static final int JUGADORES_POR_PARTIDA = 2;

    // Textos de error que se envían al cliente
    public static final String TEXTO_CODIGO_INVALIDO = "No existe una partida con ese código";
    public static final String TEXTO_PARTIDA_LLENA = "La partida ya tiene dos jugadores";
    public static final String TEXTO_ESPECIE_TOMADA = "Ese animal ya fue elegido";
    public static final String TEXTO_ESPECIE_INVALIDA = "Ese animal no existe";
    public static final String TEXTO_ESTADO_INCORRECTO = "No puedes hacer eso en este momento";
    public static final String TEXTO_RIVAL_DESCONECTADO = "El otro jugador se desconectó";

    public static final String APODO_POR_DEFECTO = "Jugador";
    public static final int ZONAS_CON_MANADA = 7;

    private Constantes() {
        // Evita que se creen objetos de esta clase.
    }
}
