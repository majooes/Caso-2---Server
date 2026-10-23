package jungleattack.modelo;

/** Animal controlado por un jugador: nunca ataca solo, el jugador le ordena a quién atacar. */
public class AnimalJugador extends Animal {

    public AnimalJugador(int id, Punto posicion, int energia, Manada manada) {
        super(id, posicion, energia, manada);
    }

    @Override
    public boolean reaccionarA(Animal cercano, long ahoraMs) {
        return false;
    }
}
