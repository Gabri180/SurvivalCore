# PROMPT PARA CLAUDE CODE - SurvivalCore v1.0.24+

Copia el siguiente prompt completo y pásalo a Claude Code para continuar el desarrollo:

---

## PROMPT COMPLETO

```
Continúa el desarrollo de SurvivalCore v1.0.23 (Minecraft Paper 1.21.1 plugin).

El plugin está completamente funcional con:
- Sistema de menús personalizados con auto-save
- Jobs, Skills, Misiones, Arena, Clan, Auction, Bounty
- Leaderboards globales con pagination
- Eventos especiales con multiplicadores
- Sistema de notificaciones completo
- Permisos dinámicos y customizables

La tarea es implementar una de las siguientes opciones:

═══════════════════════════════════════════════════════════════════════════════

OPCIÓN 1 - Database & Performance (v1.0.24) [RECOMENDADO PRIMERO]
────────────────────────────────────────────────────────────────

Optimizar infraestructura para soportar 100+ jugadores sin lag.

CAMBIOS REQUERIDOS:

1. PlayerCache Mejorado
   Archivo: src/main/java/com/atlasMC/survivalcore/cache/PlayerCache.java
   - Agregar Map<UUID, Long> para tracking de lastAccess
   - Método getOrLoadBatch(List<UUID>, Consumer<List<PlayerProfile>>)
   - Método invalidateExpired() que remueve entradas con 30+ min sin acceso
   - Configurable timeout en config.yml

2. Optimizar Queries Frecuentes
   Archivos: src/main/java/com/atlasMC/survivalcore/db/*Repository.java
   - Revisar y optimizar queries en: PlayerRepository, ClanRepository, AuctionRepository
   - Usar índices existentes correctamente en JOINs

3. Agregar Índices a BD
   Archivo: src/main/resources/db/migrations/InitialSchema.sql
   Índices a agregar:
   - clans: ALTER TABLE clans ADD INDEX idx_clans_owner_id (owner_id);
   - clan_members: ALTER TABLE clan_members ADD INDEX idx_clan_members_player_id (player_id);
   - auctions: ALTER TABLE auctions ADD INDEX idx_auctions_end_time (end_time);
   - siege_charges: ALTER TABLE siege_charges ADD INDEX idx_siege_charges_claim_id (claim_id);
   - bounties: ALTER TABLE bounties ADD INDEX idx_bounties_target_uuid (target_uuid);

4. Backup Automático
   Archivos nuevos:
   - src/main/java/com/atlasMC/survivalcore/scheduler/BackupScheduler.java
   - Comando /sc backup para admins
   
   Funcionalidad:
   - Ejecuta mysqldump cada 2 horas (async)
   - Guarda en plugins/SurvivalCore/backups/backup-YYYY-MM-DD-HH-MM.sql
   - Rotación automática (mantiene últimos 10 backups)
   - Configurable en config.yml con backup.enabled, backup.interval-hours, backup.mysqldump-path
   - Graceful fallback si mysqldump no disponible

═══════════════════════════════════════════════════════════════════════════════

OPCIÓN 2 - Eventos Especiales (v1.0.25)
──────────────────────────────────────

Agregar gamification con eventos temporales que motiven participación.

CAMBIOS REQUERIDOS:

1. Event Model & Manager
   Archivos nuevos:
   - src/main/java/com/atlasMC/survivalcore/events/Event.java (interface)
   - src/main/java/com/atlasMC/survivalcore/events/DoubleXPEvent.java
   - src/main/java/com/atlasMC/survivalcore/events/DoubleMoneyEvent.java
   - src/main/java/com/atlasMC/survivalcore/managers/EventManager.java
   
   Event interface debe tener:
   - getEventType(): String
   - getStartTime(): long
   - getEndTime(): long
   - isActive(): boolean
   - getMultiplier(): double

2. Base de Datos
   Tabla: events
   ```sql
   CREATE TABLE IF NOT EXISTS events (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       event_type VARCHAR(32) NOT NULL,
       multiplier DOUBLE NOT NULL,
       start_time DATETIME NOT NULL,
       end_time DATETIME NOT NULL,
       created_by BIGINT,
       INDEX idx_events_active (start_time, end_time)
   )
   ```

3. EventManager
   Métodos requeridos:
   - startEvent(eventType, multiplier, durationMinutes): void
   - getActiveEvent(): Event (null si no hay evento)
   - getEventMultiplier(eventType): double
   - stopEvent(): void
   - Refrescar cada 30 segundos desde BD

4. EventCommand
   Archivo: src/main/java/com/atlasMC/survivalcore/commands/EventCommand.java
   Comandos:
   - /event info → Ver evento actual
   - /event schedule <tipo> <minutos> → Programar evento
   - /event list → Listar eventos activos
   
   Permisos: survivalcore.event.admin

5. Integración
   - JobManager: Multiplicar XP ganado por eventManager.getEventMultiplier("XP")
   - Economy: Multiplicar dinero ganado por eventManager.getEventMultiplier("MONEY")
   - Listeners: Notificar al join si hay evento activo

═══════════════════════════════════════════════════════════════════════════════

OPCIÓN 3 - Leaderboards Mejorados (v1.0.26)
────────────────────────────────────────────

Sistema de rankings competitivos para motivar jugadores.

CAMBIOS REQUERIDOS:

1. LeaderboardManager
   Archivo: src/main/java/com/atlasMC/survivalcore/managers/LeaderboardManager.java
   
   Métodos:
   - getArenaLeaderboard(): List<LeaderboardEntry>
   - getClanLeaderboard(): List<LeaderboardEntry>
   - getSkillLeaderboard(SkillType): List<LeaderboardEntry>
   - getMoneyLeaderboard(): List<LeaderboardEntry>
   - getJobLeaderboard(JobType): List<LeaderboardEntry>
   - getPlayerRank(UUID, type): int
   - refreshAll(): void
   
   Caché:
   - Refrescar cada 5 minutos
   - LinkedHashMap ordenado por rank
   - Smartcache con invalidación por tipo

2. LeaderboardEntry
   Archivo: src/main/java/com/atlasMC/survivalcore/model/LeaderboardEntry.java
   ```java
   public class LeaderboardEntry {
       private int rank;
       private UUID playerUUID;
       private String playerName;
       private double value;  // ELO, dinero, XP
       private long cachedAt;
   }
   ```

3. LeaderboardCommand
   Archivo: src/main/java/com/atlasMC/survivalcore/commands/LeaderboardCommand.java
   
   Comandos:
   - /leaderboard arena → Top 10 arenas
   - /leaderboard clan → Top 10 clanes
   - /leaderboard skill <tipo> → Top 10 skill
   - /leaderboard money → Top 10 ricos
   - /leaderboard job <tipo> → Top 10 job
   
   UI: Menú interactivo con pagination

4. Datos a rankear:
   - Arena: arena_stats.elo
   - Clan: clans.power
   - Skill: player_skills.experience (por tipo)
   - Money: players.money
   - Job: player_job_progress.experience (por tipo)

═══════════════════════════════════════════════════════════════════════════════

OPCIÓN 4 - Misiones Expandidas (v1.0.27)
────────────────────────────────────────

Completar sistema de misiones con rewards y auto-reseteo.

CAMBIOS REQUERIDOS:

1. MissionProgressListener Mejorado
   Archivo: src/main/java/com/atlasMC/survivalcore/listeners/MissionProgressListener.java
   
   Auto-detectar progreso:
   - Kill missions: EntityDeathEvent
   - Collect missions: PlayerPickupItemEvent
   - Mine missions: BlockBreakEvent
   - Fish missions: PlayerFishEvent
   - Craft missions: CraftItemEvent
   - Travel missions: PlayerMoveEvent (distancia)
   
   Al completar: marcar como COMPLETED_UNCLAIMED (no auto-reward)

2. DailyMissionResetTask
   Archivo: src/main/java/com/atlasMC/survivalcore/scheduler/DailyMissionResetTask.java
   
   Funcionalidad:
   - Ejecutar cada día a las 00:00 UTC
   - Resetear missions DAILY: progress = 0, completed = FALSE
   - Mantener WEEKLY/MONTHLY/PERMANENT sin cambios
   - Notificar jugadores

3. Comando /mission claim
   Archivo: src/main/java/com/atlasMC/survivalcore/commands/MissionCommand.java (agregar)
   
   Comando:
   - /mission claim → Reclamar todas completadas
   - /mission claim <id> → Reclamar específica
   
   Lógica:
   - Validar completada
   - Validar no reclamada
   - Agregar dinero + XP
   - Marcar como claimed en BD
   - Mensaje de confirmación

4. Base de Datos
   Tabla: player_mission_progress
   - Agregar columna: claimed BOOLEAN DEFAULT FALSE
   - Agregar columna: claimed_at DATETIME NULL

5. UI Mejorado
   - Mostrar barra de progreso: [=====-----] 5/10
   - Estados: ACTIVE, COMPLETED, CLAIMED
   - Botón "Claim Rewards" si completada

═══════════════════════════════════════════════════════════════════════════════

INSTRUCCIONES CRÍTICAS:

1. ACTUALIZAR VERSIÓN ANTES DE COMPILAR:
   - pom.xml: <version>X.X.X</version> (2 lugares)
   - plugin.yml: version: X.X.X
   - NEXT_STEPS2.md: actualizar "Versión Actual" y registrar cambios

2. Lee NEXT_STEPS2.md para referencia de:
   - Todos los comandos implementados
   - Permisos disponibles
   - Placeholders
   - Patrón de código esperado

3. Patrones a seguir:
   - Managers: implementan interfaces (IXxxManager)
   - Listeners: registrados en SurvivalCorePlugin.onEnable()
   - Comandos: heredan CommandExecutor + TabExecutor
   - Menús: usar MenuFactory + MenuManager
   - BD: usar DatabaseManager.queryAsync() / executeAsync()

4. Compilación obligatoria:
   mvn clean package -DskipTests
   
   Verificar: target/SurvivalCore-X.X.X.jar existe

5. Commit & Push:
   git add -A
   git commit -m "Description (vX.X.X)"
   git push origin main

SELECCIONA UNA OPCIÓN (1-4) Y COMIENZA LA IMPLEMENTACIÓN.
```

---

## CÓMO USAR ESTE PROMPT

1. Copia **COMPLETAMENTE** el texto entre los bloques ``` (desde "Continúa..." hasta "COMIENZA...")
2. Abre Claude Code
3. Pega el prompt completo en la ventana de chat
4. Especifica cuál opción quieres: "Hazlo todo en orden (1, 2, 3, 4)" o "Hazlo la opción X"
5. Deja que Claude termine la implementación

---

## ORDEN RECOMENDADO

Para máximo impacto y estabilidad:

1. **v1.0.24** - Database & Optimizaciones (base sólida)
2. **v1.0.25** - Eventos Especiales (gamification)
3. **v1.0.26** - Leaderboards (competencia)
4. **v1.0.27** - Misiones Expandidas (completitud)

---

## NOTAS

- Cada versión tarda ~15-30 minutos en implementarse
- El plugin compila sin errores y está listo para testing
- Todos los cambios son automáticamente guardados en Git
- Se recomienda hacer backup del servidor antes de actualizar

Última actualización: 27 Julio 2026
