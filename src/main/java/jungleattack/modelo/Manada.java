package jungleattack.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jungleattack.util.Constantes;

/** Grupo de animales de una misma especie. Comparten la capacidad de daño. */
public class Manada {

    private final Especie especie;
    private final boolean esBot;
    private final List<Animal> miembros = new ArrayList<>();
    private int capacidadDanio;
    private int botsEliminados;
    private int tazasConsumidas;
    private int idJugador = Constantes.ID_SIN_JUGADOR;

    public Manada(Especie especie, boolean esBot, int capacidadDanio) {
        this.especie = especie;
        this.esBot = esBot;
        this.capacidadDanio = capacidadDanio;
    }

    public void agregarAnimal(Animal animal) {
        miembros.add(animal);
    }

    /** Una taza de café: todos los animales de la manada hacen 1 punto más de daño. */
    public void sumarDanio() {
        capacidadDanio++;
    }

    public void registrarBotEliminado() {
        botsEliminados++;
    }

    public void registrarTaza() {
        tazasConsumidas++;
        sumarDanio();
    }

    public List<Animal> obtenerVivos() {
        List<Animal> vivos = new ArrayList<>();
        for (Animal a : miembros) {
            if (a.estaVivo()) {
                vivos.add(a);
            }
        }
        return vivos;
    }

    public int contarMuertos() {
        int muertos = 0;
        for (Animal a : miembros) {
            if (!a.estaVivo()) {
                muertos++;
            }
        }
        return muertos;
    }

    public Especie getEspecie() { return especie; }
    public boolean isEsBot() { return esBot; }
    public int getCapacidadDanio() { return capacidadDanio; }
    public int getBotsEliminados() { return botsEliminados; }
    public int getTazasConsumidas() { return tazasConsumidas; }
    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }
    public List<Animal> getMiembros() { return Collections.unmodifiableList(miembros); }
}
