package jungleattack.partida;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import jungleattack.mapa.Camino;
import jungleattack.mapa.Zona;
import jungleattack.modelo.Animal;
import jungleattack.modelo.Bandera;
import jungleattack.modelo.Especie;
import jungleattack.modelo.Manada;
import jungleattack.modelo.Punto;
import jungleattack.modelo.Taza;
import jungleattack.red.Mensaje;
import jungleattack.red.TipoMensaje;
import jungleattack.util.Constantes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PartidaTest {

    private Partida partida;
    private CanalGuardado canal1;
    private CanalGuardado canal2;
    private Jugador ana;
    private Jugador luis;

    @BeforeEach
    void preparar() {
        partida = new Partida("A7K2", new Random(11));
        canal1 = new CanalGuardado();
        canal2 = new CanalGuardado();
        ana = new Jugador("Ana", canal1);
        luis = new Jugador("Luis", canal2);
    }

    /** Une a los dos jugadores y deja que cada uno elija su animal: la partida queda en curso. */
    private void iniciarPartida() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);
        partida.elegirEspecie(ana, Especie.JAGUAR);
        partida.elegirEspecie(luis, Especie.TUCAN);
    }

    private Manada manadaDe(Jugador j) {
        for (Manada m : partida.getManadas()) {
            if (!m.isEsBot() && m.getIdJugador() == j.getId()) {
                return m;
            }
        }
        return null;
    }

    private Manada primeraManadaBot() {
        for (Manada m : partida.getManadas()) {
            if (m.isEsBot()) {
                return m;
            }
        }
        return null;
    }

    /** Pone un animal en un punto del mapa completo (y en la zona que le corresponde). */
    private void ubicar(Animal animal, Punto punto) {
        animal.setPosicion(punto);
        animal.setZonaActual(partida.getMapa().zonaDe(punto).getId());
    }

    private List<Integer> ids(Animal... animales) {
        List<Integer> lista = new ArrayList<>();
        for (Animal a : animales) {
            lista.add(a.getId());
        }
        return lista;
    }

    // ---------- Lobby ----------

    @Test
    void los_dos_primeros_jugadores_entran_y_el_tercero_no() {
        assertEquals(ResultadoUnirse.OK, partida.unirJugador(ana));
        assertEquals(EstadoPartida.ESPERANDO_JUGADOR, partida.getEstado());
        assertEquals(ResultadoUnirse.OK, partida.unirJugador(luis));
        assertEquals(EstadoPartida.SELECCION, partida.getEstado());

        Jugador tercero = new Jugador("Eva", new CanalGuardado());

        assertEquals(ResultadoUnirse.PARTIDA_LLENA, partida.unirJugador(tercero));
        assertEquals(1, ana.getId());
        assertEquals(2, luis.getId());
    }

    @Test
    void no_se_puede_elegir_animal_antes_de_tener_dos_jugadores() {
        partida.unirJugador(ana);

        assertEquals(ResultadoUnirse.ESTADO_INCORRECTO, partida.elegirEspecie(ana, Especie.JAGUAR));
    }

    @Test
    void una_especie_inexistente_es_invalida() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);

        assertEquals(ResultadoUnirse.ESPECIE_INVALIDA, partida.elegirEspecie(ana, null));
    }

    @Test
    void los_dos_jugadores_no_pueden_usar_el_mismo_animal() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);
        partida.elegirEspecie(ana, Especie.JAGUAR);

        assertEquals(ResultadoUnirse.ESPECIE_TOMADA, partida.elegirEspecie(luis, Especie.JAGUAR));
        assertEquals(ResultadoUnirse.OK, partida.elegirEspecie(luis, Especie.RANA));
    }

    @Test
    void un_jugador_no_puede_elegir_dos_veces() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);
        partida.elegirEspecie(ana, Especie.JAGUAR);

        assertEquals(ResultadoUnirse.ESTADO_INCORRECTO, partida.elegirEspecie(ana, Especie.TAPIR));
    }

    @Test
    void despues_de_cada_eleccion_se_avisa_que_especies_quedan_libres() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);
        partida.elegirEspecie(ana, Especie.JAGUAR);

        JsonArray libres = canal2.ultimo(TipoMensaje.SELECCION_ANIMALES).getCarga().getAsJsonArray("disponibles");

        assertEquals(Constantes.TOTAL_ESPECIES - 1, libres.size());
        for (JsonElement e : libres) {
            assertFalse(e.getAsString().equals("JAGUAR"));
        }
    }

    // ---------- Inicio ----------

    @Test
    void al_elegir_los_dos_empieza_con_siete_manadas_diez_tazas_y_dos_banderas() {
        iniciarPartida();

        assertEquals(EstadoPartida.EN_CURSO, partida.getEstado());
        assertEquals(Constantes.TOTAL_ESPECIES, partida.getManadas().size());
        assertEquals(Constantes.TOTAL_TAZAS, partida.getTazas().size());
        assertEquals(2, partida.getBanderas().size());
        assertEquals(Constantes.MIEMBROS_JUGADOR, manadaDe(ana).getMiembros().size());
    }

    @Test
    void las_siete_manadas_empiezan_en_zonas_distintas() {
        iniciarPartida();
        Set<Integer> zonas = new HashSet<>();
        for (Manada m : partida.getManadas()) {
            zonas.add(m.getMiembros().get(0).getZonaActual());
        }

        assertEquals(Constantes.ZONAS_CON_MANADA, zonas.size());
    }

    @Test
    void las_cinco_especies_que_nadie_eligio_son_bots() {
        iniciarPartida();
        Set<Especie> bots = new HashSet<>();
        for (Manada m : partida.getManadas()) {
            if (m.isEsBot()) {
                bots.add(m.getEspecie());
            }
        }

        assertEquals(5, bots.size());
        assertFalse(bots.contains(Especie.JAGUAR));
        assertFalse(bots.contains(Especie.TUCAN));
    }

    @Test
    void cada_animal_y_objeto_tiene_un_id_distinto() {
        iniciarPartida();
        Set<Integer> ids = new HashSet<>();
        int total = 0;
        for (Manada m : partida.getManadas()) {
            for (Animal a : m.getMiembros()) {
                ids.add(a.getId());
                total++;
            }
        }
        for (Taza t : partida.getTazas()) {
            ids.add(t.getId());
            total++;
        }
        for (Bandera b : partida.getBanderas()) {
            ids.add(b.getId());
            total++;
        }

        assertEquals(total, ids.size());
    }

    @Test
    void al_empezar_cada_jugador_recibe_la_partida_iniciada_y_un_estado() {
        iniciarPartida();

        assertEquals(1, canal1.contar(TipoMensaje.PARTIDA_INICIADA));
        assertEquals(1, canal2.contar(TipoMensaje.PARTIDA_INICIADA));
        assertNotNull(canal1.ultimo(TipoMensaje.ACTUALIZACION_ESTADO));
        assertEquals(ana.getZonaVista(),
                canal1.ultimo(TipoMensaje.PARTIDA_INICIADA).getCarga().get("zona").getAsInt());
    }

    @Test
    void el_bucle_solo_se_reclama_una_vez_y_solo_con_la_partida_en_curso() {
        assertFalse(partida.reclamarBucle());
        iniciarPartida();

        assertTrue(partida.reclamarBucle());
        assertFalse(partida.reclamarBucle());
    }

    @Test
    void antes_de_empezar_el_tiempo_no_avanza() {
        partida.actualizar(1000);

        assertEquals(0, partida.getTiempoMs());
    }

    // ---------- Visibilidad ----------

    @Test
    void cada_jugador_ve_solo_su_zona_con_posiciones_relativas_a_ella() {
        iniciarPartida();
        JsonObject carga = partida.construirEstado(ana).getCarga();

        assertEquals(Constantes.MIEMBROS_JUGADOR, carga.getAsJsonArray("animales").size());
        for (JsonElement e : carga.getAsJsonArray("animales")) {
            JsonObject animal = e.getAsJsonObject();
            assertTrue(animal.get("esMio").getAsBoolean());
            assertTrue(animal.get("x").getAsDouble() >= 0 && animal.get("x").getAsDouble() < Constantes.ANCHO_ZONA);
            assertTrue(animal.get("y").getAsDouble() >= 0 && animal.get("y").getAsDouble() < Constantes.ALTO_ZONA);
        }
    }

    @Test
    void en_su_zona_inicial_el_jugador_ve_su_bandera() {
        iniciarPartida();
        boolean hayBandera = false;
        for (JsonElement e : partida.construirEstado(ana).getCarga().getAsJsonArray("objetos")) {
            if ("BANDERA".equals(e.getAsJsonObject().get("tipo").getAsString())) {
                hayBandera = true;
            }
        }

        assertTrue(hayBandera);
    }

    @Test
    void el_estado_incluye_las_estadisticas_de_la_manada() {
        iniciarPartida();
        JsonObject carga = partida.construirEstado(ana).getCarga();

        assertEquals(Constantes.DANIO_JUGADOR, carga.get("capacidadDanio").getAsInt());
        assertEquals(0, carga.get("tazasConsumidas").getAsInt());
        assertEquals(1, carga.getAsJsonArray("zonasConAnimales").size());
    }

    @Test
    void si_no_tiene_animales_en_la_zona_que_ve_no_se_le_muestra_nada() {
        iniciarPartida();
        Zona ajena = partida.getMapa().getZona(luis.getZonaVista());
        ana.setZonaVista(ajena.getId()); // trampa: ver la zona del rival

        JsonObject carga = partida.construirEstado(ana).getCarga();

        assertEquals(0, carga.getAsJsonArray("animales").size());
        assertEquals(0, carga.getAsJsonArray("objetos").size());
    }

    @Test
    void solo_puede_cambiar_a_zonas_donde_tiene_animales() {
        iniciarPartida();
        int zonaInicial = ana.getZonaVista();
        int zonaAjena = luis.getZonaVista();

        partida.cambiarZonaVista(ana, zonaAjena);
        assertEquals(zonaInicial, ana.getZonaVista());

        Animal viajero = manadaDe(ana).getMiembros().get(0);
        Zona otra = partida.getMapa().getZona(zonaAjena);
        ubicar(viajero, otra.getCentro());
        partida.actualizar(Constantes.MS_POR_TICK);
        partida.cambiarZonaVista(ana, zonaAjena);

        assertEquals(zonaAjena, ana.getZonaVista());
    }

    @Test
    void si_todos_sus_animales_salen_de_la_zona_vista_cambia_a_donde_estan() {
        iniciarPartida();
        Zona destino = partida.getMapa().getZona(luis.getZonaVista());
        for (Animal a : manadaDe(ana).getMiembros()) {
            ubicar(a, destino.getCentro());
        }

        partida.actualizar(Constantes.MS_POR_TICK);

        assertEquals(destino.getId(), ana.getZonaVista());
    }

    // ---------- Movimiento ----------

    @Test
    void ordenar_mover_hace_caminar_solo_a_los_animales_propios_indicados() {
        iniciarPartida();
        Animal a0 = manadaDe(ana).getMiembros().get(0);
        Animal a1 = manadaDe(ana).getMiembros().get(1);
        Animal ajeno = manadaDe(luis).getMiembros().get(0);
        Punto antesA0 = a0.getPosicion();
        Punto antesA1 = a1.getPosicion();
        Punto antesAjeno = ajeno.getPosicion();

        partida.ordenarMover(ana, ids(a0, ajeno), new Punto(400, 300));
        partida.actualizar(1000);

        assertTrue(a0.getPosicion().distanciaA(antesA0) > 0);
        assertEquals(0.0, a1.getPosicion().distanciaA(antesA1), 0.0001); // no estaba en la orden
        assertEquals(0.0, ajeno.getPosicion().distanciaA(antesAjeno), 0.0001); // no es suyo
    }

    @Test
    void el_animal_llega_al_destino_pedido_dentro_de_su_zona() {
        iniciarPartida();
        Animal a0 = manadaDe(ana).getMiembros().get(0);
        Zona vista = partida.getMapa().getZona(ana.getZonaVista());
        ubicar(a0, new Punto(vista.getEsquina().getX() + 100, vista.getEsquina().getY() + 100));

        partida.ordenarMover(ana, ids(a0), new Punto(100, 160));
        partida.actualizar(1000); // 80 de velocidad: ya recorrió los 60 que faltaban

        assertEquals(vista.getEsquina().getX() + 100, a0.getPosicion().getX(), 0.001);
        assertEquals(vista.getEsquina().getY() + 160, a0.getPosicion().getY(), 0.001);
    }

    @Test
    void la_orden_de_mover_fuera_del_juego_en_curso_se_ignora() {
        partida.unirJugador(ana);

        partida.ordenarMover(ana, new ArrayList<Integer>(), new Punto(1, 1));

        assertEquals(EstadoPartida.ESPERANDO_JUGADOR, partida.getEstado());
    }

    // ---------- Cruzar de zona ----------

    @Test
    void un_clic_cerca_de_la_puerta_manda_a_los_animales_a_la_zona_vecina() {
        iniciarPartida();
        Animal a0 = manadaDe(ana).getMiembros().get(0);
        Zona vista = partida.getMapa().getZona(ana.getZonaVista());
        Camino camino = vista.getCaminos().get(0);
        Punto puerta = camino.getPuerta();
        Punto centro = vista.getCentro();
        double largo = puerta.distanciaA(centro);
        double distanciaInicio = 20; // el animal empieza a 20 de la puerta, dentro de su zona
        double distanciaClic = 10;   // el clic cae a 10 de la puerta
        Punto inicio = new Punto(
                puerta.getX() + (centro.getX() - puerta.getX()) * distanciaInicio / largo,
                puerta.getY() + (centro.getY() - puerta.getY()) * distanciaInicio / largo);
        Punto clic = new Punto(
                puerta.getX() + (centro.getX() - puerta.getX()) * distanciaClic / largo,
                puerta.getY() + (centro.getY() - puerta.getY()) * distanciaClic / largo);
        ubicar(a0, inicio);

        partida.ordenarMover(ana, ids(a0), new Punto(clic.getX() - vista.getEsquina().getX(),
                                                     clic.getY() - vista.getEsquina().getY()));
        for (int tick = 0; tick < 20; tick++) { // 1 segundo: 80 de velocidad, de sobra para cruzar
            partida.actualizar(Constantes.MS_POR_TICK);
        }

        assertEquals(camino.getDestino().getId(), a0.getZonaActual());
    }

    @Test
    void un_clic_lejos_de_las_puertas_deja_a_los_animales_en_su_zona() {
        iniciarPartida();
        Animal a0 = manadaDe(ana).getMiembros().get(0);
        Zona vista = partida.getMapa().getZona(ana.getZonaVista());
        ubicar(a0, vista.getCentro());

        partida.ordenarMover(ana, ids(a0), new Punto(Constantes.ANCHO_ZONA / 2, Constantes.ALTO_ZONA / 2 + 40));
        partida.actualizar(1000);

        assertEquals(vista.getId(), a0.getZonaActual());
    }

    // ---------- Tazas ----------

    @Test
    void recoger_una_taza_sube_el_danio_de_toda_la_manada_una_sola_vez() {
        iniciarPartida();
        for (Taza t : partida.getTazas()) {
            t.setPosicion(new Punto(1, 1)); // lejos de todos
        }
        Taza taza = partida.getTazas().get(0);
        Animal a0 = manadaDe(ana).getMiembros().get(0);
        taza.setPosicion(a0.getPosicion());

        partida.actualizar(Constantes.MS_POR_TICK);
        partida.actualizar(Constantes.MS_POR_TICK);

        assertTrue(taza.estaConsumida());
        assertEquals(1, manadaDe(ana).getTazasConsumidas());
        assertEquals(Constantes.DANIO_JUGADOR + 1, manadaDe(ana).getCapacidadDanio());
        assertEquals(Constantes.DANIO_JUGADOR, manadaDe(luis).getCapacidadDanio());
    }

    @Test
    void los_bots_no_recogen_tazas() {
        iniciarPartida();
        for (Taza t : partida.getTazas()) {
            t.setPosicion(new Punto(1, 1));
        }
        Taza taza = partida.getTazas().get(0);
        taza.setPosicion(primeraManadaBot().getMiembros().get(0).getPosicion());

        partida.actualizar(Constantes.MS_POR_TICK);

        assertFalse(taza.estaConsumida());
    }

    // ---------- Combate ----------

    @Test
    void un_bot_ataca_solo_a_un_animal_de_jugador_que_se_le_acerca() {
        iniciarPartida();
        Animal bot = primeraManadaBot().getMiembros().get(0);
        Animal victima = manadaDe(ana).getMiembros().get(0);
        ubicar(victima, new Punto(bot.getPosicion().getX() + 5, bot.getPosicion().getY()));

        partida.actualizar(Constantes.MS_POR_TICK);

        assertTrue(victima.getEnergia() < Constantes.ENERGIA_JUGADOR);
    }

    @Test
    void el_jugador_ordena_atacar_y_al_matar_un_bot_se_cuenta() {
        iniciarPartida();
        Animal bot = primeraManadaBot().getMiembros().get(0);
        Animal atacante = manadaDe(ana).getMiembros().get(0);
        bot.recibirDanio(bot.getEnergia() - 1); // le queda 1 de energía
        ubicar(atacante, new Punto(bot.getPosicion().getX() + 5, bot.getPosicion().getY()));
        ana.setZonaVista(bot.getZonaActual());

        partida.ordenarAtacar(ana, ids(atacante), bot.getId());
        partida.actualizar(Constantes.MS_POR_TICK);

        assertFalse(bot.estaVivo());
        assertEquals(1, manadaDe(ana).getBotsEliminados());
    }

    @Test
    void no_se_puede_ordenar_atacar_a_un_animal_propio_ni_a_uno_que_no_se_ve() {
        iniciarPartida();
        Animal propio = manadaDe(ana).getMiembros().get(1);
        Animal atacante = manadaDe(ana).getMiembros().get(0);
        ubicar(propio, atacante.getPosicion());
        Animal rival = manadaDe(luis).getMiembros().get(0);
        ubicar(rival, atacante.getPosicion());
        rival.setZonaActual(luis.getZonaVista()); // en otra zona, no se ve

        partida.ordenarAtacar(ana, ids(atacante), propio.getId());
        partida.ordenarAtacar(ana, ids(atacante), rival.getId());
        partida.actualizar(Constantes.MS_POR_TICK);

        assertEquals(Constantes.ENERGIA_JUGADOR, propio.getEnergia());
    }

    @Test
    void el_jugador_puede_atacar_al_animal_del_rival_y_la_muerte_cuenta_como_animal_perdido() {
        iniciarPartida();
        Animal atacante = manadaDe(ana).getMiembros().get(0);
        Animal victima = manadaDe(luis).getMiembros().get(0);
        victima.recibirDanio(victima.getEnergia() - 1);
        ubicar(victima, new Punto(atacante.getPosicion().getX() + 5, atacante.getPosicion().getY()));

        partida.ordenarAtacar(ana, ids(atacante), victima.getId());
        partida.actualizar(Constantes.MS_POR_TICK);

        assertFalse(victima.estaVivo());
        assertEquals(1, manadaDe(luis).contarMuertos());
    }

    // ---------- Victoria ----------

    @Test
    void gana_quien_toca_la_bandera_del_rival() {
        iniciarPartida();
        Bandera banderaDeAna = partida.getBanderas().get(0);
        Animal intruso = manadaDe(luis).getMiembros().get(0);
        ubicar(intruso, banderaDeAna.getPosicion());

        partida.actualizar(Constantes.MS_POR_TICK);

        assertTrue(partida.estaTerminada());
        JsonObject fin = canal1.ultimo(TipoMensaje.FIN_PARTIDA).getCarga();
        assertEquals("Luis", fin.get("ganador").getAsString());
        assertEquals("Ana", fin.get("apodoA").getAsString());
        assertEquals("Luis", fin.get("apodoB").getAsString());
        assertNotNull(canal2.ultimo(TipoMensaje.FIN_PARTIDA));
    }

    @Test
    void tocar_la_propia_bandera_no_da_la_victoria() {
        iniciarPartida();
        Bandera banderaDeAna = partida.getBanderas().get(0);
        ubicar(manadaDe(ana).getMiembros().get(0), banderaDeAna.getPosicion());

        partida.actualizar(Constantes.MS_POR_TICK);

        assertFalse(partida.estaTerminada());
    }

    @Test
    void si_a_un_jugador_le_matan_todos_los_animales_gana_el_otro() {
        iniciarPartida();
        for (Animal a : manadaDe(ana).getMiembros()) {
            a.recibirDanio(a.getEnergia());
        }

        partida.actualizar(Constantes.MS_POR_TICK);

        assertTrue(partida.estaTerminada());
        assertEquals("Luis", canal1.ultimo(TipoMensaje.FIN_PARTIDA).getCarga().get("ganador").getAsString());
    }

    @Test
    void el_resumen_final_cuenta_tazas_y_duracion() {
        iniciarPartida();
        for (Taza t : partida.getTazas()) {
            t.setPosicion(new Punto(1, 1));
        }
        partida.getTazas().get(0).setPosicion(manadaDe(ana).getMiembros().get(0).getPosicion());
        partida.actualizar(Constantes.MS_POR_TICK);
        ubicar(manadaDe(luis).getMiembros().get(0), partida.getBanderas().get(0).getPosicion());

        partida.actualizar(Constantes.MS_POR_TICK);

        JsonObject fin = canal2.ultimo(TipoMensaje.FIN_PARTIDA).getCarga();
        assertEquals(1, fin.get("tazasA").getAsInt());
        assertEquals(0, fin.get("tazasB").getAsInt());
        assertEquals(2L * Constantes.MS_POR_TICK, fin.get("duracionMs").getAsLong());
    }

    @Test
    void despues_de_terminar_ya_no_se_actualiza_nada() {
        iniciarPartida();
        partida.jugadorDesconectado(ana);
        long tiempo = partida.getTiempoMs();

        partida.actualizar(1000);

        assertEquals(tiempo, partida.getTiempoMs());
    }

    // ---------- Desconexión ----------

    @Test
    void si_un_jugador_se_desconecta_en_pleno_juego_gana_el_otro() {
        iniciarPartida();

        partida.jugadorDesconectado(ana);

        assertTrue(partida.estaTerminada());
        assertEquals("Luis", canal2.ultimo(TipoMensaje.FIN_PARTIDA).getCarga().get("ganador").getAsString());
    }

    @Test
    void si_alguien_se_desconecta_antes_de_empezar_se_cancela_y_se_avisa_al_otro() {
        partida.unirJugador(ana);
        partida.unirJugador(luis);

        partida.jugadorDesconectado(ana);

        assertTrue(partida.estaTerminada());
        assertNotNull(canal2.ultimo(TipoMensaje.ERROR));
        assertNull(canal2.ultimo(TipoMensaje.FIN_PARTIDA));
    }

    @Test
    void desconectarse_dos_veces_no_manda_dos_mensajes_finales() {
        iniciarPartida();

        partida.jugadorDesconectado(ana);
        partida.jugadorDesconectado(luis);

        assertEquals(1, canal2.contar(TipoMensaje.FIN_PARTIDA));
    }
}
