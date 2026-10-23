package jungleattack.modelo;

import jungleattack.util.Constantes;

/** Taza de café costarricense. La consume el primer animal que la toca y desaparece. */
public class Taza extends Entidad {

    private boolean consumida;

    public Taza(int id, Punto posicion) {
        super(id, posicion);
    }

    public void consumir() {
        consumida = true;
    }

    public boolean estaConsumida() {
        return consumida;
    }

    /** True si la taza sigue en el mapa y el animal (vivo) está dentro de su radio. */
    public boolean fueTocadaPor(Animal animal) {
        return !consumida && animal.estaVivo() && distanciaA(animal) <= Constantes.RADIO_TAZA;
    }
}
