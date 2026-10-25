package jungleattack.modelo;

/**
 * Las 7 especies de animales nativos de Costa Rica.
 */
public enum Especie {
    JAGUAR("Jaguar"),
    PEREZOSO("Perezoso"),
    TUCAN("Tucán"),
    LAPA("Lapa roja"),
    MONO("Mono aullador"),
    TAPIR("Tapir"),
    RANA("Rana de ojos rojos");

    private final String nombre;

    Especie(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    /** Devuelve la especie con ese nombre, o null si no existe. */
    public static Especie buscar(String nombre) {
        for (Especie e : values()) {
            if (e.name().equals(nombre)) {
                return e;
            }
        }
        return null;
    }
}
