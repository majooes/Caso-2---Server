package jungleattack.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jungleattack.util.Constantes;
import org.junit.jupiter.api.Test;

class FabricaManadasTest {

    @Test
    void la_manada_de_jugador_tiene_los_valores_del_enunciado() {
        Manada m = FabricaManadas.crearManadaJugador(Especie.JAGUAR, 100);

        assertFalse(m.isEsBot());
        assertEquals(10, m.getMiembros().size());
        assertEquals(20, m.getMiembros().get(0).getEnergia());
        assertEquals(2, m.getCapacidadDanio());
        assertTrue(m.getMiembros().get(0) instanceof AnimalJugador);
    }

    @Test
    void la_manada_de_bot_tiene_los_valores_del_enunciado() {
        Manada m = FabricaManadas.crearManadaBot(Especie.RANA, 200);

        assertTrue(m.isEsBot());
        assertEquals(4, m.getMiembros().size());
        assertEquals(10, m.getMiembros().get(0).getEnergia());
        assertEquals(1, m.getCapacidadDanio());
        assertTrue(m.getMiembros().get(0) instanceof AnimalBot);
    }

    @Test
    void los_ids_son_consecutivos_desde_el_primero() {
        Manada m = FabricaManadas.crearManadaBot(Especie.TAPIR, 50);

        assertEquals(50, m.getMiembros().get(0).getId());
        assertEquals(53, m.getMiembros().get(Constantes.MIEMBROS_BOT - 1).getId());
    }

    @Test
    void todos_los_animales_pertenecen_a_su_manada() {
        Manada m = FabricaManadas.crearManadaJugador(Especie.TUCAN, 1);

        for (Animal a : m.getMiembros()) {
            assertEquals(m, a.getManada());
        }
    }
}
