# Editor de Menús In-Game - SurvivalCore

## Descripción

Ahora puedes editar menús **completamente desde el juego** usando el comando `/menu`. Sin necesidad de editar archivos YAML manualmente.

## Comandos Disponibles

### `/menu help`
Muestra la ayuda de todos los comandos.

```
/menu help
```

### `/menu edit <nombre>`
Inicia una sesión de edición para un menú existente.

```
/menu edit jobs
/menu edit shop
/menu edit arena
```

### `/menu slot <número>`
Selecciona el slot del menú que deseas editar (0-53).

```
/menu slot 0
/menu slot 5
```

### `/menu material <material>`
Cambia el material del item en el slot seleccionado.

```
/menu material DIAMOND
/menu material GOLD_INGOT
/menu material DIAMOND_SWORD
```

**Materiales disponibles:** Cualquier bloque o item de Minecraft
- DIAMOND, EMERALD, GOLD_INGOT
- DIAMOND_SWORD, IRON_SWORD, GOLDEN_SWORD
- DIAMOND_PICKAXE, GOLDEN_AXE
- BARRIER, PAPER, BOOK, REDSTONE, etc.

### `/menu name <nombre>`
Cambia el nombre visible del item. Soporta códigos de color.

```
/menu name §eMINER
/menu name §aComprar Diamante
/menu name §cCerrar Menú
```

**Códigos de color:**
- §0 = Negro, §1 = Azul, §2 = Verde, §3 = Cian
- §4 = Rojo, §5 = Púrpura, §6 = Oro, §7 = Gris
- §8 = Gris oscuro, §9 = Azul claro, §a = Verde claro
- §b = Cian claro, §c = Rojo claro, §d = Púrpura claro
- §e = Amarillo, §f = Blanco
- §l = Negrita, §o = Cursiva, §m = Tachado

### `/menu action <tipo> <valor>`
Configura la acción que ejecuta el item al clickear.

```
/menu action COMMAND job set MINER
/menu action OPEN_MENU jobs
/menu action MESSAGE §aComprado!
/menu action CLOSE
/menu action NONE
```

**Tipos de acción:**

1. **COMMAND <comando>** - Ejecuta un comando
   ```
   /menu action COMMAND job set MINER
   /menu action COMMAND pay 500
   /menu action COMMAND clan create MyClan
   ```

2. **OPEN_MENU <menú>** - Abre otro menú
   ```
   /menu action OPEN_MENU jobs
   /menu action OPEN_MENU shop
   /menu action OPEN_MENU arena
   ```

3. **MESSAGE <mensaje>** - Envía un mensaje al jugador
   ```
   /menu action MESSAGE §aCompra completada!
   /menu action MESSAGE Información del item
   ```

4. **CLOSE** - Cierra el inventario
   ```
   /menu action CLOSE
   ```

5. **NONE** - Sin acción (item decorativo)
   ```
   /menu action NONE
   ```

### `/menu save`
Guarda todos los cambios realizados al menú en el archivo YAML.

```
/menu save
```

**Resultado:**
- ✓ Menú guardado: jobs
- El menú se guarda automáticamente en `plugins/SurvivalCore/menus/`

### `/menu cancel`
Cancela la sesión de edición sin guardar cambios.

```
/menu cancel
```

## Flujo de Edición Completo

### Ejemplo: Editar el menú de Trabajos

1. **Iniciar edición:**
   ```
   /menu edit jobs
   ```
   Respuesta: `§aEdición iniciada para: jobs`

2. **Seleccionar el primer slot:**
   ```
   /menu slot 0
   ```

3. **Cambiar el material:**
   ```
   /menu material DIAMOND_PICKAXE
   ```
   Respuesta: `§aMaterial establecido a: DIAMOND_PICKAXE`

4. **Cambiar el nombre:**
   ```
   /menu name §eMINER
   ```
   Respuesta: `§aNombre establecido a: §eMINER`

5. **Establecer la acción:**
   ```
   /menu action COMMAND job set MINER
   ```
   Respuesta: `§aAcción establecida a: COMMAND`

6. **Guardar los cambios:**
   ```
   /menu save
   ```
   Respuesta: `§a✓ Menú guardado: jobs`

### Ejemplo: Crear un segundo item en el menú

1. **Continúa en la sesión de edición anterior**

2. **Selecciona el siguiente slot:**
   ```
   /menu slot 1
   ```

3. **Configura el material:**
   ```
   /menu material GOLDEN_AXE
   ```

4. **Configura el nombre:**
   ```
   /menu name §eWOODCUTTER
   ```

5. **Configura la acción:**
   ```
   /menu action COMMAND job set WOODCUTTER
   ```

6. **Guarda nuevamente:**
   ```
   /menu save
   ```

## Permisos Requeridos

```yaml
survivalcore.admin.menu    # Necesario para editar menús
```

Los operadores (OP) tienen todos los permisos automáticamente.

## Tab Completion

El comando soporta tab completion automático:

```
/menu [TAB]              # Muestra: edit, slot, material, name, action, save, cancel, help
/menu edit [TAB]         # Muestra: jobs, shop, arena, clan, auction, bounty
/menu material [TAB]     # Muestra: DIAMOND, EMERALD, GOLD_INGOT, etc.
/menu action [TAB]       # Muestra: COMMAND, OPEN_MENU, MESSAGE, CLOSE, NONE
```

## Almacenamiento

Todos los cambios se guardan en:
```
plugins/SurvivalCore/menus/
├── jobs.yml
├── shop.yml
├── arena.yml
├── clan.yml
├── auction.yml
└── bounty.yml
```

Los archivos YAML se pueden editar manualmente después de guardar.

## Ejemplos Prácticos

### Crear un Menú de Tienda

1. Crea el archivo `plugins/SurvivalCore/menus/shop.yml`:
   ```yaml
   title: "§6Tienda"
   size: 27
   items: {}
   ```

2. Registra el menú:
   ```
   /reload
   ```

3. Edita el menú:
   ```
   /menu edit shop
   /menu slot 0
   /menu material DIAMOND
   /menu name §bDiamante - $500
   /menu action COMMAND pay 500
   /menu save
   ```

### Crear un Menú de Navegación

```
/menu edit main
/menu slot 0
/menu material DIAMOND_PICKAXE
/menu name §eJobs
/menu action OPEN_MENU jobs
/menu save

/menu slot 1
/menu material GOLD_INGOT
/menu name §eShop
/menu action OPEN_MENU shop
/menu save
```

## Troubleshooting

**Error: "Menú no encontrado"**
- El archivo YAML no existe en `plugins/SurvivalCore/menus/`
- Solución: Crea el archivo YAML manualmente o recarga el plugin

**Error: "No hay sesión de edición activa"**
- No iniciaste una sesión con `/menu edit`
- Solución: Usa `/menu edit <nombre>` primero

**Los cambios no se guardan**
- Olvidaste usar `/menu save`
- Solución: Ejecuta `/menu save` después de editar

**El comando no funciona**
- No tienes el permiso `survivalcore.admin.menu`
- Solución: Pídele a un administrador que te dé permisos OP

## Notas Importantes

1. **Los slots son del 0 al 53** según el tamaño del menú
   - Menú de 27 slots: 0-26
   - Menú de 54 slots: 0-53

2. **Debes guardar** con `/menu save` para persistir cambios

3. **El nombre soporta códigos de color** (§a, §c, etc.)

4. **Las acciones se ejecutan como comandos** del jugador
   - `/menu action COMMAND job set MINER` → el jugador ejecuta `/job set MINER`

5. **Puedes editar múltiples items** en la misma sesión
   - No cierre la sesión hasta que haya terminado todos los cambios

6. **Los cambios se recargan automáticamente** cuando se guardan
