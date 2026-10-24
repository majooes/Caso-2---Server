package jungleattack.mapa;

import jungleattack.modelo.Punto;

/**
 * Camino de una zona a otra (en un solo sentido). Va en línea recta de centro a centro
 * y cruza el borde entre las zonas por una "puerta".
 */
public class Camino {

    private final Zona origen;
    private final Zona destino;
    private final Punto puerta;

    public Camino(Zona origen, Zona destino, Punto puerta) {
        this.origen = origen;
        this.destino = destino;
        this.puerta = puerta;
    }

    /** Punto del camino. t=0 es el centro de la zona de origen y t=1 el centro de la de destino. */
    public Punto puntoEnTrayecto(double t) {
        Punto a = origen.getCentro();
        Punto b = destino.getCentro();
        double x = a.getX() + (b.getX() - a.getX()) * t;
        double y = a.getY() + (b.getY() - a.getY()) * t;
        return new Punto(x, y);
    }

    public Zona getOrigen() { return origen; }
    public Zona getDestino() { return destino; }
    public Punto getPuerta() { return puerta; }
}
