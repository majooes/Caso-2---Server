# Jungle Attack - Servidor - Especificación de Clases

## Paquete `util`

### Clase `Constantes`
Solo atributos `static final`, sin métodos.

**Atributos:**
- `PUERTO: int`
- `TOTAL_ZONAS: int` — 12
- `TOTAL_ESPECIES: int` — 7
- `TOTAL_TAZAS: int` — 10
- `ENERGIA_JUGADOR: int` — 20
- `ENERGIA_BOT: int` — 10
- `MIEMBROS_JUGADOR: int` — 10
- `MIEMBROS_BOT: int` — 4
- `DANIO_JUGADOR: int` — 2
- `DANIO_BOT: int` — 1
- `RADIO_ENCUENTRO: double`
- `VELOCIDAD_ANIMAL: double`
- `ENFRIAMIENTO_ATAQUE_MS: long` — 1000
- `MAX_ENERGIA_JUGADOR: int` — 20
- `LONGITUD_CODIGO: int` — 4
- `MS_POR_TICK: int`

---

## Paquete `modelo`

### Enum `Especie`
- `JAGUAR`, `PEREZOSO`, `TUCAN`, `LAPA`, `MONO`, `TAPIR`, `RANA`

### Clase `Punto`
**Atributos:**
- `x: double`
- `y: double`

**Métodos:**
- `distanciaA(Punto otro): double`
- `getX(): double`, `getY(): double`

### Interfaz `Actualizable`
**Métodos:**
- `actualizar(long deltaMs): void`

### Clase abstracta `Entidad`
**Atributos:**
- `id: int`
- `posicion: Punto`

**Métodos:**
- `distanciaA(Entidad otra): double`
- `getId(): int`
- `getPosicion(): Punto`
- `setPosicion(Punto p): void`

### Clase abstracta `Animal` (extiende `Entidad`, implementa `Actualizable`)
**Atributos:**
- `energia: int`
- `ultimoAtaqueMs: long`
- `manada: Manada`
- `ruta: List<Punto>`
- `zonaActual: int`

**Métodos:**
- `actualizar(long deltaMs): void` — avanza por la ruta a velocidad constante
- `reaccionarA(Animal cercano, long ahoraMs): boolean` — abstracto
- `recibirDanio(int cantidad): void`
- `estaVivo(): boolean`
- `puedeAtacar(long ahoraMs): boolean`
- `asignarRuta(List<Punto> ruta): void`
- `getManada(): Manada`

### Clase `AnimalJugador` (extiende `Animal`)
**Métodos:**
- `reaccionarA(Animal cercano, long ahoraMs): boolean` — no hace nada, el jugador ordena el ataque

### Clase `AnimalBot` (extiende `Animal`)
**Métodos:**
- `reaccionarA(Animal cercano, long ahoraMs): boolean` — ataca solo si el cercano es de un jugador

### Clase `Taza` (extiende `Entidad`)
**Atributos:**
- `consumida: boolean`

**Métodos:**
- `consumir(): void`
- `estaConsumida(): boolean`

### Clase `Bandera` (extiende `Entidad`)
**Atributos:**
- `idJugadorDuenio: int`

**Métodos:**
- `fueTocada(Animal animal): boolean`

### Clase `Manada`
**Atributos:**
- `especie: Especie`
- `esBot: boolean`
- `capacidadDanio: int`
- `botsEliminados: int`
- `tazasConsumidas: int`
- `miembros: List<Animal>`

**Métodos:**
- `agregarAnimal(Animal a): void`
- `sumarDanio(): void`
- `registrarBotEliminado(): void`
- `registrarTaza(): void`
- `obtenerVivos(): List<Animal>`
- `contarMuertos(): int`

### Clase `FabricaManadas`
**Métodos (estáticos):**
- `crearManadaJugador(Especie e): Manada` — patrón Factory
- `crearManadaBot(Especie e): Manada`

---

## Paquete `mapa`

### Clase `Zona`
**Atributos:**
- `id: int`
- `caminos: List<Camino>`

**Métodos:**
- `agregarCamino(Camino c): void`
- `getVecinas(): List<Zona>`

### Clase `Camino`
**Atributos:**
- `origen: Zona`
- `destino: Zona`
- `puntos: List<Punto>`

### Clase `Mapa`
**Atributos:**
- `zonas: List<Zona>`

**Métodos:**
- `construir(): void`
- `zonaAleatoria(Random r): Zona`
- `getZona(int id): Zona`

### Clase `BuscadorRuta`
**Métodos:**
- `buscarZonas(Zona desde, Zona hasta): List<Zona>` — BFS
- `construirRuta(Zona desde, Zona hasta, Punto destino): List<Punto>`

---

## Paquete `logica`

### Clase `Combate`
**Métodos:**
- `atacar(Animal atacante, Animal objetivo, long ahoraMs): boolean`
- `aplicarBonoEnergia(Manada manada): void`
- `estanEnRango(Animal a, Animal b): boolean`

---

## Paquete `eventos`

### Clase abstracta `Evento`
**Atributos:**
- `tiempoMs: long`

**Métodos:**
- `aMensaje(): Mensaje` — abstracto

**Subclases:** `EventoAtaque`, `EventoMuerte`, `EventoTaza`, `EventoVictoria`

### Clase `RegistroEventos`
**Atributos:**
- `eventos: List<Evento>`

**Métodos:**
- `agregar(Evento e): void`
- `obtenerNuevos(): List<Evento>`
- `construirResumen(): Resumen`

### Clase `Resumen`
**Atributos:**
- `botsEliminados: int`
- `animalesPerdidosA: int`, `animalesPerdidosB: int`
- `tazasA: int`, `tazasB: int`
- `duracionMs: long`

---

## Paquete `partida`

### Enum `ResultadoUnirse`
- `OK`
- `CODIGO_INVALIDO`
- `PARTIDA_LLENA`
- `ESPECIE_TOMADA`

### Clase `Jugador`
**Atributos:**
- `apodo: String`
- `especie: Especie`
- `zonaVista: int`
- `conexion: ManejadorCliente`

**Métodos:**
- `getApodo(): String`
- `setEspecie(Especie e): void`
- `setZonaVista(int id): void`

### Clase `GeneradorCodigo`
**Métodos (estáticos):**
- `generar(Set<String> usados, Random r): String`

### Clase `GestorPartidas` (patrón Singleton)
**Atributos:**
- `instancia: GestorPartidas` — estático
- `partidas: Map<String, Partida>`

**Métodos:**
- `obtenerInstancia(): GestorPartidas` — estático
- `crearPartida(Jugador j): Partida`
- `buscarPartida(String codigo): Partida` — null si no existe
- `eliminarPartida(String codigo): void`

### Clase `Partida`
**Atributos:**
- `codigo: String`
- `jugador1: Jugador`, `jugador2: Jugador`
- `mapa: Mapa`
- `manadas: List<Manada>`
- `tazas: List<Taza>`
- `banderas: List<Bandera>`
- `combate: Combate`
- `buscadorRuta: BuscadorRuta`
- `registro: RegistroEventos`
- `terminada: boolean`
- `inicioMs: long`

**Métodos:**
- `unirJugador(Jugador j): ResultadoUnirse`
- `elegirEspecie(Jugador j, Especie e): ResultadoUnirse`
- `iniciar(Random r): void`
- `ordenarMover(Jugador j, List<Integer> ids, Punto destino): void`
- `ordenarAtacar(Jugador j, List<Integer> ids, int idObjetivo): void`
- `cambiarZonaVista(Jugador j, int idZona): void`
- `actualizar(long deltaMs): void`
- `construirEstado(Jugador j): Mensaje` — solo zonas visibles
- `estaTerminada(): boolean`

### Clase `BucleJuego` (implementa `Runnable`)
**Atributos:**
- `partida: Partida`
- `corriendo: boolean`

**Métodos:**
- `run(): void`
- `detener(): void`

---

## Paquete `red`

### Enum `TipoMensaje`
- `CREAR_PARTIDA`, `UNIRSE_PARTIDA`, `ELEGIR_ANIMAL`, `MOVER_ANIMALES`, `ATACAR`, `VER_ZONA`
- `PARTIDA_CREADA`, `PARTIDA_UNIDA`, `ERROR`, `SELECCION_ANIMALES`, `PARTIDA_INICIADA`, `ACTUALIZACION_ESTADO`, `FIN_PARTIDA`

### Clase `Mensaje`
**Atributos:**
- `tipo: TipoMensaje`
- `carga: JsonObject`

**Métodos:**
- `aJson(): String`
- `desdeJson(String json): Mensaje` — estático

### Clase `Servidor`
**Atributos:**
- `puerto: int`
- `servidor: ServerSocket`

**Métodos:**
- `iniciar(): void`

### Clase `ManejadorCliente` (implementa `Runnable`)
**Atributos:**
- `socket: Socket`
- `jugador: Jugador`
- `partida: Partida`

**Métodos:**
- `run(): void`
- `procesarMensaje(Mensaje m): void`
- `enviar(Mensaje m): void`
- `cerrar(): void`

---

## Resumen de diseño

| Aspecto | Detalle |
|---|---|
| Static | `Constantes`, `GeneradorCodigo.generar`, `FabricaManadas.crear...`, `Mensaje.desdeJson`, `GestorPartidas.obtenerInstancia` |
| Herencia | `Entidad` → `Animal` → `AnimalJugador`/`AnimalBot`; `Entidad` → `Taza`/`Bandera`; `Evento` → 4 subclases |
| Interfaces | `Actualizable`, `Runnable` |
| Polimorfismo | `Animal.reaccionarA`, `Evento.aMensaje` |
| Patrones | Singleton (`GestorPartidas`), Factory (`FabricaManadas`) |
| Errores | Sin excepciones para el flujo: `ResultadoUnirse`, `boolean` y `null` |

---

## Diagrama UML

```mermaid
classDiagram
direction TB

class Actualizable {
  <<interface>>
  +actualizar(long deltaMs) void
}
class Punto {
  -double x
  -double y
  +distanciaA(Punto otro) double
}
class Entidad {
  <<abstract>>
  -int id
  -Punto posicion
  +distanciaA(Entidad otra) double
}
class Animal {
  <<abstract>>
  -int energia
  -long ultimoAtaqueMs
  -List~Punto~ ruta
  +actualizar(long deltaMs) void
  +reaccionarA(Animal cercano, long ahoraMs) boolean
  +recibirDanio(int cantidad) void
  +estaVivo() boolean
  +puedeAtacar(long ahoraMs) boolean
}
class AnimalJugador {
  +reaccionarA(Animal cercano, long ahoraMs) boolean
}
class AnimalBot {
  +reaccionarA(Animal cercano, long ahoraMs) boolean
}
class Taza {
  -boolean consumida
  +consumir() void
}
class Bandera {
  -int idJugadorDuenio
  +fueTocada(Animal animal) boolean
}
class Manada {
  -Especie especie
  -boolean esBot
  -int capacidadDanio
  -int botsEliminados
  -int tazasConsumidas
  -List~Animal~ miembros
  +sumarDanio() void
  +registrarBotEliminado() void
  +obtenerVivos() List~Animal~
}
class FabricaManadas {
  +crearManadaJugador(Especie e) Manada$
  +crearManadaBot(Especie e) Manada$
}
class Especie {
  <<enumeration>>
  JAGUAR
  PEREZOSO
  TUCAN
  LAPA
  MONO
  TAPIR
  RANA
}

class Zona {
  -int id
  -List~Camino~ caminos
  +getVecinas() List~Zona~
}
class Camino {
  -Zona origen
  -Zona destino
  -List~Punto~ puntos
}
class Mapa {
  -List~Zona~ zonas
  +construir() void
  +zonaAleatoria(Random r) Zona
}
class BuscadorRuta {
  +buscarZonas(Zona desde, Zona hasta) List~Zona~
  +construirRuta(Zona desde, Zona hasta, Punto destino) List~Punto~
}
class Combate {
  +atacar(Animal atacante, Animal objetivo, long ahoraMs) boolean
  +aplicarBonoEnergia(Manada manada) void
  +estanEnRango(Animal a, Animal b) boolean
}

class Evento {
  <<abstract>>
  -long tiempoMs
  +aMensaje() Mensaje
}
class EventoAtaque
class EventoMuerte
class EventoTaza
class EventoVictoria
class RegistroEventos {
  -List~Evento~ eventos
  +agregar(Evento e) void
  +construirResumen() Resumen
}
class Resumen {
  -int botsEliminados
  -long duracionMs
}

class ResultadoUnirse {
  <<enumeration>>
  OK
  CODIGO_INVALIDO
  PARTIDA_LLENA
  ESPECIE_TOMADA
}
class Jugador {
  -String apodo
  -Especie especie
  -int zonaVista
}
class GeneradorCodigo {
  +generar(Set~String~ usados, Random r) String$
}
class GestorPartidas {
  <<singleton>>
  -Map~String, Partida~ partidas
  +obtenerInstancia() GestorPartidas$
  +crearPartida(Jugador j) Partida
  +buscarPartida(String codigo) Partida
  +eliminarPartida(String codigo) void
}
class Partida {
  -String codigo
  -boolean terminada
  +unirJugador(Jugador j) ResultadoUnirse
  +elegirEspecie(Jugador j, Especie e) ResultadoUnirse
  +iniciar(Random r) void
  +ordenarMover(Jugador j, List~Integer~ ids, Punto destino) void
  +ordenarAtacar(Jugador j, List~Integer~ ids, int idObjetivo) void
  +actualizar(long deltaMs) void
  +construirEstado(Jugador j) Mensaje
}
class BucleJuego {
  -boolean corriendo
  +run() void
  +detener() void
}

class Mensaje {
  -TipoMensaje tipo
  -JsonObject carga
  +aJson() String
  +desdeJson(String json) Mensaje$
}
class Servidor {
  -int puerto
  +iniciar() void
}
class ManejadorCliente {
  -Socket socket
  +run() void
  +procesarMensaje(Mensaje m) void
  +enviar(Mensaje m) void
}
class Constantes {
  <<utility>>
  +RADIO_ENCUENTRO$
  +VELOCIDAD_ANIMAL$
  +ENFRIAMIENTO_ATAQUE_MS$
}

Actualizable <|.. Animal
Entidad <|-- Animal
Entidad <|-- Taza
Entidad <|-- Bandera
Animal <|-- AnimalJugador
Animal <|-- AnimalBot
Evento <|-- EventoAtaque
Evento <|-- EventoMuerte
Evento <|-- EventoTaza
Evento <|-- EventoVictoria

Entidad --> Punto
Manada "1" o-- "*" Animal
Manada --> Especie
FabricaManadas ..> Manada
Mapa "1" *-- "12" Zona
Zona "1" o-- "*" Camino
BuscadorRuta ..> Zona
RegistroEventos "1" o-- "*" Evento
RegistroEventos ..> Resumen

Partida "1" *-- "7" Manada
Partida "1" *-- "10" Taza
Partida "1" *-- "2" Bandera
Partida --> Mapa
Partida --> Combate
Partida --> BuscadorRuta
Partida --> RegistroEventos
Partida "1" o-- "2" Jugador
Partida ..> ResultadoUnirse
BucleJuego "1" --> "1" Partida
GestorPartidas "1" o-- "*" Partida
GestorPartidas ..> GeneradorCodigo
Servidor ..> ManejadorCliente
ManejadorCliente --> Jugador
ManejadorCliente ..> GestorPartidas
ManejadorCliente ..> Mensaje
Jugador --> ManejadorCliente
```
