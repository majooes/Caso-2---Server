package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class AnimalJugadorTest {

    @Test
    void el_animal_de_jugador_nunca_ataca_solo() {
        Manada manada = new Manada(Especie.JAGUAR, false, 2);
        Animal propio = new AnimalJugador(1, new Punto(0, 0), 20, manada);
        Animal enemigo = new AnimalBot(2, new Punto(1, 0), 10, new Manada(Especie.RANA, true, 1));

        assertFalse(propio.reaccionarA(enemigo, 0));
        assertEquals(10, enemigo.getEnergia());
    }
}
