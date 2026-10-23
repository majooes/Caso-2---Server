package jungleattack.modelo;

/** Todo lo que cambia con el paso del tiempo (por ejemplo, un animal que camina). */
public interface Actualizable {

    /** Avanza el estado del objeto. deltaMs es el tiempo transcurrido desde el último tick. */
    void actualizar(long deltaMs);
}
