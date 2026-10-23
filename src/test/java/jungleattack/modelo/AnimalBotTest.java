package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnimalBotTest {

    private Manada manadaBot;
    private Manada manadaJugador;
    private Animal bot;
    private Animal jugador;

    @BeforeEach
    void preparar() {
        manadaBot = new Manada(Especie.RANA, true, 1);
        manadaJugador = new Manada(Especie.JAGUAR, false, 2);
        bot = new AnimalBot(1, new Punto(0, 0), 10, manadaBot);
        jugador = new AnimalJugador(2, new Punto(5, 0), 20, manadaJugador);
    }

    @Test
    void el_bot_ataca_solo_a_un_animal_de_jugador_cercano() {
        assertTrue(bot.reaccionarA(jugador, 0));

        assertEquals(19, jugador.getEnergia());
    }

    @Test
    void el_bot_respeta_el_enfriamiento() {
        assertTrue(bot.reaccionarA(jugador, 0));
        assertFalse(bot.reaccionarA(jugador, 300));
    }

    @Test
    void el_bot_no_ataca_si_el_jugador_esta_lejos() {
        jugador.setPosicion(new Punto(900, 900));

        assertFalse(bot.reaccionarA(jugador, 0));
    }

    @Test
    void los_bots_no_se_atacan_entre_ellos() {
        Animal otroBot = new AnimalBot(3, new Punto(1, 0), 10, new Manada(Especie.TAPIR, true, 1));

        assertFalse(bot.reaccionarA(otroBot, 0));
        assertEquals(10, otroBot.getEnergia());
    }

    @Test
    void cuatro_bots_juntos_hacen_cuatro_de_danio_por_segundo() {
        Animal b2 = new AnimalBot(3, new Punto(0, 1), 10, manadaBot);
        Animal b3 = new AnimalBot(4, new Punto(1, 0), 10, manadaBot);
        Animal b4 = new AnimalBot(5, new Punto(1, 1), 10, manadaBot);

        bot.reaccionarA(jugador, 0);
        b2.reaccionarA(jugador, 0);
        b3.reaccionarA(jugador, 0);
        b4.reaccionarA(jugador, 0);

        assertEquals(16, jugador.getEnergia());
    }
}
