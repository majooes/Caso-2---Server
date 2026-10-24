package jungleattack.mapa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jungleattack.modelo.Punto;
import jungleattack.util.Constantes;

/** El bosque: 12 zonas en cuadrícula de 4 columnas por 3 filas, unidas por caminos. */
public class Mapa {

    private final List<Zona> zonas = new ArrayList<>();

    public Mapa() {
        construir();
    }

    private void construir() {
        for (int fila = 0; fila < Constantes.FILAS_MAPA; fila++) {
            for (int columna = 0; columna < Constantes.COLUMNAS_MAPA; columna++) {
                int id = fila * Constantes.COLUMNAS_MAPA + columna;
                Punto esquina = new Punto(columna * Constantes.ANCHO_ZONA, fila * Constantes.ALTO_ZONA);
                zonas.add(new Zona(id, esquina));
            }
        }
        for (int fila = 0; fila < Constantes.FILAS_MAPA; fila++) {
            for (int columna = 0; columna < Constantes.COLUMNAS_MAPA; columna++) {
                Zona actual = zonas.get(fila * Constantes.COLUMNAS_MAPA + columna);
                if (columna < Constantes.COLUMNAS_MAPA - 1) {
                    Zona derecha = zonas.get(fila * Constantes.COLUMNAS_MAPA + columna + 1);
                    Punto puerta = new Punto(derecha.getEsquina().getX(), actual.getCentro().getY());
                    conectar(actual, derecha, puerta);
                }
                if (fila < Constantes.FILAS_MAPA - 1) {
                    Zona abajo = zonas.get((fila + 1) * Constantes.COLUMNAS_MAPA + columna);
                    Punto puerta = new Punto(actual.getCentro().getX(), abajo.getEsquina().getY());
                    conectar(actual, abajo, puerta);
                }
            }
        }
    }

    /** Une dos zonas con un camino en cada sentido. */
    private void conectar(Zona a, Zona b, Punto puerta) {
        a.agregarCamino(new Camino(a, b, puerta));
        b.agregarCamino(new Camino(b, a, puerta));
    }

    /** Zona con ese id, o null si no existe. */
    public Zona getZona(int id) {
        for (Zona z : zonas) {
            if (z.getId() == id) {
                return z;
            }
        }
        return null;
    }

    /** Zona que contiene el punto, o null si está fuera del mapa. */
    public Zona zonaDe(Punto p) {
        for (Zona z : zonas) {
            if (z.contiene(p)) {
                return z;
            }
        }
        return null;
    }

    public Zona zonaAleatoria(Random azar) {
        return zonas.get(azar.nextInt(zonas.size()));
    }

    /** Punto al azar sobre algún camino (aquí se colocan las tazas de café). */
    public Punto puntoAleatorioEnCamino(Random azar) {
        Zona zona = zonaAleatoria(azar);
        List<Camino> caminos = zona.getCaminos();
        Camino camino = caminos.get(azar.nextInt(caminos.size()));
        double t = Constantes.TRAYECTO_MIN
                + azar.nextDouble() * (Constantes.TRAYECTO_MAX - Constantes.TRAYECTO_MIN);
        return camino.puntoEnTrayecto(t);
    }

    public List<Zona> getZonas() { return zonas; }
}
