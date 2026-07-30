# User Guide

Complete guide to using SurvivalCore features as a player.

## 🎮 Getting Started

### Welcome to SurvivalCore
When you first join, you'll receive:
- ✅ Welcome message
- ✅ Starting balance: $10,000
- ✅ Tutorial access
- ✅ Inventory items (optional)

### First Login
```bash
# Commands to try:
/balance                 # Check your money
/job menu               # Choose a job
/arena list             # View available arenas
/leaderboard money      # See top players
```

---

## 💰 Economy System

### Checking Balance
```bash
/balance                # Your current balance
/balance [player]       # Check another player
/transactions list      # Transaction history
```

### Earning Money
**Methods to earn:**
1. **Jobs** - Earn XP and money
2. **Arena** - Win fights for rewards
3. **Auction** - Sell items to players
4. **Bounties** - Complete missions
5. **Events** - Special event rewards

### Spending Money
**Where to spend:**
1. **Arena Entry** - Join 1v1 fights
2. **Clan Creation** - Start your clan
3. **Auction** - Bid on items
4. **Bounties** - Place bounties
5. **Skills** - Unlock abilities

---

## ⚔️ Arena System

### Joining an Arena

```bash
# View available arenas
/arena list

# View arena details
/arena info arena1

# Join an arena (costs money)
/arena join arena1

# Leave current arena
/arena leave
```

### Arena Features
- **1v1 Combat** - One-on-one duels
- **Entry Fee** - Cost to join
- **Prize Pool** - Winner gets reward
- **ELO Ranking** - Competitive rating
- **Spectate** - Watch ongoing fights

### Arena Rankings
```bash
# View top arena players
/leaderboard arena

# Your arena stats
/stats arena

# Check ELO rating
/stats elo
```

---

## 👥 Clan System

### Creating a Clan
```bash
# Create new clan (costs $5,000)
/clan create MyAwesomeClan

# You become clan leader
# Invite players to join
```

### Managing Your Clan
```bash
# Invite a player
/clan invite PlayerName

# View clan members
/clan members

# Check clan bank
/clan bank

# Clan info
/clan info

# Leave clan
/clan leave
```

### Clan Features
- **Bank** - Shared treasury
- **Members** - Up to 50 players
- **Roles** - Leader, Officer, Member
- **Allies** - Form alliances
- **Territory** - Claim land

---

## 💼 Jobs System

### Choosing a Job
```bash
# View job menu
/job menu

# Set your job
/job set miner

# View job details
/job info
```

### Available Jobs
1. **Miner** - Mine blocks
2. **Farmer** - Farm crops
3. **Fisher** - Fish items
4. **Logger** - Cut wood
5. **Hunter** - Kill mobs

### Leveling Up
- Earn XP by performing job actions
- Level up for better rewards
- Unlock new abilities
- Progress displayed in `/job stats`

### Job Earnings
```bash
/job earnings      # Current session earnings
/job total         # Total earnings all time
/job rank          # Your ranking
```

---

## 🎯 Skills System

### Skill Categories
1. **Combat** - Fighting abilities
2. **Mining** - Mining effectiveness
3. **Foraging** - Collecting items
4. **Fishing** - Fishing bonuses
5. **Farming** - Crop growth
6. **Crafting** - Crafting speed

### Managing Skills
```bash
# View all skills
/skill list

# Skill details
/skill info combat

# Skill tree
/skill tree

# Your stats
/stats
```

### Skill Progression
- Gain XP through actions
- Level up for bonuses
- Unlock new abilities
- Max level: 100

---

## 🏆 Achievements

### Viewing Achievements
```bash
# View your achievements
/achievements

# Achievement details
/achievement info money_1k

# Progress percentage
/achievement progress
```

### Achievement Categories
1. **Money** - Accumulate currency
2. **Arena** - Win combats
3. **Clan** - Build your clan
4. **Skill** - Level up skills
5. **Mission** - Complete tasks
6. **Auction** - Trade items
7. **PvP** - Player kills

### Achievement Rewards
- Money bonuses
- Achievement badges
- Titles and recognition
- Special items (if configured)

---

## 📊 Leaderboards

### Viewing Rankings
```bash
# Money ranking
/leaderboard money [page]

# Arena ranking (ELO)
/leaderboard arena [page]

# Clan ranking
/leaderboard clan [page]

# Skill ranking
/leaderboard skill [page]

# Job ranking
/leaderboard job [page]
```

### Shorthand Commands
```bash
/lb money          # Quick access
/ranking arena     # Alternative alias
```

### Leaderboard Info
- Rankings updated daily
- Shows top 100 players
- Pagination support
- Your personal rank shown

---

## 📋 Missions

### Active Missions
```bash
# View all missions
/mission list

# Mission details
/mission info mission_name

# Your progress
/mission progress

# Claim rewards
/mission claim
```

### Mission Types
1. **Daily** - Reset each day
2. **Weekly** - Reset each week
3. **Seasonal** - Long-term goals
4. **Permanent** - Always available

### Mission Rewards
- Money
- XP
- Items
- Achievements

---

## 🎪 Events & Multipliers

### Active Events
```bash
# View current events
/event list

# Event details
/event info event_name

# Time remaining
/event status
```

### Event Types
- **Double XP** - 2x XP rewards
- **Double Money** - 2x earnings
- **Bonus Arena** - Increased prizes
- **Seasonal** - Special events
- **Holiday** - Themed events

### Event Bonuses
- Multipliers stack
- Apply to jobs, arenas, skills
- Limited duration
- Check schedule for next event

---

## 🏪 Auction House

### Viewing Auctions
```bash
# List active auctions
/auction list

# My auctions
/auction mylist

# Auction details
/auction info auction_id
```

### Selling Items
```bash
# Hold item in hand
/auction sell 500

# Sets starting price: $500
# Others can bid higher
```

### Bidding
```bash
# Place bid on auction
/auction bid auction_id 750

# Bid amount: $750
# You must have funds
# Highest bidder wins
```

### Claiming Items
```bash
# Claim won auctions
/auction claim

# Items go to inventory
# Check /auction mylist
```

---

## 🎁 Daily Rewards

### Claiming Rewards
```bash
# Daily rewards
/daily claim

# Check cooldown
/daily cooldown

# View reward history
/daily history
```

### Reward Schedule
- One claim per day
- Streak bonuses
- Increasing values
- Reset at midnight

---

## 💬 Chat & Social

### Social Features
```bash
# Message player
/msg PlayerName Hello!

# Clan chat
/cc This is clan only message

# Party chat
/party message Hello team!
```

### Notifications
```bash
# Manage notifications
/notif toggle all

# By category
/notif toggle arena

# View preferences
/notif status
```

---

## 📱 Mobile & Remote Access

### Web Dashboard
- View stats remotely
- Check leaderboards
- Manage clan
- See events

### Mobile App (if available)
- Quick stats check
- Auction notifications
- Clan messaging
- Event alerts

---

## ⚙️ Player Settings

### Personal Preferences
```bash
/settings language english
/settings theme dark
/settings notifications on
/settings privacy public
```

### Profile Info
```bash
# View your profile
/profile

# Edit bio
/profile bio "Awesome player!"

# Set status
/profile status "Playing with friends"
```

---

## 🆘 Getting Help

### In-Game Help
```bash
/help               # General help
/help arena         # Arena help
/help clan          # Clan help
/help job           # Job help
/help skill         # Skill help
```

### Support
- **Discord:** Join support server
- **Wiki:** Read full documentation
- **Contact:** Email support
- **Issues:** Report bugs on GitHub

---

## 📚 Advanced Features

### Customization
- Custom HUDs
- Custom skins
- Custom cloaks
- Custom colors

### Integrations
- Discord bot
- Web API
- Mobile app
- Streaming overlays

### Premium Features (if available)
- Enhanced skins
- Custom titles
- VIP perks
- Early events access

---

**Total Features:** 50+  
**Learning Time:** 1-2 hours  
**Skill Ceiling:** Very High
