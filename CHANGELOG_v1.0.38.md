# SurvivalCore v1.0.38 - Changelog

## 🎉 Nuevas Features Agregadas

### 1. Sistema de Eventos Especiales
**Descripción:** Crea eventos temporales que afecten a todos los jugadores del servidor.

**Características:**
- Tipos de eventos: Double XP, Double Money, Bonus Arena, Seasonal
- Multiplicadores configurables
- Tiempo de duración personalizable
- Estado activo/inactivo
- Expiración automática

**Clases Agregadas:**
- `SpecialEvent.java` - Modelo de eventos
- `IEventManager.java` - Interface del gestor
- `EventManagerImpl.java` - Implementación

**Comandos:**
```bash
/event create <id> <nombre> <tipo> <multiplicador> [duracion]
/event start <id>
/event stop <id>
/event list
/event info <id>
/event delete <id>
```

---

### 2. Leaderboards Globales (Rankings)
**Descripción:** Sistema de rankings para jugadores en diferentes categorías.

**Características:**
- Categorías: Dinero, Victorias en Arena, Poder de Clan, Nivel de Skills, Nivel de Trabajo
- Ranking dinámico por categoría
- Paginación de resultados
- Búsqueda de posición individual
- Actualización en tiempo real

**Clases Agregadas:**
- `LeaderboardEntry.java` - Entrada de leaderboard
- `ILeaderboardManager.java` - Interface del gestor
- `LeaderboardManagerImpl.java` - Implementación

**Comandos:**
```bash
/leaderboard <money|arena|clan|skill|job> [página]
/lb [página]
/ranking [página]
```

---

### 3. Optimizaciones de Base de Datos
**Descripción:** Mejoras de rendimiento y eficiencia en la BD.

**Características:**
- Creación automática de índices en tablas principales
- Análisis de tabla para optimizar queries
- Limpieza automática de datos fragmentados
- Mejora de velocidad de consultas

**Índices Creados:**
- `idx_player_uuid` - Búsqueda rápida de jugadores
- `idx_arena_active` - Filtro de arenas activas
- `idx_clan_owner` - Búsqueda por propietario
- `idx_auction_active` - Subastas activas
- `idx_bounty_active` - Recompensas activas
- `idx_job_player` - Trabajos por jugador
- `idx_skill_player` - Skills por jugador
- `idx_mission_player` - Misiones por jugador
- `idx_transaction_player` - Transacciones por jugador
- `idx_validation_logs_timestamp` - Logs por timestamp

**Clase Agregada:**
- `DatabaseOptimizer.java` - Optimizador de BD

---

### 4. Sistema de Backups Automáticos
**Descripción:** Backups automáticos periódicos de la base de datos.

**Características:**
- Backups automáticos cada 2 horas (configurable)
- Almacenamiento en carpeta `backups/`
- Limpieza automática de backups antiguos (máx 20)
- Rotación de backups
- Logging de estado

**Clase Agregada:**
- `BackupManager.java` - Gestor de backups

**Métodos Principales:**
```java
startAutoBackup()      // Inicia backups periódicos
stopAutoBackup()       // Detiene backups
performBackup()        // Realiza un backup inmediato
setBackupIntervalMinutes(long)  // Cambia intervalo
getBackupList()        // Lista todos los backups
```

---

## 📊 Mejoras de Rendimiento

### Caching Asincrónico
- Uso de `ConcurrentHashMap` para concurrencia segura
- Operaciones no bloqueantes
- Actualización en background

### Optimizaciones SQL
- Índices en columnas frecuentemente consultadas
- Análisis automático de tablas
- Reducción de full table scans

### Limitaciones
- Máximo 20 backups almacenados
- Limpieza automática de eventos expirados
- Caché thread-safe

---

## 🔧 Cambios Técnicos

### Archivos Modificados
- `pom.xml` - Versión actualizada a 1.0.38
- `plugin.yml` - Versión actualizada a 1.0.38

### Nuevos Paquetes
- `com.atlasMC.survivalcore.events.*` - Sistema de eventos
- `com.atlasMC.survivalcore.leaderboard.*` - Sistema de rankings
- `com.atlasMC.survivalcore.backup.*` - Sistema de backups
- `com.atlasMC.survivalcore.database.*` - Optimizaciones de BD

---

## 📋 Checklist de Implementación

- [x] Crear SpecialEvent model
- [x] Implementar EventManager (interface + impl)
- [x] Crear LeaderboardEntry model
- [x] Implementar LeaderboardManager (interface + impl)
- [x] Crear BackupManager
- [x] Crear DatabaseOptimizer
- [x] Actualizar versión en archivos
- [x] Compilar sin errores
- [x] Validar estructura del proyecto

---

## 🚀 Próximos Pasos (v1.0.39+)

1. Implementar persistencia de eventos (base de datos)
2. Agregar LeaderboardCommand con menús interactivos
3. Integrar eventos con JobManager (afectar XP)
4. Dashboard de estadísticas de servidor
5. Eventos semanales/mensuales automáticos
6. Almacenamiento de leaderboards históricos

---

## ⚙️ Compatibilidad

- **Minecraft:** Paper 1.21.1
- **Java:** JDK 16+
- **Maven:** 3.11.0+
- **Dependencias:** HikariCP 5.1.0, MySQL 8.4.0

---

**Fecha:** 2026-07-30  
**Autor:** SurvivalCore Development Team  
**Versión:** 1.0.38
