package jungleattack.eventos;

import jungleattack.red.Mensaje;

/** Un animal golpeó a otro. */
public class EventoAtaque extends Evento {

    private final int idAtacante;
    private final int idObjetivo;
    private final int danio;

    public EventoAtaque(long tiempoMs, int idAtacante, int idObjetivo, int danio) {
        super(tiempoMs);
        this.idAtacante = idAtacante;
        this.idObjetivo = idObjetivo;
        this.danio = danio;
    }

    public int getIdAtacante() { return idAtacante; }
    public int getIdObjetivo() { return idObjetivo; }
    public int getDanio() { return danio; }

    /** El cliente se entera del ataque por el estado completo, no por un mensaje aparte. */
    @Override
    public Mensaje aMensaje() {
        return null;
    }
}
