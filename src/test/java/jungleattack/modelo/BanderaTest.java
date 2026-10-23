package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BanderaTest {

    private Bandera banderaDelJugador1;
    private Animal animalDelJugador2;

    @BeforeEach
    void preparar() {
        banderaDelJugador1 = new Bandera(1, new Punto(50, 50), 1);
        Manada manada2 = new Manada(Especie.MONO, false, 2);
        manada2.setIdJugador(2);
        animalDelJugador2 = new AnimalJugador(2, new Punto(55, 50), 20, manada2);
    }

    @Test
    void el_rival_cerca_de_la_bandera_la_toca() {
        assertTrue(banderaDelJugador1.fueTocada(animalDelJugador2));
    }

    @Test
    void el_dueno_no_toca_su_propia_bandera() {
        Manada manada1 = new Manada(Especie.JAGUAR, false, 2);
        manada1.setIdJugador(1);
        Animal propio = new AnimalJugador(3, new Punto(50, 50), 20, manada1);

        assertFalse(banderaDelJugador1.fueTocada(propio));
    }

    @Test
    void un_bot_no_cuenta() {
        Animal bot = new AnimalBot(4, new Punto(50, 50), 10, new Manada(Especie.RANA, true, 1));

        assertFalse(banderaDelJugador1.fueTocada(bot));
    }

    @Test
    void el_rival_lejos_no_la_toca() {
        animalDelJugador2.setPosicion(new Punto(900, 900));

        assertFalse(banderaDelJugador1.fueTocada(animalDelJugador2));
    }

    @Test
    void el_rival_muerto_no_la_toca() {
        animalDelJugador2.recibirDanio(20);

        assertFalse(banderaDelJugador1.fueTocada(animalDelJugador2));
    }

    @Test
    void guarda_el_dueno() {
        assertEquals(1, banderaDelJugador1.getIdJugadorDuenio());
    }
}
