package jungleattack.partida;

import jungleattack.util.Constantes;

/** Resultado de unirse a una partida o de elegir un animal. Así no se usan excepciones para los errores. */
public enum ResultadoUnirse {
    OK(""),
    CODIGO_INVALIDO(Constantes.TEXTO_CODIGO_INVALIDO),
    PARTIDA_LLENA(Constantes.TEXTO_PARTIDA_LLENA),
    ESPECIE_TOMADA(Constantes.TEXTO_ESPECIE_TOMADA),
    ESPECIE_INVALIDA(Constantes.TEXTO_ESPECIE_INVALIDA),
    ESTADO_INCORRECTO(Constantes.TEXTO_ESTADO_INCORRECTO);

    private final String texto;

    ResultadoUnirse(String texto) {
        this.texto = texto;
    }

    /** Mensaje que se le muestra al jugador cuando no fue OK. */
    public String getTexto() {
        return texto;
    }
}
