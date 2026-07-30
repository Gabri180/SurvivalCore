# Configuration Guide

Complete configuration documentation for SurvivalCore.

## 📂 Configuration Files

### File Structure
```
plugins/SurvivalCore/
├── config.yml              # Main configuration
├── database.yml            # Database connection
├── economy.yml             # Economy settings
├── events.yml              # Event configuration
├── permissions.yml         # Permission setup
├── license.yml             # License key
└── backups/               # Backup storage
```

---

## 🎛️ config.yml

### Basic Settings
```yaml
# SurvivalCore Main Configuration
plugin:
  name: "SurvivalCore"
  version: "1.0.41"
  debug: false
  
# Server Settings
server:
  name: "My Awesome Server"
  motd: "Welcome to SurvivalCore!"
  max-players: 100
  
# Performance
performance:
  async-tasks: true
  cache-enabled: true
  cache-expire-minutes: 30
  thread-pool-size: 10
```

### Economy Settings
```yaml
economy:
  # Currency configuration
  starting-balance: 10000
  currency-symbol: "$"
  decimal-places: 2
  
  # Transaction settings
  transaction-fee: 0.0     # 0% fee
  max-balance: 999999999
  min-transaction: 1
  
  # Tax system
  tax-enabled: false
  tax-rate: 0.05           # 5% tax
  
  # Rich player limits
  rich-player-limit: 1000000
  excess-redistribution: false
```

### Arena Settings
```yaml
arena:
  enabled: true
  entry-fee: 1000
  prize-pool: 1500
  prize-multiplier: 1.5
  timeout-minutes: 5
  
  # Spectator mode
  spectator-enabled: true
  spectator-slot-cost: 100
  
  # ELO ranking
  elo-enabled: true
  elo-k-factor: 32
  starting-elo: 1200
```

### Clan Settings
```yaml
clan:
  enabled: true
  creation-cost: 5000
  max-members: 50
  max-clans: 0           # 0 = unlimited
  
  # Bank settings
  bank-enabled: true
  bank-deposit-tax: 0.0
  
  # Territory
  territory-enabled: true
  chunk-cost: 500
  max-chunks: 100
```

---

## 🗄️ database.yml

### Database Connection
```yaml
database:
  # Type: mysql or postgresql
  type: mysql
  
  # Connection settings
  host: localhost
  port: 3306
  name: survivalcore
  
  # Authentication
  username: survivalcore
  password: secure_password
  
  # Connection pool
  pool-size: 10
  max-idle-time: 300
  connection-timeout: 30
  
  # SSL (optional)
  use-ssl: false
  ssl-mode: REQUIRED
  
  # Performance
  enable-query-cache: true
  cache-ttl-seconds: 300
```

### Multiple Databases
```yaml
# Primary database
databases:
  primary:
    type: mysql
    host: db1.example.com
    name: survivalcore_main
    
  backup:
    type: mysql
    host: db2.example.com
    name: survivalcore_backup
    
# Failover settings
failover:
  enabled: true
  retry-count: 3
  retry-delay-seconds: 5
```

---

## 🎪 events.yml

### Event Configuration
```yaml
events:
  # Auto-scheduling
  auto-schedule: true
  
  # Event limits
  max-concurrent: 5
  max-duration-hours: 168
  default-multiplier: 2.0
  
  # Event storage
  persist-to-database: true
  load-on-startup: true
  
  # Weekly events
  weekly-events:
    monday:
      - name: "Monday XP Boost"
        type: DOUBLE_XP
        multiplier: 2.0
        duration-hours: 24
    
    wednesday:
      - name: "Money Wednesday"
        type: DOUBLE_MONEY
        multiplier: 2.0
        duration-hours: 24
    
    friday:
      - name: "Arena Friday"
        type: BONUS_ARENA
        multiplier: 1.5
        duration-hours: 24
    
    saturday:
      - name: "Weekend Combo"
        type: SEASONAL
        multiplier: 3.0
        duration-hours: 48
```

---

## 🔐 permissions.yml

### Permission Groups
```yaml
# Player group
groups:
  player:
    description: "Default player permissions"
    permissions:
      - survivalcore.player.*
      - survivalcore.command.user
      - survivalcore.balance
      - survivalcore.job.*
      - survivalcore.arena.*
      - survivalcore.clan.*
      - survivalcore.auction.*
  
  # VIP group
  vip:
    description: "VIP member benefits"
    permissions:
      - survivalcore.player.*
      - survivalcore.vip.*
      - survivalcore.vip.priority-join
      - survivalcore.vip.extra-balance
      - survivalcore.vip.auction-boost
  
  # Moderator group
  moderator:
    description: "Moderator permissions"
    permissions:
      - survivalcore.admin
      - survivalcore.moderator.*
      - survivalcore.command.mod
  
  # Admin group
  admin:
    description: "Full admin access"
    permissions:
      - survivalcore.admin.*
      - survivalcore.command.admin
```

### Player Permissions
```yaml
players:
  YourName:
    group: admin
    permissions:
      - survivalcore.admin.*
  
  ModName:
    group: moderator
    permissions:
      - survivalcore.moderator.*
  
  VipName:
    group: vip
    permissions:
      - survivalcore.vip.*
```

---

## 🛡️ license.yml

### License Configuration
```yaml
license:
  # Your license key
  key: "your-license-key-here"
  
  # License server
  server: "https://license.survivalcore.io"
  
  # Validation
  validate-online: true
  offline-grace-period-days: 7
  validation-interval-hours: 24
  
  # License info
  tier: "PROFESSIONAL"
  max-players: 200
  max-servers: 3
  expires: "2027-12-31"
```

---

## ⚙️ economy.yml

### Advanced Economy Settings
```yaml
economy:
  # Multipliers
  job-xp-multiplier: 1.0
  arena-reward-multiplier: 1.0
  auction-tax-rate: 0.05
  
  # Price controls
  inflation-enabled: false
  inflation-rate: 0.01    # 1% per day
  
  # Dynamic pricing
  dynamic-pricing: false
  price-floor: 10
  price-ceiling: 10000
  
  # Bank system
  bank-enabled: true
  bank-interest-rate: 0.02  # 2% daily
  interest-compound: true
```

---

## 📊 Advanced Settings

### Performance Tuning
```yaml
performance:
  # Thread settings
  thread-pool-size: 10
  queue-size: 1000
  
  # Caching
  cache-enabled: true
  cache-type: memory        # memory or redis
  cache-ttl-minutes: 30
  
  # Async operations
  async-database: true
  batch-updates: true
  batch-size: 100
  
  # Tick settings
  ticks-per-second: 20
  max-lag-ms: 50
```

### Debug Settings
```yaml
debug:
  enabled: false
  level: INFO              # DEBUG, INFO, WARN, ERROR
  log-sql-queries: false
  log-performance: true
  
  # Profiling
  profiling-enabled: false
  profile-threshold-ms: 100
```

---

## 🔄 Configuration Examples

### Small Server (10-20 players)
```yaml
pool-size: 5
thread-pool-size: 4
cache-ttl-minutes: 15
backup-interval-hours: 6
```

### Medium Server (20-50 players)
```yaml
pool-size: 10
thread-pool-size: 8
cache-ttl-minutes: 30
backup-interval-hours: 2
```

### Large Server (50+ players)
```yaml
pool-size: 20
thread-pool-size: 16
cache-ttl-minutes: 60
backup-interval-hours: 1
```

---

## ✅ Configuration Validation

### Check Configuration
```bash
# Validate syntax
/sc config validate

# Check database connection
/sc database test

# Verify all systems
/sc diagnose

# Performance check
/sc performance check
```

---

## 🆘 Common Configuration Issues

### Database Connection Error
- Check username/password
- Verify database exists
- Ensure host is reachable
- Check firewall rules

### Low Performance
- Increase pool-size
- Reduce cache-ttl
- Enable async-database
- Check server resources

### Memory Issues
- Reduce cache-ttl
- Decrease thread-pool-size
- Enable garbage collection
- Check for memory leaks

---

## 📝 Best Practices

1. **Backup config** before editing
2. **Validate syntax** after changes
3. **Test changes** on dev server first
4. **Monitor performance** after changes
5. **Document custom settings**
6. **Keep backups** of all configs
7. **Use consistent** settings across servers

---

**Total Configuration Options:** 100+  
**Recommended Setup Time:** 30 minutes  
**Complexity:** Easy-Medium
