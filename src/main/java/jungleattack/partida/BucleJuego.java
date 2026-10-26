package jungleattack.partida;

import jungleattack.util.Constantes;

/**
 * Hace avanzar una partida: cada MS_POR_TICK milisegundos la actualiza con el tiempo real que
 * pasó (así la velocidad es constante aunque un tick se atrase). Cuando la partida termina,
 * la quita del gestor.
 */
public class BucleJuego implements Runnable {

    private final Partida partida;
    private final GestorPartidas gestor;
    private volatile boolean corriendo = true;

    public BucleJuego(Partida partida, GestorPartidas gestor) {
        this.partida = partida;
        this.gestor = gestor;
    }

    @Override
    public void run() {
        long anterior = System.currentTimeMillis();
        while (corriendo && !partida.estaTerminada()) {
            esperar(Constantes.MS_POR_TICK);
            long ahora = System.currentTimeMillis();
            partida.actualizar(ahora - anterior);
            anterior = ahora;
        }
        gestor.eliminarPartida(partida.getCodigo());
    }

    public void detener() {
        corriendo = false;
    }

    /** Pausa entre ticks. Si interrumpen el hilo, el bucle se detiene. */
    private void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            corriendo = false;
        }
    }
}
