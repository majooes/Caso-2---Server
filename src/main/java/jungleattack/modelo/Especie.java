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
}
