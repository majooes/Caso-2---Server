package jungleattack.partida;

import jungleattack.modelo.Especie;
import jungleattack.red.CanalCliente;
import jungleattack.red.Mensaje;
import jungleattack.util.Constantes;

/** Un jugador conectado. Guarda su apodo, su animal elegido y la zona que está viendo. */
public class Jugador {

    private final String apodo;
    private final CanalCliente canal;
    private int id = Constantes.ID_SIN_JUGADOR;
    private Especie especie;
    private int zonaVista;

    public Jugador(String apodo, CanalCliente canal) {
        this.apodo = apodo;
        this.canal = canal;
    }

    public void enviar(Mensaje mensaje) {
        canal.enviar(mensaje);
    }

    public String getApodo() { return apodo; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public int getZonaVista() { return zonaVista; }
    public void setZonaVista(int zonaVista) { this.zonaVista = zonaVista; }
}
