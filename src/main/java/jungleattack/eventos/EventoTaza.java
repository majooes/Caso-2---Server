package jungleattack.eventos;

import jungleattack.red.Mensaje;

/** Un animal consumió una taza de café. */
public class EventoTaza extends Evento {

    private final int idTaza;
    private final int duenio;

    /** duenio es el jugador (JUGADOR_A o JUGADOR_B) que consumió la taza. */
    public EventoTaza(long tiempoMs, int idTaza, int duenio) {
        super(tiempoMs);
        this.idTaza = idTaza;
        this.duenio = duenio;
    }

    public int getIdTaza() { return idTaza; }
    public int getDuenio() { return duenio; }

    /** El cliente ve la taza desaparecer y sube el contador del HUD, sin mensaje aparte. */
    @Override
    public Mensaje aMensaje() {
        return null;
    }
}
