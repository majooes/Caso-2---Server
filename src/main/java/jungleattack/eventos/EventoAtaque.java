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

    @Override
    public void aplicarA(Resumen resumen) {
        // Un ataque no cambia las estadísticas finales.
    }

    public Mensaje aMensaje() {
        return null;
    }
}
