package jungleattack.mapa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;

/** Una de las 12 zonas del bosque. Es un rectángulo con caminos hacia las zonas vecinas. */
public class Zona {

    private final int id;
    private final Punto esquina;
    private final List<Camino> caminos = new ArrayList<>();

    public Zona(int id, Punto esquina) {
        this.id = id;
        this.esquina = esquina;
    }

    public void agregarCamino(Camino camino) {
        caminos.add(camino);
    }

    public boolean contiene(Punto p) {
        boolean dentroX = p.getX() >= esquina.getX() && p.getX() < esquina.getX() + Constantes.ANCHO_ZONA;
        boolean dentroY = p.getY() >= esquina.getY() && p.getY() < esquina.getY() + Constantes.ALTO_ZONA;
        return dentroX && dentroY;
    }

    public Punto getCentro() {
        return new Punto(esquina.getX() + Constantes.ANCHO_ZONA / 2,
                         esquina.getY() + Constantes.ALTO_ZONA / 2);
    }

    /** Punto al azar dentro de la zona, sin pegarse a los bordes. */
    public Punto puntoAleatorio(Random azar) {
        double x = esquina.getX() + Constantes.MARGEN_ZONA
                + azar.nextDouble() * (Constantes.ANCHO_ZONA - 2 * Constantes.MARGEN_ZONA);
        double y = esquina.getY() + Constantes.MARGEN_ZONA
                + azar.nextDouble() * (Constantes.ALTO_ZONA - 2 * Constantes.MARGEN_ZONA);
        return new Punto(x, y);
    }

    public List<Zona> getVecinas() {
        List<Zona> vecinas = new ArrayList<>();
        for (Camino c : caminos) {
            vecinas.add(c.getDestino());
        }
        return vecinas;
    }

    /** Camino hacia una zona vecina, o null si no son vecinas. */
    public Camino getCaminoA(Zona vecina) {
        for (Camino c : caminos) {
            if (c.getDestino() == vecina) {
                return c;
            }
        }
        return null;
    }

    public int getId() { return id; }
    public Punto getEsquina() { return esquina; }
    public List<Camino> getCaminos() { return Collections.unmodifiableList(caminos); }
}
