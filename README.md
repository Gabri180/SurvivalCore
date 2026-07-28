# SurvivalCore v1.0.25 — Survival PvP Enterprise

**Status:** ✅ **v1.0.36 - Production Ready (ULTRA FINAL + PETS)**

---

## 📊 Resumen General

SurvivalCore es un plugin completo para servidores Minecraft Paper 1.21.1 con múltiples sistemas integrados:
- **Economía**: Sistema de dinero con transacciones, subastas y economía de jugadores
- **Progresión**: Jobs (trabajos), Skills (habilidades), Misiones (dailies, weeklies, permanentes)
- **PvP**: Sistema de arena competitivo con ranking ELO
- **Clanes**: Sistema de clanes con guerras, miembros y roles
- **Raideo**: Sistema de claims con ventanas horarias y asedios
- **Menús**: Editor visual de menús personalizados in-game
- **Eventos**: Sistema de eventos especiales (Double XP, Double Money)
- **Leaderboards**: Rankings globales en tiempo real
- **Base de datos**: MySQL con 16+ tablas, caché en memoria, backups automáticos

**Líneas de código:** ~6500  
**Compilación:** Maven con shade (todas las dependencias incluidas)

---

## ✅ CARACTERÍSTICAS IMPLEMENTADAS

### 1️⃣ CORE & ECONOMÍA (v1.0.0 - v1.0.5)
- ✅ **EconomyAPI** - Sistema de balance de jugadores
- ✅ **PlayerCache** - Caché en memoria con TTL y batch operations
- ✅ **DatabaseManager** - HikariCP connection pool + async queries
- ✅ **InitialSchema.sql** - 16 tablas MySQL con índices optimizados
- ✅ **PlayerRepository** - CRUD de perfiles, rangos, prestigios
- ✅ **ConfigManager** - Carga recursiva de YAMLs organizados
- ✅ **Notificaciones** - Sistema de notificaciones configurables

### 2️⃣ JOBS & SKILLS (v1.0.6 - v1.0.10)
- ✅ **JobManager** - Sistemas de trabajos con niveles y pagos
- ✅ **SkillManager** - Árbol de habilidades con experiencia
- ✅ **JobRepository** - Persistencia en BD
- ✅ **SkillRepository** - Persistencia en BD
- ✅ **Comandos**: `/job menu`, `/job set <tipo>`, `/job info`
- ✅ **Comandos**: `/skill menu`, `/skill list`, `/skill info <tipo>`
- ✅ **Listeners**: Detección automática de progreso (sin progreso visual aún)

### 3️⃣ MISIONES (v1.0.11 - v1.0.15)
- ✅ **MissionManager** - Sistema de misiones con múltiples tipos
- ✅ **MissionRepository** - Persistencia de progreso en BD
- ✅ **Tipos soportados**: KILL, COLLECT, MINE, FISH, CRAFT, TRAVEL
- ✅ **Frecuencias**: DAILY (auto-reset), WEEKLY, MONTHLY, PERMANENT
- ✅ **Auto-detección**: Progreso automático sin necesidad de comandos
- ✅ **Claim system** (v1.0.25): `/mission claim` para reclamar rewards
- ✅ **Comandos**: `/mission menu`, `/mission list`, `/mission info`, `/mission claim`
- ✅ **Listeners**: MissionProgressListener con auto-reset diario

### 4️⃣ PvP & ARENA (v1.0.16 - v1.0.20)
- ✅ **ArenaManager** - Sistema 1v1 con ranking ELO
- ✅ **ArenaRepository** - Persistencia de estadísticas
- ✅ **Comandos**: `/arena join`, `/arena stats`, `/arena ranking`
- ✅ **Listeners**: Detección de kills, muertes, abandonos
- ✅ **UI**: Menú interactivo para ver ranking

### 5️⃣ CLANES (v1.0.16 - v1.0.20)
- ✅ **ClanManager** - Crear, invitar, expulsar, roles
- ✅ **ClanWarManager** - Declarar guerra, puntuación
- ✅ **ClanRepository** - BD de clanes + miembros
- ✅ **Comandos**: `/clan create`, `/clan invite`, `/clan members`, `/clan war`
- ✅ **Listeners**: Integración con eventos del plugin
- ✅ **Roles**: Owner, Moderator, Member con permisos diferentes

### 6️⃣ RAIDEO & ASEDIOS (v1.0.16 - v1.0.20)
- ✅ **ClaimManager** - Sistema de claims/territorios
- ✅ **SiegeManager** - Sistema de asedios
- ✅ **ClaimRepository** - BD de claims + cargas de asedio
- ✅ **Ventanas horarias**: Configurables por claim
- ✅ **Inmunidad post-raideo**: Configurable (default 24h)
- ✅ **Límite diario de cargas**: Configurable
- ✅ **Comandos**: `/claim info`, `/siege start`, `/siege status`

### 7️⃣ SUBASTAS & BOUNTIES (v1.0.16 - v1.0.20, mejorado en v1.0.26)
- ✅ **AuctionManager** - Sistema completo de subastas con pujas
- ✅ **Auto-finalización**: Scheduler limpia subastas expiradas cada minuto
- ✅ **Notificaciones**: Alertas cuando se es superado en puja
- ✅ **Notificaciones finales**: Vendedor y ganador reciben confirmación
- ✅ **BountyManager** - Sistema completo de recompensas por cabeza
- ✅ **Notificaciones de bounty**: Target y asesino reciben mensajes
- ✅ **Killstreak tracking**: Seguimiento de muertes consecutivas
- ✅ **AuctionRepository** - BD con actualización en tiempo real
- ✅ **Comandos**: `/auction create`, `/auction bid`, `/auction info`
- ✅ **Comandos**: `/bounty create`, `/bounty list`

### 8️⃣ JEFES (v1.0.16 - v1.0.20, mejorado en v1.0.26)
- ✅ **BossManager** - Completo con tracking de daño
- ✅ **Damage tracking** - Seguimiento de daño por jugador
- ✅ **Top damagers** - Ranking de top 3 dañadores
- ✅ **Boss rewards** - Dinero + XP automático (10k$+500exp / 12k$+600exp)
- ✅ **Broadcast notifications** - Anuncio servidor cuando jefe cae
- ✅ **BossRepository** - Persistencia en BD
- ✅ **Comandos**: `/boss info`, `/boss stats`
- ✅ **Listeners**: Drops especiales, evento de muerte

### 9️⃣ MENÚS PERSONALIZADOS (v1.0.22 - v1.0.24)
- ✅ **CustomMenuBuilder** - Fluent API para crear menús
- ✅ **MenuEditor** - Editor visual in-game
- ✅ **MenuYamlWriter** - Persistencia automática de menús
- ✅ **MenuLoader** - Carga de menús al startup
- ✅ **Comandos**: `/custommenu create|item|itemaction|save`
- ✅ **Comandos**: `/menu editor` - Editor visual
- ✅ **Tab-completion**: Sugerencias contextuales
- ✅ **Auto-save**: Guardado automático tras cada edición
- ✅ **Placeholder support**: %player%, %uuid%, %display_name%, etc.
- ✅ **Tipos de acciones**: COMMAND, MESSAGE, OPEN_MENU, CLOSE, NONE

### 🔟 EVENTOS ESPECIALES (v1.0.25)
- ✅ **EventManager** - Gestor de eventos globales
- ✅ **EventRepository** - Persistencia en BD
- ✅ **Tipos de eventos**: DOUBLE_XP, DOUBLE_MONEY
- ✅ **Duración configurable**: Por evento
- ✅ **Multiplicadores automáticos**: Aplicados a ganancias
- ✅ **Notificaciones**: Inicio y fin de evento
- ✅ **Comandos**: `/event info`, `/event schedule`, `/event list`
- ✅ **Integración**: JobManager y EconomyAPI

### 1️⃣1️⃣ LEADERBOARDS (v1.0.25)
- ✅ **LeaderboardManager** - 5 sistemas de ranking
- ✅ **Rankings**:
  - Arena ELO ranking
  - Clan power ranking
  - Skill rankings por tipo
  - Money ranking (top ricos)
  - Jobs XP ranking
- ✅ **Caché actualizado**: Cada 5 minutos
- ✅ **Pagination**: 10 jugadores por página
- ✅ **Rango personal**: Muestra posición del jugador
- ✅ **Comandos**: `/leaderboard <type> [page]`
- ✅ **UI interactivo**: Menú clickeable con navegación

### 1️⃣2️⃣ DATABASE & PERFORMANCE (v1.0.25)
- ✅ **PlayerCache mejorado**: TTL, expiry tracking, batch operations
- ✅ **Índices de BD**: Optimizados para queries frecuentes
- ✅ **BackupScheduler**: Backups automáticos cada 2 horas
- ✅ **BackupCommand**: `/sc backup` para backups manuales
- ✅ **Rotación automática**: Mantiene últimos 10 backups
- ✅ **Soporte mysqldump**: Backups cifrados y comprimidos

---

## ✅ CARACTERÍSTICAS EXPANDIDAS (v1.0.27-v1.0.30)

### 1️⃣3️⃣ TORNEOS DE ARENA (v1.0.27)
- ✅ **TournamentManager** - Crear y gestionar torneos
- ✅ **Bracket system** - Auto-start cuando se llena
- ✅ **Prize pools** - Dinero distribuido al ganador
- ✅ **Matchmaking** - Score de compatibilidad de jugadores
- ✅ **Status tracking** - OPEN → IN_PROGRESS → FINISHED
- ✅ **Broadcast notifications** - Anuncios de eventos

### 1️⃣4️⃣ SISTEMA DE CLANES EXPANDIDO (v1.0.28)
- ✅ **ClanBankManager** - Banco compartido del clan
- ✅ **Deposits/Withdrawals** - Gestión de dinero del clan
- ✅ **Slot management** - 36-108 slots configurables
- ✅ **ClanAllianceManager** - Alianzas entre clanes
- ✅ **Ally lookup** - Verificación de alianzas activas
- ✅ **Cross-clan partnerships** - Múltiples alianzas simultáneas

### 1️⃣5️⃣ VISUALIZACIÓN DE RAIDS (v1.0.29)
- ✅ **RaidVisualizationManager** - Mostrar bordes de claims
- ✅ **3D cube borders** - Partículas en los 12 bordes
- ✅ **Real-time display** - Visualización dinámica para jugadores
- ✅ **Raid progress** - Indicador visual durante ataques
- ✅ **Particle effects** - END_ROD para bordes, FLAME para raideo
- ✅ **Smooth animation** - 100 ticks de duración

### 1️⃣6️⃣ SUBASTAS AVANZADAS & REPUTACIÓN (v1.0.30)

### 1️⃣7️⃣ REINFORCING SYSTEM (v1.0.31)
- ✅ **ReinforcingManager** - Reforzar bloques en claims
- ✅ **Reinforcement Levels** - IRON (5k$) → DIAMOND (15k$) → OBSIDIAN (50k$)
- ✅ **Damage Reduction** - 25% por nivel (máx 75%)
- ✅ **Health Tracking** - Bloques con vida/salud
- ✅ **Upgrade System** - Mejorar nivel de refuerzo
- ✅ **Auto-destruction** - Bloques se rompen cuando mueren
- ✅ **Repair System** - Reparar bloques reforzados

### 1️⃣8️⃣ ITEM REPAIR SYSTEM (v1.0.32)
- ✅ **ItemRepairManager** - Reparar durability
- ✅ **Cost System** - 100 dinero por punto de durability
- ✅ **Durability Indicator** - Green/Yellow/Red/Dark Red status
- ✅ **Enchantment Support** - Reparar items encantados
- ✅ **Wear Tracking** - Porcentaje de desgaste
- ✅ **Perfect State Check** - No reparar items nuevos

### 1️⃣9️⃣ BLOCK PROTECTION (v1.0.33)
- ✅ **BlockProtectionManager** - Proteger bloques estratégicos
- ✅ **Per-Claim Protection** - Por cada claim
- ✅ **Owner-Only Access** - Solo owner puede editar
- ✅ **Bulk Operations** - Limpiar protección de claims
- ✅ **Protected Block Listing** - Ver bloques protegidos
- ✅ **Integration Ready** - Compatible con ClanManager

### 2️⃣0️⃣ ADVANCED PVP STATS (v1.0.34)
- ✅ **AdvancedPvPManager** - Estadísticas avanzadas
- ✅ **Kill Streaks** - Racha de kills con notificaciones
- ✅ **Streak Notifications**:
  - x3: [Racha x3]
  - x5: [Racha x5]
  - x10+: [¡RACHA x10!]
- ✅ **Total K/D Tracking** - Kills, deaths, ratio
- ✅ **Top Killers** - Leaderboard de asesinos
- ✅ **Server Announcements** - Anuncios servidor-wide
- ✅ **Auto-Reset** - Reinicio de racha en muerte

### 2️⃣1️⃣ QUALITY OF LIFE (v1.0.35)
- ✅ **QualityOfLifeManager** - Mejoras de UX
  - AFK detection (5 min timeout)
  - AFK/BACK announcements
  - Custom MOTD
  - Server status display
  - Player highlight effects
  - Teleport with messages
- ✅ **CacheOptimizer** - Monitoreo de performance
  - Cache hit rate tracking
  - Memory usage monitoring
  - Auto-GC cuando > 90%
  - Real-time performance metrics
- ✅ **ReputationManager** - Sistema de reputación global
- ✅ **Reputation levels**: OUTLAW (-100), NEUTRAL (0), HERO (+100)
- ✅ **Dynamic pricing**:
  - Outlaws: 30% más caro (penalty)
  - Neutral: precio normal
  - Heroes: 15% descuento
- ✅ **Reputation events**:
  - PvP victory: +5 ganador, -3 perdedor
  - Bounty kill: +10 asesino, -15 target
  - Tournament win: +20 al ganador
- ✅ **Reputation perks**: Heroes acceso prioritario, Outlaws bloqueados de AH
- ✅ **ScheduledAuction** - Subastas programadas para el futuro
- ✅ **Leaderboard** - Top reputados del servidor

---

## ❌ LO QUE FALTA (Futuras Mejoras)

### FASE 1: Mejoras Visuales
- ⏳ Progreso visual en UI de misiones
- ⏳ Árbol visual de skills (GUI)
- ⏳ Mapa interactivo de territorio
- ⏳ Efectos especiales al level-up

### FASE 2: Sistemas Avanzados
- ⏳ Sistema de reinforcing (bloques reforzados)
- ⏳ Protección de bloques estratégicos
- ⏳ Compras programadas automáticas
- ⏳ Subastas con puja automática

### FASE 3: Infraestructura
- ⏳ REST API pública
- ⏳ Redis caching distribuido
- ⏳ Dashboard web de administración
- ⏳ Webhook de eventos

### FASE 4: Optimización
- ⏳ Query optimization (análisis de performance)
- ⏳ Compresión de datos en BD
- ⏳ Async/await para operaciones pesadas
- ⏳ Clustering de servidor

---

## 📋 COMANDOS DISPONIBLES

### Admin
```
/sc reload              # Recargar configuración
/sc gui                 # Abrir GUI de admin
/sc help                # Ayuda de admin
/sc backup              # Hacer backup manual
```

### Jugador - Economía
```
/money balance          # Ver balance
/money pay <player>     # Pagar a jugador
```

### Jugador - Progresión
```
/job menu               # Menú de trabajos
/job set <tipo>         # Cambiar trabajo
/job info               # Ver info del trabajo

/skill menu             # Menú de skills
/skill list             # Listar skills
/skill info <tipo>      # Info de skill
/stats [jugador]        # Ver estadísticas
```

### Jugador - Misiones
```
/mission menu           # Menú de misiones
/mission list           # Listar misiones
/mission info <id>      # Info de misión
/mission claim          # Reclamar todas las rewards
/mission claim <id>     # Reclamar reward específica
```

### Jugador - PvP & Clanes
```
/arena join             # Entrar a arena
/arena stats            # Ver estadísticas
/arena ranking          # Ver ranking

/clan create <nombre>   # Crear clan
/clan invite <player>   # Invitar jugador
/clan members           # Ver miembros
/clan war               # Declarar guerra
```

### Jugador - Subastas & Bounties
```
/auction create         # Crear subasta
/auction bid <cantidad> # Pujar
/auction info           # Ver subastas activas

/bounty create          # Crear recompensa
/bounty list            # Ver bounties
```

### Jugador - Eventos
```
/event info             # Ver evento actual
/event list             # Listar eventos activos
/event schedule         # Programar evento (admin)
```

### Jugador - Rankings
```
/leaderboard money      # Top 10 ricos
/leaderboard arena      # Top 10 PvP
/leaderboard clan       # Top 10 clanes
/leaderboard skill      # Top 10 skills
/leaderboard job        # Top 10 jobs
```

### Editor de Menús
```
/custommenu create <id>                    # Crear menú
/custommenu item <id> <slot> <material>    # Agregar item
/custommenu itemaction <id> <slot> <tipo>  # Agregar acción
/custommenu save <id>                      # Guardar menú
/custommenu list                           # Listar menús
/custommenu open <id>                      # Abrir menú

/menu editor                                # Editor visual
```

---

## 🔧 CONFIGURACIÓN

Todos los archivos de configuración están en `plugins/SurvivalCore/`:

### Archivos principales:
- `config.yml` - Conexión a BD, logging, mensajes
- `economy.yml` - Sistema económico (salarios, precios base)
- `progression.yml` - XP rates, multiplicadores
- `pvp.yml` - Sistema de arena, ELO rates
- `clans.yml` - Sistema de clanes
- `claims.yml` - Ventanas horarias, inmunidad, límites
- `missions.yml` - Definición de misiones
- `jobs.yml` - Definición de trabajos
- `skills.yml` - Árbol de skills
- `bosses.yml` - Spawns de jefes semanales
- `events.yml` - Configuración de eventos
- `backup.yml` - Configuración de backups

### Ejemplo: Crear un evento
```yaml
events:
  double_xp:
    enabled: true
    duration_minutes: 120
    multiplier: 2.0
```

---

## 📦 REQUISITOS

- **JDK 21** - Compilación
- **Java 21+** - Runtime en servidor
- **MySQL 8+** - Base de datos
- **Paper 1.21.1** - Servidor Minecraft
- **Maven 3.9+** - Build

---

## 🚀 COMPILACIÓN & DEPLOY

```bash
# Compilar
mvn clean package -DskipTests

# Resultado: target/SurvivalCore-1.0.25.jar

# Deploy
cp target/SurvivalCore-1.0.25.jar /ruta/servidor/plugins/
```

El plugin se genera "shaded" (todas las dependencias incluidas: HikariCP, MySQL Driver).

---

## 📊 ESTADÍSTICAS DEL PROYECTO

| Métrica | Valor |
|---------|-------|
| **Versión Actual** | 1.0.36 (ULTRA FINAL + PETS) |
| **Líneas de código** | ~6,500 |
| **Tablas de BD** | 16+ |
| **Comandos** | 30+ |
| **Permisos** | 20+ |
| **Listeners** | 15+ |
| **Managers** | 12 |
| **Repositorios** | 10+ |
| **Compilación** | ✅ 100% exitosa |
| **Status** | ✅ Producción lista |

---

## 🛠️ ARQUITECTURA

```
SurvivalCore/
├── api/
│   ├── EventAPI
│   ├── EconomyAPI
│   ├── IJobManager
│   ├── ISkillManager
│   ├── IMissionManager
│   ├── IArenaManager
│   ├── IClanManager
│   ├── IClanWarManager
│   ├── IClaimManager
│   ├── IBossManager
│   ├── IAuctionManager
│   └── IBountyManager
│
├── managers/
│   ├── LeaderboardManager
│   ├── EventManager
│   └── [impl]
│
├── db/
│   ├── DatabaseManager (HikariCP)
│   ├── PlayerRepository
│   ├── JobRepository
│   ├── SkillRepository
│   ├── MissionRepository
│   ├── ClanRepository
│   ├── ArenaRepository
│   ├── AuctionRepository
│   └── EventRepository
│
├── menu/
│   ├── CustomMenuBuilder
│   ├── MenuManager
│   ├── MenuEditorUI
│   ├── MenuYamlWriter
│   └── MenuLoader
│
├── cache/
│   └── PlayerCache (con TTL)
│
├── scheduler/
│   ├── BackupScheduler
│   └── DailyMissionResetTask
│
├── listeners/
│   ├── MissionProgressListener
│   ├── MenuEditorListener
│   └── [15+ listeners]
│
├── commands/
│   ├── EventCommand
│   ├── LeaderboardCommand
│   ├── BackupCommand
│   └── [30+ commands]
│
└── models/
    ├── Mission
    ├── MissionProgress
    ├── Job
    ├── Skill
    └── [30+ models]
```

---

## 🔐 Seguridad

- ✅ Prepared Statements (SQL Injection prevention)
- ✅ Async queries (no bloqueos en thread principal)
- ✅ Input validation en todos los comandos
- ✅ Permission checks en cada comando
- ✅ Rate limiting en algunas operaciones
- ✅ Backups automáticos + rotación

---

## 📝 Changelog

### v1.0.36 (Último - PETS SYSTEM)
- ✅ **Pet System** - Mascotas completas (7 tipos)
- ✅ **Pet Types** - Wolf, Dragon, Phoenix, Demon, Angel, Cat, Fairy
- ✅ **Pet Leveling** - Exp-based con rates configurables
- ✅ **Pet Menu** - Integración en /menu edit
- ✅ **Pet Shop** - Comprar mascotas desde menú
- ✅ **Pet Commands** - /pet create|list|summon|dismiss|info|menu
- ✅ **Disabled by Default** - Configurable en pets.yml
- ✅ **Menu Integration** - Accesible desde editor de menús

### v1.0.35 (ULTRA FINAL)
- ✅ **Reinforcing System** - Refuerzo de bloques con 4 niveles
- ✅ **Item Repairs** - Reparar durability de items por dinero
- ✅ **Block Protection** - Proteger bloques estratégicos
- ✅ **Advanced PvP Stats** - Kill streaks, K/D ratio, top killers
- ✅ **Quality of Life** - AFK detection, MOTD, server status
- ✅ **Cache Optimizer** - Monitoreo de performance y memoria

### v1.0.30 (FINAL)
- ✅ **Arena Tournaments** - Sistema de torneos con brackets y rewards
- ✅ **Clan Banking** - Banco compartido con gestión de dinero del clan
- ✅ **Clan Alliances** - Sistema de alianzas entre clanes
- ✅ **Raid Visualization** - Visualización 3D de bordes de claims con partículas
- ✅ **Reputation System** - Reputación global con 3 niveles y precios dinámicos
- ✅ **Scheduled Auctions** - Soporte para subastas programadas
- ✅ **Price Multipliers** - Precios basados en reputación del comprador

### v1.0.27-v1.0.29 (Integrated in v1.0.30)
- ✅ Tournament management system (v1.0.27)
- ✅ Clan banking and alliances (v1.0.28)
- ✅ Raid visualization (v1.0.29)

### v1.0.26
- ✅ **Auction System COMPLETO**: Auto-finalización, notificaciones de pujas
- ✅ **Bounty System COMPLETO**: Notificaciones, killstreak tracking
- ✅ **Boss System COMPLETO**: Damage tracking, top damagers, rewards automáticas
- ✅ **AuctionFinalizerScheduler**: Limpieza automática cada minuto
- ✅ **Notifications**: Sistema de notificaciones integrado

### v1.0.25
- ✅ Leaderboards globales (5 tipos)
- ✅ Database optimization + índices
- ✅ Eventos especiales (DOUBLE_XP, DOUBLE_MONEY)
- ✅ Sistema de misiones expandido (claim, auto-reset)
- ✅ Backup automático cada 2 horas

### v1.0.24
- ✅ Editor visual de menús
- ✅ Auto-save de menús
- ✅ Placeholder support (%player%, etc.)

### v1.0.22 - v1.0.23
- ✅ Sistema de menús personalizados
- ✅ CustomMenuBuilder API

### v1.0.16 - v1.0.21
- ✅ PvP, Clanes, Raideo, Subastas, Bounties
- ✅ Repositorios completos

---

## 👥 Autores

- **Gabriel** - Arquitectura, Core, Menús, v1.0.25
- **Hauch** - Jobs, Skills, Misiones
- **Dev3** - PvP, Clanes, Raideo, Subastas

---

## 📄 Licencia

Proyecto privado - AtlasMC

---

## 💬 Support

Para reportar bugs o sugerencias, abre un issue en GitHub.

---

**Última actualización:** 2026-07-27  
**Versión:** 1.0.25  
**Status:** ✅ Production Ready
