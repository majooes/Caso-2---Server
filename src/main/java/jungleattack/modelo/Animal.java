package jungleattack.modelo;

import java.util.ArrayList;
import java.util.List;
import jungleattack.util.Constantes;

/**
 * Animal del juego. Puede ser de un jugador o un bot; la diferencia está en
 * cómo reacciona cuando se encuentra con otro animal (método reaccionarA).
 */
public abstract class Animal extends Entidad implements Actualizable {

    private final Manada manada;
    private final List<Punto> ruta = new ArrayList<>();
    private int energia;
    private long ultimoAtaqueMs = -Constantes.ENFRIAMIENTO_ATAQUE_MS; // así el primer ataque no espera
    private int zonaActual;

    protected Animal(int id, Punto posicion, int energia, Manada manada) {
        super(id, posicion);
        this.energia = energia;
        this.manada = manada;
    }

    /** Qué hace este animal al encontrarse con otro. Devuelve true si lo atacó. */
    public abstract boolean reaccionarA(Animal cercano, long ahoraMs);

    /** Camina por la ruta a velocidad constante, sin importar cuánto tarde cada tick. */
    @Override
    public void actualizar(long deltaMs) {
        if (!estaVivo()) {
            return;
        }
        double paso = Constantes.VELOCIDAD_ANIMAL * deltaMs / Constantes.MS_POR_SEGUNDO;
        while (paso > 0 && !ruta.isEmpty()) {
            Punto destino = ruta.get(0);
            double distancia = getPosicion().distanciaA(destino);
            if (distancia <= paso) {
                setPosicion(destino);
                paso -= distancia;
                ruta.remove(0);
            } else {
                double fraccion = paso / distancia;
                double x = getPosicion().getX() + (destino.getX() - getPosicion().getX()) * fraccion;
                double y = getPosicion().getY() + (destino.getY() - getPosicion().getY()) * fraccion;
                setPosicion(new Punto(x, y));
                paso = 0;
            }
        }
    }

    /** Ataca al objetivo si está vivo, en rango y ya pasó el enfriamiento. Devuelve true si atacó. */
    public boolean atacar(Animal objetivo, long ahoraMs) {
        boolean ambosVivos = estaVivo() && objetivo.estaVivo();
        boolean enRango = distanciaA(objetivo) <= Constantes.RADIO_ENCUENTRO;
        if (ambosVivos && enRango && puedeAtacar(ahoraMs)) {
            objetivo.recibirDanio(manada.getCapacidadDanio());
            ultimoAtaqueMs = ahoraMs;
            return true;
        }
        return false;
    }

    public void recibirDanio(int cantidad) {
        energia = Math.max(0, energia - cantidad);
    }

    public boolean estaVivo() {
        return energia > 0;
    }

    /** True si ya pasó el enfriamiento desde el último ataque (1 ataque por segundo). */
    public boolean puedeAtacar(long ahoraMs) {
        return ahoraMs - ultimoAtaqueMs >= Constantes.ENFRIAMIENTO_ATAQUE_MS;
    }

    public void asignarRuta(List<Punto> nuevaRuta) {
        ruta.clear();
        for (Punto p : nuevaRuta) {
            ruta.add(p);
        }
    }

    public boolean tieneRuta() {
        return !ruta.isEmpty();
    }

    /** Sube la energía sin pasar del máximo permitido. */
    public void ganarEnergia(int cantidad, int maximo) {
        energia = Math.min(maximo, energia + cantidad);
    }

    public Manada getManada() { return manada; }
    public int getEnergia() { return energia; }
    public int getZonaActual() { return zonaActual; }
    public void setZonaActual(int zonaActual) { this.zonaActual = zonaActual; }
}
