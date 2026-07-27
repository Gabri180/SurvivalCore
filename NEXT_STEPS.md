# SurvivalCore - Próximos Pasos

**⚠️ CRÍTICO: Actualiza versión en estos 3 archivos ANTES de compilar:**
```
1. pom.xml:              <version>X.X.X</version>
2. pom.xml:              <finalName>SurvivalCore-X.X.X</finalName>
3. plugin.yml:           version: X.X.X
```

**Versión Actual: 1.0.16**  
**Próxima: 1.0.17**

---

## 🎯 FASE ACTUAL (1.0.17) - MEDIUM PRIORITY

### 1. Sistema de Misiones Completo
- [ ] Expandir `IMissionManager` con más métodos
- [ ] `/mission` comando interactivo
- [ ] Cargar misiones desde `missions.yml`
- [ ] Progreso de misiones diarias/semanales
- [ ] Rewards al completar
- [ ] Menú con pagination de misiones activas

**Tipos de misiones:**
- [ ] Kill missions (matar X mobs específicos)
- [ ] Collect missions (recolectar X items)
- [ ] Travel missions (ir a X ubicación)
- [ ] Craft missions (craftear X items)

---

## ✅ COMPLETADO (1.0.16)

### Job Menu Interactivo
**Estado:** ✅ Implementado | Impacto: Alto

- [x] Menú interactivo de jobs con `/job menu`
- [x] Material-coded items para cada job:
  - [x] DIAMOND_PICKAXE para Miner
  - [x] FISHING_ROD para Fisherman
  - [x] IRON_AXE para Lumberjack
  - [x] WOODEN_HOE para Farmer
  - [x] BOW para Hunter
- [x] Ver stats completos: Nivel, XP, nombre del job
- [x] Click en job para cambiar
- [x] Menú integrado con MenuFactory
- [x] Slot de "Mi Trabajo" para ver info rápida

### Sistema de Notificaciones Completo
**Estado:** ✅ Implementado | Impacto: Medio

- [x] NotificationManager - Hub centralizado para todos los eventos
- [x] Notificaciones por tipo de evento:
  - [x] Arena (join, win, loss)
  - [x] Clan (creation, invitations)
  - [x] Auction (bid placed, outbid, won)
  - [x] Bounty (created, claimed, on player)
  - [x] Job (XP gain, level up)
- [x] Chat messages formateados con [Notificación] prefix
- [x] Sonidos contextuales por evento:
  - [x] ENTITY_PLAYER_LEVELUP para victorias
  - [x] ENTITY_EXPERIENCE_ORB_PICKUP para subastas
  - [x] BLOCK_ANVIL_LAND para pujas superadas
  - [x] ENTITY_WARDEN_HEARTBEAT para recompensas
- [x] Títulos/subtítulos en eventos importantes
- [x] `/notificaciones` comando con menú interactivo
- [x] NotificationPreferences por jugador:
  - [x] Toggle individual por tipo (arena, clan, auction, bounty, job)
  - [x] Toggles globales (sonidos, títulos, chat)
  - [x] Valores por defecto (todo activado)
- [x] Alias de comando: `/notif`, `/notifs`
- [x] Subcomandos: toggle, status, menú

---

## ✅ COMPLETADO (1.0.15)

### Subcomandos Dev3
**Estado:** ✅ Implementado | Impacto: Máximo

**Arena Subcomandos:**
- [x] `/arena join <id>` - Unirse a arena (restar dinero, trackear en ArenaManager)
- [x] `/arena leave` - Salir de arena (descontar participante)
- [x] `/arena info <id>` - Ver info de arena (jugadores, entrada, premio)
- [x] `/arena ranking` - Ver top 10 del ranking (placeholder para futuro)

**Clan Subcomandos:**
- [x] `/clan create <nombre>` - Crear clan (deducir dinero, crear propietario)
- [x] `/clan invite <jugador>` - Invitar a clan (validar miembro)
- [x] `/clan accept` - Aceptar invitación (placeholder para futuro)
- [x] `/clan leave` - Salir del clan
- [x] `/clan info` - Ver info del clan (dinero, miembros, alianzas)
- [x] `/clan bank` - Ver tesorería
- [x] `/clan members` - Listar miembros con roles

**Auction Subcomandos:**
- [x] `/auction sell <precio>` - Vender item en mano
- [x] `/auction list` - Ver subastas activas
- [x] `/auction mylist` - Ver mis subastas
- [x] `/auction bid <id> <cantidad>` - Pujar en subasta
- [x] `/auction claim` - Reclamar items ganados (placeholder para futuro)

**Bounty Subcomandos:**
- [x] `/bounty create <jugador> <cantidad>` - Crear recompensa
- [x] `/bounty list` - Ver recompensas activas
- [x] `/bounty mylist` - Ver mis recompensas creadas
- [x] `/bounty history` - Ver recompensas pagadas (placeholder para futuro)

**Archivos a modificar:**
- `ArenaCommand.java` - Agregar handler de subcomandos
- `ClanCommand.java` - Agregar handler de subcomandos
- `AuctionCommand.java` - Agregar handler de subcomandos
- `BountyCommand.java` - Agregar handler de subcomandos

---

### 2. Notificaciones y Feedback - IMPORTANTE
**Estado:** ❌ No implementado | Impacto: Medio

- [ ] Notificar cuando suben puja en auction
- [ ] Notificar cuando crean bounty sobre ti
- [ ] Chat messages al ganar XP de jobs
- [ ] Sonidos en eventos (victoria arena, nueva puja)
- [ ] Títulos/subtítulos en eventos importantes

**Archivos a crear:**
- `NotificationManager.java` - Centralizar notificaciones
- Integrar en listeners de eventos

---

## 🎯 FASE 2 (1.0.16) - MEDIUM PRIORITY

### 1. Sistema de Misiones Completo
- [ ] Expandir `IMissionManager` con más métodos
- [ ] `/mission` comando interactivo
- [ ] Cargar misiones desde `missions.yml`
- [ ] Progreso de misiones diarias/semanales
- [ ] Rewards al completar
- [ ] Menú con pagination de misiones activas

**Tipos de misiones:**
- [ ] Kill missions (matar X mobs específicos)
- [ ] Collect missions (recolectar X items)
- [ ] Travel missions (ir a X ubicación)
- [ ] Craft missions (craftear X items)

---

### 2. Menu Avanzado - Pagination y Efectos
- [ ] Pagination para menús con 10+ items
- [ ] Botones anterior/siguiente
- [ ] Items animados (material que cambia)
- [ ] Efectos de partículas al clickear
- [ ] Menus con búsqueda/filtros

**Archivos a crear:**
- `PaginatedMenu.java` - Sistema de pagination
- `AnimatedMenuItem.java` - Items con animación

---

## 🎯 FASE 3 (1.0.17) - MEDIUM PRIORITY

### Sistema de Skills Completo
- [ ] Implementar `SkillManagerImpl` totalmente
- [ ] Skill trees con múltiples paths
- [ ] Bonificadores de stats basados en skills
- [ ] Perks/habilidades especiales
- [ ] `/skills` menú interactivo
- [ ] Subir skills al realizar acciones relacionadas

---

## 🎯 FASE 4 (1.0.18+) - LOW PRIORITY

### Database & Optimizaciones
- [ ] Caching asincrónico mejorado
- [ ] Optimizar queries a BD
- [ ] Agregar índices a tablas
- [ ] Backup automático de datos

### Features Avanzadas
- [ ] Eventos especiales del servidor
- [ ] Double XP events
- [ ] Seasonal content
- [ ] Leaderboards globales
- [ ] Rankings por skill/clan/arena

### Documentación
- [ ] Actualizar README
- [ ] Guía completa de comandos
- [ ] Wiki del plugin
- [ ] Ejemplos de configuración

---

## 📋 CHECKLIST ANTES DE PUSH

```bash
# 1. Actualizar versión (SIEMPRE)
# 2. Compilar sin errores
mvn clean package -DskipTests

# 3. Verificar cambios
git status

# 4. Hacer commit descriptivo
git add -A
git commit -m "Feature description (vX.X.X)"

# 5. Pushear
git push origin main
```

---

## 🔧 ÚTILES

**Compilar:**
```bash
mvn clean package -DskipTests
```

**Ver commits pendientes:**
```bash
git log --oneline origin/main..HEAD
```

**Resetear a última versión:**
```bash
git reset --hard origin/main
```

---

## 📊 ESTADO ACTUAL (1.0.16)

| Módulo | Estado | % |
|--------|--------|-----|
| Click Listeners | ✅ | 100% |
| Managers (Arena/Clan/Auction/Bounty) | ✅ | 100% |
| Job Listeners | ✅ | 100% |
| Comandos Menú | ✅ | 100% |
| Subcomandos Dev3 | ✅ | 100% |
| **Job Menu Interactivo** | ✅ | 100% |
| **Sistema de Notificaciones** | ✅ | 100% |
| Misiones | ⚠️ | 20% |
| Menu Avanzado | ❌ | 0% |
| Skills | ⚠️ | 20% |

---

## 🎯 PRÓXIMO PASO

**Ver PROMPT.md para instrucciones para Claude Code**
