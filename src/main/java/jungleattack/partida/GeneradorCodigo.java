package jungleattack.partida;

import java.util.Random;
import java.util.Set;
import jungleattack.util.Constantes;

/** Genera los códigos de 4 letras o números que identifican cada partida. */
public final class GeneradorCodigo {

    private GeneradorCodigo() {
        // Solo métodos estáticos.
    }

    /**
     * Código nuevo que no esté en el conjunto de usados.
     * Devuelve null solo si ya se usaron todas las combinaciones posibles.
     */
    public static String generar(Set<String> usados, Random azar) {
        double posibles = Math.pow(Constantes.CARACTERES_CODIGO.length(), Constantes.LONGITUD_CODIGO);
        if (usados.size() >= posibles) {
            return null;
        }
        String codigo = construir(azar);
        while (usados.contains(codigo)) {
            codigo = construir(azar);
        }
        return codigo;
    }

    private static String construir(Random azar) {
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < Constantes.LONGITUD_CODIGO; i++) {
            int posicion = azar.nextInt(Constantes.CARACTERES_CODIGO.length());
            codigo.append(Constantes.CARACTERES_CODIGO.charAt(posicion));
        }
        return codigo.toString();
    }
}
