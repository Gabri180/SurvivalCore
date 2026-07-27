# SurvivalCore - Guía de Desarrollo v1.0.23+

**⚠️ IMPORTANTE: SIEMPRE actualiza la versión ANTES de compilar**

---

## 📌 CAMBIAR VERSIÓN (PASO OBLIGATORIO ANTES DE COMPILAR)

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
   - Registrar cambios en la sección "IMPLEMENTADO EN vX.X.X"
```

### Paso 2: Compilar
```bash
mvn clean package -DskipTests
```

### Paso 3: Verificar compilación
```bash
# Comprobar que SurvivalCore-X.X.X.jar existe:
ls -lh target/SurvivalCore-*.jar
```

### Paso 4: Commit & Push
```bash
git add -A
git commit -m "Description of changes (vX.X.X)"
git push origin main
```

---

## 🎉 IMPLEMENTADO EN v1.0.24

### Editor Visual de Menús
Menú interactivo completo para browsear y editar menús sin comandos.

**Comando:** `/custommenu editor`

**Características:**
- Listar todos los menús creados visualmente
- Clickear menú para ver todos sus items
- Clickear item para ver detalles completos
- Información de: nombre, material, tipo de acción, valor
- Sugerencias de comandos de edición en chat
- Navegación atrás en todos los niveles

**Flujo:**
1. `/custommenu editor` - Abre lista de menús
2. Clickea un menú - Ver sus items
3. Clickea un item - Ver información y opciones de edición
4. Botón "Atrás" - Volver a pantalla anterior

**Menús visuales creados:**
- **Selecciona Menú**: Lista todos los menús (items con BOOK)
- **Items de [menuId]**: Lista items del menú seleccionado
- **Info Item (Slot N)**: Detalles del item con opciones

---

## 🎉 IMPLEMENTADO EN v1.0.23

### Permisos Personalizados para Menús
Ahora se pueden asignar permisos custom a menús sin validación estricta.

**Comando actualizado:**
```bash
/custommenu permission <menuId> set <permiso>
```

**Ejemplos:**
```bash
/custommenu permission vipshop set vip.access
/custommenu permission adminpanel set admin.panel
/custommenu permission customshop set customshop.use
```

---

## 🎉 IMPLEMENTADO EN v1.0.22

### Auto-Save de Menús Personalizados
Los menús ahora se guardan automáticamente después de cada comando de edición.

**Características:**
- Auto-guardar después de: item, title, size, bgcolor
- Abrir menús sin haber guardado manualmente
- Manual save sigue disponible como opción
- Cargar menús guardados en modo edición

**Flujo de trabajo:**
```bash
/custommenu create test              # Crea menú
/custommenu item test 0 DIAMOND      # Auto-guarda
/custommenu title test §6My Menu    # Auto-guarda
/custommenu open test                # Abre sin guardar manualmente
/custommenu save test                # Opcional (si quieres)
```

---

## 🎉 IMPLEMENTADO EN v1.0.17+

### Sistema Completo de Menús Personalizados
Builder tipo **CommandPanels** que permite crear menús in-game sin código Java.

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
- `title(String)` - Cambiar título (soporta color codes §)
- `rows(int)` - Tamaño 1-6 filas
- `backgroundColor(String)` - Material para fondo
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

## 📋 TODOS LOS COMANDOS IMPLEMENTADOS

### /custommenu - Constructor de Menús (Admin)

```
CREACIÓN:
  /custommenu create <id>                           # Crear nuevo menú
  /custommenu item <id> <slot> <material>           # Agregar item
  /custommenu title <id> <título>                   # Cambiar título
  /custommenu size <id> <filas>                     # Tamaño 1-6
  /custommenu bgcolor <id> <material>               # Color fondo
  /custommenu save <id>                             # Guardar menú
  /custommenu cancel <id>                           # Cancelar edición

GESTIÓN:
  /custommenu open <id>                             # Abrir menú
  /custommenu list                                  # Listar menús

ALIASES:
  /custommenu command <id> set <cmd>                # Crear alias
  /custommenu unalias <cmd>                         # Eliminar alias
  /custommenu aliases                               # Listar aliases

PERMISOS:
  /custommenu permission <id> set <permiso>         # Asignar permiso custom
  /custommenu permission <id> clear                 # Remover permiso
```

**Alias:** `/customm`, `/cmenu`

**Permisos requeridos:** `survivalcore.admin`

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

### /job - Sistema de Trabajos

```
/job              # Abre menú interactivo
/job set <tipo>   # Cambiar trabajo
/job info         # Ver estadísticas
/job list         # Listar todos
```

**Alias:** `/jobs`

**Trabajos disponibles:**
- MINER (Minero) → DIAMOND_PICKAXE
- FISHERMAN (Pescador) → FISHING_ROD
- LUMBERJACK (Leñador) → IRON_AXE
- FARMER (Granjero) → WOODEN_HOE
- HUNTER (Cazador) → BOW

---

### /notificaciones - Gestión de Notificaciones

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

### /arena - Sistema PvP 1v1

```
/arena join <id>    # Unirse (requiere dinero)
/arena leave        # Salir
/arena info <id>    # Ver info
/arena ranking      # Top 10
```

**Alias:** `/arenas`

---

### /clan - Sistema de Clanes

```
/clan create <nombre>    # Crear ($10,000)
/clan invite <jugador>   # Invitar
/clan accept            # Aceptar
/clan leave             # Salir
/clan info              # Ver info
/clan members           # Listar miembros
/clan bank              # Ver tesorería
```

**Alias:** `/clanes`

---

### /auction - Casa de Subastas

```
/auction sell <precio>   # Vender item
/auction list            # Ver subastas
/auction mylist          # Mis subastas
/auction bid <id> <qty>  # Pujar
/auction claim           # Reclamar
```

**Alias:** `/ah`

---

### /bounty - Sistema de Recompensas

```
/bounty create <player> <qty>   # Crear recompensa
/bounty list                    # Ver recompensas
/bounty mylist                  # Mis recompensas
/bounty history                 # Historial
```

**Alias:** `/recompensa`

---

### /leaderboard - Ranking Global

```
/leaderboard arena                      # Top 10 arenas
/leaderboard clan                       # Top 10 clanes
/leaderboard skill <type>               # Top 10 de skill
/leaderboard money                      # Top 10 ricos
/leaderboard job <type>                 # Top 10 de job
```

**Alias:** `/lb`, `/ranking`

---

### /event - Eventos Especiales

```
/event info                    # Ver evento actual
/event schedule <tipo> <mins>  # Programar evento
/event list                    # Listar eventos
```

**Alias:** `/eventos`

**Tipos de eventos:**
- DOUBLE_XP (2x XP)
- DOUBLE_MONEY (2x Dinero)

---

### /stats - Estadísticas

```
/stats              # Tus estadísticas
/stats <jugador>    # Estadísticas de otro
```

---

### /sc - Administración de SurvivalCore

```
/sc reload          # Recargar configuración
/sc gui             # Panel de admin
/sc help            # Ayuda
/sc backup          # Ejecutar backup manual
```

**Permisos requeridos:** `survivalcore.admin`

---

## 🔐 PERMISOS IMPLEMENTADOS

### Admin & Base
```yaml
survivalcore.admin              # Acceso administrativo total
survivalcore.player             # Acceso base (default: true)
survivalcore.event.admin        # Crear/gestionar eventos especiales
```

### Comandos
```yaml
survivalcore.custommenu.create      # Crear menús
survivalcore.mission.*              # Comandos de misiones
survivalcore.skill.*                # Comandos de skills
survivalcore.job.set                # Cambiar job
survivalcore.stats                  # Ver estadísticas
```

### Arena
```yaml
survivalcore.arena.join             # Unirse a arenas
survivalcore.arena.info             # Ver info
survivalcore.arena.ranking          # Ver ranking
```

### Clan
```yaml
survivalcore.clan.create            # Crear clan
survivalcore.clan.invite            # Invitar
survivalcore.clan.info              # Ver info
```

### Auction
```yaml
survivalcore.auction.sell           # Vender
survivalcore.auction.bid            # Pujar
survivalcore.auction.list           # Listar subastas
```

### Bounty
```yaml
survivalcore.bounty.create          # Crear recompensa
survivalcore.bounty.claim           # Reclamar
survivalcore.bounty.list            # Listar recompensas
```

### Menús Personalizados
```yaml
survivalcore.vip.access             # Acceso a menús VIP
survivalcore.admin.panel            # Panel de admin
server.builder                      # Para builder tools
```

### Custom (Dinámicos)
```yaml
# Puedes crear cualquier permiso custom:
vip.access
vip.shop
admin.panel
customshop.use
# etc...
```

---

## 📝 PLACEHOLDERS DISPONIBLES

### Jugador
```
%player%            # Nombre del jugador
%uuid%              # UUID único
%display_name%      # Nombre mostrado
%game_mode%         # SURVIVAL, CREATIVE, etc
%level%             # Nivel XP vanilla
%exp%               # XP actual (0.0-1.0)
%ping%              # Ping en ms
%online_players%    # Jugadores conectados
%max_players%       # Máximo de jugadores
```

### Salud y Hambre
```
%health%            # Vida actual
%max_health%        # Vida máxima
%hunger%            # Hambre (0-20)
%saturation%        # Saturación (0.0-20.0)
```

### Ubicación
```
%world%             # Mundo actual
%x% %y% %z%         # Coordenadas
%yaw%               # Rotación horizontal
%pitch%             # Rotación vertical
```

### Inventario
```
%held_item%         # Item en mano
%off_hand%          # Item en mano secundaria
```

### Tiempo
```
%time%              # Milliseconds (unix)
%timestamp%         # Segundos (unix)
```

### Sistemas Especiales
```
%clan_name%         # Nombre del clan
%clan_level%        # Nivel del clan
%job_name%          # Trabajo actual
%job_level%         # Nivel del trabajo
%money%             # Dinero del jugador
%arena_elo%         # ELO en arena
```

---

## 🎨 USAR PLACEHOLDERS EN MENÚS

```bash
# En títulos de menú:
/custommenu title myMenu §e%player% en Arena

# En lore de items:
/custommenu item myMenu 0 DIAMOND "§eTe conectaste: %timestamp%"

# En acciones:
/custommenu item myMenu 0 DIAMOND "test" MESSAGE "§aHola %player%"
```

---

## 📊 ESTADÍSTICAS ACTUALES (v1.0.24)

| Componente | Estado | Líneas |
|-----------|--------|--------|
| Comandos | ✅ | ~1650 |
| Managers | ✅ | ~2150 |
| Listeners | ✅ | ~950 |
| Menús | ✅ | ~900 |
| Editor Visual | ✅ | ~350 |
| Notificaciones | ✅ | ~400 |
| Misiones | ✅ | ~250 |
| Skills | ✅ | ~200 |
| **TOTAL** | **✅** | **~6850** |

---

## 🚀 PRÓXIMO A IMPLEMENTAR (v1.0.24+)

### Fase 1: Database & Optimizaciones (v1.0.24)
- [ ] Caching asincrónico mejorado con PlayerCache
- [ ] Optimizar queries frecuentes
- [ ] Agregar índices a tablas BD
- [ ] Backup automático de datos

### Fase 2: Eventos Especiales (v1.0.25)
- [ ] EventManager para manejar eventos
- [ ] DoubleXPEvent y DoubleMoneyEvent
- [ ] Comando /event con subcomandos
- [ ] Auto-notificaciones de eventos

### Fase 3: Leaderboards Mejorados (v1.0.26)
- [ ] LeaderboardManager
- [ ] Rankings por categoría (money, arena, clan, skill)
- [ ] Menú interactivo con pagination
- [ ] Top 10 actualizado cada 5 minutos

### Fase 4: Misiones Expandidas (v1.0.27)
- [ ] MissionProgressListener mejorado
- [ ] Auto-detección de progreso
- [ ] Comando /mission claim
- [ ] Auto-reseteo diario a 00:00

---

## 📋 CHECKLIST ANTES DE COMPILAR

```bash
✓ Actualizar versión en pom.xml (2 lugares)
✓ Actualizar versión en plugin.yml
✓ Actualizar NEXT_STEPS2.md con versión y cambios
✓ Hacer cambios de código
✓ Compilar: mvn clean package -DskipTests
✓ Verificar SurvivalCore-X.X.X.jar existe
✓ git add -A
✓ git commit -m "Description (vX.X.X)"
✓ git push origin main
```

---

## 📚 REFERENCIAS ÚTILES

- **WIKI.md** - API completa y documentación
- **plugin.yml** - Permisos y comandos registrados
- **SurvivalCorePlugin.java** - Punto de entrada
- **config.yml** - Configuración del servidor
- **menu-aliases.yml** - Aliases de menús

---

## 🔧 CONFIGURACIÓN (config.yml)

Archivo: `plugins/SurvivalCore/config.yml`

Secciones principales:
- **database** - Conexión MySQL
- **server** - Nombre y mensaje
- **worlds** - Mundos configurables
- **economy** - Sistema de dinero
- **jobs** - Configuración de trabajos
- **missions** - Configuración de misiones
- **skills** - Configuración de skills
- **arena** - Sistema PvP
- **clans** - Sistema de clanes
- **auction** - Casa de subastas
- **cache** - Configuración de caché
- **backup** - Backup automático
- **events** - Eventos especiales
- **notifications** - Notificaciones
- **debug** - Logs y debugging

---

## 🎯 PRÓXIMAS TAREAS

1. **Implementar Database & Optimizaciones (v1.0.24)**
   - Mejorar PlayerCache con expiry tracking
   - Agregar índices en BD
   - Crear BackupScheduler

2. **Implementar Eventos Especiales (v1.0.25)**
   - EventManager completo
   - Eventos DoubleXP y DoubleMoney
   - Notificaciones automáticas

3. **Mejorar Leaderboards (v1.0.26)**
   - LeaderboardManager con caché smart
   - Comando /leaderboard con pagination
   - Rankings por múltiples categorías

4. **Expandir Misiones (v1.0.27)**
   - Auto-detección de progreso
   - Comando /mission claim
   - Reset automático diario

---

**Última actualización:** 27 Julio 2026  
**Versión Actual:** 1.0.24  
**Estado:** ✅ Editor visual de menús completamente funcional  
**Siguiente versión:** 1.0.25 (Database & Optimizaciones)
