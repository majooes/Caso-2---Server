package jungleattack.eventos;

/** Números finales de la partida. A es el jugador 1 y B el jugador 2. */
public class Resumen {

    private final int botsEliminados;
    private final int animalesPerdidosA;
    private final int animalesPerdidosB;
    private final int tazasA;
    private final int tazasB;
    private final long duracionMs;

    public Resumen(int botsEliminados, int animalesPerdidosA, int animalesPerdidosB,
                   int tazasA, int tazasB, long duracionMs) {
        this.botsEliminados = botsEliminados;
        this.animalesPerdidosA = animalesPerdidosA;
        this.animalesPerdidosB = animalesPerdidosB;
        this.tazasA = tazasA;
        this.tazasB = tazasB;
        this.duracionMs = duracionMs;
    }

    public int getBotsEliminados() { return botsEliminados; }
    public int getAnimalesPerdidosA() { return animalesPerdidosA; }
    public int getAnimalesPerdidosB() { return animalesPerdidosB; }
    public int getTazasA() { return tazasA; }
    public int getTazasB() { return tazasB; }
    public long getDuracionMs() { return duracionMs; }
}
