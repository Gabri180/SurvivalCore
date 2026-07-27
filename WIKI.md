# 📖 SurvivalCore WIKI - Guía Completa del Plugin

**Versión:** 1.0.14  
**Minecraft:** Paper 1.21.1  
**Java:** 16+

---

## 📑 TABLA DE CONTENIDOS

1. [Instalación](#instalación)
2. [Comandos](#comandos)
3. [Permisos](#permisos)
4. [Placeholders](#placeholders)
5. [Sistemas Implementados](#sistemas-implementados)
6. [Configuración](#configuración)
7. [API para Desarrolladores](#api-para-desarrolladores)
8. [Troubleshooting](#troubleshooting)

---

## 🔧 Instalación

### Requisitos:
- Paper 1.21.1+
- Java 16+
- Base de datos MySQL (preferible)

### Pasos:
1. Descargar `SurvivalCore-1.0.14.jar`
2. Copiar a carpeta `plugins/`
3. Reiniciar servidor
4. Configurar `config.yml` si es necesario
5. Recargar plugin: `/reload confirm`

---

## 🎮 Comandos

### Sistema Arena (`/arena`)

```
/arena                          # Abre menú de arenas (GUI)
/arena join <id>                # Unirse a arena (requiere dinero)
/arena leave                     # Salir de arena actual
/arena info <id>                # Ver detalles de arena
/arena ranking                  # Ver top 10 de ranking
```

**Alias:** `/arenas`

**Permisos:**
- `survivalcore.arena.join` - Unirse a arena
- `survivalcore.arena.info` - Ver info
- `survivalcore.admin` - Crear/editar arenas

**Ejemplo:**
```
/arena join main
# ✓ ¡Te uniste a Arena Principal! Entrada: $1000 | Premio: $5000
```

---

### Sistema Clan (`/clan`)

```
/clan                           # Abre menú de clanes (GUI)
/clan create <nombre>           # Crear nuevo clan
/clan invite <jugador>          # Invitar jugador al clan
/clan accept                    # Aceptar invitación
/clan leave                     # Salir del clan
/clan info                      # Ver detalles del clan
/clan members                   # Listar miembros con roles
/clan bank                      # Ver tesorería del clan
```

**Alias:** `/clanes`

**Permisos:**
- `survivalcore.clan.create` - Crear clan
- `survivalcore.clan.invite` - Invitar
- `survivalcore.clan.admin` - Gestionar clan

**Requisitos:**
- Crear clan: $10,000 (configurable)
- Invitar: Ser propietario o admin

**Ejemplo:**
```
/clan create MyClan
# ✓ ¡Clan creado! Dinero: $0 | Miembros: 1 | Poder: 0
```

---

### Sistema Auction (`/auction`)

```
/auction                        # Abre menú de subastas (GUI)
/auction sell <precio>          # Vender item en mano
/auction list                   # Ver todas las subastas activas
/auction mylist                 # Ver mis subastas
/auction bid <id> <cantidad>    # Pujar en subasta
/auction claim                  # Reclamar items ganados
```

**Alias:** `/ah`

**Permisos:**
- `survivalcore.auction.sell` - Vender items
- `survivalcore.auction.bid` - Pujar

**Duración:** 24 horas por subasta

**Comisión:** Sin comisión (configurable)

**Ejemplo:**
```
/auction sell 5000
# ✓ Item listado a $5000 por 24 horas (ID: 42)
```

---

### Sistema Bounty (`/bounty`)

```
/bounty                         # Abre menú de recompensas (GUI)
/bounty create <jugador> <cantidad>  # Crear recompensa
/bounty list                    # Ver recompensas activas
/bounty mylist                  # Ver mis recompensas creadas
/bounty history                 # Ver recompensas pagadas
```

**Alias:** `/recompensa`

**Permisos:**
- `survivalcore.bounty.create` - Crear recompensas
- `survivalcore.bounty.claim` - Reclamar recompensas

**Automático:** Se reclama automáticamente al matar al jugador

**Ejemplo:**
```
/bounty create Player123 10000
# ✓ Recompensa de $10000 en Player123
# 💀 Si Player123 muere, recibirás $10000
```

---

### Sistema Job (`/job`)

```
/job                            # Abre menú de trabajos (GUI)
/job set <tipo>                 # Cambiar trabajo
/job info                       # Ver info del trabajo actual
```

**Tipos de Job:**
- `MINER` - Minar bloques
- `FISHERMAN` - Pescar
- `LUMBERJACK` - Talar árboles
- `FARMER` - Cosechar/plantar
- `HUNTER` - Matar mobs

**Permisos:**
- `survivalcore.job.set` - Cambiar job

**Ganancias por Job:**
```
MINER:
  - Diamond: 100 XP
  - Iron: 50 XP
  - Gold: 60 XP
  - Coal: 25 XP

FISHERMAN:
  - Tropical Fish: 30 XP
  - Salmon: 20 XP
  - Cod: 15 XP
  - Enchanted Book: 80 XP

HUNTER:
  - Jugador: 100 XP
  - Bosses: 250-500 XP
  - Mobs: 10-80 XP
```

---

### Sistema Stats (`/stats`)

```
/stats                          # Ver tus estadísticas
/stats <jugador>                # Ver stats de otro jugador
```

**Muestra:**
- Nivel actual
- XP acumulada
- Dinero disponible
- Job actual
- Estadísticas de arena
- Recompensas pendientes

---

### Sistema Menú (`/menu`)

```
/menu edit <nombre>             # Editar menú (Admin)
/menu slot <número>             # Seleccionar slot
/menu material <material>       # Cambiar material
/menu name <nombre>             # Cambiar nombre
/menu lore <línea>              # Agregar lore
/menu action <tipo> <valor>     # Agregar acción
/menu save                      # Guardar cambios
/menu cancel                    # Cancelar cambios
```

**Alias:** `/menued`

**Permisos:**
- `survivalcore.admin` - Editar menús

**Tipos de Acción:**
- `COMMAND <comando>` - Ejecutar comando
- `OPEN_MENU <nombre>` - Abrir otro menú
- `MESSAGE <texto>` - Enviar mensaje
- `CLOSE` - Cerrar inventario

---

### Comando Admin (`/sc`)

```
/sc reload                      # Recargar configuración
/sc gui edit <menú>             # Editar menú (alias)
/sc help                        # Ver ayuda
```

**Permisos:**
- `survivalcore.admin` - Acceso total

---

## 🔐 Permisos

```yaml
# Permisos base
survivalcore.player              # Acceso básico (default: true)
survivalcore.admin               # Acceso administrativo (default: op)

# Arena
survivalcore.arena.join          # Unirse a arenas
survivalcore.arena.info          # Ver info de arena
survivalcore.arena.admin         # Crear/editar arenas

# Clan
survivalcore.clan.create         # Crear clanes
survivalcore.clan.invite         # Invitar a clan
survivalcore.clan.admin          # Gestionar clanes

# Auction
survivalcore.auction.sell        # Vender items
survivalcore.auction.bid         # Pujar en subastas
survivalcore.auction.admin       # Gestionar subastas

# Bounty
survivalcore.bounty.create       # Crear recompensas
survivalcore.bounty.claim        # Reclamar recompensas
survivalcore.bounty.admin        # Gestionar recompensas

# Job
survivalcore.job.set             # Cambiar job
survivalcore.job.admin           # Crear/editar jobs

# Menu
survivalcore.menu.edit           # Editar menús
```

---

## 📝 Placeholders

Usables en comandos de menú y mensajes:

### Jugador
```
%player%              # Nombre del jugador
%uuid%                # UUID del jugador
%display_name%        # Nombre mostrado
%game_mode%           # Modo de juego (SURVIVAL, CREATIVE, etc)
%level%               # Nivel XP vanilla
%exp%                 # XP actual (0.0-1.0)
%ping%                # Ping en ms
%online_players%      # Jugadores conectados
%max_players%         # Máximo de jugadores
```

### Salud y Hambre
```
%health%              # Vida actual (ej: 20.0)
%max_health%          # Vida máxima (ej: 20.0)
%hunger%              # Hambre (0-20)
%saturation%          # Saturación (0.0-20.0)
```

### Ubicación
```
%world%               # Mundo actual
%x%                   # Coordenada X
%y%                   # Coordenada Y
%z%                   # Coordenada Z
%yaw%                 # Rotación horizontal
%pitch%               # Rotación vertical
```

### Inventario
```
%held_item%           # Item en mano (material)
%off_hand%            # Item en mano secundaria
```

### Tiempo
```
%time%                # Milliseconds (unix timestamp)
%timestamp%           # Segundos (unix timestamp)
```

### Ejemplos:
```
/menu name §e%player% en Arena
# Resultado: "§eJuan en Arena"

/menu action COMMAND job set MINER
# Ejecuta: "job set MINER" cuando clickea

/menu action MESSAGE §a+100 XP por %job%
# Envía: "§a+100 XP por MINER"
```

---

## 🎮 Sistemas Implementados

### ✅ Arena System

**Descripción:** Sistema PvP 1v1 con rankings y rewards

**Características:**
- 2 arenas predefinidas (Principal, Lava)
- Sistema de ranking automático
- Rewards por victoria
- Tracking de estadísticas (W/L)
- Entrada de dinero requerida

**Base de Datos:**
```sql
-- Tabla: arenas
id              INT PRIMARY KEY
name            VARCHAR(50)
world           VARCHAR(50)
max_players     INT
entry_fee       BIGINT
win_reward      BIGINT
active          BOOLEAN
created_at      TIMESTAMP

-- Tabla: arena_stats
player_id       INT
arena_id        INT
wins            INT
losses          INT
rank            INT
total_earnings  BIGINT
last_match      TIMESTAMP
```

---

### ✅ Clan System

**Descripción:** Sistema de clanes con tesorería compartida y alianzas

**Características:**
- Crear/desbandir clanes
- Sistema de roles (Admin, Moderador, Miembro)
- Tesorería compartida
- Sistema de alianzas entre clanes
- Invitaciones de miembros

**Base de Datos:**
```sql
-- Tabla: clans
id              INT PRIMARY KEY
name            VARCHAR(50) UNIQUE
owner_id        INT
money           BIGINT
power           INT
created_at      TIMESTAMP

-- Tabla: clan_members
clan_id         INT
player_id       INT
role            ENUM('ADMIN', 'MODERATOR', 'MEMBER')
joined_at       TIMESTAMP

-- Tabla: clan_alliances
clan_1_id       INT
clan_2_id       INT
created_at      TIMESTAMP
```

---

### ✅ Auction System

**Descripción:** Casa de subastas para vender/comprar items

**Características:**
- Listar items para venta
- Sistema de pujas con validación
- Reembolso automático de pujas anteriores
- Subastas de 24 horas
- Auto-completar subastas expiradas

**Base de Datos:**
```sql
-- Tabla: auctions
id              INT PRIMARY KEY
seller_id       INT
item_name       VARCHAR(100)
quantity        INT
start_price     BIGINT
current_bid     BIGINT
current_bidder  INT
start_time      TIMESTAMP
end_time        TIMESTAMP
status          ENUM('ACTIVE', 'COMPLETED', 'CANCELLED')
```

---

### ✅ Bounty System

**Descripción:** Sistema de recompensas por cabeza

**Características:**
- Crear recompensas en otros jugadores
- Reclamación automática al matar
- Killstreak tracking
- Historial de ganancias
- Notificaciones

**Base de Datos:**
```sql
-- Tabla: bounties
id              INT PRIMARY KEY
target_uuid     VARCHAR(36)
reward          BIGINT
created_by      INT
created_at      TIMESTAMP
claimed_by      INT
claimed_at      TIMESTAMP
status          ENUM('ACTIVE', 'CLAIMED', 'EXPIRED')
```

---

### ✅ Job System

**Descripción:** Sistema de experiencia por actividades

**Características:**
- 5 tipos de job (Miner, Fisherman, Lumberjack, Farmer, Hunter)
- XP dinámico por tipo de bloque/item
- Tracking de ganancias diarias
- Cambio de job con dinero

**Base de Datos:**
```sql
-- Tabla: player_jobs
player_id       INT
job_type        ENUM(...)
level           INT
experience      BIGINT
money_today     BIGINT
last_reset      DATE
```

---

## ⚙️ Configuración

### config.yml

```yaml
# ========== CONFIGURACIÓN GENERAL ==========
plugin:
  name: "SurvivalCore"
  version: "1.0.14"
  author: "Gabriel"
  
# ========== BASE DE DATOS ==========
database:
  host: "localhost"
  port: 3306
  database: "survivalcore"
  username: "root"
  password: "password"
  
  # Pool de conexiones
  pool:
    max_size: 10
    min_idle: 2
    max_lifetime_ms: 1800000
    idle_timeout_ms: 600000

# ========== ECONOMY (DINERO) ==========
economy:
  # Dinero inicial para nuevos jugadores
  starting_money: 5000
  
  # Dinero máximo
  max_money: 999999999

# ========== ARENA ==========
arena:
  # Precio de entrada por arena
  entry_fee:
    main: 1000
    lava: 2000
  
  # Dinero ganado por victoria
  win_reward:
    main: 5000
    lava: 10000
  
  # Duración de match (segundos)
  match_duration: 300
  
  # Permitir combate en arenas
  pvp_enabled: true

# ========== CLAN ==========
clan:
  # Precio de crear clan
  creation_cost: 10000
  
  # Dinero máximo por clan
  max_clan_money: 500000
  
  # Máximo de miembros
  max_members: 50
  
  # Requerir invitación (si false, cualquiera puede unirse)
  require_invite: true

# ========== AUCTION ==========
auction:
  # Duración de subasta (horas)
  duration_hours: 24
  
  # Comisión por venta (%)
  commission_percent: 5
  
  # Precio mínimo de venta
  min_price: 100
  
  # Precio máximo de venta
  max_price: 1000000

# ========== BOUNTY ==========
bounty:
  # Precio mínimo de recompensa
  min_bounty: 1000
  
  # Precio máximo de recompensa
  max_bounty: 100000
  
  # Recompensas expiran después de (días)
  expiration_days: 30

# ========== JOB ==========
job:
  # Dinero para cambiar de job
  job_change_cost: 500
  
  # Multiplicador de XP
  xp_multiplier: 1.0
  
  # Dinero ganado por XP (ratio)
  money_per_xp: 0.1

# ========== NOTIFICACIONES ==========
notifications:
  # Sonidos
  enabled_sounds: true
  
  # Chat messages
  enabled_chat: true
  
  # Títulos
  enabled_titles: true
  
  # Duración de título (ticks, 20 = 1 segundo)
  title_duration: 40

# ========== LOGGING ==========
logging:
  # Nivel de log
  level: "INFO"  # INFO, DEBUG, WARNING, ERROR
  
  # Guardar logs en archivo
  file_logging: true
  
  # Transacciones financieras
  log_transactions: true

# ========== DEBUG ==========
debug:
  # Mostrar mensajes de debug en consola
  debug_mode: false
  
  # Tiempo de queries (milisegundos)
  log_query_time: 100
```

---

### messages_es.yml

```yaml
# ========== MENSAJES ARENA ==========
arena:
  joined:
    - "§a✓ Te uniste a %arena%"
    - "§7Entrada: §6$%fee%"
    - "§7Premio: §a$%reward%"
  
  left:
    - "§c✗ Saliste de la arena"
  
  not_found:
    - "§cArena no encontrada: %arena%"
  
  full:
    - "§cLa arena está llena"
  
  insufficient_money:
    - "§cNo tienes suficiente dinero"
    - "§cRequerido: §6$%required%"
    - "§cTienes: §6$%have%"

# ========== MENSAJES CLAN ==========
clan:
  created:
    - "§a✓ Clan creado: %clan%"
  
  invited:
    - "§e%inviter% te invitó a: %clan%"
    - "§7Escribe: /clan accept"
  
  invite_sent:
    - "§a✓ Invitación enviada a %player%"
  
  already_in_clan:
    - "§cYa estás en un clan"

# ========== MENSAJES AUCTION ==========
auction:
  listed:
    - "§a✓ Item listado a §6$%price%"
    - "§7ID: §b%id%"
    - "§7Expira en: §b24 horas"
  
  bid_placed:
    - "§a✓ Puja de §6$%amount% colocada"
  
  outbid:
    - "§cSuperaron tu puja en: %item%"
    - "§7Nueva puja: §6$%amount%"
  
  won:
    - "§a✓ ¡Ganaste la subasta!"
    - "§7Item: %item%"

# ========== MENSAJES BOUNTY ==========
bounty:
  created:
    - "§a✓ Recompensa creada"
    - "§7Objetivo: §c%target%"
    - "§7Precio: §6$%amount%"
  
  claimed:
    - "§a✓ ¡Recompensa reclamada!"
    - "§7Ganaste: §6$%reward%"
  
  target_warned:
    - "§c⚠ Hay una recompensa sobre ti"
    - "§7Precio: §6$%amount%"

# ========== MENSAJES JOB ==========
job:
  changed:
    - "§a✓ Cambió tu job a: %job%"
  
  gained_xp:
    - "§6+%xp% XP §7de §e%job%"
  
  level_up:
    - "§a✓ ¡Subiste de nivel!"
    - "§7%job%: §bNivel %level%"
```

---

## 🔌 API para Desarrolladores

### Obtener Managers

```java
// En tu plugin
import com.atlasMC.survivalcore.api.*;

// Obtener APIs
EconomyAPI economyAPI = (EconomyAPI) Bukkit.getServer()
    .getPluginManager()
    .getPlugin("SurvivalCore");

IArenaManager arenaManager = // ...
IClanManager clanManager = // ...
IAuctionManager auctionManager = // ...
IBountyManager bountyManager = // ...
IJobManager jobManager = // ...
```

### Ejemplos de uso

```java
// Arena
UUID playerUuid = player.getUniqueId();
arenaManager.joinArena(playerUuid, "main");
arenaManager.recordWin(playerUuid, "main", 5000);
int rank = arenaManager.getArenaRank(playerUuid, "main");

// Clan
Clan clan = clanManager.createClan(playerUuid, "MyClan");
clanManager.invite(clan.getId(), otherPlayerUuid);
clanManager.addClanMoney(clan.getId(), 1000);

// Auction
Auction auction = auctionManager.listItem(playerUuid, "Diamond", 1, 5000);
boolean bidPlaced = auctionManager.placeBid(auction.getId(), playerUuid, 6000);

// Bounty
Bounty bounty = bountyManager.setBounty(creatorUuid, targetUuid, 10000);
bountyManager.claimBounty(bounty.getId(), killerUuid);

// Economy
long balance = economyAPI.getBalance(playerUuid);
economyAPI.addBalance(playerUuid, 1000);
boolean removed = economyAPI.removeBalance(playerUuid, 500);
```

---

## 🐛 Troubleshooting

### Los clics en menú no funcionan
**Solución:**
- Reinicia el servidor
- Verifica que MenuClickListener esté registrado
- Mira logs: `/server` console

### No se guarda dinero en BD
**Solución:**
- Verifica conexión MySQL: `config.yml`
- Mira permisos de usuario BD
- Revisa logs de errores SQL

### XP no se suma
**Solución:**
- Verifica que tienes un job: `/job`
- Mira que el listener está registrado
- Reinicia el servidor

### Clan no se crea
**Solución:**
- Verifica que tienes suficiente dinero
- Comprueba permisos: `survivalcore.clan.create`
- Mira que no estás en otro clan

---

## 📞 Contacto y Soporte

**GitHub:** https://github.com/Gabri180/SurvivalCore

**Bugs/Features:** Abre un Issue en GitHub

**Documentación:** Ver `NEXT_STEPS.md` para desarrollo

---

**Última actualización:** 27 Julio 2026  
**Versión:** 1.0.14
