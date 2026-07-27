-- SurvivalCore - Initial schema
-- MySQL 8+

CREATE TABLE IF NOT EXISTS players (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid VARCHAR(36) NOT NULL UNIQUE,
    name VARCHAR(16) NOT NULL,
    money BIGINT NOT NULL DEFAULT 0,
    premium_money BIGINT NOT NULL DEFAULT 0,
    `rank` VARCHAR(32) NOT NULL DEFAULT 'DEFAULT',
    prestige INT NOT NULL DEFAULT 0,
    last_login DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_players_uuid (uuid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS player_jobs (
    player_id BIGINT NOT NULL,
    job_type VARCHAR(32) NOT NULL,
    level INT NOT NULL DEFAULT 1,
    exp BIGINT NOT NULL DEFAULT 0,
    money_earned BIGINT NOT NULL DEFAULT 0,
    last_paid DATETIME NULL,
    PRIMARY KEY (player_id, job_type),
    CONSTRAINT fk_player_jobs_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS player_skills (
    player_id BIGINT NOT NULL,
    skill_type VARCHAR(32) NOT NULL,
    level INT NOT NULL DEFAULT 1,
    exp BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (player_id, skill_type),
    CONSTRAINT fk_player_skills_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS missions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_type VARCHAR(32) NOT NULL,
    frequency VARCHAR(16) NOT NULL,
    difficulty VARCHAR(16) NOT NULL,
    target INT NOT NULL,
    reward_money BIGINT NOT NULL DEFAULT 0,
    reward_exp BIGINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS player_mission_progress (
    player_id BIGINT NOT NULL,
    mission_id BIGINT NOT NULL,
    progress INT NOT NULL DEFAULT 0,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at DATETIME NULL,
    PRIMARY KEY (player_id, mission_id),
    CONSTRAINT fk_progress_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_progress_mission FOREIGN KEY (mission_id) REFERENCES missions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE,
    owner_id BIGINT NOT NULL,
    money BIGINT NOT NULL DEFAULT 0,
    power INT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_clans_owner FOREIGN KEY (owner_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS clan_members (
    clan_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    role VARCHAR(16) NOT NULL DEFAULT 'MEMBER',
    joined_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (clan_id, player_id),
    CONSTRAINT fk_clan_members_clan FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE CASCADE,
    CONSTRAINT fk_clan_members_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS claims (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_id BIGINT NOT NULL,
    clan_id BIGINT NULL,
    x1 INT NOT NULL,
    z1 INT NOT NULL,
    x2 INT NOT NULL,
    z2 INT NOT NULL,
    world VARCHAR(64) NOT NULL,
    power INT NOT NULL DEFAULT 1000,
    last_damaged DATETIME NULL,
    damage_immunity_until DATETIME NULL,
    CONSTRAINT fk_claims_owner FOREIGN KEY (owner_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_claims_clan FOREIGN KEY (clan_id) REFERENCES clans(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS siege_charges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attacker_id BIGINT NOT NULL,
    claim_id BIGINT NOT NULL,
    damage INT NOT NULL,
    used_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_siege_attacker FOREIGN KEY (attacker_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_siege_claim FOREIGN KEY (claim_id) REFERENCES claims(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS bounties (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    target_uuid VARCHAR(36) NOT NULL,
    reward BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    claimed_by BIGINT NULL,
    claimed_at DATETIME NULL,
    CONSTRAINT fk_bounty_creator FOREIGN KEY (created_by) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_bounty_claimer FOREIGN KEY (claimed_by) REFERENCES players(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS weekly_bosses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    boss_type VARCHAR(32) NOT NULL,
    spawn_time DATETIME NOT NULL,
    spawn_location VARCHAR(128) NOT NULL,
    health INT NOT NULL,
    defeated_by BIGINT NULL,
    rewards VARCHAR(512) NULL,
    CONSTRAINT fk_boss_defeater FOREIGN KEY (defeated_by) REFERENCES players(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS auctions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    item_name VARCHAR(64) NOT NULL,
    quantity INT NOT NULL,
    start_price BIGINT NOT NULL,
    current_price BIGINT NOT NULL,
    current_bidder_id BIGINT NULL,
    end_time DATETIME NOT NULL,
    CONSTRAINT fk_auction_seller FOREIGN KEY (seller_id) REFERENCES players(id) ON DELETE CASCADE,
    CONSTRAINT fk_auction_bidder FOREIGN KEY (current_bidder_id) REFERENCES players(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS transaction_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    player_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    type VARCHAR(32) NOT NULL,
    description VARCHAR(255) NULL,
    timestamp DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_txlog_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE,
    INDEX idx_txlog_player (player_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS seasons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    season_number INT NOT NULL,
    start_date DATETIME NOT NULL,
    end_date DATETIME NOT NULL,
    reward_pool BIGINT NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS season_rewards (
    season_id BIGINT NOT NULL,
    player_id BIGINT NOT NULL,
    `rank` INT NOT NULL,
    reward_money BIGINT NOT NULL DEFAULT 0,
    claimed BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (season_id, player_id),
    CONSTRAINT fk_season_rewards_season FOREIGN KEY (season_id) REFERENCES seasons(id) ON DELETE CASCADE,
    CONSTRAINT fk_season_rewards_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS player_prestige (
    player_id BIGINT NOT NULL PRIMARY KEY,
    prestige_level INT NOT NULL DEFAULT 0,
    reset_count INT NOT NULL DEFAULT 0,
    last_prestige_date DATETIME NULL,
    CONSTRAINT fk_prestige_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Fase 2: estadisticas de ranking de arena (ELO simple + wins/losses)
CREATE TABLE IF NOT EXISTS arena_stats (
    player_id BIGINT NOT NULL PRIMARY KEY,
    wins INT NOT NULL DEFAULT 0,
    losses INT NOT NULL DEFAULT 0,
    elo INT NOT NULL DEFAULT 1000,
    CONSTRAINT fk_arena_stats_player FOREIGN KEY (player_id) REFERENCES players(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Fase 2: guerras entre clanes (tracking de daño y resultado)
CREATE TABLE IF NOT EXISTS clan_wars (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attacking_clan_id BIGINT NOT NULL,
    defending_clan_id BIGINT NOT NULL,
    started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ended_at DATETIME NULL,
    winner_clan_id BIGINT NULL,
    attacker_score BIGINT NOT NULL DEFAULT 0,
    defender_score BIGINT NOT NULL DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_war_attacker FOREIGN KEY (attacking_clan_id) REFERENCES clans(id) ON DELETE CASCADE,
    CONSTRAINT fk_war_defender FOREIGN KEY (defending_clan_id) REFERENCES clans(id) ON DELETE CASCADE,
    INDEX idx_clan_wars_active (active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- v1.0.18: Optimizaciones de índices para queries frecuentes
ALTER TABLE clans ADD INDEX idx_clans_owner_id (owner_id);
ALTER TABLE clan_members ADD INDEX idx_clan_members_player_id (player_id);
ALTER TABLE auctions ADD INDEX idx_auctions_end_time (end_time);
ALTER TABLE siege_charges ADD INDEX idx_siege_charges_claim_id (claim_id);
ALTER TABLE bounties ADD INDEX idx_bounties_target_uuid (target_uuid);

-- v1.0.19: Sistema de eventos especiales
CREATE TABLE IF NOT EXISTS events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_type VARCHAR(32) NOT NULL,
    multiplier DOUBLE NOT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    created_by BIGINT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_events_active (start_time, end_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
