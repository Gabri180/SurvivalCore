# 🎮 SurvivalCore - Comprehensive Minecraft Plugin

**SurvivalCore v1.0.41** - Enterprise-grade Minecraft Paper 1.21.1 plugin with PvP arenas, economy system, clan warfare, and advanced analytics.

## 📋 Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Configuration](#configuration)
- [Commands](#commands)
- [Support](#support)

---

## 🚀 Quick Start

1. **Download** `SurvivalCore-1.0.41.jar`
2. **Copy** to `server/plugins/`
3. **Restart** your Minecraft server
4. **Configure** via `license.yml`
5. **Reload** with `/sc reload`

---

## ✨ Features

### Core Systems

#### 1. **PvP Arena System**
- 1v1 Combat arenas with ELO ranking
- Configurable entry fees and rewards
- Real-time spectator mode
- Leaderboards and statistics

#### 2. **Economy Management**
- Virtual currency system
- Transactions logging
- Auction house with bidding
- Economic reports and analysis

#### 3. **Clan System**
- Create and manage clans
- Territorial claims
- Clan warfare mechanics
- Alliance system

#### 4. **Jobs & Skills**
- 5+ job types (Miner, Farmer, Fisher, etc.)
- Skill progression system
- XP multipliers
- Level-based rewards

#### 5. **Events System** (v1.0.38+)
- Special events with multipliers
- Weekly recurring events
- Holiday events
- Automatic scheduling

#### 6. **Leaderboards** (v1.0.39+)
- 5 category rankings
- Real-time updates
- Pagination support
- Personal position tracking

#### 7. **Analytics Dashboard** (v1.0.40+)
- 7 key metrics
- Daily/Weekly/Monthly reports
- Performance tracking
- Trend analysis

#### 8. **Achievements** (v1.0.41+)
- 8 achievement categories
- 10+ milestones per category
- Progress tracking
- Reward system

---

## ⚙️ Requirements

### Minimum
- **Minecraft:** Paper 1.21.1+
- **Java:** JDK 16+
- **RAM:** 2GB minimum
- **Storage:** 500MB+

### Database
- **MySQL:** 8.0+ or MariaDB 10.5+
- **PostgreSQL:** 12+ (optional)

### Recommended
- **RAM:** 4GB+
- **CPU:** 2+ cores
- **SSD:** For better performance

---

## 📥 Installation

### Step 1: Download
- Get `SurvivalCore-1.0.41.jar` from GitHub releases
- SHA256 verification recommended

### Step 2: Deploy
```bash
cp SurvivalCore-1.0.41.jar server/plugins/
```

### Step 3: Configure
- Edit `plugins/SurvivalCore/license.yml`
- Set database credentials
- Configure server settings

### Step 4: Restart
```bash
# Server console
restart
```

### Step 5: Verify
```bash
# Server console
/sc status
# Should show: ✓ SurvivalCore v1.0.41 Loaded
```

---

## 🔧 Basic Configuration

### Database Setup
```yaml
database:
  type: mysql
  host: localhost
  port: 3306
  database: survivalcore
  username: root
  password: your_password
  pool-size: 10
```

### Economy Setup
```yaml
economy:
  starting-balance: 10000
  currency-symbol: "$"
  decimal-places: 2
```

### Events Setup
```yaml
events:
  auto-schedule: true
  default-multiplier: 1.5
  max-duration-hours: 168
```

---

## 💻 Commands Quick Reference

### Player Commands
```bash
/arena list          # View arenas
/clan create <name>  # Create clan
/auction list        # View auctions
/job menu            # Job selector
/skill info          # Skill stats
/leaderboard money   # Money ranking
/missions            # Available missions
/achievements        # Your achievements
```

### Admin Commands
```bash
/sc reload           # Reload config
/sc backup           # Force backup
/sc dashboard        # View metrics
/event create        # Create event
```

---

## 📚 Documentation

- [Installation Guide](./guides/installation.md)
- [Configuration Guide](./guides/configuration.md)
- [User Guide](./guides/user-guide.md)
- [Admin Guide](./guides/admin-guide.md)
- [Command Reference](./reference/commands.md)
- [API Documentation](./reference/api.md)
- [Troubleshooting](./guides/troubleshooting.md)

---

## 🆘 Support

### Documentation
- 📖 [Full Docs](https://survivalcore.gitbook.io)
- 🔍 [FAQ](./guides/faq.md)
- 🐛 [Troubleshooting](./guides/troubleshooting.md)

### Resources
- 🌐 [GitHub](https://github.com/Gabri180/SurvivalCore)
- 💬 [Discord](https://discord.gg/example)
- 📧 [Email](mailto:gabrielsummers11@icloud.com)

---

## 📄 License

**SurvivalCore v1.0.41**  
All Rights Reserved © 2026

---

## 👥 Credits

**Development Team:**
- Gabriel Summers (Lead Developer)
- Hauch (Jobs & Skills System)
- Dev3 (PvP & Clans System)

**Contributors:**
- Community testers and feedback providers

---

**Version:** 1.0.41  
**Last Updated:** 2026-07-30  
**Status:** Production Ready 🟢
