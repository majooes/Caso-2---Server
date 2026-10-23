package jungleattack.modelo;

import jungleattack.util.Constantes;

/** Bandera de un jugador. Si un animal del rival la toca, el rival gana la partida. */
public class Bandera extends Entidad {

    private final int idJugadorDuenio;

    public Bandera(int id, Punto posicion, int idJugadorDuenio) {
        super(id, posicion);
        this.idJugadorDuenio = idJugadorDuenio;
    }

    /** Solo cuenta un animal vivo, de un jugador distinto al dueño (los bots no ganan). */
    public boolean fueTocada(Animal animal) {
        Manada manada = animal.getManada();
        boolean esDelRival = !manada.isEsBot() && manada.getIdJugador() != idJugadorDuenio;
        return esDelRival && animal.estaVivo()
                && distanciaA(animal) <= Constantes.RADIO_BANDERA;
    }

    public int getIdJugadorDuenio() {
        return idJugadorDuenio;
    }
}
