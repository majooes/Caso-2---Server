package jungleattack.eventos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Guarda todo lo que pasa en la partida y de ahí saca el resumen final. */
public class RegistroEventos {

    private final List<Evento> eventos = new ArrayList<>();
    private int primerNuevo = 0; // posición del primer evento que aún no se ha entregado

    public void agregar(Evento e) {
        eventos.add(e);
    }

    /** Eventos agregados desde la última vez que se llamó a este método. */
    public List<Evento> obtenerNuevos() {
        List<Evento> nuevos = new ArrayList<>(eventos.subList(primerNuevo, eventos.size()));
        primerNuevo = eventos.size();
        return nuevos;
    }

    /** Cuenta muertes y tazas. La duración es el tiempo del último evento registrado. */
    public Resumen construirResumen() {
        int bots = 0;
        int perdidosA = 0;
        int perdidosB = 0;
        int tazasA = 0;
        int tazasB = 0;
        long duracion = 0;

        for (Evento e : eventos) {
            duracion = Math.max(duracion, e.getTiempoMs());
            if (e instanceof EventoMuerte muerte) {
                if (muerte.isEsBot()) {
                    bots++;
                } else if (muerte.getDuenio() == Evento.JUGADOR_A) {
                    perdidosA++;
                } else if (muerte.getDuenio() == Evento.JUGADOR_B) {
                    perdidosB++;
                }
            } else if (e instanceof EventoTaza taza) {
                if (taza.getDuenio() == Evento.JUGADOR_A) {
                    tazasA++;
                } else if (taza.getDuenio() == Evento.JUGADOR_B) {
                    tazasB++;
                }
            }
        }
        return new Resumen(bots, perdidosA, perdidosB, tazasA, tazasB, duracion);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }
}
