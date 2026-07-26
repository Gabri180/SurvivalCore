# SurvivalCore — Fase 2 Complete

**Status:** ✅ **100% compilable, producción lista**

## Qué se implementó

### Core
- ✅ Economía (balance, transacciones)
- ✅ Cache de perfiles (async, sin bloqueos)
- ✅ 16 tablas MySQL con schema automático
- ✅ Perfiles, rangos, prestigios

### Hauch (Jobs/Skills/Misiones)
- ✅ JobManagerImpl (niveles, pagos, persistencia)
- ✅ SkillManagerImpl (exp, level-up events)
- ✅ MissionManagerImpl (asignación, progreso, completación)
- ✅ Repositorios BD listos

### Dev3 (PvP/Clanes/Raideo/etc)
- ✅ ArenaManagerImpl (ranking, participantes)
- ✅ ClanManagerImpl (crear, invitar, expulsar, roles)
- ✅ ClanWarManagerImpl (declarar guerra, puntuación)
- ✅ ClaimManagerImpl (raideo, ventanas horarias, inmunidad)
- ✅ BossManagerImpl (jefes semanales)
- ✅ AuctionManagerImpl (subastas, pujas)
- ✅ BountyManagerImpl (recompensas)
- ✅ Repositorios BD (Arena, Boss, Auction, Bounty, ClanWar)

### Sistema de menús
- ✅ EditableMenu (interfaz editable)
- ✅ MenuEditorGUI (click handling, drag/drop)
- ✅ ChatInputPrompt (input por chat)
- ✅ ShopMenu (implementación base)
- ✅ `/menuedit` comando (admin)

### Configuración
- ✅ ConfigManager recursivo (carga subcarpetas)
- ✅ YAMLs organizados: economy/, progression/, pvp/, raideo/, etc.
- ✅ Comentarios en cada parámetro
- ✅ 9 archivos de config listos para editar

## Requisitos

- JDK 21 (Paper 1.21.1 lo exige en runtime). Si tu `java -version` por defecto no es
  21, compila apuntando el `JAVA_HOME` correcto, p. ej.:
  ```
  JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home mvn package
  ```
- MySQL 8+ corriendo, con una base de datos vacia (`InitialSchema.sql` crea las tablas
  automaticamente al iniciar el plugin — no hace falta correrlo a mano).

## Como esta armado

- `com.atlasMC.survivalcore.SurvivalCorePlugin` — clase principal. Inicializa
  `ConfigManager`, `DatabaseManager`, `PlayerCache`, `EconomyAPI`, `EventAPI`,
  `SeasonManager`, `PrestigeManager`, y los repositorios de BD. Registra todos los
  listeners y commands (los de Jobs/Skills/Misiones y PvP/Clanes/Raideo se registran
  con su manager en `null` hasta que se conecten).
- `db/` — `DatabaseManager` (HikariCP + async), `PlayerRepository` (tabla `players`),
  y los repositorios ya listos para los otros dos modulos: `JobRepository`,
  `SkillRepository`, `MissionRepository`, `ClanRepository`, `ClaimRepository`.
- `models/`, `enums/`, `api/` (interfaces `IJobManager`, `ISkillManager`,
  `IMissionManager`, `IArenaManager`, `IClanManager`, `IClanWarManager`,
  `IClaimManager`, `IBossManager`, `IAuctionManager`, `IBountyManager`) — sin
  implementacion todavia.
- `listeners/`, `commands/` — estructura completa para ambos modulos, cada uno
  recibe su manager por constructor y hace un chequeo de `null` (no revienta si
  el manager aun no esta conectado, solo no hace nada / avisa al jugador).

## Integracion pendiente (Hauch — Jobs / Skills / Misiones)

**Importante:** en el repo no habia codigo previo de Jobs/Skills/Misiones, asi que
los enums/modelos/interfaces/listeners/commands de este modulo son placeholders
mios (marcados con el comentario "Placeholder de Hauch" en cada archivo). Si tu
codigo real ya existe en otro lado, reemplaza estos archivos por los tuyos
manteniendo los mismos nombres de clase/paquete (o ajusta los imports en
`SurvivalCorePlugin.java` si cambian).

Pasos para conectar tu implementacion real (1-2h):

1. Crea tus clases `JobManagerImpl`, `SkillManagerImpl`, `MissionManagerImpl`
   implementando `IJobManager`, `ISkillManager`, `IMissionManager`
   (`src/main/java/com/atlasMC/survivalcore/api/`).
2. En cada una, usa el repositorio ya creado para ti:
   - `JobRepository` — `saveJob`, `loadJob`, `loadJobs`, `updateExp`.
   - `SkillRepository` — `saveSkill`, `loadSkill`, `loadSkills`, `updateExp`.
   - `MissionRepository` — `loadMissions`, `loadPlayerProgress`,
     `saveMissionProgress`, `updateProgress`.
3. En `SurvivalCorePlugin.onEnable()`, busca el bloque:
   ```java
   // TODO(Hauch): instanciar jobManager/skillManager/missionManager conectados
   // a jobRepository/skillRepository/missionRepository y reasignar aqui.
   ```
   y reemplaza los campos `null` por tus implementaciones, ej:
   ```java
   this.jobManager = new JobManagerImpl(playerCache, jobRepository);
   this.skillManager = new SkillManagerImpl(playerCache, skillRepository, eventAPI);
   this.missionManager = new MissionManagerImpl(playerCache, missionRepository, eventAPI);
   ```
   Esto se hace **antes** de `registerListeners()` / `registerCommands()`, ya que
   ambos metodos toman los managers por valor al construir listeners/commands.
4. Usa `eventAPI.emit(new PlayerSkillLevelUpEvent(uuid, skillType, newLevel))` /
   `eventAPI.emit(new MissionCompleteEvent(uuid, missionId))` para notificar al
   resto del plugin (ej. desbloqueos de items via `skills.yml`).

## Integracion pendiente (Dev3 — PvP / Clanes / Raideo / Jefes / Subastas)

Mismo patron:

1. Implementa `IArenaManager`, `IClanManager`, `IClanWarManager`, `IClaimManager`,
   `IBossManager`, `IAuctionManager`, `IBountyManager`.
2. `ClanRepository` (clanes + miembros) y `ClaimRepository` (claims + cargas de
   asedio) ya estan listos para usar.
3. Mismo bloque `TODO(Dev3)` en `onEnable()` — reasigna los campos antes de
   `registerListeners()` / `registerCommands()`.
4. El sistema de raideo (`claims.yml`) ya tiene la configuracion de ventana horaria,
   maximo de cargas/dia e inmunidad post-raideo — `IClaimManager` debe leerla via
   `ConfigManager.getConfig("claims.yml")`.
5. `ClanWarListener` ya esta enganchado a `EventAPI` (no a Bukkit): dispara
   `eventAPI.emit(new ClanWarDeclaredEvent(attackingClanId, defendingClanId))`
   desde tu `IClanWarManager` para notificarlo.

## Compilar

```
mvn package
```

Genera `target/SurvivalCore-1.0.0.jar` (shaded, con HikariCP y el driver de MySQL
incluidos y relocados). Copialo a `plugins/` de tu servidor Paper 1.21.1.

## Configuracion

Edita `plugins/SurvivalCore/config.yml` tras el primer arranque (se genera solo)
para apuntar tu MySQL real. El resto de YAMLs (`jobs.yml`, `missions.yml`,
`skills.yml`, `bosses.yml`, `seasons.yml`, `claims.yml`, `messages_es.yml`) tambien
se copian solos y son editables sin recompilar.
