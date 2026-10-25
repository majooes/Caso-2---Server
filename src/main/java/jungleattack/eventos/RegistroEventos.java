package jungleattack.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Guarda en orden todo lo que pasó en la partida. */
public class RegistroEventos {

    private final List<Evento> eventos = new ArrayList<>();

    public void agregar(Evento evento) {
        eventos.add(evento);
    }

    /** Recorre los eventos y arma las estadísticas finales. */
    public Resumen construirResumen() {
        Resumen resumen = new Resumen();
        for (Evento e : eventos) {
            e.aplicarA(resumen);
        }
        return resumen;
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }
}
