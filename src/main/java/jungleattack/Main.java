package jungleattack;

import jungleattack.conexion.Servidor;
import jungleattack.partida.GestorPartidas;
import jungleattack.util.Constantes;

/** Punto de entrada del servidor de Jungle Attack. */
public class Main {

    public static void main(String[] args) {
        Servidor servidor = new Servidor(Constantes.PUERTO, GestorPartidas.obtenerInstancia());
        if (servidor.abrir()) {
            System.out.println(String.format(Constantes.TEXTO_SERVIDOR_LISTO, Constantes.PUERTO));
            servidor.atender();
        } else {
            System.out.println(String.format(Constantes.TEXTO_PUERTO_OCUPADO, Constantes.PUERTO));
        }
    }
}
