package jungleattack.partida;

import java.util.Random;
import java.util.Set;
import jungleattack.util.Constantes;

/** Crea los códigos de 4 letras o números con los que se une el segundo jugador. */
public class GeneradorCodigo {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private GeneradorCodigo() {
        // Solo métodos estáticos
    }

    /** Código nuevo que no está en usados. */
    public static String generar(Set<String> usados, Random r) {
        String codigo;
        do {
            StringBuilder texto = new StringBuilder();
            for (int i = 0; i < Constantes.LONGITUD_CODIGO; i++) {
                texto.append(CARACTERES.charAt(r.nextInt(CARACTERES.length())));
            }
            codigo = texto.toString();
        } while (usados.contains(codigo));
        return codigo;
    }
}
