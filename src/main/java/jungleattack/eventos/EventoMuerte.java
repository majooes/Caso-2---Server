package jungleattack.eventos;

import jungleattack.red.Mensaje;

/** Un animal se quedó sin energía. */
public class EventoMuerte extends Evento {

    private final int idAnimal;
    private final boolean esBot;
    private final int duenio;

    /** duenio es SIN_JUGADOR, JUGADOR_A o JUGADOR_B. */
    public EventoMuerte(long tiempoMs, int idAnimal, boolean esBot, int duenio) {
        super(tiempoMs);
        this.idAnimal = idAnimal;
        this.esBot = esBot;
        this.duenio = duenio;
    }

    public int getIdAnimal() { return idAnimal; }
    public boolean isEsBot() { return esBot; }
    public int getDuenio() { return duenio; }

    /** El cliente ve que el animal desaparece del estado, no necesita un mensaje aparte. */
    @Override
    public Mensaje aMensaje() {
        return null;
    }
}
