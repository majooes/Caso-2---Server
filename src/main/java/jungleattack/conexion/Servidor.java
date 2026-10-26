package jungleattack.conexion;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import jungleattack.partida.GestorPartidas;

/** Acepta conexiones de clientes y le da a cada uno su propio ManejadorCliente en un hilo aparte. */
public class Servidor {

    private final int puerto;
    private final GestorPartidas gestor;
    private ServerSocket servidor;

    public Servidor(int puerto, GestorPartidas gestor) {
        this.puerto = puerto;
        this.gestor = gestor;
    }

    /** Abre el puerto. Devuelve false si no se pudo (por ejemplo, ya está ocupado). */
    public boolean abrir() {
        try {
            servidor = new ServerSocket(puerto);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /** Puerto en el que realmente escucha (sirve cuando se pidió el puerto 0). */
    public int getPuertoReal() {
        return servidor.getLocalPort();
    }

    /** Atiende clientes hasta que se llame a cerrar(). */
    public void atender() {
        while (!servidor.isClosed()) {
            Socket socket = aceptar();
            if (socket != null) {
                new Thread(new ManejadorCliente(socket, gestor)).start();
            }
        }
    }

    private Socket aceptar() {
        try {
            return servidor.accept();
        } catch (IOException e) {
            return null; // se cerró el servidor mientras esperaba
        }
    }

    public void cerrar() {
        try {
            servidor.close();
        } catch (IOException e) {
            // Ya estaba cerrado.
            return;
        }
    }
}
