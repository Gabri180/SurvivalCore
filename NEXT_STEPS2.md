# SurvivalCore - Guía de Desarrollo v1.0.17+

**IMPORTANTE: Actualiza SIEMPRE la versión antes de compilar**

---

## 📌 CAMBIAR VERSIÓN (ANTES DE COMPILAR)

### Paso 1: Actualizar archivos de versión
```bash
# Editar estos 3 archivos:
1. pom.xml:
   - <version>X.X.X</version>
   - <finalName>SurvivalCore-X.X.X</finalName>

2. src/main/resources/plugin.yml:
   - version: X.X.X

3. Este archivo (NEXT_STEPS2.md):
   - Cambiar "Versión Actual" al inicio
```

### Paso 2: Compilar
```bash
mvn clean package -DskipTests
```

### Paso 3: Commit & Push
```bash
git add -A
git commit -m "Describe changes (vX.X.X)"
git push origin main
```

---

## 🎉 IMPLEMENTADO EN v1.0.17

### Sistema Completo de Menús Personalizados
Un builder tipo **CommandPanels** que permite crear menús in-game sin código Java.

#### 1. **CustomMenuBuilder** (Fluent API)
```java
CustomMenuBuilder builder = new CustomMenuBuilder("myMenu")
    .title("§6Mi Menú")
    .rows(3)
    .backgroundColor("LIGHT_GRAY_STAINED_GLASS_PANE")
    .fillBackground(true)
    .addButton(0, "§eClick Me", "COMMAND", "say Hello!")
    .addCloseButton(26)
    .build();
```

**Métodos disponibles:**
- `title(String)` - Cambiar título (soporta color codes)
- `rows(int)` - Tamaño 1-6 filas
- `backgroundColor(String)` - Material para fondo (LIGHT_GRAY_STAINED_GLASS_PANE, etc)
- `fillBackground(boolean)` - Auto-llenar fondo vacío
- `permission(String)` - Requerir permiso para acceder
- `addItem(slot, material, name, action, lore)` - Agregar item
- `addButton(slot, name, action, lore)` - Botón DIAMOND_BLOCK
- `addCloseButton(slot)` - Botón de cerrar (BARRIER)
- `addBackButton(slot, menuName)` - Botón atrás (ARROW)
- `addSpacer(slot)` - Espacio vacío con fondo
- `addSpacerRow(row)` - Fila completa de espacios

#### 2. **PaginatedMenu** (Menús Grandes)
```java
PaginatedMenu.Builder builder = new PaginatedMenu.Builder("lista", "Mi Lista")
    .itemsPerPage(18)
    .addItem(Material.DIAMOND, "Item 1", MenuAction.command("..."))
    .build();

MenuData page = builder.buildCurrentPage(3);
builder.nextPage();
```

**Características:**
- Pagination automática
- Botones anterior/siguiente
- Items por página configurable
- Tracking de página actual
- Fila de navegación automática

#### 3. **MenuAliasManager** (Comando Alias)
Sistema de aliases dinámicos para menús.

```bash
/custommenu command myMenu set shop
# Ahora /shop abre el menú myMenu
```

**Archivo:** `menu-aliases.yml`
```yaml
shop:myshop
admin:adminpanel

PERM:myshop=survivalcore.player
PERM:admin=survivalcore.admin
```

#### 4. **Sistema de Permisos**
Control de acceso granular para menús.

```bash
/custommenu permission myMenu set survivalcore.vip.access
# Solo jugadores con ese permiso pueden acceder
```

---

## 📋 COMANDOS IMPLEMENTADOS

### /custommenu - Constructor de Menús (Admin)

```
CREACIÓN:
  /custommenu create <id>                    # Crear nuevo menú
  /custommenu item <id> <slot> <material>    # Agregar item
  /custommenu title <id> <título>            # Cambiar título
  /custommenu size <id> <filas>              # Tamaño 1-6
  /custommenu bgcolor <id> <material>        # Color fondo
  /custommenu save <id>                      # Guardar menú
  /custommenu cancel <id>                    # Cancelar edición

GESTIÓN:
  /custommenu open <id>                      # Abrir menú
  /custommenu list                           # Listar menús

ALIASES:
  /custommenu command <id> set <cmd>         # Crear alias
  /custommenu unalias <cmd>                  # Eliminar alias
  /custommenu aliases                        # Listar aliases

PERMISOS:
  /custommenu permission <id> set <perm>     # Asignar permiso
  /custommenu permission <id> clear          # Remover permiso
```

**Alias:** `/customm`, `/cmenu`

---

### /mission - Sistema de Misiones

```
/mission                # Abre menú de misiones
/mission list          # Ver todas las misiones
/mission info <id>     # Detalles de misión
/mission progress      # Ver tu progreso diario
```

**Alias:** `/missions`

**Tipos de misiones:**
- Kill missions (matar X mobs)
- Collect missions (recolectar X items)
- Travel missions (viajar a X ubicación)
- Craft missions (craftear X items)
- Mine missions (minar bloques)
- Fish missions (pescar items)

**Frecuencias:**
- Daily (diaria)
- Weekly (semanal)
- Monthly (mensual)
- Permanent (permanente)

---

### /skill - Sistema de Skills

```
/skill              # Abre árbol de skills
/skill list        # Ver todos tus skills
/skill info <id>   # Detalles de skill
/skill stats       # Estadísticas totales
```

**Alias:** `/skills`

**Categorías de Skills:**
- Combat (Combate) - §c
- Mining (Minería) - §8
- Foraging (Recolección) - §2
- Fishing (Pesca) - §b
- Farming (Granjería) - §6
- Crafting (Artesanía) - §d

**Sistema de progresión:**
- Niveles ilimitados
- XP exponencial (cada nivel = 1.1x anterior)
- Porcentaje al siguiente nivel
- Bonificadores (placeholder para expansión)

---

### /job - Sistema de Trabajos (Mejorado en v1.0.16)

```
/job              # Abre menú interactivo
/job set <tipo>   # Cambiar trabajo
/job info         # Ver estadísticas
/job list         # Listar todos
```

**Alias:** `/jobs`

**Material Codes:**
- MINER → DIAMOND_PICKAXE
- FISHERMAN → FISHING_ROD
- LUMBERJACK → IRON_AXE
- FARMER → WOODEN_HOE
- HUNTER → BOW

---

### /notificaciones - Gestión de Notificaciones (v1.0.16)

```
/notificaciones              # Abre menú de gestión
/notificaciones toggle <tipo>  # Toggle rápido
/notificaciones status       # Ver preferencias
```

**Alias:** `/notif`, `/notifs`

**Toggle types:**
- arena, clan, auction, bounty, job
- sounds, titles, chat

---

### Arena/Clan/Auction/Bounty (v1.0.15 - Subcomandos)

```
ARENA:
  /arena join <id>    # Unirse (requiere dinero)
  /arena leave        # Salir
  /arena info <id>    # Ver info
  /arena ranking      # Top 10

CLAN:
  /clan create <nombre>    # Crear ($10,000)
  /clan invite <jugador>   # Invitar
  /clan accept            # Aceptar
  /clan leave             # Salir
  /clan info              # Ver info
  /clan members           # Listar miembros
  /clan bank              # Ver tesorería

AUCTION:
  /auction sell <precio>   # Vender item
  /auction list            # Ver subastas
  /auction mylist          # Mis subastas
  /auction bid <id> <qty>  # Pujar
  /auction claim           # Reclamar

BOUNTY:
  /bounty create <player> <qty>   # Crear recompensa
  /bounty list                    # Ver recompensas
  /bounty mylist                  # Mis recompensas
  /bounty history                 # Historial
```

---

## 🔐 PERMISOS IMPLEMENTADOS

```yaml
# Admin & Base
survivalcore.admin           # Acceso total
survivalcore.player          # Acceso base (default: true)

# Comandos
survivalcore.custommenu.create    # Crear menús
survivalcore.mission.*            # Comandos de misiones
survivalcore.skill.*              # Comandos de skills
survivalcore.job.set              # Cambiar job

# Arena
survivalcore.arena.join           # Unirse a arenas
survivalcore.arena.info           # Ver info

# Clan
survivalcore.clan.create          # Crear clan
survivalcore.clan.invite          # Invitar

# Auction
survivalcore.auction.sell         # Vender
survivalcore.auction.bid          # Pujar

# Bounty
survivalcore.bounty.create        # Crear recompensa
survivalcore.bounty.claim         # Reclamar

# Custom Menus
survivalcore.vip.access           # Acceso a menús VIP
survivalcore.admin.panel          # Panel de admin
server.builder                     # Para builder tools
```

---

## 📝 PLACEHOLDERS DISPONIBLES

### Jugador
```
%player%           # Nombre del jugador
%uuid%             # UUID único
%display_name%     # Nombre mostrado
%game_mode%        # SURVIVAL, CREATIVE, etc
%level%            # Nivel XP vanilla
%exp%              # XP actual (0.0-1.0)
%ping%             # Ping en ms
%online_players%   # Jugadores conectados
%max_players%      # Máximo de jugadores
```

### Salud y Hambre
```
%health%           # Vida actual
%max_health%       # Vida máxima
%hunger%           # Hambre (0-20)
%saturation%       # Saturación (0.0-20.0)
```

### Ubicación
```
%world%            # Mundo actual
%x% %y% %z%        # Coordenadas
%yaw%              # Rotación horizontal
%pitch%            # Rotación vertical
```

### Inventario
```
%held_item%        # Item en mano
%off_hand%         # Item en mano secundaria
```

### Tiempo
```
%time%             # Milliseconds (unix)
%timestamp%        # Segundos (unix)
```

---

## 🎨 PLACEHOLDERS EN MENÚS

```bash
# En títulos de menú:
/custommenu title myMenu §e%player% en Arena

# En lore de items:
/custommenu item myMenu 0 DIAMOND "§eTe conectaste: %timestamp%"

# En acciones:
/custommenu item myMenu 0 DIAMOND "test" MESSAGE "§aHola %player%"
```

---

## 📊 NOTIFICACIONES (v1.0.16)

### Tipos de Notificaciones
```
Arena:   Join, Win, Loss
Clan:    Creation, Invitations
Auction: Bid Placed, Outbid, Won
Bounty:  Created, Claimed, Warned
Job:     XP Gain, Level Up
```

### Sonidos
```
ENTITY_PLAYER_LEVELUP      # Victorias
ENTITY_EXPERIENCE_ORB_PICKUP  # Pujas
BLOCK_ANVIL_LAND           # Superado
ENTITY_WARDEN_HEARTBEAT    # Recompensas
ENTITY_VILLAGER_YES        # Invitaciones
```

### Toggleables
- Notifications por tipo (arena, clan, auction, bounty, job)
- Sonidos globales
- Títulos globales
- Chat globales

---

## 🎯 PRÓXIMO A IMPLEMENTAR (v1.0.18+)

### Fase 1: Database & Optimizaciones
- [ ] Caching asincrónico mejorado
- [ ] Optimizar queries a BD
- [ ] Agregar índices a tablas
- [ ] Backup automático de datos

### Fase 2: Features Avanzadas
- [ ] Eventos especiales del servidor
- [ ] Double XP events
- [ ] Seasonal content
- [ ] Leaderboards globales
- [ ] Rankings por skill/clan/arena

### Fase 3: Expansiones de Sistemas
- [ ] Completar API de Misiones
- [ ] Agregar bonificadores a Skills
- [ ] Expandir sistema de Notificaciones
- [ ] Crear comando `/settings` para preferencias

### Fase 4: Documentación
- [ ] Guía completa de API
- [ ] Tutorial CustomMenuBuilder
- [ ] Ejemplos de plugins que extienden SurvivalCore

---

## 📋 CHECKLIST ANTES DE COMPILAR (IMPORTANTE)

```bash
✓ Actualizar versión en pom.xml (2 lugares)
✓ Actualizar versión en plugin.yml
✓ Actualizar NEXT_STEPS2.md con versión
✓ Hacer cambios de código
✓ Compilar: mvn clean package -DskipTests
✓ Verificar SurvivalCore-X.X.X.jar existe
✓ git add -A
✓ git commit -m "Description (vX.X.X)"
✓ git push origin main
```

---

## 📊 ESTADÍSTICAS ACTUALES (v1.0.17)

| Componente | Estado | Líneas |
|-----------|--------|--------|
| Comandos | ✅ | ~1500 |
| Managers | ✅ | ~2000 |
| Listeners | ✅ | ~800 |
| Menús | ✅ | ~600 |
| Notificaciones | ✅ | ~400 |
| Misiones | ✅ | ~200 |
| Skills | ✅ | ~150 |
| **TOTAL** | **✅** | **~5650** |

---

## 🚀 CÓMO CONTINUAR

### Para la próxima versión:
1. Decide qué feature agregar (ver "Próximo a Implementar")
2. **Copia el PROMPT para Claude Code** (abajo)
3. Proporciona el prompt a Claude Code
4. Deja que complete la tarea
5. Verifica que compile sin errores
6. Commit & Push

---

## 📝 PROMPT PARA CLAUDE CODE

Copia este prompt completo y proporciónselo a Claude Code:

```
Continúa el desarrollo de SurvivalCore v1.0.17 (Minecraft Paper 1.21.1 plugin).

La tarea es implementar [SELECCIONA UNA]:

OPCIÓN 1 - Database & Performance (v1.0.18):
- Implementar caching asincrónico mejorado en PlayerCache
- Optimizar queries más frecuentes (jugadores, arenas, clanes)
- Agregar índices en tablas de BD (MySQL)
- Crear backup automático cada 2 horas

OPCIÓN 2 - Eventos Especiales (v1.0.18):
- Crear EventManager para manejar eventos
- Implementar DoubleXPEvent (multiplicador 2x)
- Implementar DoubleMoneyEvent (multiplicador 2x)
- Crear comando /event info para admins
- Guardar estado de eventos en BD

OPCIÓN 3 - Leaderboards Globales (v1.0.18):
- Crear LeaderboardManager
- /leaderboard arena <arena> - Top 10 por arena
- /leaderboard clan - Top 10 clanes por poder
- /leaderboard skill <skill> - Top 10 por skill
- /leaderboard money - Top 10 jugadores ricos
- Actualizar cada 5 minutos desde BD
- Menú interactivo con pagination

OPCIÓN 4 - Expandir Misiones (v1.0.18):
- Implementar MissionProgressListener
- Auto-completar misiones al cumplir objetivo
- Sistema de recompensas (dinero + XP)
- Comando /mission claim para reclamar recompensas
- Mostrar progreso visual en chat
- Resetear misiones diarias a las 00:00

INSTRUCCIONES CRÍTICAS:
1. SIEMPRE actualiza versión ANTES de compilar:
   - pom.xml: <version>1.0.18</version>
   - pom.xml: <finalName>SurvivalCore-1.0.18</finalName>
   - plugin.yml: version: 1.0.18
   - NEXT_STEPS2.md: Actualizar "Versión Actual"

2. Lee WIKI.md para referencia de API

3. Usa patrones del código existente:
   - Managers implementan interfaces (IXxxManager)
   - Listeners registrados en SurvivalCorePlugin
   - Comandos heredan CommandExecutor + TabExecutor
   - Menús usar MenuFactory + MenuManager

4. Compilación:
   mvn clean package -DskipTests

5. Commit:
   git add -A
   git commit -m "Describe feature (v1.0.18)"
   git push origin main

Repositorio: https://github.com/Gabri180/SurvivalCore
Branch: main
```

---

## 📚 REFERENCIAS ÚTILES

- **WIKI.md** - API completa y documentación
- **NEXT_STEPS.md** - Estado de fases anteriores
- **Plugin.yml** - Permisos y comandos registrados
- **SurvivalCorePlugin.java** - Punto de entrada

---

**Última actualización:** 27 Julio 2026  
**Versión Actual:** 1.0.21  
**Siguiente:** 1.0.22  
**Estado:** ✅ Completamente funcional y listo para expandir
