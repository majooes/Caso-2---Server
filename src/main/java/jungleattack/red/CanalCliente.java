package jungleattack.red;

/** Por dónde se le manda un mensaje a un cliente. Así la lógica del juego no conoce los sockets. */
public interface CanalCliente {
    void enviar(Mensaje mensaje);
}
