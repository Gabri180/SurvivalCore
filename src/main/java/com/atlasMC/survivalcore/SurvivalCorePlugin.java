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
import com.atlasMC.survivalcore.commands.BountyCommand;
import com.atlasMC.survivalcore.commands.ClanCommand;
import com.atlasMC.survivalcore.commands.JobCommand;
import com.atlasMC.survivalcore.commands.MenuCommand;
import com.atlasMC.survivalcore.commands.MenuEditCommand;
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
import com.atlasMC.survivalcore.listeners.MissionProgressListener;
import com.atlasMC.survivalcore.listeners.PlayerDataListener;
import com.atlasMC.survivalcore.listeners.PvPArenaListener;
import com.atlasMC.survivalcore.listeners.PvPKillstreakListener;
import com.atlasMC.survivalcore.menu.ChatInputPrompt;
import com.atlasMC.survivalcore.menu.MenuEditorManager;
import com.atlasMC.survivalcore.menu.MenuLoader;
import com.atlasMC.survivalcore.menu.MenuManager;
import com.atlasMC.survivalcore.menu.MenuYamlWriter;
import com.atlasMC.survivalcore.prestige.PrestigeManager;
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
    private SeasonManager seasonManager;
    private PrestigeManager prestigeManager;
    private MenuManager menuManager;
    private MenuEditorManager menuEditorManager;

    // Repositorios listos para Hauch (Jobs/Skills/Misiones)
    private JobRepository jobRepository;
    private SkillRepository skillRepository;
    private MissionRepository missionRepository;

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
        this.playerCache = new PlayerCache(playerRepository);
        this.eventAPI = new EventAPI();
        this.economyAPI = new EconomyAPI(playerCache, databaseManager);
        this.seasonManager = new SeasonManager(databaseManager);
        this.prestigeManager = new PrestigeManager(playerCache, databaseManager);

        this.jobRepository = new JobRepository(databaseManager);
        this.skillRepository = new SkillRepository(databaseManager);
        this.missionRepository = new MissionRepository(databaseManager);

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

        MenuYamlWriter yamlWriter = new MenuYamlWriter(this);
        this.menuEditorManager = new MenuEditorManager(menuManager, yamlWriter);

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
    }

    private void registerCommands() {
        // Hauch
        JobCommand jobCmd = new JobCommand(jobManager, playerCache);
        getCommand("job").setExecutor(jobCmd);
        getCommand("job").setTabCompleter(jobCmd);

        StatsCommand statsCmd = new StatsCommand(skillManager, playerCache);
        getCommand("stats").setExecutor(statsCmd);

        // Dev3
        ArenaCommand arenaCmd = new ArenaCommand(arenaManager);
        getCommand("arena").setExecutor(arenaCmd);
        getCommand("arena").setTabCompleter(arenaCmd);

        ClanCommand clanCmd = new ClanCommand(clanManager, clanWarManager);
        getCommand("clan").setExecutor(clanCmd);
        getCommand("clan").setTabCompleter(clanCmd);

        AuctionCommand auctionCmd = new AuctionCommand(auctionManager);
        getCommand("auction").setExecutor(auctionCmd);
        getCommand("auction").setTabCompleter(auctionCmd);

        BountyCommand bountyCmd = new BountyCommand(bountyManager);
        getCommand("bounty").setExecutor(bountyCmd);
        getCommand("bounty").setTabCompleter(bountyCmd);

        // Menú editor
        getCommand("menuedit").setExecutor(new MenuEditCommand(this));

        MenuCommand menuCmd = new MenuCommand(menuEditorManager);
        getCommand("menu").setExecutor(menuCmd);
        getCommand("menu").setTabCompleter(menuCmd);

        // Admin command
        AdminCommand adminCmd = new AdminCommand(this);
        getCommand("sc").setExecutor(adminCmd);
        getCommand("sc").setTabCompleter(adminCmd);
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
