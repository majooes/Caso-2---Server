package jungleattack.mapa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import jungleattack.modelo.Punto;

/** Calcula por dónde caminar entre zonas usando BFS (búsqueda en anchura). */
public class BuscadorRuta {

    /**
     * Zonas que hay que cruzar para ir de una a otra, incluyendo ambas.
     * Es el camino con menos zonas. Si no hay camino devuelve una lista vacía.
     */
    public List<Zona> buscarZonas(Zona desde, Zona hasta) {
        Map<Zona, Zona> anterior = new HashMap<>();
        Queue<Zona> cola = new ArrayDeque<>();
        anterior.put(desde, desde);
        cola.add(desde);

        while (!cola.isEmpty() && !anterior.containsKey(hasta)) {
            Zona actual = cola.remove();
            for (Zona vecina : actual.getVecinas()) {
                if (!anterior.containsKey(vecina)) {
                    anterior.put(vecina, actual);
                    cola.add(vecina);
                }
            }
        }

        List<Zona> zonas = new ArrayList<>();
        if (!anterior.containsKey(hasta)) {
            return zonas;
        }
        Zona paso = hasta;
        while (paso != desde) {
            zonas.add(0, paso);
            paso = anterior.get(paso);
        }
        zonas.add(0, desde);
        return zonas;
    }

    /**
     * Lista de puntos por los que debe pasar un animal para llegar al destino:
     * la puerta de cada cambio de zona, el centro de las zonas intermedias y al final el destino.
     * Si no hay camino devuelve una lista vacía.
     */
    public List<Punto> construirRuta(Zona desde, Zona hasta, Punto destino) {
        List<Zona> zonas = buscarZonas(desde, hasta);
        List<Punto> ruta = new ArrayList<>();
        if (zonas.isEmpty()) {
            return ruta;
        }
        for (int i = 0; i < zonas.size() - 1; i++) {
            Zona actual = zonas.get(i);
            Zona siguiente = zonas.get(i + 1);
            if (i > 0) {
                ruta.add(actual.getCentro());
            }
            ruta.add(actual.getCaminoA(siguiente).getPuerta());
        }
        ruta.add(destino);
        return ruta;
    }
}
