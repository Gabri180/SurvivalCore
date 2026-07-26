# SurvivalCore - Próximos Pasos

**⚠️ IMPORTANTE: Actualiza la versión en pom.xml, finalName y plugin.yml cada vez que hagas cambios**

```bash
# Versión actual: 1.0.9
# Siguiente: 1.0.10
# Cambios necesarios:
# 1. pom.xml: <version>1.0.X</version>
# 2. pom.xml: <finalName>SurvivalCore-1.0.X</finalName>
# 3. plugin.yml: version: 1.0.X
```

---

## 🎯 Fase 1: Implementar Click Listeners para Menús (HIGH PRIORITY)

### Menu Click Actions
- [ ] Implementar `MenuClickListener` para interceptar clicks en items del menú
- [ ] Ejecutar acciones al clickear items (COMMAND, OPEN_MENU, MESSAGE, CLOSE)
- [ ] Soporte para prevenir que los items se muevan/droppeen del menú
- [ ] Feedback visual al jugador cuando clickea un item

**Archivos a crear:**
- Mejorar `MenuClickListener.java` para ejecutar acciones reales
- Crear `MenuItemClickEvent.java` para eventos personalizados

---

## 🎯 Fase 2: Implementar Lógica de Dev3 (HIGH PRIORITY)

### Arena System
- [ ] Implementar join/leave arena con `ArenaManagerImpl`
- [ ] Sistema de puntuación 1v1
- [ ] Rewards al ganar/perder
- [ ] Mostrar ranking de arena

### Clan System
- [ ] Crear clan con `/clan create <nombre>`
- [ ] Invitar jugadores a clan
- [ ] Sistema de roles (Admin, Moderador, Miembro)
- [ ] Dinero compartido del clan
- [ ] Alianzas entre clanes

### Auction System
- [ ] Listar items para subasta
- [ ] Sistema de pujas
- [ ] Notificaciones cuando suben tu puja
- [ ] Recolectar items ganados

### Bounty System
- [ ] Crear recompensa por cabeza
- [ ] Listar recompensas activas
- [ ] Reclamar recompensas
- [ ] Historial de recompensas pagadas

---

## 🎯 Fase 3: Mejorar Sistema de Jobs (MEDIUM PRIORITY)

### Job Listeners Faltantes
- [ ] `JobBlockBreakListener` - Experiencia al romper bloques (MINER)
- [ ] `JobFishingListener` - Experiencia al pescar (FISHERMAN)
- [ ] `JobKillListener` - Experiencia al matar mobs (WARRIOR)
- [ ] Implementar sistema de recompensas por nivel

### Job Menu Interactivo
- [ ] Click en item → seleccionar job
- [ ] Ver info del job (nivel, exp, ganancias)
- [ ] Upgrade de job con dinero

---

## 🎯 Fase 4: Sistema de Misiones (MEDIUM PRIORITY)

### Missions Implementation
- [ ] Cargar misiones desde YAML
- [ ] Sistema de progreso de misiones
- [ ] Misiones diarias/semanales
- [ ] Rewards al completar misiones
- [ ] `/mission` comando para ver misiones activas

### Mission Types
- [ ] Kill missions (matar X mobs)
- [ ] Collect missions (recolectar X items)
- [ ] Travel missions (ir a X ubicación)
- [ ] Craft missions (craftear X items)

---

## 🎯 Fase 5: Sistema de Skills (MEDIUM PRIORITY)

### Skill Trees
- [ ] Implementar `SkillManagerImpl` totalmente
- [ ] Subir skills al realizar acciones
- [ ] Bonificadores de permisos basados en skills
- [ ] Sistema de perks/habilidades especiales

### Integración con Jobs
- [ ] Skills relacionadas a cada job
- [ ] Multiplicador de XP por skill level

---

## 🎯 Fase 6: GUI Avanzada (LOW PRIORITY)

### Mejoras de Menú
- [ ] Pagination para menús con muchos items
- [ ] Botones de navegación (siguiente/anterior)
- [ ] Items animados/cambiantes
- [ ] Efectos visuales al clickear

### Nuevos Menús
- [ ] Menú de tienda principal
- [ ] Menú de perks/habilidades
- [ ] Menú de achievements
- [ ] Menú de statisticas completo

---

## 🎯 Fase 7: Database & Persistencia (LOW PRIORITY)

### Optimizaciones
- [ ] Implementar caching asincrónico
- [ ] Optimizar queries a base de datos
- [ ] Agregar índices a tablas importantes
- [ ] Backup automático de datos

### Nuevas Tablas
- [ ] `player_achievements` - Logros desbloqueados
- [ ] `player_trades` - Historial de intercambios
- [ ] `server_events` - Eventos importantes del servidor

---

## 🎯 Fase 8: Features Avanzadas (LOW PRIORITY)

### Sistema de Eventos
- [ ] Eventos diarios especiales
- [ ] Double XP events
- [ ] Seasonal content
- [ ] Holiday specials

### Economy
- [ ] Banco para guardar dinero
- [ ] Préstamos entre jugadores
- [ ] Inversiones
- [ ] Impuestos para clanes

### Ranking
- [ ] Leaderboard global de XP
- [ ] Ranking por skill
- [ ] Ranking de clanes
- [ ] Rewards de final de temporada

---

## 📝 Checklist de Testing

Antes de cada release:
- [ ] Compilar sin errores (`mvn clean package`)
- [ ] Probar todos los comandos de menú
- [ ] Verificar que los placeholders funcionan
- [ ] Probar click en items del menú
- [ ] Revisar que los cambios persisten en YAML
- [ ] Verificar que no hay memory leaks
- [ ] Probar con múltiples jugadores
- [ ] Revisar logs en consola

---

## 🔧 Comandos Útiles

```bash
# Actualizar versión (SIEMPRE HACER ESTO)
# Editar: pom.xml (2 lugares), plugin.yml

# Compilar
mvn clean package -DskipTests

# Ver commits pendientes
git log --oneline origin/main..HEAD

# Pushear a GitHub
git add -A && git commit -m "mensaje" && git push origin main

# Ver menús registrados
/sc gui edit [TAB]

# Editar menú
/menu edit jobs
/menu slot 0
/menu material DIAMOND
/menu name §eMINER
/menu lore §7Experiencia minera
/menu action COMMAND job set MINER
/menu save
```

---

## 📋 Notas Importantes

1. **Versionado**: Siempre incrementar versión en los 3 lugares
2. **Commits**: Hacer commits pequeños y descriptivos
3. **Testing**: Probar cambios en servidor antes de pushear
4. **Placeholders**: Agregar nuevos placeholders conforme se necesiten
5. **Documentación**: Actualizar MENUS.md, MENU_EDITOR.md con nuevas características

---

## 🎯 Prioridad Actual

1. **HIGH**: Implementar click listeners para menús (impacto máximo)
2. **HIGH**: Completar Dev3 logic (arena, clanes, auction, bounty)
3. **MEDIUM**: Job listeners y misiones
4. **LOW**: GUI avanzada y features secundarias

---

## 📞 Contacto

Para continuar el desarrollo:
- Repository: https://github.com/Gabri180/SurvivalCore
- Latest Version: 1.0.9
- Minecraft: Paper 1.21.1
- Java: 16+
