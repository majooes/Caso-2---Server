package jungleattack.partida;

/** Resultado de unirse a una partida o de elegir un animal. Reemplaza a las excepciones. */
public enum ResultadoUnirse {
    OK(""),
    CODIGO_INVALIDO("El código de la partida no existe"),
    PARTIDA_LLENA("La partida ya tiene dos jugadores"),
    ESPECIE_TOMADA("Ese animal ya fue elegido por el otro jugador");

    private final String mensaje;

    ResultadoUnirse(String mensaje) {
        this.mensaje = mensaje;
    }

    /** Texto que se envía al cliente en el mensaje ERROR. */
    public String getMensaje() {
        return mensaje;
    }
}
