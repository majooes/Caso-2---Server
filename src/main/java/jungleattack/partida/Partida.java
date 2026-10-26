package jungleattack.partida;

import com.google.gson.JsonArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import jungleattack.eventos.EventoMuerte;
import jungleattack.eventos.EventoTaza;
import jungleattack.eventos.EventoVictoria;
import jungleattack.eventos.RegistroEventos;
import jungleattack.eventos.Resumen;
import jungleattack.logica.Combate;
import jungleattack.mapa.BuscadorRuta;
import jungleattack.mapa.Camino;
import jungleattack.mapa.Mapa;
import jungleattack.mapa.Zona;
import jungleattack.modelo.Animal;
import jungleattack.modelo.Bandera;
import jungleattack.modelo.Especie;
import jungleattack.modelo.FabricaManadas;
import jungleattack.modelo.Manada;
import jungleattack.modelo.Punto;
import jungleattack.modelo.Taza;
import jungleattack.red.FabricaMensajes;
import jungleattack.red.Mensaje;
import jungleattack.util.Constantes;

/**
 * Una partida entre dos jugadores. El servidor es la autoridad: aquí se calculan el movimiento,
 * los combates, las tazas y la victoria. Los métodos son synchronized porque los llaman a la vez
 * los hilos de cada cliente y el hilo del bucle de juego.
 */
public class Partida {

    private final String codigo;
    private final Random azar;
    private final Mapa mapa = new Mapa();
    private final BuscadorRuta buscador = new BuscadorRuta();
    private final Combate combate = new Combate();
    private final RegistroEventos registro = new RegistroEventos();
    private final List<Jugador> jugadores = new ArrayList<>();
    private final List<Manada> manadas = new ArrayList<>();
    private final List<Taza> tazas = new ArrayList<>();
    private final List<Bandera> banderas = new ArrayList<>();
    private final Map<Animal, Animal> ataques = new HashMap<>();
    private EstadoPartida estado = EstadoPartida.ESPERANDO_JUGADOR;
    private long tiempoMs;
    private boolean bucleReclamado;

    public Partida(String codigo, Random azar) {
        this.codigo = codigo;
        this.azar = azar;
    }

    // ---------- Lobby ----------

    public synchronized ResultadoUnirse unirJugador(Jugador jugador) {
        if (estado != EstadoPartida.ESPERANDO_JUGADOR) {
            return ResultadoUnirse.PARTIDA_LLENA;
        }
        jugadores.add(jugador);
        jugador.setId(jugadores.size());
        if (jugadores.size() == Constantes.JUGADORES_POR_PARTIDA) {
            estado = EstadoPartida.SELECCION;
        }
        return ResultadoUnirse.OK;
    }

    /** Le avisa a los dos jugadores qué animales siguen libres. */
    public synchronized void anunciarSeleccion() {
        Mensaje mensaje = FabricaMensajes.seleccion(especiesLibres());
        for (Jugador j : jugadores) {
            j.enviar(mensaje);
        }
    }

    /** Un jugador elige su animal. Cuando los dos eligieron, empieza el juego. */
    public synchronized ResultadoUnirse elegirEspecie(Jugador jugador, Especie especie) {
        if (estado != EstadoPartida.SELECCION || jugador.getEspecie() != null) {
            return ResultadoUnirse.ESTADO_INCORRECTO;
        }
        if (especie == null) {
            return ResultadoUnirse.ESPECIE_INVALIDA;
        }
        for (Jugador otro : jugadores) {
            if (otro != jugador && otro.getEspecie() == especie) {
                return ResultadoUnirse.ESPECIE_TOMADA;
            }
        }
        jugador.setEspecie(especie);
        if (todosEligieron()) {
            iniciar();
        } else {
            anunciarSeleccion();
        }
        return ResultadoUnirse.OK;
    }

    private boolean todosEligieron() {
        for (Jugador j : jugadores) {
            if (j.getEspecie() == null) {
                return false;
            }
        }
        return true;
    }

    private List<Especie> especiesLibres() {
        List<Especie> libres = new ArrayList<>();
        for (Especie e : Especie.values()) {
            boolean tomada = false;
            for (Jugador j : jugadores) {
                if (j.getEspecie() == e) {
                    tomada = true;
                }
            }
            if (!tomada) {
                libres.add(e);
            }
        }
        return libres;
    }

    // ---------- Inicio ----------

    /** Crea las 7 manadas en zonas distintas al azar, las banderas y las 10 tazas. */
    private void iniciar() {
        List<Zona> zonasLibres = new ArrayList<>(mapa.getZonas());
        Collections.shuffle(zonasLibres, azar);
        int siguienteId = 1;
        int siguienteZona = 0;

        for (Jugador j : jugadores) {
            Manada manada = FabricaManadas.crearManadaJugador(j.getEspecie(), siguienteId);
            manada.setIdJugador(j.getId());
            siguienteId += manada.getMiembros().size();
            Zona zona = zonasLibres.get(siguienteZona++);
            colocar(manada, zona);
            j.setZonaVista(zona.getId());
            banderas.add(new Bandera(siguienteId++, zona.puntoAleatorio(azar), j.getId()));
        }
        for (Especie especie : especiesLibres()) {
            Manada bots = FabricaManadas.crearManadaBot(especie, siguienteId);
            siguienteId += bots.getMiembros().size();
            colocar(bots, zonasLibres.get(siguienteZona++));
        }
        for (int i = 0; i < Constantes.TOTAL_TAZAS; i++) {
            tazas.add(new Taza(siguienteId++, mapa.puntoAleatorioEnCamino(azar)));
        }

        estado = EstadoPartida.EN_CURSO;
        tiempoMs = 0;
        for (Jugador j : jugadores) {
            j.enviar(FabricaMensajes.partidaIniciada(j.getZonaVista()));
        }
        enviarEstados();
    }

    /** Pone cada animal de la manada en un punto al azar de la zona. */
    private void colocar(Manada manada, Zona zona) {
        for (Animal a : manada.getMiembros()) {
            a.setPosicion(zona.puntoAleatorio(azar));
            a.setZonaActual(zona.getId());
        }
        manadas.add(manada);
    }

    // ---------- Órdenes de los jugadores ----------

    /**
     * Manda a los animales indicados hacia un punto de la zona que el jugador está viendo.
     * Si el clic cae cerca de la puerta de un camino, van al centro de la zona vecina.
     */
    public synchronized void ordenarMover(Jugador jugador, List<Integer> ids, Punto destinoLocal) {
        Zona vista = mapa.getZona(jugador.getZonaVista());
        Manada propia = manadaDe(jugador);
        if (estado != EstadoPartida.EN_CURSO || vista == null || propia == null) {
            return;
        }
        Punto destino = new Punto(vista.getEsquina().getX() + destinoLocal.getX(),
                                  vista.getEsquina().getY() + destinoLocal.getY());
        Zona zonaDestino = mapa.zonaDe(destino);
        if (zonaDestino == null) {
            return;
        }
        Camino puerta = puertaCercana(vista, destino);
        if (puerta != null) { // clic al final de un camino: ir a la zona vecina
            zonaDestino = puerta.getDestino();
            destino = zonaDestino.getCentro();
        }
        for (Integer id : ids) {
            Animal animal = buscarEn(propia, id);
            Zona origen = (animal == null) ? null : mapa.zonaDe(animal.getPosicion());
            if (animal != null && animal.estaVivo() && origen != null) {
                animal.asignarRuta(buscador.construirRuta(origen, zonaDestino, destino));
                ataques.remove(animal);
            }
        }
    }

    /** Camino de la zona cuya puerta está cerca del punto, o null si no hay ninguno. */
    private Camino puertaCercana(Zona zona, Punto punto) {
        for (Camino camino : zona.getCaminos()) {
            if (camino.getPuerta().distanciaA(punto) <= Constantes.RADIO_PUERTA) {
                return camino;
            }
        }
        return null;
    }

    /** Los animales indicados atacan al objetivo mientras estén en rango (1 ataque por segundo). */
    public synchronized void ordenarAtacar(Jugador jugador, List<Integer> ids, int idObjetivo) {
        Manada propia = manadaDe(jugador);
        Animal objetivo = buscarAnimalVivo(idObjetivo);
        if (estado != EstadoPartida.EN_CURSO || propia == null || objetivo == null) {
            return;
        }
        boolean esVisible = objetivo.getZonaActual() == jugador.getZonaVista();
        boolean esPropio = objetivo.getManada() == propia;
        if (!esVisible || esPropio) {
            return;
        }
        for (Integer id : ids) {
            Animal atacante = buscarEn(propia, id);
            if (atacante != null && atacante.estaVivo()) {
                atacante.asignarRuta(new ArrayList<Punto>());
                ataques.put(atacante, objetivo);
            }
        }
    }

    /** Cambia la zona que ve el jugador, solo a zonas donde tiene animales vivos. */
    public synchronized void cambiarZonaVista(Jugador jugador, int idZona) {
        Manada propia = manadaDe(jugador);
        if (estado == EstadoPartida.EN_CURSO && propia != null
                && zonasConAnimales(propia).contains(idZona)) {
            jugador.setZonaVista(idZona);
        }
    }

    // ---------- Paso del tiempo ----------

    /** Avanza el juego deltaMs milisegundos: mover, recoger tazas, combatir y revisar la victoria. */
    public synchronized void actualizar(long deltaMs) {
        if (estado != EstadoPartida.EN_CURSO) {
            return;
        }
        tiempoMs += deltaMs;
        moverAnimales(deltaMs);
        recogerTazas();
        atacarBots();
        resolverAtaquesDeJugadores();
        verificarVictoria();
        if (estado == EstadoPartida.EN_CURSO) {
            ajustarZonasVistas();
            enviarEstados();
        }
    }

    private void moverAnimales(long deltaMs) {
        for (Manada manada : manadas) {
            for (Animal a : manada.obtenerVivos()) {
                a.actualizar(deltaMs);
                Zona zona = mapa.zonaDe(a.getPosicion());
                if (zona != null) {
                    a.setZonaActual(zona.getId());
                }
            }
        }
    }

    private void recogerTazas() {
        for (Taza taza : tazas) {
            Manada manada = quienRecoge(taza);
            if (manada != null) {
                taza.consumir();
                manada.registrarTaza();
                registro.agregar(new EventoTaza(tiempoMs, manada.getIdJugador()));
            }
        }
    }

    /** Manada de jugador que toca la taza, o null si ninguna. */
    private Manada quienRecoge(Taza taza) {
        for (Manada manada : manadas) {
            if (!manada.isEsBot()) {
                for (Animal a : manada.obtenerVivos()) {
                    if (taza.fueTocadaPor(a)) {
                        return manada;
                    }
                }
            }
        }
        return null;
    }

    private void atacarBots() {
        for (Manada manada : manadas) {
            if (manada.isEsBot()) {
                for (Animal bot : manada.obtenerVivos()) {
                    botAtaca(bot);
                }
            }
        }
    }

    /** Un bot ataca a cada animal de jugador que esté cerca (respeta el enfriamiento). */
    private void botAtaca(Animal bot) {
        for (Manada manada : manadas) {
            if (!manada.isEsBot()) {
                for (Animal victima : manada.obtenerVivos()) {
                    boolean mismaZona = bot.getZonaActual() == victima.getZonaActual();
                    if (mismaZona && combate.estanEnRango(bot, victima)) {
                        boolean estabaVivo = victima.estaVivo();
                        if (bot.reaccionarA(victima, tiempoMs)) {
                            registrarMuerte(victima, estabaVivo);
                        }
                    }
                }
            }
        }
    }

    private void resolverAtaquesDeJugadores() {
        List<Animal> atacantes = new ArrayList<>(ataques.keySet());
        for (Animal atacante : atacantes) {
            Animal objetivo = ataques.get(atacante);
            boolean sigue = atacante.estaVivo() && objetivo.estaVivo()
                    && combate.estanEnRango(atacante, objetivo);
            if (!sigue) {
                ataques.remove(atacante); // murió alguno o el objetivo se alejó
            } else {
                boolean estabaVivo = objetivo.estaVivo();
                if (combate.atacar(atacante, objetivo, tiempoMs)) {
                    registrarMuerte(objetivo, estabaVivo);
                }
            }
        }
    }

    private void registrarMuerte(Animal animal, boolean estabaVivo) {
        if (estabaVivo && !animal.estaVivo()) {
            Manada manada = animal.getManada();
            registro.agregar(new EventoMuerte(tiempoMs, manada.isEsBot(), manada.getIdJugador()));
        }
    }

    // ---------- Fin de la partida ----------

    /** Gana quien toca la bandera rival; si a un jugador le matan todos los animales, gana el otro. */
    private void verificarVictoria() {
        for (Bandera bandera : banderas) {
            for (Manada manada : manadas) {
                if (!manada.isEsBot()) {
                    for (Animal a : manada.obtenerVivos()) {
                        if (bandera.fueTocada(a)) {
                            terminar(manada.getIdJugador());
                            return;
                        }
                    }
                }
            }
        }
        for (Manada manada : manadas) {
            if (!manada.isEsBot() && manada.obtenerVivos().isEmpty()) {
                terminar(otroJugador(manada.getIdJugador()));
                return;
            }
        }
    }

    private void terminar(int idGanador) {
        estado = EstadoPartida.TERMINADA;
        ataques.clear();
        registro.agregar(new EventoVictoria(tiempoMs, idGanador));
        Resumen resumen = registro.construirResumen();
        Mensaje fin = FabricaMensajes.fin(resumen, apodoDe(Constantes.ID_JUGADOR_1),
                apodoDe(Constantes.ID_JUGADOR_2), apodoDe(resumen.getIdGanador()));
        for (Jugador j : jugadores) {
            j.enviar(fin);
        }
    }

    /**
     * Si un jugador se desconecta con la partida en curso, gana el otro. Si todavía no
     * había empezado, la partida se cancela y se avisa al otro jugador.
     */
    public synchronized void jugadorDesconectado(Jugador jugador) {
        if (estado == EstadoPartida.TERMINADA) {
            return;
        }
        if (estado == EstadoPartida.EN_CURSO) {
            terminar(otroJugador(jugador.getId()));
        } else {
            estado = EstadoPartida.TERMINADA;
            for (Jugador otro : jugadores) {
                if (otro != jugador) {
                    otro.enviar(FabricaMensajes.error(Constantes.TEXTO_RIVAL_DESCONECTADO));
                }
            }
        }
    }

    private int otroJugador(int id) {
        return (id == Constantes.ID_JUGADOR_1) ? Constantes.ID_JUGADOR_2 : Constantes.ID_JUGADOR_1;
    }

    private String apodoDe(int idJugador) {
        for (Jugador j : jugadores) {
            if (j.getId() == idJugador) {
                return j.getApodo();
            }
        }
        return "";
    }

    // ---------- Lo que ve cada jugador ----------

    private void enviarEstados() {
        for (Jugador j : jugadores) {
            j.enviar(construirEstado(j));
        }
    }

    /** Foto del juego para un jugador: solo la zona que ve, y solo si tiene animales ahí. */
    public synchronized Mensaje construirEstado(Jugador jugador) {
        Manada propia = manadaDe(jugador);
        Zona vista = mapa.getZona(jugador.getZonaVista());
        List<Integer> zonas = (propia == null) ? new ArrayList<Integer>() : zonasConAnimales(propia);
        JsonArray animales = new JsonArray();
        JsonArray objetos = new JsonArray();
        if (vista != null && zonas.contains(vista.getId())) {
            for (Manada manada : manadas) {
                for (Animal a : manada.obtenerVivos()) {
                    if (a.getZonaActual() == vista.getId()) {
                        boolean esMio = manada == propia;
                        animales.add(FabricaMensajes.animal(a, esMio, aLocal(vista, a.getPosicion())));
                    }
                }
            }
            agregarObjetosVisibles(vista, objetos);
        }
        return FabricaMensajes.estado(jugador.getZonaVista(), zonas, propia, animales, objetos);
    }

    private void agregarObjetosVisibles(Zona vista, JsonArray objetos) {
        for (Taza taza : tazas) {
            if (!taza.estaConsumida() && vista.contiene(taza.getPosicion())) {
                objetos.add(FabricaMensajes.objeto(Constantes.TIPO_OBJETO_TAZA,
                        aLocal(vista, taza.getPosicion())));
            }
        }
        for (Bandera bandera : banderas) {
            if (vista.contiene(bandera.getPosicion())) {
                objetos.add(FabricaMensajes.objeto(Constantes.TIPO_OBJETO_BANDERA,
                        aLocal(vista, bandera.getPosicion())));
            }
        }
    }

    /** Pasa una posición del mapa completo a coordenadas dentro de la zona. */
    private Punto aLocal(Zona zona, Punto p) {
        return new Punto(p.getX() - zona.getEsquina().getX(), p.getY() - zona.getEsquina().getY());
    }

    /** Ids de las zonas donde la manada tiene animales vivos, de menor a mayor. */
    private List<Integer> zonasConAnimales(Manada manada) {
        List<Integer> zonas = new ArrayList<>();
        for (Animal a : manada.obtenerVivos()) {
            if (!zonas.contains(a.getZonaActual())) {
                zonas.add(a.getZonaActual());
            }
        }
        Collections.sort(zonas);
        return zonas;
    }

    /** Si el jugador ya no tiene animales en la zona que veía, pasa a otra donde sí tenga. */
    private void ajustarZonasVistas() {
        for (Jugador j : jugadores) {
            Manada propia = manadaDe(j);
            List<Integer> zonas = (propia == null) ? new ArrayList<Integer>() : zonasConAnimales(propia);
            if (!zonas.isEmpty() && !zonas.contains(j.getZonaVista())) {
                j.setZonaVista(zonas.get(0));
            }
        }
    }

    // ---------- Búsquedas ----------

    private Manada manadaDe(Jugador jugador) {
        for (Manada manada : manadas) {
            if (!manada.isEsBot() && manada.getIdJugador() == jugador.getId()) {
                return manada;
            }
        }
        return null;
    }

    private Animal buscarEn(Manada manada, int id) {
        for (Animal a : manada.getMiembros()) {
            if (a.getId() == id) {
                return a;
            }
        }
        return null;
    }

    private Animal buscarAnimalVivo(int id) {
        for (Manada manada : manadas) {
            Animal a = buscarEn(manada, id);
            if (a != null && a.estaVivo()) {
                return a;
            }
        }
        return null;
    }

    // ---------- Consultas ----------

    /** Devuelve true solo la primera vez que se llama con la partida en curso (para crear un solo bucle). */
    public synchronized boolean reclamarBucle() {
        if (estado == EstadoPartida.EN_CURSO && !bucleReclamado) {
            bucleReclamado = true;
            return true;
        }
        return false;
    }

    public synchronized boolean estaTerminada() { return estado == EstadoPartida.TERMINADA; }
    public synchronized EstadoPartida getEstado() { return estado; }
    public synchronized long getTiempoMs() { return tiempoMs; }
    public String getCodigo() { return codigo; }
    public Mapa getMapa() { return mapa; }
    public List<Jugador> getJugadores() { return Collections.unmodifiableList(jugadores); }
    public List<Manada> getManadas() { return Collections.unmodifiableList(manadas); }
    public List<Taza> getTazas() { return Collections.unmodifiableList(tazas); }
    public List<Bandera> getBanderas() { return Collections.unmodifiableList(banderas); }
}
