# Sistema de Menús Interactivos - SurvivalCore

## Descripción

El nuevo sistema de menús permite crear menús interactivos con acciones configurables en archivos YAML. Cada item del menú puede ejecutar comandos, abrir otros menús, mostrar mensajes, o simplemente cerrar el inventario.

## Estructura de Carpetas

Los menús se guardan en: `plugins/SurvivalCore/menus/`

```
menus/
  ├── jobs.yml
  ├── shop.yml
  ├── arena.yml
  ├── clan.yml
  ├── auction.yml
  └── bounty.yml
```

## Formato YAML de un Menú

```yaml
title: "§6Mi Menú"           # Título del menú (permite códigos de color)
size: 27                      # Tamaño del menú (9, 18, 27, 36, 45, 54)

items:
  0:                          # Slot del item (0-53)
    material: DIAMOND         # Material de Minecraft
    name: "§bDiamante"        # Nombre del item
    lore:                     # Descripción (lista)
      - "§7Haz clic para comprar"
      - "§7Precio: $100"
    action-type: COMMAND      # Tipo de acción
    action-value: "pay 100"   # Valor de la acción

  1:
    material: EMERALD
    name: "§aEsmeralda"
    lore:
      - "§7Haz clic para información"
    action-type: MESSAGE
    action-value: "§aEsmeralda disponible"

  2:
    material: BARRIER
    name: "§cCerrar"
    action-type: CLOSE
```

## Tipos de Acciones

### 1. COMMAND - Ejecutar Comando
Ejecuta un comando como si lo escribiera el jugador.

```yaml
action-type: COMMAND
action-value: "job set MINER"
```

### 2. OPEN_MENU - Abrir Otro Menú
Abre otro menú registrado en el sistema.

```yaml
action-type: OPEN_MENU
action-value: "jobs"        # Nombre del archivo sin .yml
```

### 3. MESSAGE - Mostrar Mensaje
Envía un mensaje al jugador.

```yaml
action-type: MESSAGE
action-value: "§a¡Bienvenido!"
```

### 4. CLOSE - Cerrar Menú
Cierra el inventario del jugador.

```yaml
action-type: CLOSE
action-value: ""            # Vacío
```

### 5. NONE - Sin Acción
El item no hace nada al clickear.

```yaml
action-type: NONE
action-value: ""
```

## Ejemplo Completo: Menú de Trabajos

Archivo: `plugins/SurvivalCore/menus/jobs.yml`

```yaml
title: "§6Trabajos - Selecciona tu Trabajo"
size: 27

items:
  0:
    material: DIAMOND_PICKAXE
    name: "§eMINER"
    lore:
      - "§7Minería de diamantes y minerales"
      - "§7Ganancia: $500-$2000 por nivel"
    action-type: COMMAND
    action-value: "job set MINER"

  1:
    material: GOLDEN_AXE
    name: "§eWOODCUTTER"
    lore:
      - "§7Tala de árboles"
      - "§7Ganancia: $300-$1000 por nivel"
    action-type: COMMAND
    action-value: "job set WOODCUTTER"

  2:
    material: DIAMOND_SWORD
    name: "§eWARRIOR"
    lore:
      - "§7Combate PvP"
      - "§7Ganancia: $1000-$5000 por nivel"
    action-type: COMMAND
    action-value: "job set WARRIOR"

  25:
    material: BARRIER
    name: "§cCerrar"
    action-type: CLOSE
```

## Ejemplo Completo: Menú de Tienda

Archivo: `plugins/SurvivalCore/menus/shop.yml`

```yaml
title: "§6Tienda"
size: 27

items:
  0:
    material: DIAMOND
    name: "§bDiamante"
    lore:
      - "§7Precio: $500"
      - "§7Cantidad: 1"
    action-type: MESSAGE
    action-value: "§aDiamante: $500"

  1:
    material: EMERALD
    name: "§aEsmeralda"
    lore:
      - "§7Precio: $300"
    action-type: COMMAND
    action-value: "pay 300"

  26:
    material: BARRIER
    name: "§cCerrar"
    action-type: CLOSE
```

## Cómo Usar en Comandos

### Abrir un menú registrado:
```yaml
# En cualquier comando, usa MenuManager.openMenu()
MenuManager manager = SurvivalCorePlugin.getInstance().getMenuManager();
manager.openMenu(player, "jobs");    // Abre el menú definido en jobs.yml
manager.openMenu(player, "shop");    // Abre el menú definido en shop.yml
```

### Registrar un menú programáticamente:
```java
MenuManager manager = SurvivalCorePlugin.getInstance().getMenuManager();
MenuData menuData = new MenuData("custom", "§6Menú Custom", 27);

ItemStack item = new ItemStack(Material.DIAMOND);
ItemMeta meta = item.getItemMeta();
meta.setDisplayName("§bComprar Diamante");
item.setItemMeta(meta);

menuData.setItem(0, item, MenuAction.command("say ¡Comprado!"));
manager.registerMenu("custom", menuData);
```

## Códigos de Color Minecraft

```
§0 = Negro      §8 = Gris oscuro
§1 = Azul       §9 = Azul claro
§2 = Verde      §a = Verde claro
§3 = Cian       §b = Cian claro
§4 = Rojo       §c = Rojo claro
§5 = Púrpura    §d = Púrpura claro
§6 = Oro        §e = Amarillo
§7 = Gris       §f = Blanco

§l = Negrita
§m = Tachado
§n = Subrayado
§o = Cursiva
§k = Ofuscado
§r = Reset
```

## Permisos

```yaml
survivalcore.admin.menuedit:    # Para editar menús
survivalcore.player:             # Para ver menús
```

## Notas Importantes

1. **Los nombres de archivo sin extensión** se usan para abrir menús:
   - Archivo: `jobs.yml` → Se abre con: `/job` o `action-value: "jobs"`

2. **Los slots van del 0-53** según el tamaño:
   - 9 slots (size: 9)
   - 18 slots (size: 18)
   - 27 slots (size: 27)
   - 36 slots (size: 36)
   - 45 slots (size: 45)
   - 54 slots (size: 54)

3. **Los items sin definir** se muestran como vacíos

4. **Los comandos se ejecutan** como si los escribiera el jugador
   - Ejemplo: `job set MINER` se ejecuta como `/job set MINER`

## Troubleshooting

**Los menús no cargan:**
- Verifica que los archivos estén en `plugins/SurvivalCore/menus/`
- Verifica la sintaxis YAML
- Revisa la consola para errores

**Los items no aparecen:**
- Verifica que el `material` existe en Minecraft
- Verifica que el `slot` es válido para el tamaño

**Las acciones no se ejecutan:**
- Verifica el tipo de acción (`action-type`)
- Verifica el valor de la acción (`action-value`)
- Verifica que los permisos son correctos
