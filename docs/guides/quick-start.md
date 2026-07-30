# Quick Start Guide

Get SurvivalCore up and running in 5 minutes!

## ⚡ 5-Minute Setup

### 1️⃣ Download & Install
```bash
# Download the JAR
wget https://github.com/Gabri180/SurvivalCore/releases/download/v1.0.41/SurvivalCore-1.0.41.jar

# Copy to plugins folder
cp SurvivalCore-1.0.41.jar /path/to/server/plugins/

# Restart server
```

### 2️⃣ Initial Configuration
Server will auto-generate config files on first run.

```bash
# Restart server to generate files
# Check: plugins/SurvivalCore/license.yml
```

### 3️⃣ Database Setup
Edit `plugins/SurvivalCore/database.yml`:

```yaml
database:
  type: mysql
  host: localhost
  port: 3306
  database: survivalcore
  username: root
  password: your_password
```

### 4️⃣ Start Server
```bash
java -Xmx4G -Xms2G -jar paper-latest.jar nogui
```

### 5️⃣ Verify Installation
```bash
# In-game command
/sc status

# Should show: ✓ SurvivalCore v1.0.41 Loaded Successfully
```

---

## 🎮 First Time Player

### Join Server
1. Connect to your Minecraft server
2. You'll receive welcome message
3. Starting balance: $10,000

### First Commands
```bash
# View available jobs
/job menu

# Check your balance
/balance

# Join an arena
/arena list
/arena join arena1

# Create a clan
/clan create MyAwesomeClan
```

### First Steps
1. ✅ Complete a job to earn XP
2. ✅ Participate in arena combat
3. ✅ Create or join a clan
4. ✅ Bid on items in auction
5. ✅ Track your progress on leaderboards

---

## 👨‍💼 First Time Admin

### Complete Setup
1. ✅ Create MySQL database
2. ✅ Configure license key
3. ✅ Set economy parameters
4. ✅ Create initial events
5. ✅ Set admin permissions

### Essential Commands
```bash
# Check system status
/sc status

# Force backup
/sc backup

# View dashboard
/sc dashboard

# Create event
/event create weekend_boost DoublingEvent DOUBLE_XP 2.0
/event start weekend_boost

# Reload config
/sc reload
```

### First Admin Tasks
1. ✅ Set up database connection
2. ✅ Configure admin permissions
3. ✅ Create welcome events
4. ✅ Set economy rules
5. ✅ Monitor server metrics

---

## 🎯 Common First Steps

### Creating an Event
```bash
# Create double XP event for 24 hours
/event create monday_xp "Monday XP Boost" DOUBLE_XP 2.0 1440

# Start the event
/event start monday_xp

# Check event info
/event info monday_xp

# Stop when done
/event stop monday_xp
```

### Creating a Clan
```bash
# As a player
/clan create MyClanName

# Share the clan name with friends
# They can join via:
/clan invite YourName
```

### Running a Tournament
```bash
# Create arena event
/event create tournament TournamentWeekend BONUS_ARENA 1.5 168

# Players join arenas
/arena join tourney1

# View rankings
/leaderboard arena
```

---

## 📊 First Look at Dashboard

View real-time server metrics:
```bash
/sc dashboard
```

Shows:
- 👥 Players online
- 💰 Economic activity
- ⚔️ Arena fights
- 🏛️ Active clans
- 🎪 Running events

---

## ✅ Next Steps

- 📖 Read [User Guide](user-guide.md) for detailed features
- ⚙️ Review [Configuration Guide](configuration.md) for customization
- 👔 Check [Admin Guide](admin-guide.md) for server management

---

## 🆘 Need Help?

- **Check:** [FAQ](faq.md)
- **Troubleshoot:** [Troubleshooting Guide](troubleshooting.md)
- **Report Issues:** [GitHub Issues](https://github.com/Gabri180/SurvivalCore/issues)

---

**Estimated Setup Time:** 5-10 minutes  
**Difficulty:** Easy  
**Prerequisites:** Paper Server 1.21.1+, MySQL 8.0+
