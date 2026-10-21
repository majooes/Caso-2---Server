package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PuntoTest {

    @Test
    void distanciaEntreDosPuntosUsaPitagoras() {
        Punto a = new Punto(0, 0);
        Punto b = new Punto(3, 4);
        assertEquals(5.0, a.distanciaA(b), 0.0001);
    }

    @Test
    void distanciaAlMismoPuntoEsCero() {
        Punto a = new Punto(7, 2);
        assertEquals(0.0, a.distanciaA(a), 0.0001);
    }

    @Test
    void distanciaEsSimetrica() {
        Punto a = new Punto(1, 1);
        Punto b = new Punto(4, 5);
        assertEquals(a.distanciaA(b), b.distanciaA(a), 0.0001);
    }

    @Test
    void getXyGetYDevuelvenLosValoresDados() {
        Punto p = new Punto(2.5, 9.0);
        assertEquals(2.5, p.getX(), 0.0001);
        assertEquals(9.0, p.getY(), 0.0001);
    }
}
