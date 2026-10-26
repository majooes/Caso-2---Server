package jungleattack.conexion;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import jungleattack.modelo.Especie;
import jungleattack.modelo.Punto;
import jungleattack.partida.BucleJuego;
import jungleattack.partida.GestorPartidas;
import jungleattack.partida.Jugador;
import jungleattack.partida.Partida;
import jungleattack.partida.ResultadoUnirse;
import jungleattack.red.CanalCliente;
import jungleattack.red.FabricaMensajes;
import jungleattack.red.LectorJson;
import jungleattack.red.Mensaje;
import jungleattack.util.Constantes;

/**
 * Atiende a un cliente en su propio hilo: lee sus mensajes (uno por línea) y se los pasa
 * a la partida. Solo traduce; las reglas del juego viven en Partida.
 */
public class ManejadorCliente implements Runnable, CanalCliente {

    private final Socket socket;
    private final GestorPartidas gestor;
    private PrintWriter salida;
    private Jugador jugador;
    private Partida partida;

    public ManejadorCliente(Socket socket, GestorPartidas gestor) {
        this.socket = socket;
        this.gestor = gestor;
    }

    @Override
    public void run() {
        BufferedReader entrada = abrirFlujos();
        if (entrada != null) {
            leerMensajes(entrada);
        }
        desconectar();
    }

    /** Prepara la lectura y la escritura en UTF-8. Devuelve null si el socket ya no sirve. */
    private BufferedReader abrirFlujos() {
        try {
            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            salida = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            return entrada;
        } catch (IOException e) {
            return null;
        }
    }

    private void leerMensajes(BufferedReader entrada) {
        try {
            String linea = entrada.readLine();
            while (linea != null) {
                Mensaje mensaje = Mensaje.desdeJson(linea);
                if (mensaje != null) {
                    procesar(mensaje);
                }
                linea = entrada.readLine();
            }
        } catch (IOException e) {
            // La conexión se cayó: se sale del ciclo y se limpia en desconectar().
            return;
        }
    }

    /** Manda el mensaje al cliente. Lo llaman este hilo y el bucle de la partida, por eso es synchronized. */
    @Override
    public synchronized void enviar(Mensaje mensaje) {
        if (salida != null) {
            salida.println(mensaje.aJson());
        }
    }

    private void procesar(Mensaje mensaje) {
        JsonObject carga = mensaje.getCarga();
        switch (mensaje.getTipo()) {
            case CREAR_PARTIDA -> crearPartida(carga);
            case UNIRSE_PARTIDA -> unirsePartida(carga);
            case ELEGIR_ANIMAL -> elegirAnimal(carga);
            case MOVER_ANIMALES -> moverAnimales(carga);
            case ATACAR -> atacar(carga);
            case VER_ZONA -> verZona(carga);
            default -> { } // los demás tipos solo los manda el servidor
        }
    }

    private void crearPartida(JsonObject carga) {
        if (partida != null) {
            enviar(FabricaMensajes.error(ResultadoUnirse.ESTADO_INCORRECTO.getTexto()));
            return;
        }
        Jugador nuevo = new Jugador(leerApodo(carga), this);
        Partida creada = gestor.crearPartida(nuevo);
        if (creada == null) {
            enviar(FabricaMensajes.error(ResultadoUnirse.ESTADO_INCORRECTO.getTexto()));
            return;
        }
        jugador = nuevo;
        partida = creada;
        enviar(FabricaMensajes.partidaCreada(creada.getCodigo()));
    }

    private void unirsePartida(JsonObject carga) {
        if (partida != null) {
            enviar(FabricaMensajes.error(ResultadoUnirse.ESTADO_INCORRECTO.getTexto()));
            return;
        }
        Partida buscada = gestor.buscarPartida(LectorJson.leerTexto(carga, Constantes.JSON_CODIGO, ""));
        if (buscada == null) {
            enviar(FabricaMensajes.error(ResultadoUnirse.CODIGO_INVALIDO.getTexto()));
            return;
        }
        Jugador nuevo = new Jugador(leerApodo(carga), this);
        ResultadoUnirse resultado = buscada.unirJugador(nuevo);
        if (resultado != ResultadoUnirse.OK) {
            enviar(FabricaMensajes.error(resultado.getTexto()));
            return;
        }
        jugador = nuevo;
        partida = buscada;
        enviar(FabricaMensajes.partidaUnida(buscada.getCodigo()));
        buscada.anunciarSeleccion();
    }

    private void elegirAnimal(JsonObject carga) {
        if (partida == null) {
            enviar(FabricaMensajes.error(ResultadoUnirse.ESTADO_INCORRECTO.getTexto()));
            return;
        }
        Especie especie = Especie.buscar(LectorJson.leerTexto(carga, Constantes.JSON_ESPECIE, ""));
        ResultadoUnirse resultado = partida.elegirEspecie(jugador, especie);
        if (resultado != ResultadoUnirse.OK) {
            enviar(FabricaMensajes.error(resultado.getTexto()));
        } else if (partida.reclamarBucle()) {
            new Thread(new BucleJuego(partida, gestor)).start();
        }
    }

    private void moverAnimales(JsonObject carga) {
        if (partida != null) {
            Punto destino = new Punto(
                    LectorJson.leerDouble(carga, Constantes.JSON_DESTINO_X, 0),
                    LectorJson.leerDouble(carga, Constantes.JSON_DESTINO_Y, 0));
            partida.ordenarMover(jugador, LectorJson.leerEnteros(carga, Constantes.JSON_ANIMAL_IDS), destino);
        }
    }

    private void atacar(JsonObject carga) {
        if (partida != null) {
            partida.ordenarAtacar(jugador, LectorJson.leerEnteros(carga, Constantes.JSON_ANIMAL_IDS),
                    LectorJson.leerEntero(carga, Constantes.JSON_OBJETIVO_ID, -1));
        }
    }

    private void verZona(JsonObject carga) {
        if (partida != null) {
            partida.cambiarZonaVista(jugador, LectorJson.leerEntero(carga, Constantes.JSON_ZONA, -1));
        }
    }

    private String leerApodo(JsonObject carga) {
        String apodo = LectorJson.leerTexto(carga, Constantes.JSON_APODO, "").trim();
        return apodo.isEmpty() ? Constantes.APODO_POR_DEFECTO : apodo;
    }

    /** Avisa a la partida que este jugador se fue y cierra el socket. */
    private void desconectar() {
        if (partida != null && jugador != null) {
            partida.jugadorDesconectado(jugador);
            if (partida.estaTerminada()) {
                gestor.eliminarPartida(partida.getCodigo());
            }
        }
        try {
            socket.close();
        } catch (IOException e) {
            // Ya estaba cerrado; no hay nada más que hacer.
            return;
        }
    }
}
