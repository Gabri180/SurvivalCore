# Installation Guide

Complete step-by-step installation instructions for SurvivalCore.

## 📋 Prerequisites

### Software Requirements
- **Java:** JDK 16 or higher
- **Minecraft Server:** Paper 1.21.1
- **Database:** MySQL 8.0+ or MariaDB 10.5+
- **RAM:** 2GB minimum (4GB recommended)
- **Disk Space:** 500MB+ free space

### Verify Prerequisites
```bash
# Check Java version
java -version
# Should output: java version "16.0.0" or higher

# Check MySQL
mysql --version
# Should output: mysql Ver 8.0.x or higher
```

---

## 📥 Step 1: Download

### Option A: GitHub Releases (Recommended)
1. Visit [GitHub Releases](https://github.com/Gabri180/SurvivalCore/releases)
2. Download `SurvivalCore-1.0.41.jar`
3. Verify SHA256 checksum (if provided)

### Option B: Build from Source
```bash
# Clone repository
git clone https://github.com/Gabri180/SurvivalCore.git
cd SurvivalCore

# Build with Maven
mvn clean package -DskipTests

# JAR will be in: target/SurvivalCore-1.0.41.jar
```

---

## 🗄️ Step 2: Database Setup

### Create MySQL Database
```sql
-- Connect to MySQL
mysql -u root -p

-- Create database
CREATE DATABASE survivalcore;

-- Create user (recommended for security)
CREATE USER 'survivalcore'@'localhost' IDENTIFIED BY 'secure_password_here';
GRANT ALL PRIVILEGES ON survivalcore.* TO 'survivalcore'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SHOW DATABASES;
-- Should list: survivalcore
```

### Verify Connection
```bash
# Test connection
mysql -u survivalcore -p -h localhost survivalcore

# If successful, you'll enter MySQL prompt
# Exit with: exit
```

---

## 📦 Step 3: Deploy Plugin

### Copy JAR File
```bash
# Navigate to server directory
cd /path/to/minecraft/server

# Create plugins folder if it doesn't exist
mkdir -p plugins

# Copy SurvivalCore JAR
cp SurvivalCore-1.0.41.jar plugins/

# Verify
ls -la plugins/SurvivalCore*
```

### Verify File Permissions
```bash
# Check permissions (should be readable)
chmod 644 plugins/SurvivalCore-1.0.41.jar

# Verify
ls -l plugins/SurvivalCore-1.0.41.jar
```

---

## ⚙️ Step 4: Initial Configuration

### First Server Start
```bash
# Start the server (will generate config files)
java -Xmx4G -Xms2G -jar paper-latest.jar nogui

# Wait for message: "Done"
# Then stop server: /stop or Ctrl+C
```

### Edit Configuration Files
```bash
# Navigate to plugin folder
cd plugins/SurvivalCore

# Edit database configuration
nano database.yml
# Or use your favorite editor
```

### Configure database.yml
```yaml
# Database Connection Settings
database:
  # Database type: mysql or postgresql
  type: mysql
  
  # Connection settings
  host: localhost
  port: 3306
  name: survivalcore
  
  # Authentication
  username: survivalcore
  password: secure_password_here
  
  # Connection pool settings
  pool-size: 10
  max-idle-time: 300
  connection-timeout: 30
```

### Configure config.yml
```yaml
# SurvivalCore Configuration

# Economy Settings
economy:
  starting-balance: 10000
  currency-symbol: "$"
  decimal-places: 2
  
# Event Settings
events:
  auto-schedule: true
  max-duration-hours: 168
  
# Backup Settings
backup:
  enabled: true
  interval-hours: 2
  max-backups: 20
```

---

## 🚀 Step 5: Start Server

### Start Minecraft Server
```bash
# Navigate to server directory
cd /path/to/minecraft/server

# Start server with sufficient RAM
java -Xmx4G -Xms2G -jar paper-latest.jar nogui

# Wait for these messages:
# - "Loaded server"
# - "[SurvivalCore] Enabled successfully"
```

### Monitor Initialization
```
[SurvivalCore] Initializing databases...
[SurvivalCore] Creating tables...
[SurvivalCore] Loading configurations...
[SurvivalCore] Registering commands...
[SurvivalCore] Starting managers...
[SurvivalCore] Enabled successfully! v1.0.41
```

---

## ✅ Step 6: Verify Installation

### In-Game Verification
```bash
# Connect to server with OP account

# Check plugin status
/sc status

# Should show:
# ✓ SurvivalCore v1.0.41
# ✓ Database: Connected
# ✓ All systems operational
```

### Check Logs
```bash
# Review server logs
tail -f logs/latest.log

# Look for:
# [SurvivalCore] Successfully initialized
# [SurvivalCore] No errors detected
```

---

## 🔒 Security Checklist

- ✅ Change MySQL password from default
- ✅ Use strong database credentials
- ✅ Restrict database access to localhost
- ✅ Set file permissions to 644
- ✅ Enable firewall rules
- ✅ Backup database regularly
- ✅ Enable SSL for remote connections

---

## 🔧 Post-Installation

### Initialize Data
```bash
# Create admin account
/admin add YourName

# Set permissions
/admin perms YourName grant admin

# Create initial event
/event create startup "Startup Event" BONUS_ARENA 1.5 24
/event start startup
```

### Configure Plugins
1. Set admin players in `config.yml`
2. Configure economy settings
3. Set up event schedule
4. Configure backups

### Verify All Systems
```bash
# Dashboard
/sc dashboard

# Check database
/sc database status

# Run diagnostics
/sc diagnose
```

---

## 🆘 Installation Issues

### "Database Connection Failed"
- Verify MySQL is running
- Check credentials in `database.yml`
- Ensure database exists
- Check firewall rules

### "Plugin fails to load"
- Check server logs: `logs/latest.log`
- Verify Java version: `java -version`
- Check JAR file integrity
- Ensure all required files are present

### "Out of Memory"
- Increase heap size: `-Xmx8G -Xms4G`
- Check number of players
- Monitor with: `/sc metrics`

---

## 📞 Getting Help

If installation fails:
1. Check [Troubleshooting Guide](troubleshooting.md)
2. Review server logs
3. Verify all prerequisites
4. Create GitHub issue with logs
5. Contact support

---

**Installation Time:** 15-30 minutes  
**Difficulty:** Easy-Medium  
**Success Rate:** 99%+ with proper setup
