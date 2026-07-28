# SurvivalCore v1.0.25 — Survival PvP Enterprise

**Status:** ✅ **v1.0.26 - Production Ready**

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

## ❌ LO QUE FALTA IMPLEMENTAR

### FASE 1: Misiones (Progreso Visual & Mejoras)
- ⏳ Barra de progreso visual en UI
- ⏳ Interfaz mejorada para mostrar progreso
- ⏳ Notificaciones de progreso en tiempo real
- ⏳ Integración con barra de acción (action bar)

### FASE 2: Skills (Árbol Visual & Desbloqueos)
- ⏳ Árbol visual de skills (GUI)
- ⏳ Sistema de desbloqueos por prerequisitos
- ⏳ Efectos especiales al subir nivel
- ⏳ Integración con otros sistemas

### FASE 3: Arena (Mejoras Competitivas)
- ⏳ Sistema de torneos
- ⏳ Rewards automáticos por victoria
- ⏳ Sistema de temporadas
- ⏳ Estadísticas detalladas por jugador

### FASE 4: Clanes (Expansión Completa)
- ⏳ Guerra de clanes automática
- ⏳ Sistema de alianzas
- ⏳ Territorio visual en mapa
- ⏳ Banco de clan (storage compartido)

### FASE 5: Raideo (Sistema Completo)
- ⏳ Visualización 3D de claims
- ⏳ Protección de bloques estratégicos
- ⏳ Sistema de "reinforcing" (refuerzo de bloques)
- ⏳ Notificaciones de ataque en tiempo real

### FASE 6: API Pública
- ⏳ REST API para estadísticas
- ⏳ Webhook de eventos
- ⏳ Integración con sitios web
- ⏳ Dashboard de administración

### FASE 7: Optimizaciones Avanzadas
- ⏳ Redis caching (caché distribuido)
- ⏳ Query optimization (análisis de queries lentas)
- ⏳ Async/await para operaciones largas
- ⏳ Compresión de datos en BD

### FASE 8: Sistema de Reputación
- ⏳ Puntos de reputación por acciones
- ⏳ Niveles de reputación (outlaw, neutral, hero)
- ⏳ Permisos basados en reputación
- ⏳ Mercado con precio dinámico según reputación

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
| **Versión Actual** | 1.0.26 |
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

### v1.0.26 (Último)
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
