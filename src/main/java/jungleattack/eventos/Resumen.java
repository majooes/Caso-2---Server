package jungleattack.eventos;

import jungleattack.util.Constantes;

/** Estadísticas de la partida. Se llena recorriendo los eventos (ver Evento.aplicarA). */
public class Resumen {

    private int botsEliminados;
    private int animalesPerdidosA;
    private int animalesPerdidosB;
    private int tazasA;
    private int tazasB;
    private long duracionMs;
    private int idGanador = Constantes.ID_SIN_JUGADOR;

    public void sumarBotEliminado() {
        botsEliminados++;
    }

    /** Suma un animal perdido al jugador 1 (A) o al 2 (B). */
    public void sumarAnimalPerdido(int idJugador) {
        if (idJugador == Constantes.ID_JUGADOR_1) {
            animalesPerdidosA++;
        } else if (idJugador == Constantes.ID_JUGADOR_2) {
            animalesPerdidosB++;
        }
    }

    /** Suma una taza de café al jugador 1 (A) o al 2 (B). */
    public void sumarTaza(int idJugador) {
        if (idJugador == Constantes.ID_JUGADOR_1) {
            tazasA++;
        } else if (idJugador == Constantes.ID_JUGADOR_2) {
            tazasB++;
        }
    }

    public void terminar(int idGanador, long duracionMs) {
        this.idGanador = idGanador;
        this.duracionMs = duracionMs;
    }

    public int getBotsEliminados() { return botsEliminados; }
    public int getAnimalesPerdidosA() { return animalesPerdidosA; }
    public int getAnimalesPerdidosB() { return animalesPerdidosB; }
    public int getTazasA() { return tazasA; }
    public int getTazasB() { return tazasB; }
    public long getDuracionMs() { return duracionMs; }
    public int getIdGanador() { return idGanador; }
}
