package com.atlasMC.survivalcore;

import com.atlasMC.survivalcore.api.EconomyAPI;
import com.atlasMC.survivalcore.api.EventAPI;
import com.atlasMC.survivalcore.api.IArenaManager;
import com.atlasMC.survivalcore.api.IAuctionManager;
import com.atlasMC.survivalcore.api.IBossManager;
import com.atlasMC.survivalcore.api.IBountyManager;
import com.atlasMC.survivalcore.api.IClanManager;
import com.atlasMC.survivalcore.api.IClanWarManager;
import com.atlasMC.survivalcore.api.IClaimManager;
import com.atlasMC.survivalcore.api.IJobManager;
import com.atlasMC.survivalcore.api.IMissionManager;
import com.atlasMC.survivalcore.api.ISkillManager;
import com.atlasMC.survivalcore.api.impl.ArenaManagerImpl;
import com.atlasMC.survivalcore.api.impl.AuctionManagerImpl;
import com.atlasMC.survivalcore.api.impl.BossManagerImpl;
import com.atlasMC.survivalcore.api.impl.BountyManagerImpl;
import com.atlasMC.survivalcore.api.impl.ClanManagerImpl;
import com.atlasMC.survivalcore.api.impl.ClanWarManagerImpl;
import com.atlasMC.survivalcore.api.impl.ClaimManagerImpl;
import com.atlasMC.survivalcore.api.impl.JobManagerImpl;
import com.atlasMC.survivalcore.api.impl.MissionManagerImpl;
import com.atlasMC.survivalcore.api.impl.SkillManagerImpl;
import com.atlasMC.survivalcore.cache.PlayerCache;
import com.atlasMC.survivalcore.commands.AdminCommand;
import com.atlasMC.survivalcore.commands.ArenaCommand;
import com.atlasMC.survivalcore.commands.AuctionCommand;
import com.atlasMC.survivalcore.commands.BackupCommand;
import com.atlasMC.survivalcore.commands.BountyCommand;
import com.atlasMC.survivalcore.commands.ClanCommand;
import com.atlasMC.survivalcore.commands.CustomMenuCommand;
import com.atlasMC.survivalcore.commands.EventCommand;
import com.atlasMC.survivalcore.commands.JobCommand;
import com.atlasMC.survivalcore.commands.LeaderboardCommand;
import com.atlasMC.survivalcore.commands.MenuCommand;
import com.atlasMC.survivalcore.commands.MenuEditCommand;
import com.atlasMC.survivalcore.commands.MissionCommand;
import com.atlasMC.survivalcore.commands.NotificationsCommand;
import com.atlasMC.survivalcore.commands.SkillCommand;
import com.atlasMC.survivalcore.commands.StatsCommand;
import com.atlasMC.survivalcore.config.ConfigManager;
import com.atlasMC.survivalcore.db.ArenaRepository;
import com.atlasMC.survivalcore.db.AuctionRepository;
import com.atlasMC.survivalcore.db.BossRepository;
import com.atlasMC.survivalcore.db.BountyRepository;
import com.atlasMC.survivalcore.db.ClaimRepository;
import com.atlasMC.survivalcore.db.ClanRepository;
import com.atlasMC.survivalcore.db.ClanWarRepository;
import com.atlasMC.survivalcore.db.DatabaseManager;
import com.atlasMC.survivalcore.db.EventRepository;
import com.atlasMC.survivalcore.db.JobRepository;
import com.atlasMC.survivalcore.db.MissionRepository;
import com.atlasMC.survivalcore.db.PlayerRepository;
import com.atlasMC.survivalcore.db.SkillRepository;
import com.atlasMC.survivalcore.listeners.BossDeathListener;
import com.atlasMC.survivalcore.listeners.ClaimProtectionListener;
import com.atlasMC.survivalcore.listeners.ClanWarListener;
import com.atlasMC.survivalcore.listeners.JobBlockBreakListener;
import com.atlasMC.survivalcore.listeners.JobFishingListener;
import com.atlasMC.survivalcore.listeners.JobHarvestListener;
import com.atlasMC.survivalcore.listeners.JobKillListener;
import com.atlasMC.survivalcore.listeners.MenuEditorListener;
import com.atlasMC.survivalcore.listeners.MissionProgressListener;
import com.atlasMC.survivalcore.listeners.PlayerDataListener;
import com.atlasMC.survivalcore.listeners.PvPArenaListener;
import com.atlasMC.survivalcore.listeners.PvPKillstreakListener;
import com.atlasMC.survivalcore.managers.LeaderboardManager;
import com.atlasMC.survivalcore.menu.ChatInputPrompt;
import com.atlasMC.survivalcore.menu.MenuAliasManager;
import com.atlasMC.survivalcore.menu.MenuEditorManager;
import com.atlasMC.survivalcore.menu.MenuLoader;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.menu.MenuYamlWriter;
import com.atlasMC.survivalcore.events.EventManager;
import com.atlasMC.survivalcore.notifications.NotificationManager;
import com.atlasMC.survivalcore.prestige.PrestigeManager;
import com.atlasMC.survivalcore.scheduler.BackupScheduler;
import com.atlasMC.survivalcore.scheduler.SchedulerManager;
import com.atlasMC.survivalcore.seasons.SeasonManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Punto de entrada de SurvivalCore. Inicializa la arquitectura core
 * (BD, config, cache, economia, temporadas, prestigio) y registra los
 * modulos de Jobs/Skills/Misiones (Hauch) y PvP/Clanes/Raideo (Dev3).
 */
public final class SurvivalCorePlugin extends JavaPlugin {

    private static SurvivalCorePlugin instance;

    private ConfigManager configManager;
    private DatabaseManager databaseManager;
    private PlayerRepository playerRepository;
    private PlayerCache playerCache;
    private EconomyAPI economyAPI;
    private EventAPI eventAPI;
    private NotificationManager notificationManager;
    private SeasonManager seasonManager;
    private PrestigeManager prestigeManager;
    private MenuManager menuManager;
    private MenuEditorManager menuEditorManager;
    private MenuAliasManager menuAliasManager;
    private BackupScheduler backupScheduler;
    private EventManager eventManager;
    private LeaderboardManager leaderboardManager;

    // Repositorios listos para Hauch (Jobs/Skills/Misiones)
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private MissionRepository missionRepository;
    private EventRepository eventRepository;

    // Repositorios listos para Dev3 (Clanes/Raideo)
    private ClanRepository clanRepository;
    private ClaimRepository claimRepository;
    private ArenaRepository arenaRepository;
    private BossRepository bossRepository;
    private AuctionRepository auctionRepository;
    private BountyRepository bountyRepository;
    private ClanWarRepository clanWarRepository;

    // Managers de Hauch: null hasta que el implemente IJobManager/ISkillManager/IMissionManager.
    private IJobManager jobManager;
    private ISkillManager skillManager;
    private IMissionManager missionManager;

    // Managers de Dev3: null hasta que implemente las interfaces de PvP/Clanes/Raideo.
    private IArenaManager arenaManager;
    private IClanManager clanManager;
    private IClanWarManager clanWarManager;
    private IClaimManager claimManager;
    private IBossManager bossManager;
    private IAuctionManager auctionManager;
    private IBountyManager bountyManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.configManager = new ConfigManager(this);
        configManager.loadAll();

        this.databaseManager = new DatabaseManager(this);
        databaseManager.connect();
        databaseManager.initializeTables();

        this.playerRepository = new PlayerRepository(databaseManager);
        long cacheExpiryMs = getConfig().getLong("cache.expiry-minutes", 30) * 60 * 1000;
        this.playerCache = new PlayerCache(playerRepository, cacheExpiryMs);
        this.eventAPI = new EventAPI();
        this.economyAPI = new EconomyAPI(playerCache, databaseManager);
        this.notificationManager = new NotificationManager();
        this.seasonManager = new SeasonManager(databaseManager);
        this.prestigeManager = new PrestigeManager(playerCache, databaseManager);

        this.jobRepository = new JobRepository(databaseManager);
        this.skillRepository = new SkillRepository(databaseManager);
        this.missionRepository = new MissionRepository(databaseManager);
        this.eventRepository = new EventRepository(databaseManager);

        this.eventManager = new EventManager(this, eventRepository);
        this.leaderboardManager = new LeaderboardManager(this, databaseManager, playerCache);

        this.clanRepository = new ClanRepository(databaseManager);
        this.claimRepository = new ClaimRepository(databaseManager);
        this.arenaRepository = new ArenaRepository(databaseManager);
        this.bossRepository = new BossRepository(databaseManager);
        this.auctionRepository = new AuctionRepository(databaseManager);
        this.bountyRepository = new BountyRepository(databaseManager);
        this.clanWarRepository = new ClanWarRepository(databaseManager);

        this.jobManager = new JobManagerImpl(playerCache, jobRepository, economyAPI);
        this.skillManager = new SkillManagerImpl(playerCache, skillRepository, eventAPI);
        this.missionManager = new MissionManagerImpl(playerCache, missionRepository, economyAPI, eventAPI, this);

        this.arenaManager = new ArenaManagerImpl(arenaRepository, economyAPI);
        this.clanManager = new ClanManagerImpl(clanRepository, playerCache);
        this.clanWarManager = new ClanWarManagerImpl(clanWarRepository);
        this.claimManager = new ClaimManagerImpl(claimRepository, playerCache, getConfig());
        this.bossManager = new BossManagerImpl(bossRepository);
        this.auctionManager = new AuctionManagerImpl(auctionRepository, playerCache, economyAPI);
        this.bountyManager = new BountyManagerImpl(bountyRepository, playerCache, economyAPI);

        this.menuManager = new MenuManager(this);
        MenuLoader menuLoader = new MenuLoader(this, menuManager);
        menuLoader.loadMenus();

        this.menuAliasManager = new MenuAliasManager(menuManager, this);

        MenuYamlWriter yamlWriter = new MenuYamlWriter(this);
        this.menuEditorManager = new MenuEditorManager(menuManager, yamlWriter);

        this.backupScheduler = new BackupScheduler(this, getConfig(), playerCache);

        registerListeners();
        registerCommands();
        new SchedulerManager(this, playerCache, seasonManager, bossManager, auctionManager);

        getLogger().info("SurvivalCore habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        if (playerCache != null) {
            playerCache.clear();
        }
        getLogger().info("SurvivalCore deshabilitado correctamente.");
    }

    private void registerListeners() {
        var pluginManager = getServer().getPluginManager();

        // Core
        pluginManager.registerEvents(new PlayerDataListener(playerCache, playerRepository, getConfig()), this);

        // Hauch: Jobs / Skills / Misiones
        pluginManager.registerEvents(new JobBlockBreakListener(jobManager), this);
        pluginManager.registerEvents(new JobFishingListener(jobManager), this);
        pluginManager.registerEvents(new JobHarvestListener(jobManager, playerCache), this);
        pluginManager.registerEvents(new JobKillListener(jobManager, bountyManager), this);
        pluginManager.registerEvents(new MissionProgressListener(missionManager), this);

        // Dev3: PvP / Clanes / Raideo / Jefes
        pluginManager.registerEvents(new PvPArenaListener(arenaManager), this);
        pluginManager.registerEvents(new PvPKillstreakListener(bountyManager), this);
        pluginManager.registerEvents(new ClaimProtectionListener(claimManager), this);
        pluginManager.registerEvents(new BossDeathListener(bossManager), this);
        new ClanWarListener(eventAPI, clanWarManager);

        // Menu editor
        pluginManager.registerEvents(new ChatInputPrompt(this), this);
        pluginManager.registerEvents(new MenuEditorListener(menuManager), this);
    }

    private void registerCommands() {
        // Hauch
        JobCommand jobCmd = new JobCommand(jobManager, playerCache);
        getCommand("job").setExecutor(jobCmd);
        getCommand("job").setTabCompleter(jobCmd);

        StatsCommand statsCmd = new StatsCommand(skillManager, playerCache);
        getCommand("stats").setExecutor(statsCmd);

        // Dev3
        ArenaCommand arenaCmd = new ArenaCommand(arenaManager, economyAPI, playerCache, menuManager);
        getCommand("arena").setExecutor(arenaCmd);
        getCommand("arena").setTabCompleter(arenaCmd);

        ClanCommand clanCmd = new ClanCommand(clanManager, clanWarManager, economyAPI, menuManager);
        getCommand("clan").setExecutor(clanCmd);
        getCommand("clan").setTabCompleter(clanCmd);

        AuctionCommand auctionCmd = new AuctionCommand(auctionManager, economyAPI, menuManager);
        getCommand("auction").setExecutor(auctionCmd);
        getCommand("auction").setTabCompleter(auctionCmd);

        BountyCommand bountyCmd = new BountyCommand(bountyManager, economyAPI, menuManager);
        getCommand("bounty").setExecutor(bountyCmd);
        getCommand("bounty").setTabCompleter(bountyCmd);

        NotificationsCommand notifCmd = new NotificationsCommand(notificationManager, menuManager);
        getCommand("notificaciones").setExecutor(notifCmd);
        getCommand("notificaciones").setTabCompleter(notifCmd);

        MissionCommand missionCmd = new MissionCommand(missionManager, menuManager);
        getCommand("mission").setExecutor(missionCmd);
        getCommand("mission").setTabCompleter(missionCmd);

        SkillCommand skillCmd = new SkillCommand(skillManager, menuManager);
        getCommand("skill").setExecutor(skillCmd);
        getCommand("skill").setTabCompleter(skillCmd);

        CustomMenuCommand customMenuCmd = new CustomMenuCommand(menuManager, menuAliasManager);
        getCommand("custommenu").setExecutor(customMenuCmd);
        getCommand("custommenu").setTabCompleter(customMenuCmd);

        // Menú editor
        getCommand("menuedit").setExecutor(new MenuEditCommand(this));

        MenuCommand menuCmd = new MenuCommand(menuEditorManager, menuManager);
        getCommand("menu").setExecutor(menuCmd);
        getCommand("menu").setTabCompleter(menuCmd);

        // Admin command
        AdminCommand adminCmd = new AdminCommand(this);
        getCommand("sc").setExecutor(adminCmd);
        getCommand("sc").setTabCompleter(adminCmd);

        // v1.0.19: Eventos
        EventCommand eventCmd = new EventCommand(eventManager);
        getCommand("event").setExecutor(eventCmd);
        getCommand("event").setTabCompleter(eventCmd);

        // v1.0.20: Leaderboards
        LeaderboardCommand leaderboardCmd = new LeaderboardCommand(leaderboardManager);
        getCommand("leaderboard").setExecutor(leaderboardCmd);
        getCommand("leaderboard").setTabCompleter(leaderboardCmd);
    }

    public static SurvivalCorePlugin getInstance() {
        return instance;
    }

    // ---- APIs publicas ----

    public EconomyAPI getEconomyAPI() {
        return economyAPI;
    }

    public EventAPI getEventAPI() {
        return eventAPI;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    public SeasonManager getSeasonManager() {
        return seasonManager;
    }

    public PrestigeManager getPrestigeManager() {
        return prestigeManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public MenuEditorManager getMenuEditorManager() {
        return menuEditorManager;
    }

    public MenuAliasManager getMenuAliasManager() {
        return menuAliasManager;
    }

    public BackupScheduler getBackupScheduler() {
        return backupScheduler;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    // ---- Repositorios (listos para conectar managers de Hauch/Dev3) ----

    public JobRepository getJobRepository() {
        return jobRepository;
    }

    public SkillRepository getSkillRepository() {
        return skillRepository;
    }

    public MissionRepository getMissionRepository() {
        return missionRepository;
    }

    public ClanRepository getClanRepository() {
        return clanRepository;
    }

    public ClaimRepository getClaimRepository() {
        return claimRepository;
    }
}
