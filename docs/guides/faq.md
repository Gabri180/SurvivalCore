# Frequently Asked Questions (FAQ)

## ❓ General Questions

### Q: Is SurvivalCore compatible with my server version?
**A:** SurvivalCore requires Paper 1.21.1+. Older versions won't work due to API changes.

### Q: Do I need MySQL for SurvivalCore?
**A:** Yes, SurvivalCore requires MySQL 8.0+ or MariaDB 10.5+ for data storage. Embedded databases aren't supported.

### Q: Can I run multiple servers with one database?
**A:** Yes, you can use the same database for multiple servers by configuring unique server IDs and enabling network sync.

### Q: What's the recommended server size?
**A:** SurvivalCore handles 10-200+ players efficiently with proper configuration.

---

## 💰 Economy Questions

### Q: How can I give players starting money?
**A:** Set `starting-balance` in `economy.yml` or use `/balance give [player] [amount]` to give individual players money.

### Q: Can I set economy taxes?
**A:** Yes, enable `tax-enabled` in `economy.yml` and set `tax-rate` (0.05 = 5%).

### Q: How do I prevent money duplication?
**A:** Use database transactions and audit logs. Enable `transaction-logging` to track all changes.

### Q: Can players lose money?
**A:** Yes, through arena entries, clan creation, auction bids, and bounty creation. These are configurable.

---

## ⚔️ Arena Questions

### Q: How do I create an arena?
**A:** Use `/arena create [name]` in-game or configure in `arena.yml` file.

### Q: Can I customize arena rewards?
**A:** Yes, modify `prize-multiplier` and `entry-fee` in `config.yml`.

### Q: How does ELO ranking work?
**A:** Standard chess ELO system. Winners gain points, losers lose points based on relative strength.

### Q: Can players spectate fights?
**A:** Yes, if `spectator-enabled: true` in config. Use `/arena spectate [arena_id]`.

---

## 👥 Clan Questions

### Q: What's the clan limit?
**A:** Configurable. Set `max-clans: 0` for unlimited, or set a number.

### Q: Can clans go to war?
**A:** Yes, with clan war system enabled. Configure territorial warfare rules.

### Q: How do clan banks work?
**A:** Members can deposit money. Withdrawals require leadership approval.

### Q: Can I disband a clan?
**A:** Yes, only clan leaders can use `/clan disband`.

---

## 🎪 Event Questions

### Q: Can I create custom events?
**A:** Yes, use `/event create [id] [name] CUSTOM [multiplier] [duration]`.

### Q: Do events stack multipliers?
**A:** Yes, multiple active events multiply together (2x and 3x = 6x).

### Q: How long can events last?
**A:** Configurable, default max is 168 hours (7 days).

### Q: Can events repeat automatically?
**A:** Yes, enable `auto-schedule` for weekly recurring events.

---

## 🎯 Achievement Questions

### Q: How do achievements give rewards?
**A:** Automatic payout when unlocked. Check `/achievement info [name]` for reward amount.

### Q: Can I disable specific achievements?
**A:** Yes, remove them from `achievements.yml` before server start.

### Q: Are achievements per-server or global?
**A:** Per-server. Use network sync to share across multiple servers.

---

## 📊 Leaderboard Questions

### Q: How often do leaderboards update?
**A:** Real-time, but top 100 cached for performance. Check settings for refresh rate.

### Q: Can players hide from leaderboards?
**A:** Set `leaderboard-privacy: true` in player settings.

### Q: Do leaderboards reset?
**A:** By default, no. You can manually reset with `/leaderboard reset [type]`.

### Q: Can I view historical leaderboards?
**A:** Yes, if database archiving is enabled.

---

## 🔧 Configuration Questions

### Q: Where are config files located?
**A:** `plugins/SurvivalCore/` directory.

### Q: Do I need to restart to apply changes?
**A:** Some changes are hot-reloadable via `/sc reload`. Major changes require restart.

### Q: How do I backup configs?
**A:** Copy the entire `SurvivalCore` folder to a backup location.

### Q: What's the default config format?
**A:** YAML (.yml files). JSON not supported currently.

---

## 🆘 Troubleshooting Questions

### Q: Database connection fails. What do I do?
**A:** 
1. Verify MySQL is running
2. Check credentials in database.yml
3. Ensure database exists
4. Run `/sc database test`

### Q: Performance is slow. How do I optimize?
**A:**
1. Check `/sc metrics`
2. Increase `thread-pool-size`
3. Enable caching
4. Optimize database indexes
5. Check server resources

### Q: Plugin fails to load. What's wrong?
**A:**
1. Check logs in `logs/latest.log`
2. Verify Java 16+
3. Check JAR file integrity
4. Ensure all dependencies installed

### Q: How do I fix out-of-memory errors?
**A:**
1. Increase heap size: `-Xmx8G`
2. Reduce cache TTL
3. Check for memory leaks
4. Restart server

---

## 🔐 Security Questions

### Q: How secure is the plugin?
**A:** Enterprise-grade security with SQL injection prevention, permission validation, and audit logging.

### Q: How are passwords stored?
**A:** Passwords never stored. Only license keys and API credentials, encrypted at rest.

### Q: Can admins see player data?
**A:** Yes, admins can see all data. Use permissions to limit access.

### Q: Is player data backed up?
**A:** Yes, automatic backups every 2 hours. Set `max-backups: 20` to keep last 20.

---

## 📈 Performance Questions

### Q: How many players can one server handle?
**A:** 50-200+ depending on configuration. Test with your hardware.

### Q: What's the optimal database size?
**A:** Unlimited, but archive old data for performance (>1M records).

### Q: Can I use Redis for caching?
**A:** Yes, set `cache-type: redis` in performance settings.

### Q: How do I monitor server health?
**A:** Use `/sc dashboard` and `/sc metrics`.

---

## 💾 Backup Questions

### Q: Where are backups stored?
**A:** `plugins/SurvivalCore/backups/` by default.

### Q: How often are backups taken?
**A:** Every 2 hours (configurable). Set `backup-interval-hours`.

### Q: How do I restore a backup?
**A:** Use `/sc restore [backup_name]`.

### Q: How much storage do backups need?
**A:** ~50-100MB per backup. Keep 20 backups = ~2GB.

---

## 🌐 Multi-Server Questions

### Q: Can servers share data?
**A:** Yes, use same database with network sync enabled.

### Q: Do leaderboards sync across servers?
**A:** Yes, with `network-sync: true` in config.

### Q: Can players see other servers in chat?
**A:** Yes, if network chat enabled.

### Q: How do I prevent duplicate player data?
**A:** Use unique server IDs and enable conflict resolution.

---

## 📱 API & Development Questions

### Q: Is there an API for third-party plugins?
**A:** Yes, SurvivalCore provides events and managers for hook into.

### Q: Can I extend SurvivalCore?
**A:** Yes, via plugin hooks. See [API Documentation](../reference/api.md).

### Q: How do I create custom events?
**A:** Use the EventAPI or create custom event types.

### Q: Where's the source code?
**A:** Available on [GitHub](https://github.com/Gabri180/SurvivalCore).

---

## 👥 Admin Questions

### Q: How do I become an admin?
**A:** Server owner uses `/admin add [yourname]`.

### Q: What admin commands exist?
**A:** See [Admin Guide](admin-guide.md) or use `/help admin`.

### Q: Can I ban players?
**A:** Yes, use `/admin ban [player] [reason]`.

### Q: How do I manage permissions?
**A:** Edit `permissions.yml` or use in-game commands.

---

## 📞 Still Have Questions?

- **Check:** [Troubleshooting Guide](troubleshooting.md)
- **Read:** [Full Documentation](../README.md)
- **Support:** [Discord Server]
- **Email:** support@example.com

---

**Total Questions:** 50+  
**Last Updated:** 2026-07-30
