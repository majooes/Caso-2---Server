package jungleattack.partida;

import java.util.ArrayList;
import java.util.List;
import jungleattack.red.CanalCliente;
import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;

/** Canal de prueba: en vez de mandar por un socket, guarda los mensajes para revisarlos. */
class CanalGuardado implements CanalCliente {

    private final List<Mensaje> mensajes = new ArrayList<>();

    @Override
    public void enviar(Mensaje mensaje) {
        mensajes.add(mensaje);
    }

    List<Mensaje> getMensajes() {
        return mensajes;
    }

    /** Último mensaje de ese tipo, o null si no llegó ninguno. */
    Mensaje ultimo(TipoMensaje tipo) {
        Mensaje encontrado = null;
        for (Mensaje m : mensajes) {
            if (m.getTipo() == tipo) {
                encontrado = m;
            }
        }
        return encontrado;
    }

    int contar(TipoMensaje tipo) {
        int total = 0;
        for (Mensaje m : mensajes) {
            if (m.getTipo() == tipo) {
                total++;
            }
        }
        return total;
    }
}
