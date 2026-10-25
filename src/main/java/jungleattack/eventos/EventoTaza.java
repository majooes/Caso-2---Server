package jungleattack.eventos;

/** Un animal de un jugador recogió una taza de café. */
public class EventoTaza extends Evento {

    private final int idJugador;

    public EventoTaza(long tiempoMs, int idJugador) {
        super(tiempoMs);
        this.idJugador = idJugador;
    }

    @Override
    public void aplicarA(Resumen resumen) {
        resumen.sumarTaza(idJugador);
    }
}
