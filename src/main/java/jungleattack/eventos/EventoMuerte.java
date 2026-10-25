package jungleattack.eventos;

/** Murió un animal: de un bot o de uno de los jugadores. */
public class EventoMuerte extends Evento {

    private final boolean victimaEsBot;
    private final int idJugadorVictima;

    public EventoMuerte(long tiempoMs, boolean victimaEsBot, int idJugadorVictima) {
        super(tiempoMs);
        this.victimaEsBot = victimaEsBot;
        this.idJugadorVictima = idJugadorVictima;
    }

    @Override
    public void aplicarA(Resumen resumen) {
        if (victimaEsBot) {
            resumen.sumarBotEliminado();
        } else {
            resumen.sumarAnimalPerdido(idJugadorVictima);
        }
    }
}
