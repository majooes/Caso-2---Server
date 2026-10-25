package jungleattack.eventos;

/** Un jugador ganó la partida. */
public class EventoVictoria extends Evento {

    private final int idGanador;

    public EventoVictoria(long tiempoMs, int idGanador) {
        super(tiempoMs);
        this.idGanador = idGanador;
    }

    @Override
    public void aplicarA(Resumen resumen) {
        resumen.terminar(idGanador, getTiempoMs());
    }
}
