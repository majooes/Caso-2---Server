package jungleattack.eventos;

import com.google.gson.JsonObject;
import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;

/** Un jugador llevó su manada a la bandera rival: termina la partida. */
public class EventoVictoria extends Evento {

    // Claves del FIN_PARTIDA: deben ser iguales a las que lee el cliente
    private static final String CLAVE_BOTS = "botsEliminados";
    private static final String CLAVE_PERDIDOS_A = "animalesPerdidosA";
    private static final String CLAVE_PERDIDOS_B = "animalesPerdidosB";
    private static final String CLAVE_TAZAS_A = "tazasA";
    private static final String CLAVE_TAZAS_B = "tazasB";
    private static final String CLAVE_DURACION = "duracionMs";
    private static final String CLAVE_APODO_A = "apodoA";
    private static final String CLAVE_APODO_B = "apodoB";
    private static final String CLAVE_GANADOR = "ganador";

    private final String ganador;
    private final String apodoA;
    private final String apodoB;
    private final Resumen resumen;

    public EventoVictoria(long tiempoMs, String ganador, String apodoA, String apodoB,
                          Resumen resumen) {
        super(tiempoMs);
        this.ganador = ganador;
        this.apodoA = apodoA;
        this.apodoB = apodoB;
        this.resumen = resumen;
    }

    public String getGanador() { return ganador; }
    public Resumen getResumen() { return resumen; }

    /** Es el único evento que viaja al cliente: se manda como FIN_PARTIDA. */
    @Override
    public Mensaje aMensaje() {
        JsonObject carga = new JsonObject();
        carga.addProperty(CLAVE_BOTS, resumen.getBotsEliminados());
        carga.addProperty(CLAVE_PERDIDOS_A, resumen.getAnimalesPerdidosA());
        carga.addProperty(CLAVE_PERDIDOS_B, resumen.getAnimalesPerdidosB());
        carga.addProperty(CLAVE_TAZAS_A, resumen.getTazasA());
        carga.addProperty(CLAVE_TAZAS_B, resumen.getTazasB());
        carga.addProperty(CLAVE_DURACION, resumen.getDuracionMs());
        carga.addProperty(CLAVE_APODO_A, apodoA);
        carga.addProperty(CLAVE_APODO_B, apodoB);
        carga.addProperty(CLAVE_GANADOR, ganador);
        return new Mensaje(TipoMensaje.FIN_PARTIDA, carga);
    }
}
