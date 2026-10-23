package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TazaTest {

    private Taza taza;
    private Animal animal;

    @BeforeEach
    void preparar() {
        taza = new Taza(1, new Punto(100, 100));
        animal = new AnimalJugador(2, new Punto(105, 100), 20, new Manada(Especie.TUCAN, false, 2));
    }

    @Test
    void un_animal_cercano_toca_la_taza() {
        assertTrue(taza.fueTocadaPor(animal));
    }

    @Test
    void un_animal_lejano_no_toca_la_taza() {
        animal.setPosicion(new Punto(900, 900));

        assertFalse(taza.fueTocadaPor(animal));
    }

    @Test
    void una_taza_consumida_ya_no_se_puede_tocar() {
        taza.consumir();

        assertTrue(taza.estaConsumida());
        assertFalse(taza.fueTocadaPor(animal));
    }

    @Test
    void un_animal_muerto_no_recoge_tazas() {
        animal.recibirDanio(20);

        assertFalse(taza.fueTocadaPor(animal));
    }
}
