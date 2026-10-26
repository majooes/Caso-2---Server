package jungleattack.conexion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import jungleattack.partida.GestorPartidas;
import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Prueba de punta a punta: un servidor de verdad y dos clientes por socket. */
class ServidorTest {

    private static final int ESPERA_MS = 5000;

    /** Cliente de prueba que habla con el servidor igual que el juego real. */
    private static class ClientePrueba {
        private final Socket socket;
        private final BufferedReader entrada;
        private final PrintWriter salida;

        ClientePrueba(int puerto) throws IOException {
            socket = new Socket("localhost", puerto);
            socket.setSoTimeout(ESPERA_MS);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        }

        void enviar(TipoMensaje tipo, JsonObject carga) {
            salida.println(new Mensaje(tipo, carga).aJson());
        }

        /** Lee mensajes hasta encontrar uno del tipo pedido (ignora los demás). */
        Mensaje esperar(TipoMensaje tipo) throws IOException {
            Mensaje m = Mensaje.desdeJson(entrada.readLine());
            while (m != null && m.getTipo() != tipo) {
                m = Mensaje.desdeJson(entrada.readLine());
            }
            return m;
        }

        void cerrar() throws IOException {
            socket.close();
        }
    }

    private final GestorPartidas gestor = GestorPartidas.obtenerInstancia();
    private Servidor servidor;
    private ClientePrueba ana;
    private ClientePrueba luis;

    @BeforeEach
    void abrirServidor() throws Exception {
        servidor = new Servidor(0, gestor);
        assertTrue(servidor.abrir());
        Thread atencion = new Thread(new Runnable() {
            @Override
            public void run() {
                servidor.atender();
            }
        });
        atencion.setDaemon(true);
        atencion.start();
        ana = new ClientePrueba(servidor.getPuertoReal());
        luis = new ClientePrueba(servidor.getPuertoReal());
    }

    @AfterEach
    void cerrarTodo() throws Exception {
        ana.cerrar();
        luis.cerrar();
        servidor.cerrar();
    }

    private JsonObject con(String clave, String valor) {
        JsonObject carga = new JsonObject();
        carga.addProperty(clave, valor);
        return carga;
    }

    private String crearPartidaDeAna() throws IOException {
        ana.enviar(TipoMensaje.CREAR_PARTIDA, con("apodo", "Ana"));
        return ana.esperar(TipoMensaje.PARTIDA_CREADA).getCarga().get("codigo").getAsString();
    }

    private void unirLuis(String codigo) {
        JsonObject carga = con("apodo", "Luis");
        carga.addProperty("codigo", codigo.toLowerCase()); // el código no distingue mayúsculas
        luis.enviar(TipoMensaje.UNIRSE_PARTIDA, carga);
    }

    @Test
    void crear_una_partida_devuelve_un_codigo_de_cuatro_caracteres() throws Exception {
        String codigo = crearPartidaDeAna();

        assertEquals(4, codigo.length());
        assertNotNull(gestor.buscarPartida(codigo));
        gestor.eliminarPartida(codigo);
    }

    @Test
    void unirse_con_un_codigo_que_no_existe_da_error() throws Exception {
        unirLuis("ZZZZ");

        Mensaje error = luis.esperar(TipoMensaje.ERROR);

        assertNotNull(error);
        assertTrue(error.getCarga().get("mensaje").getAsString().length() > 0);
    }

    @Test
    void al_unirse_el_segundo_jugador_los_dos_reciben_la_seleccion_de_animales() throws Exception {
        String codigo = crearPartidaDeAna();

        unirLuis(codigo);

        assertNotNull(luis.esperar(TipoMensaje.PARTIDA_UNIDA));
        JsonArray libresLuis = luis.esperar(TipoMensaje.SELECCION_ANIMALES).getCarga().getAsJsonArray("disponibles");
        JsonArray libresAna = ana.esperar(TipoMensaje.SELECCION_ANIMALES).getCarga().getAsJsonArray("disponibles");
        assertEquals(7, libresLuis.size());
        assertEquals(7, libresAna.size());
        gestor.eliminarPartida(codigo);
    }

    @Test
    void partida_completa_elegir_jugar_y_ganar_por_desconexion() throws Exception {
        String codigo = crearPartidaDeAna();
        unirLuis(codigo);
        luis.esperar(TipoMensaje.SELECCION_ANIMALES);

        ana.enviar(TipoMensaje.ELEGIR_ANIMAL, con("especie", "JAGUAR"));
        // Luis recibe la lista actualizada cuando el servidor ya procesó la elección de Ana
        luis.esperar(TipoMensaje.SELECCION_ANIMALES);
        luis.enviar(TipoMensaje.ELEGIR_ANIMAL, con("especie", "JAGUAR")); // ya está tomado
        Mensaje rechazo = luis.esperar(TipoMensaje.ERROR);
        assertNotNull(rechazo);
        luis.enviar(TipoMensaje.ELEGIR_ANIMAL, con("especie", "TUCAN"));

        Mensaje inicio = ana.esperar(TipoMensaje.PARTIDA_INICIADA);
        assertNotNull(inicio);
        Mensaje estado = ana.esperar(TipoMensaje.ACTUALIZACION_ESTADO);
        assertEquals(10, estado.getCarga().getAsJsonArray("animales").size());
        assertNotNull(luis.esperar(TipoMensaje.PARTIDA_INICIADA));

        ana.cerrar(); // Ana se desconecta: gana Luis
        Mensaje fin = luis.esperar(TipoMensaje.FIN_PARTIDA);

        assertNotNull(fin);
        assertEquals("Luis", fin.getCarga().get("ganador").getAsString());
        esperarQueSeBorre(codigo);
        assertNull(gestor.buscarPartida(codigo));
    }

    @Test
    void mensajes_raros_no_tumban_al_servidor() throws Exception {
        ana.enviar(TipoMensaje.MOVER_ANIMALES, new JsonObject()); // antes de tener partida
        ana.enviar(TipoMensaje.ELEGIR_ANIMAL, con("especie", "JAGUAR"));
        Mensaje error = ana.esperar(TipoMensaje.ERROR);
        assertNotNull(error);

        String codigo = crearPartidaDeAna(); // el servidor sigue atendiendo

        assertEquals(4, codigo.length());
        gestor.eliminarPartida(codigo);
    }

    /** Espera hasta 2 segundos a que el bucle de juego quite la partida del gestor. */
    private void esperarQueSeBorre(String codigo) throws InterruptedException {
        int intentos = 0;
        while (gestor.buscarPartida(codigo) != null && intentos < 40) {
            Thread.sleep(50);
            intentos++;
        }
    }
}
