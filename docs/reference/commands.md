# Commands Reference

Complete command reference for SurvivalCore.

## 🎮 Player Commands

### Economy Commands
```bash
/balance                    # Check your balance
/balance [player]           # Check player balance
/transactions list          # View transaction history
/pay [player] [amount]      # Send money to player
```

### Job Commands
```bash
/job                        # Job main menu
/job menu                   # Interactive job selection
/job set [job_name]         # Change to job
/job info                   # Job details
/job stats                  # Your job statistics
/job earnings               # Current earnings
/job list                   # All available jobs
```

### Arena Commands
```bash
/arena                      # Arena menu
/arena list                 # Available arenas
/arena info [arena_id]      # Arena details
/arena join [arena_id]      # Join arena
/arena leave                # Leave current arena
/arena ranking              # Arena leaderboard
/arena stats                # Your arena stats
/arena spectate [arena_id]  # Watch ongoing fight
```

### Clan Commands
```bash
/clan                       # Clan menu
/clan create [name]         # Create new clan
/clan invite [player]       # Invite to clan
/clan accept                # Accept invitation
/clan leave                 # Leave clan
/clan info                  # Clan details
/clan members               # List members
/clan bank                  # Clan treasury
/clan upgrade               # Upgrade clan
/clan disband               # Disband clan (leader only)
```

### Auction Commands
```bash
/auction                    # Auction menu
/auction list               # Active auctions
/auction mylist             # Your auctions
/auction sell [price]       # Sell item in hand
/auction bid [id] [amount]  # Place bid
/auction cancel [id]        # Cancel auction
/auction claim              # Claim won items
```

### Bounty Commands
```bash
/bounty                     # Bounty menu
/bounty list                # Active bounties
/bounty mylist              # Your bounties
/bounty create [player] [amount]  # Create bounty
/bounty cancel [id]         # Cancel bounty
/bounty claim [id]          # Claim bounty
```

### Skill Commands
```bash
/skill                      # Skills menu
/skill list                 # All skills
/skill info [skill]         # Skill details
/skill tree                 # Skill tree view
/skill stats                # Your statistics
```

### Mission Commands
```bash
/mission                    # Mission menu
/mission list               # Available missions
/mission info [mission]     # Mission details
/mission progress           # Your progress
/mission claim              # Claim rewards
```

### Achievement Commands
```bash
/achievements               # Your achievements
/achievement info [name]    # Achievement details
/achievement progress       # Progress percentage
/achievement claim [id]     # Claim reward
```

### Leaderboard Commands
```bash
/leaderboard money [page]   # Money ranking
/leaderboard arena [page]   # Arena ranking
/leaderboard clan [page]    # Clan ranking
/leaderboard skill [page]   # Skill ranking
/leaderboard job [page]     # Job ranking
/lb [type] [page]           # Shorthand
/ranking [type] [page]      # Alternative
```

### Event Commands
```bash
/event                      # Event menu
/event list                 # Active events
/event info [id]            # Event details
/event active               # Currently running
```

### Notification Commands
```bash
/notifications              # Notification menu
/notif toggle [type]        # Toggle notifications
/notif status               # Preference status
/notif sound [on|off]       # Sound toggle
```

### Stats Commands
```bash
/stats                      # Your statistics
/stats [player]             # Player stats
/stats arena                # Arena stats
/stats job                  # Job stats
/stats skill                # Skill stats
```

---

## 👔 Admin Commands

### System Commands
```bash
/sc status                  # Plugin status
/sc reload                  # Reload config
/sc backup                  # Force backup
/sc dashboard               # View dashboard
/sc diagnose                # Run diagnostics
/sc metrics                 # Performance metrics
```

### Event Management
```bash
/event create [id] [name] [type] [multiplier] [duration]
/event start [id]           # Start event
/event stop [id]            # Stop event
/event delete [id]          # Delete event
/event info [id]            # Event details
/event list                 # All events
/event schedule             # View schedule
```

### Player Management
```bash
/admin add [player]         # Add admin
/admin remove [player]      # Remove admin
/admin ban [player] [reason]  # Ban player
/admin unban [player]       # Unban player
/admin mute [player] [time]   # Mute player
/admin unmute [player]      # Unmute player
/admin kick [player] [reason] # Kick player
/admin perms [player]       # Manage permissions
/admin badge [player] [badge] # Give badge
```

### Economy Management
```bash
/balance give [player] [amount]  # Give money
/balance remove [player] [amount] # Remove money
/balance set [player] [amount]  # Set balance
/balance check [player]     # Check balance
```

### Database Commands
```bash
/sc database status         # Database status
/sc database optimize       # Optimize tables
/sc database rebuild        # Rebuild indexes
/sc database check          # Check integrity
/sc database repair         # Repair database
/sc backups list            # List backups
/sc restore [backup_name]   # Restore backup
```

### Monitoring Commands
```bash
/sc report daily            # Daily report
/sc report weekly           # Weekly report
/sc report monthly          # Monthly report
/sc logs view               # View logs
/sc logs player [name]      # Player logs
/sc cache stats             # Cache statistics
/sc memory stats            # Memory usage
```

---

## 🎫 Permission Groups

### Admin Permissions
```
survivalcore.admin.*
- survivalcore.admin.manage
- survivalcore.admin.events
- survivalcore.admin.economy
- survivalcore.admin.logs
```

### Moderator Permissions
```
survivalcore.moderator.*
- survivalcore.moderator.kick
- survivalcore.moderator.mute
- survivalcore.moderator.report
```

### Player Permissions
```
survivalcore.player.*
- survivalcore.player.balance
- survivalcore.player.job
- survivalcore.player.clan
- survivalcore.player.arena
```

---

## 🔑 Command Syntax

### Bracket Notation
- `[required]` - Must provide
- `[optional]` - Can omit
- `[option1|option2]` - Choose one

### Examples
```bash
# Required arguments
/job set miner              # Must provide job name

# Optional arguments
/leaderboard money 1        # Page 1 is optional

# Multiple options
/event create id name TYPE MULT DURATION
```

---

## ⌨️ Aliases

### Shorthand Commands
```bash
/lb [type] [page]           # = /leaderboard
/msg [player] [text]        # = /message
/balance                    # = /money
/jobs                       # = /job
/arenas                     # = /arena
/clans                      # = /clan
/ah                         # = /auction
/lb                         # = /leaderboard
/ranking                    # = /leaderboard
/notif                      # = /notifications
/notifs                     # = /notifications
```

---

## 🎯 Command Categories

### User Information
```
/balance, /stats, /job, /skill, /missions
```

### Activities
```
/arena, /auction, /bounty, /clan, /missions
```

### Leaderboards
```
/leaderboard, /lb, /ranking
```

### Admin
```
/sc, /admin, /event, /broadcast
```

### Settings
```
/settings, /notifications, /preferences
```

---

## 📝 Command Examples

### Earn Money
```bash
# Method 1: Jobs
/job set miner              # Choose job
# Perform mining actions to earn

# Method 2: Arena
/arena list                 # View arenas
/arena join arena1          # Join and win

# Method 3: Auction
/auction sell 1000          # Sell item for $1000

# Method 4: Bounty
/bounty claim bounty_id     # Claim bounty reward
```

### Participate in Event
```bash
/event list                 # See active events
# Perform actions during event
/leaderboard money          # Check new ranking
```

### Manage Clan
```bash
/clan create MyClan         # Create clan
/clan invite PlayerName     # Invite players
/clan members               # View members
/clan bank                  # Check treasury
```

---

## 🆘 Help Commands

### In-Game Help
```bash
/help                       # General help
/help [command]             # Specific command help
/help job                   # Job system help
/help arena                 # Arena system help
/help clan                  # Clan system help
```

### Command Feedback
- **Hover** over commands in chat
- **Click** for quick info
- **Read** descriptions carefully
- **Check** syntax before use

---

## ⚡ Useful Tricks

### Combo Commands
```bash
# Quick arena entry
/arena list && /arena join arena1

# Check and act
/balance && /auction list

# Multiple actions
/job stats && /skill stats
```

### Tab Completion
Most commands support tab completion:
```bash
/arena [TAB]               # Shows options
/event [TAB]               # Shows subcommands
/clan invite [TAB]         # Shows players
```

---

**Total Commands:** 100+  
**Player Commands:** 60+  
**Admin Commands:** 40+  
**Aliases:** 20+
