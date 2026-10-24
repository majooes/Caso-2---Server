package jungleattack.logica;

import jungleattack.modelo.Animal;
import jungleattack.modelo.Manada;
import jungleattack.util.Constantes;

/** Reglas que se aplican alrededor de un ataque: bots eliminados y bono de energía. */
public class Combate {

    /**
     * El atacante intenta golpear al objetivo. Si con ese golpe mata a un bot,
     * se cuenta para su manada y cada 2 bots eliminados toda la manada gana energía.
     * Devuelve true si el ataque se realizó.
     */
    public boolean atacar(Animal atacante, Animal objetivo, long ahoraMs) {
        boolean ataco = atacante.atacar(objetivo, ahoraMs);
        boolean mataBot = ataco && !objetivo.estaVivo() && objetivo.getManada().isEsBot();
        if (mataBot) {
            Manada manada = atacante.getManada();
            manada.registrarBotEliminado();
            if (manada.getBotsEliminados() % Constantes.BOTS_POR_BONO_ENERGIA == 0) {
                aplicarBonoEnergia(manada);
            }
        }
        return ataco;
    }

    /** Cada animal vivo de la manada gana 1 de energía, sin pasar del máximo (20). */
    public void aplicarBonoEnergia(Manada manada) {
        for (Animal a : manada.obtenerVivos()) {
            a.ganarEnergia(Constantes.BONO_ENERGIA, Constantes.MAX_ENERGIA_JUGADOR);
        }
    }

    public boolean estanEnRango(Animal a, Animal b) {
        return a.distanciaA(b) <= Constantes.RADIO_ENCUENTRO;
    }
}
