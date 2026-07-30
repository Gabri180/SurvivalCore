# Admin Guide

Complete administration and management guide for SurvivalCore server administrators.

## 👔 Admin Basics

### Getting Admin Access
```bash
# Add admin player
/admin add PlayerName

# Grant permissions
/admin perms PlayerName grant admin

# Check admin status
/admin info PlayerName
```

### Admin Commands
All admin commands start with `/sc` or specific command prefixes.

---

## 🖥️ Server Management

### System Status
```bash
# Check overall status
/sc status

# Detailed diagnostics
/sc diagnose

# Performance metrics
/sc metrics
```

### View Dashboard
```bash
# Real-time dashboard
/sc dashboard

# Shows:
# - Players online
# - Economic activity
# - Arena fights
# - Active events
# - Server health
```

### Reload Configuration
```bash
# Reload all configs
/sc reload

# Reload specific module
/sc reload economy
/sc reload events
/sc reload database
```

---

## 💾 Backup & Restore

### Manual Backup
```bash
# Force immediate backup
/sc backup

# Backup progress shows in console
# Files stored in: plugins/SurvivalCore/backups/
```

### Backup Settings
Edit `config.yml`:
```yaml
backup:
  enabled: true
  interval-hours: 2      # Auto-backup every 2 hours
  max-backups: 20        # Keep last 20 backups
  location: backups/
```

### Restore Backup
```bash
# List available backups
/sc backups list

# Restore specific backup
/sc restore backup_2026-07-30_12-00-00.zip

# Verification required
# Server restarts after restore
```

---

## 🎪 Event Management

### Create Events
```bash
# Create new event
/event create event_id "Event Name" TYPE MULTIPLIER DURATION

# Example: Double XP for 24 hours
/event create monday_xp "Monday XP Boost" DOUBLE_XP 2.0 1440

# Example: Triple combo for weekend
/event create weekend "Weekend Triple" SEASONAL 3.0 2880
```

### Event Types
- `DOUBLE_XP` - 2x experience
- `DOUBLE_MONEY` - 2x earnings
- `BONUS_ARENA` - Arena bonuses
- `SEASONAL` - Special events
- `CUSTOM` - Custom multipliers

### Manage Events
```bash
# List all events
/event list

# Event details
/event info event_id

# Start event
/event start event_id

# Stop event
/event stop event_id

# Delete event
/event delete event_id
```

### Automatic Events
Events can be scheduled:
- Every Monday (Double XP)
- Every Wednesday (Double Money)
- Every Friday (Bonus Arena)
- Every Saturday-Sunday (Triple Combo)

---

## 👥 Player Management

### Player Administration
```bash
# Ban player
/admin ban PlayerName reason

# Unban player
/admin unban PlayerName

# Mute player
/admin mute PlayerName duration

# Unmute player
/admin unmute PlayerName

# Kick player
/kick PlayerName reason

# Teleport player
/teleport PlayerName location
```

### Economy Control
```bash
# Give money
/balance give PlayerName 10000

# Remove money
/balance remove PlayerName 5000

# Set balance
/balance set PlayerName 50000

# Check balance
/balance check PlayerName
```

### Rank Management
```bash
# Add permission
/admin perms PlayerName grant permission

# Remove permission
/admin perms PlayerName revoke permission

# List permissions
/admin perms PlayerName list

# Give badge
/admin badge PlayerName badge_name

# Remove badge
/admin badge remove PlayerName badge_name
```

---

## 📊 Monitoring & Analytics

### View Reports
```bash
# Daily report
/sc report daily

# Weekly report
/sc report weekly

# Monthly report
/sc report monthly
```

### Key Metrics
- **Online Players** - Current active players
- **Economic Activity** - Total transactions
- **Arena Fights** - Combats completed
- **Clan Activity** - Clan creation rate
- **Event Participation** - Players in events
- **Leaderboard Updates** - Rankings changes

### Server Health
```bash
# CPU usage
/sc metrics cpu

# Memory usage
/sc metrics memory

# Network usage
/sc metrics network

# Database performance
/sc metrics database
```

---

## 🔐 Permissions

### Permission Nodes
```bash
# Admin access
survivalcore.admin

# Event management
survivalcore.event.admin
survivalcore.event.create
survivalcore.event.manage

# Player management
survivalcore.manage.players
survivalcore.manage.economy
survivalcore.manage.bans

# View commands
survivalcore.command.admin
survivalcore.command.report
survivalcore.command.analytics
```

### Setup Permissions
Edit `permissions.yml`:
```yaml
groups:
  admin:
    permissions:
      - survivalcore.*
      
  moderator:
    permissions:
      - survivalcore.admin
      - survivalcore.command.admin
      
  player:
    permissions:
      - survivalcore.player
      - survivalcore.command.user
```

---

## 🔧 Configuration Management

### Economy Configuration
```yaml
# config.yml
economy:
  starting-balance: 10000
  currency-symbol: "$"
  decimal-places: 2
  transaction-fee: 0.05      # 5% tax
  max-balance: 999999999
```

### Arena Configuration
```yaml
arenas:
  enabled: true
  entry-fee: 1000
  prize-multiplier: 1.5
  timeout-minutes: 5
  spectator-enabled: true
```

### Event Configuration
```yaml
events:
  auto-schedule: true
  max-concurrent: 5
  default-multiplier: 2.0
  max-duration-hours: 168
```

---

## 📈 Performance Tuning

### Database Optimization
```bash
# Optimize database
/sc database optimize

# Rebuild indexes
/sc database rebuild

# Check integrity
/sc database check

# Repair if needed
/sc database repair
```

### Cache Management
```bash
# Clear cache
/sc cache clear

# Cache stats
/sc cache stats

# Enable/disable cache
/sc cache enable
/sc cache disable
```

### Memory Management
```bash
# Force garbage collection
/sc memory gc

# Memory stats
/sc memory stats

# Increase heap size (server restart required)
# java -Xmx8G -Xms4G -jar paper.jar
```

---

## 🌐 Multi-Server Setup

### Sync Between Servers
```bash
# Enable sync
/sc network sync enable

# Sync economy
/sc network sync economy

# Sync leaderboards
/sc network sync leaderboards
```

### Server Communication
```bash
# Send message to all servers
/broadcast-all "Server message"

# Sync player data
/network sync players

# Network status
/network status
```

---

## 📝 Logging & Auditing

### View Logs
```bash
# Plugin logs
/sc logs view

# Recent actions
/sc logs actions 50

# Player actions
/sc logs player PlayerName

# Economy transactions
/sc logs economy 100
```

### Audit Trail
- All transactions logged
- Admin actions recorded
- Ban records kept
- Event history saved

---

## 🆘 Troubleshooting

### Common Issues
```bash
# Database connection failed
/sc database reconnect

# Plugin not responding
/sc restart

# High CPU usage
/sc metrics cpu
/sc performance analyze

# Out of memory
# Increase heap size and restart
```

### Debug Mode
```bash
# Enable debug logging
/sc debug on

# Verbose output
/sc debug verbose

# Show timing
/sc debug timing on

# Disable debug
/sc debug off
```

---

## 🚀 Best Practices

### Daily Admin Tasks
- ✅ Check `/sc status`
- ✅ Review logs
- ✅ Monitor player count
- ✅ Check database status
- ✅ Verify backups

### Weekly Admin Tasks
- ✅ Review `/sc report weekly`
- ✅ Create scheduled events
- ✅ Check leaderboard accuracy
- ✅ Update economy settings
- ✅ Plan upcoming events

### Monthly Admin Tasks
- ✅ Review `/sc report monthly`
- ✅ Update server policies
- ✅ Upgrade if needed
- ✅ Archive old data
- ✅ Security audit

---

## 📞 Admin Support

### Resources
- 📖 Full documentation
- 💬 Admin Discord channel
- 📧 Admin support email
- 🐛 Bug report system

### Emergency Contacts
- **Critical Issues:** emergency@survivalcore.example.com
- **Urgent Help:** support@survivalcore.example.com
- **Feedback:** feedback@survivalcore.example.com

---

**Required Rank:** Server Admin  
**Learning Time:** 2-3 hours  
**Complexity:** Medium-High
