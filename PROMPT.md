# 🤖 PROMPT PARA CLAUDE CODE - SurvivalCore

Copia y pega esto en Claude Code para continuar el desarrollo:

---

## INSTRUCCIONES PARA CLAUDE CODE

Estoy desarrollando **SurvivalCore v1.0.14**, un plugin de Minecraft Paper 1.21.1 con sistemas de PvP (Arena), Clanes, Subastas y Recompensas.

### ESTADO ACTUAL:
- ✅ Menu click listeners funcionando
- ✅ Managers completos (Arena, Clan, Auction, Bounty)
- ✅ Job listeners (BlockBreak, Fishing, Kill)
- ✅ Comandos interactivos (/arena, /clan, /auction, /bounty)
- ❌ Subcomandos NO implementados

### SIGUIENTE A HACER (Prioridad 1):

Implementar **subcomandos Dev3** (v1.0.15):

1. **Arena Subcomandos:**
   - `/arena join <id>` - Unirse a arena (restar dinero, trackear participante)
   - `/arena leave` - Salir
   - `/arena info <id>` - Ver detalles
   - `/arena ranking` - Top 10

2. **Clan Subcomandos:**
   - `/clan create <nombre>` - Crear (deducir dinero)
   - `/clan invite <jugador>` - Invitar
   - `/clan leave` - Salir
   - `/clan info` - Ver detalles (dinero, miembros, alianzas)
   - `/clan members` - Listar con roles

3. **Auction Subcomandos:**
   - `/auction sell <precio>` - Vender item en mano (crear auction)
   - `/auction bid <id> <cantidad>` - Pujar

4. **Bounty Subcomandos:**
   - `/bounty create <jugador> <cantidad>` - Crear recompensa (deducir dinero)

### REGLAS CRÍTICAS:

1. **SIEMPRE actualiza versión a 1.0.15:**
   - pom.xml: `<version>1.0.15</version>`
   - pom.xml: `<finalName>SurvivalCore-1.0.15</finalName>`
   - plugin.yml: `version: 1.0.15`

2. **Compilación:**
   ```bash
   mvn clean package -DskipTests
   ```

3. **Después de terminar:**
   ```bash
   git add -A
   git commit -m "Implement Dev3 subcommands (v1.0.15)"
   git push origin main
   ```

4. **Validación de errores:**
   - Si algo falla, diagnostica el problema
   - No continues sin que compile correctamente
   - Verifica que los clicks de menú aún funcionan

### ARCHIVOS A MODIFICAR:

- `ArenaCommand.java` - Agregar handler de subcomandos
- `ClanCommand.java` - Agregar handler de subcomandos
- `AuctionCommand.java` - Agregar handler de subcomandos
- `BountyCommand.java` - Agregar handler de subcomandos

### ESTRUCTURA DE SUBCOMANDOS:

```java
@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
        // Abrir menú principal
        menuManager.openMenu(player, "nombreMenu");
        return true;
    }
    
    String subcommand = args[0].toLowerCase();
    switch (subcommand) {
        case "join":
            handleJoin(player, args);
            break;
        case "leave":
            handleLeave(player, args);
            break;
        // ... más subcomandos
    }
    return true;
}
```

### APIS DISPONIBLES:

**EconomyAPI:**
- `economyAPI.getBalance(uuid)` - Obtener dinero
- `economyAPI.addBalance(uuid, amount)` - Agregar dinero
- `economyAPI.removeBalance(uuid, amount)` - Restar dinero

**ArenaManager:**
- `arenaManager.joinArena(uuid, arenaId)` - Unirse
- `arenaManager.leaveArena(uuid)` - Salir
- `arenaManager.getAllArenas()` - Todas las arenas
- `arenaManager.recordWin/Loss(uuid, arenaId, reward)` - Registrar resultado

**ClanManager:**
- `clanManager.createClan(uuid, name)` - Crear clan
- `clanManager.getClanByPlayer(uuid)` - Obtener clan del jugador
- `clanManager.invite(clanId, uuid)` - Invitar
- `clanManager.addClanMoney/removeClanMoney()` - Dinero del clan

**AuctionManager:**
- `auctionManager.listItem(uuid, name, quantity, price)` - Crear auction
- `auctionManager.placeBid(auctionId, uuid, amount)` - Pujar
- `auctionManager.getActiveAuctions()` - Listar activas

**BountyManager:**
- `bountyManager.setBounty(creatorUuid, targetUuid, reward)` - Crear bounty
- `bountyManager.getActiveBounties()` - Listar activas
- `bountyManager.claimBounty(bountyId, killerUuid)` - Reclamar

### VALIDACIONES REQUERIDAS:

- Verificar si el jugador tiene suficiente dinero
- Verificar si el jugador ya está en una arena
- Verificar si el clan existe
- Verificar si el jugador es propietario/admin del clan
- Mensajes de error descriptivos en color rojo (§c)

### FEEDBACK AL USUARIO:

- Mensajes de éxito en verde (§a)
- Títulos con información importante
- Sonidos en eventos (pujar, crear clan, etc)
- Actualizar contadores en menús

---

## COMANDO PARA INICIAR:

"Continúa el desarrollo de SurvivalCore v1.0.14 (Minecraft Paper 1.21.1 plugin). Implementa los subcomandos Dev3 como está en NEXT_STEPS.md. Actualiza versión a 1.0.15 ANTES de compilar. Lee NEXT_STEPS.md y WIKI.md para contexto completo."

---

**Repo:** https://github.com/Gabri180/SurvivalCore

**Rama:** main

**Versión Actual:** 1.0.14

**Próxima:** 1.0.15
