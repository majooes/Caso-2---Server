package jungleattack.logica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jungleattack.modelo.Animal;
import jungleattack.modelo.AnimalBot;
import jungleattack.modelo.AnimalJugador;
import jungleattack.modelo.Especie;
import jungleattack.modelo.Manada;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CombateTest {

    private Combate combate;
    private Manada manadaJugador;
    private Manada manadaBot;
    private Animal atacante;
    private Animal companero;

    @BeforeEach
    void preparar() {
        combate = new Combate();
        manadaJugador = new Manada(Especie.JAGUAR, false, 2);
        manadaBot = new Manada(Especie.RANA, true, 1);
        atacante = new AnimalJugador(1, new Punto(0, 0), 18, manadaJugador);
        companero = new AnimalJugador(2, new Punto(500, 500), 20, manadaJugador);
        manadaJugador.agregarAnimal(atacante);
        manadaJugador.agregarAnimal(companero);
    }

    /** Bot con poca energía para que muera de un solo golpe (daño 2). */
    private Animal botDebil(int id) {
        return new AnimalBot(id, new Punto(5, 0), 2, manadaBot);
    }

    @Test
    void un_ataque_normal_quita_energia_sin_contar_bots() {
        Animal bot = new AnimalBot(10, new Punto(5, 0), 10, manadaBot);

        assertTrue(combate.atacar(atacante, bot, 0));

        assertEquals(8, bot.getEnergia());
        assertEquals(0, manadaJugador.getBotsEliminados());
    }

    @Test
    void matar_un_bot_lo_cuenta_pero_un_solo_bot_no_da_bono() {
        combate.atacar(atacante, botDebil(10), 0);

        assertEquals(1, manadaJugador.getBotsEliminados());
        assertEquals(18, atacante.getEnergia());
    }

    @Test
    void cada_dos_bots_eliminados_toda_la_manada_viva_gana_un_punto() {
        combate.atacar(atacante, botDebil(10), 0);
        combate.atacar(atacante, botDebil(11), Constantes.ENFRIAMIENTO_ATAQUE_MS);

        assertEquals(2, manadaJugador.getBotsEliminados());
        assertEquals(19, atacante.getEnergia());
        assertEquals(20, companero.getEnergia()); // ya estaba en el máximo
    }

    @Test
    void el_bono_nunca_pasa_del_maximo() {
        combate.aplicarBonoEnergia(manadaJugador);
        combate.aplicarBonoEnergia(manadaJugador);
        combate.aplicarBonoEnergia(manadaJugador);

        assertEquals(Constantes.MAX_ENERGIA_JUGADOR, atacante.getEnergia());
    }

    @Test
    void el_bono_no_se_le_da_a_los_animales_muertos() {
        companero.recibirDanio(20);

        combate.aplicarBonoEnergia(manadaJugador);

        assertEquals(0, companero.getEnergia());
    }

    @Test
    void matar_un_animal_de_otro_jugador_no_cuenta_como_bot() {
        Manada rival = new Manada(Especie.TUCAN, false, 2);
        Animal enemigo = new AnimalJugador(20, new Punto(5, 0), 2, rival);

        combate.atacar(atacante, enemigo, 0);

        assertFalse(enemigo.estaVivo());
        assertEquals(0, manadaJugador.getBotsEliminados());
    }

    @Test
    void un_ataque_que_no_se_realiza_no_cuenta_nada() {
        combate.atacar(atacante, botDebil(10), 0);
        Animal otroBot = botDebil(11);

        boolean ataco = combate.atacar(atacante, otroBot, 100); // todavía en enfriamiento

        assertFalse(ataco);
        assertEquals(1, manadaJugador.getBotsEliminados());
    }

    @Test
    void estan_en_rango_depende_del_radio_de_encuentro() {
        Animal cerca = new AnimalBot(30, new Punto(10, 0), 10, manadaBot);
        Animal lejos = new AnimalBot(31, new Punto(900, 0), 10, manadaBot);

        assertTrue(combate.estanEnRango(atacante, cerca));
        assertFalse(combate.estanEnRango(atacante, lejos));
    }
}
