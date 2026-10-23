package jungleattack.modelo;

/** Cualquier cosa que existe en el mapa y tiene una posición: animales, tazas y banderas. */
public abstract class Entidad {

    private final int id;
    private Punto posicion;

    protected Entidad(int id, Punto posicion) {
        this.id = id;
        this.posicion = posicion;
    }

    public int getId() {
        return id;
    }

    public Punto getPosicion() {
        return posicion;
    }

    public void setPosicion(Punto posicion) {
        this.posicion = posicion;
    }

    /** Distancia en línea recta hasta otra entidad. */
    public double distanciaA(Entidad otra) {
        return posicion.distanciaA(otra.posicion);
    }
}
