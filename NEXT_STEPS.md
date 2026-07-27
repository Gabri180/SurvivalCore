# SurvivalCore - Próximos Pasos

**⚠️ CRÍTICO: Actualiza versión en estos 3 archivos ANTES de compilar:**
```
1. pom.xml:              <version>X.X.X</version>
2. pom.xml:              <finalName>SurvivalCore-X.X.X</finalName>
3. plugin.yml:           version: X.X.X
```

**Versión Actual: 1.0.14**  
**Próxima: 1.0.15**

---

## 🎯 FASE ACTUAL (1.0.15) - HIGH PRIORITY

### 1. Subcomandos Dev3 - CRÍTICO
**Estado:** ❌ No implementado | Impacto: Máximo

**Arena Subcomandos:**
- [ ] `/arena join <id>` - Unirse a arena (restar dinero, trackear en ArenaManager)
- [ ] `/arena leave` - Salir de arena (descontar participante)
- [ ] `/arena info <id>` - Ver info de arena (jugadores, entrada, premio)
- [ ] `/arena ranking` - Ver top 10 del ranking

**Clan Subcomandos:**
- [ ] `/clan create <nombre>` - Crear clan (deducir dinero, crear propietario)
- [ ] `/clan invite <jugador>` - Invitar a clan (validar miembro)
- [ ] `/clan accept` - Aceptar invitación
- [ ] `/clan leave` - Salir del clan
- [ ] `/clan info` - Ver info del clan (dinero, miembros, alianzas)
- [ ] `/clan bank` - Ver tesorería
- [ ] `/clan members` - Listar miembros con roles

**Auction Subcomandos:**
- [ ] `/auction sell <precio>` - Vender item en mano
- [ ] `/auction list` - Ver subastas activas (con menú paginated)
- [ ] `/auction mylist` - Ver mis subastas
- [ ] `/auction bid <id> <cantidad>` - Pujar en subasta
- [ ] `/auction claim` - Reclamar items ganados

**Bounty Subcomandos:**
- [ ] `/bounty create <jugador> <cantidad>` - Crear recompensa
- [ ] `/bounty list` - Ver recompensas activas (con menú paginated)
- [ ] `/bounty mylist` - Ver mis recompensas creadas
- [ ] `/bounty history` - Ver recompensas pagadas

**Archivos a modificar:**
- `ArenaCommand.java` - Agregar handler de subcomandos
- `ClanCommand.java` - Agregar handler de subcomandos
- `AuctionCommand.java` - Agregar handler de subcomandos
- `BountyCommand.java` - Agregar handler de subcomandos

---

### 2. Job Menu Interactivo - IMPORTANTE
**Estado:** ⚠️ Base lista | Impacto: Alto

- [ ] Crear `/job menu` o `/job` (abre menú de jobs)
- [ ] Clickeable: Mostrar job actual con color
- [ ] Ver stats: Nivel, XP, ganancias del día
- [ ] Click en job para cambiar
- [ ] Upgrade de job con dinero (si existe sistema)

**Archivos a crear/modificar:**
- `JobCommand.java` - Mejorar con menú
- `MenuFactory.addJobMenu()` - Menú dinámico de jobs

---

### 3. Notificaciones y Feedback - IMPORTANTE
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

## 📊 ESTADO ACTUAL (1.0.14)

| Módulo | Estado | % |
|--------|--------|-----|
| Click Listeners | ✅ | 100% |
| Managers (Arena/Clan/Auction/Bounty) | ✅ | 100% |
| Job Listeners | ✅ | 100% |
| Comandos Menú | ✅ | 100% |
| **Subcomandos Dev3** | ❌ | 0% |
| **Job Menu** | ❌ | 0% |
| **Notificaciones** | ❌ | 0% |
| Misiones | ⚠️ | 20% |
| Menu Avanzado | ❌ | 0% |
| Skills | ⚠️ | 20% |

---

## 🎯 PRÓXIMO PASO

**Ver PROMPT.md para instrucciones para Claude Code**
