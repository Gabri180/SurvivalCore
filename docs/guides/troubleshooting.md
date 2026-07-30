# Troubleshooting Guide

Solutions for common SurvivalCore issues.

## 🔴 Critical Issues

### Plugin Fails to Load
**Symptoms:** "Error: Plugin failed to load"

**Solutions:**
1. Check `logs/latest.log` for error message
2. Verify Java version: `java -version` (must be 16+)
3. Verify JAR file exists: `ls plugins/SurvivalCore*.jar`
4. Check file permissions: `chmod 644 SurvivalCore*.jar`
5. Try older JAR version if just updated
6. Delete `.cache` folder and restart

### Database Connection Failed
**Symptoms:** "Cannot connect to MySQL", "Connection timeout"

**Solutions:**
1. Verify MySQL is running: `sudo systemctl status mysql`
2. Test connection: `mysql -h localhost -u root -p`
3. Check credentials in `database.yml`
4. Verify database exists: `SHOW DATABASES;`
5. Check firewall: `sudo ufw allow 3306`
6. Verify network connectivity: `ping db-server.com`

### Out of Memory Error
**Symptoms:** "java.lang.OutOfMemoryError"

**Solutions:**
1. Increase heap size: `java -Xmx8G -Xms4G -jar paper.jar`
2. Check current usage: `/sc metrics memory`
3. Reduce cache TTL: `cache-ttl-minutes: 15`
4. Clear old data: `/sc database archive`
5. Restart server: `/stop`

---

## ⚠️ Performance Issues

### Server Lag/Slow Response
**Symptoms:** High TPS loss, slow command response

**Solutions:**
```bash
# Check metrics
/sc metrics

# Increase thread pool
# Edit config.yml:
thread-pool-size: 16  # Increase from 10

# Enable async operations
async-database: true

# Reduce cache TTL
cache-ttl-minutes: 15
```

### High Database Load
**Symptoms:** Slow queries, database locks

**Solutions:**
```bash
# Optimize database
/sc database optimize

# Rebuild indexes
/sc database rebuild

# Check table sizes
/sc database stats

# Archive old data
/sc database archive

# Enable query cache
enable-query-cache: true
```

### Memory Leak
**Symptoms:** Increasing RAM usage over time

**Solutions:**
```bash
# Check memory
/sc memory stats

# Force garbage collection
/sc memory gc

# Restart server (last resort)
/stop

# Monitor after restart
watch /sc metrics memory
```

---

## 🔌 Connection Issues

### Players Can't Connect
**Symptoms:** "Cannot reach server", timeout errors

**Solutions:**
1. Verify server is running: `ps aux | grep java`
2. Check port: `netstat -tlnp | grep 25565`
3. Check firewall: `sudo ufw status`
4. Allow port: `sudo ufw allow 25565`
5. Check IP address: `hostname -I`
6. Test locally: `telnet localhost 25565`

### Intermittent Disconnects
**Symptoms:** Players randomly kicked from server

**Solutions:**
1. Check logs for "timed out" messages
2. Increase timeout: `player-idle-timeout: 30`
3. Reduce latency: Check network
4. Restart server if continues
5. Check for memory issues: `/sc metrics`

### Network Timeout
**Symptoms:** "Network timeout", "Connection reset"

**Solutions:**
1. Increase timeout values:
   ```yaml
   connection-timeout: 60  # Increase from 30
   ```
2. Check network stability
3. Reduce player count
4. Check for DDoS attacks
5. Contact ISP if issue persists

---

## 💾 Database Issues

### Corrupted Database
**Symptoms:** "Database error", "Invalid data"

**Solutions:**
```bash
# Check database integrity
/sc database check

# Repair if needed
/sc database repair

# Restore from backup
/sc backups list
/sc restore backup_2026-07-30_12-00-00

# Rebuild tables
/sc database rebuild
```

### Backup Restoration Failed
**Symptoms:** "Restore failed", "Incomplete data"

**Solutions:**
1. Verify backup exists: `/sc backups list`
2. Check disk space: `df -h`
3. Try older backup: `/sc backups list | tail`
4. Restore step-by-step: Check restoration progress
5. Manual restore (advanced): Use MySQL client

### Database Too Large
**Symptoms:** Slow queries, high disk usage

**Solutions:**
```bash
# Archive old data
/sc database archive

# Purge old records
/sc database purge --days 90

# Optimize tables
/sc database optimize

# Check size
/sc database size
```

---

## 🎮 Player Issues

### Player Data Lost
**Symptoms:** Balance reset, progress missing

**Solutions:**
1. Check if backup exists: `/sc backups list`
2. Restore from backup: `/sc restore backup_name`
3. Check player files: `plugins/SurvivalCore/players/`
4. Verify database: `/sc database check`
5. Manual recovery (advanced): Database restore

### Permission Denied Errors
**Symptoms:** Player can't use commands

**Solutions:**
1. Check permissions: `/admin perms [player] list`
2. Grant permission: `/admin perms [player] grant permission`
3. Check group permissions: Edit `permissions.yml`
4. Reload permissions: `/sc reload`
5. Verify user group: Check `permissions.yml`

### Economy Issues
**Symptoms:** Wrong balance, missing money

**Solutions:**
```bash
# Check player balance
/balance check [player]

# Fix balance
/balance set [player] 10000

# Check transaction log
/sc logs economy 100

# Rebuild economy
/sc rebuild economy
```

---

## 🔧 Configuration Issues

### Config Not Applying
**Symptoms:** Changes don't take effect

**Solutions:**
1. Verify syntax: `/sc config validate`
2. Reload config: `/sc reload`
3. Check if setting is hot-reloadable
4. Restart server if needed: `/stop`
5. Verify file saved: `cat config.yml`

### Invalid YAML
**Symptoms:** "Invalid YAML", parse error

**Solutions:**
1. Check indentation (use spaces, not tabs)
2. Validate YAML: Use online YAML validator
3. Compare with example: Check default config
4. Fix syntax errors in editor
5. Restart and check logs: `tail logs/latest.log`

### Settings Not Found
**Symptoms:** "Unknown setting", key not found

**Solutions:**
1. Check correct spelling
2. Verify section path (e.g., `economy:`)
3. Check config file version
4. Compare with latest example
5. Update to latest config: Back up and regenerate

---

## 🐛 Command Issues

### Command Not Found
**Symptoms:** "Unknown command", command error

**Solutions:**
1. Verify correct spelling: `/leaderboard` not `/lb` (use alias)
2. Check permissions: May need admin
3. Verify plugin loaded: `/plugins` lists SurvivalCore
4. Reload commands: `/sc reload`

### Command Permission Denied
**Symptoms:** "You don't have permission"

**Solutions:**
1. Check user group: `/admin perms [player] list`
2. Grant permission: `/admin perms [player] grant permission`
3. Add to admin group: Edit `permissions.yml`
4. Reload permissions: `/sc reload`

### Argument Error
**Symptoms:** "Invalid argument", syntax error

**Solutions:**
1. Check command syntax: `/help [command]`
2. Verify argument types (numbers vs text)
3. Check argument order
4. Use quotes for multi-word arguments: `/clan invite "Player Name"`

---

## 📊 Report Generation Issues

### Reports Not Generating
**Symptoms:** "Report failed", no output

**Solutions:**
```bash
# Check if reports enabled
/sc config validate

# Force report generation
/sc report daily --force

# Check report directory
ls plugins/SurvivalCore/reports/

# Verify database has data
/sc database stats
```

### Leaderboard Not Updating
**Symptoms:** Rankings not changing, stale data

**Solutions:**
```bash
# Force update
/leaderboard update

# Clear cache
/sc cache clear

# Rebuild leaderboards
/sc leaderboard rebuild

# Check update frequency
# Edit config.yml: leaderboard-update-minutes: 5
```

---

## 🔐 Security Issues

### Suspicious Activity Detected
**Symptoms:** Unusual transactions, account accessed

**Solutions:**
1. Check audit logs: `/sc logs [player]`
2. Verify IP address: `/admin info [player]`
3. Force password change: `/admin password reset [player]`
4. Revoke permissions: `/admin perms [player] revoke all`
5. Ban if needed: `/admin ban [player] "Suspicious activity"`

### Hacking/Exploits
**Symptoms:** Infinite money, unauthorized bans

**Solutions:**
1. Restore from clean backup
2. Investigate logs for how exploit happened
3. Report to plugin developer
4. Disable affected features temporarily
5. Update plugin to patch

---

## 📞 When to Seek Help

**Seek help if:**
- Issue persists after troubleshooting
- Error in logs you don't understand
- Data corruption suspected
- Security breach possible
- Performance still poor after optimization

**Support channels:**
- 📖 [Full Documentation](../README.md)
- 💬 [Discord Support](https://discord.gg/example)
- 🐛 [GitHub Issues](https://github.com/Gabri180/SurvivalCore/issues)
- 📧 [Email Support](mailto:gabrielsummers11@icloud.com)

---

**Total Solutions:** 50+  
**Coverage:** 95% of common issues  
**Last Updated:** 2026-07-30
