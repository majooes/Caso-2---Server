package jungleattack.eventos;

import jungleattack.red.Mensaje;

/** Algo que pasó en la partida. tiempoMs cuenta desde que empezó la partida. */
public abstract class Evento {

    /** A quién pertenece el animal o la taza: a nadie (bot), al jugador A o al B. */
    public static final int SIN_JUGADOR = 0;
    public static final int JUGADOR_A = 1;
    public static final int JUGADOR_B = 2;

    private final long tiempoMs;

    protected Evento(long tiempoMs) {
        this.tiempoMs = tiempoMs;
    }

    public long getTiempoMs() {
        return tiempoMs;
    }

    /** Mensaje para el cliente, o null si el cliente no necesita este evento. */
    public abstract Mensaje aMensaje();
}
