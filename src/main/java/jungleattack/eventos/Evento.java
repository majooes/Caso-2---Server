package jungleattack.eventos;

/** Algo que pasó en la partida. Cada tipo de evento sabe cómo afecta al resumen final. */
public abstract class Evento {

    private final long tiempoMs;

    protected Evento(long tiempoMs) {
        this.tiempoMs = tiempoMs;
    }

    /** Suma este evento a las estadísticas. */
    public abstract void aplicarA(Resumen resumen);

    public long getTiempoMs() {
        return tiempoMs;
    }
}
