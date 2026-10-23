package jungleattack.modelo;

/** Animal de un bot: ataca solo a los animales de jugadores que se le acercan. */
public class AnimalBot extends Animal {

    public AnimalBot(int id, Punto posicion, int energia, Manada manada) {
        super(id, posicion, energia, manada);
    }

    @Override
    public boolean reaccionarA(Animal cercano, long ahoraMs) {
        if (cercano.getManada().isEsBot()) {
            return false; // los bots no se atacan entre ellos
        }
        return atacar(cercano, ahoraMs);
    }
}
