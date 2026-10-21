package jungleattack.modelo;

/**
 * Posición en el mapa. Es inmutable: para "mover" se crea un Punto nuevo.
 */
public class Punto {

    private final double x;
    private final double y;

    public Punto(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distanciaA(Punto otro) {
        double difX = otro.x - this.x;
        double difY = otro.y - this.y;
        return Math.sqrt(difX * difX + difY * difY);
    }
}
