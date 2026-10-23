package jungleattack.modelo;

import jungleattack.util.Constantes;

/** Patrón Factory: crea las manadas con los valores iniciales que pide el enunciado. */
public final class FabricaManadas {

    private FabricaManadas() {
        // No se crean objetos de esta clase.
    }

    /** Manada de jugador: 10 animales, 20 de energía, daño 2. Los ids van desde primerId. */
    public static Manada crearManadaJugador(Especie especie, int primerId) {
        Manada manada = new Manada(especie, false, Constantes.DANIO_JUGADOR);
        for (int i = 0; i < Constantes.MIEMBROS_JUGADOR; i++) {
            Animal animal = new AnimalJugador(primerId + i, new Punto(0, 0),
                    Constantes.ENERGIA_JUGADOR, manada);
            manada.agregarAnimal(animal);
        }
        return manada;
    }

    /** Manada de bot: 4 animales, 10 de energía, daño 1. Los ids van desde primerId. */
    public static Manada crearManadaBot(Especie especie, int primerId) {
        Manada manada = new Manada(especie, true, Constantes.DANIO_BOT);
        for (int i = 0; i < Constantes.MIEMBROS_BOT; i++) {
            Animal animal = new AnimalBot(primerId + i, new Punto(0, 0),
                    Constantes.ENERGIA_BOT, manada);
            manada.agregarAnimal(animal);
        }
        return manada;
    }
}
