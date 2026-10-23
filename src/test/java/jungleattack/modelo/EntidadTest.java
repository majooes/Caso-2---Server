package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EntidadTest {

    /** Entidad mínima solo para poder probar la clase abstracta. */
    private static class EntidadDePrueba extends Entidad {
        EntidadDePrueba(int id, Punto posicion) {
            super(id, posicion);
        }
    }

    @Test
    void guarda_el_id_y_la_posicion() {
        Entidad e = new EntidadDePrueba(7, new Punto(3, 4));

        assertEquals(7, e.getId());
        assertEquals(3.0, e.getPosicion().getX(), 0.0001);
    }

    @Test
    void distancia_entre_dos_entidades() {
        Entidad a = new EntidadDePrueba(1, new Punto(0, 0));
        Entidad b = new EntidadDePrueba(2, new Punto(3, 4));

        assertEquals(5.0, a.distanciaA(b), 0.0001);
    }

    @Test
    void set_posicion_cambia_la_posicion() {
        Entidad e = new EntidadDePrueba(1, new Punto(0, 0));

        e.setPosicion(new Punto(9, 9));

        assertEquals(9.0, e.getPosicion().getY(), 0.0001);
    }
}
