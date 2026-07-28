const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');
const bodyParser = require('body-parser');
const path = require('path');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(bodyParser.json());

// Servir archivos estáticos (HTML, CSS, JS)
app.use(express.static(path.join(__dirname, 'public')));

// Ruta principal para servir index.html
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// PostgreSQL Connection
const pool = new Pool({
  user: process.env.DB_USER || 'postgres',
  password: process.env.DB_PASSWORD || 'password',
  host: process.env.DB_HOST || 'localhost',
  port: process.env.DB_PORT || 5432,
  database: process.env.DB_NAME || 'survivalcore'
});

// Initialize database
async function initDatabase() {
  try {
    await pool.query(`
      CREATE TABLE IF NOT EXISTS licenses (
        id SERIAL PRIMARY KEY,
        key VARCHAR(255) UNIQUE NOT NULL,
        tier VARCHAR(50) NOT NULL,
        server_uuid VARCHAR(255),
        created_at TIMESTAMP DEFAULT NOW(),
        expires_at TIMESTAMP,
        last_validated_at TIMESTAMP DEFAULT NOW(),
        max_players INT DEFAULT 50,
        max_servers INT DEFAULT 1,
        active BOOLEAN DEFAULT true,
        validation_count INT DEFAULT 0
      )
    `);

    await pool.query(`
      CREATE TABLE IF NOT EXISTS validation_logs (
        id SERIAL PRIMARY KEY,
        license_key VARCHAR(255) NOT NULL,
        server_uuid VARCHAR(255),
        player_count INT,
        validated_at TIMESTAMP DEFAULT NOW(),
        ip_address VARCHAR(45),
        status VARCHAR(50)
      )
    `);

    console.log('✅ Database initialized');
  } catch (err) {
    console.error('❌ Database initialization error:', err);
  }
}

// Endpoint: Validar licencia
app.post('/api/licenses/validate', async (req, res) => {
  try {
    const { key, serverUuid, playerCount } = req.body;

    if (!key || !serverUuid) {
      return res.status(400).json({ error: 'Missing key or serverUuid' });
    }

    // Obtener licencia de BD
    const result = await pool.query(
      'SELECT * FROM licenses WHERE key = $1 AND active = true',
      [key]
    );

    if (result.rows.length === 0) {
      // Log validación fallida
      await logValidation(key, serverUuid, playerCount, req.ip, 'INVALID');
      return res.status(404).json({ error: 'License not found' });
    }

    const license = result.rows[0];

    // Verificar expiración
    if (license.expires_at && new Date(license.expires_at) < new Date()) {
      await logValidation(key, serverUuid, playerCount, req.ip, 'EXPIRED');
      return res.status(403).json({ error: 'License expired' });
    }

    // Verificar hardware lock (server_uuid)
    if (license.server_uuid && license.server_uuid !== serverUuid) {
      await logValidation(key, serverUuid, playerCount, req.ip, 'HARDWARE_MISMATCH');
      return res.status(403).json({ error: 'Hardware mismatch' });
    }

    // Verificar límite de jugadores
    if (license.max_players && playerCount > license.max_players) {
      return res.status(429).json({ error: 'Max players exceeded' });
    }

    // Actualizar última validación
    await pool.query(
      'UPDATE licenses SET last_validated_at = NOW(), validation_count = validation_count + 1 WHERE key = $1',
      [key]
    );

    await logValidation(key, serverUuid, playerCount, req.ip, 'SUCCESS');

    // Retornar licencia válida
    return res.json({
      key: license.key,
      tier: license.tier,
      serverUuid: license.server_uuid,
      maxPlayers: license.max_players,
      maxServers: license.max_servers,
      active: license.active,
      expiresAt: license.expires_at
    });

  } catch (err) {
    console.error('❌ Validation error:', err);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// Endpoint: Crear licencia (desde Gumroad webhook)
app.post('/api/licenses/create', async (req, res) => {
  try {
    const { key, tier, expiresAt, maxPlayers, maxServers } = req.body;
    const authHeader = req.headers['authorization'];

    // Verificar token de webhook
    if (authHeader !== `Bearer ${process.env.WEBHOOK_TOKEN}`) {
      return res.status(401).json({ error: 'Unauthorized' });
    }

    // Crear licencia
    const result = await pool.query(
      `INSERT INTO licenses (key, tier, expires_at, max_players, max_servers, active)
       VALUES ($1, $2, $3, $4, $5, true)
       RETURNING *`,
      [key, tier, expiresAt ? new Date(expiresAt) : null, maxPlayers, maxServers]
    );

    console.log('✅ License created:', key);
    return res.json(result.rows[0]);

  } catch (err) {
    console.error('❌ License creation error:', err);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// Endpoint: Revocar licencia
app.post('/api/licenses/revoke', async (req, res) => {
  try {
    const { key } = req.body;
    const authHeader = req.headers['authorization'];

    if (authHeader !== `Bearer ${process.env.WEBHOOK_TOKEN}`) {
      return res.status(401).json({ error: 'Unauthorized' });
    }

    await pool.query(
      'UPDATE licenses SET active = false WHERE key = $1',
      [key]
    );

    console.log('✅ License revoked:', key);
    return res.json({ success: true });

  } catch (err) {
    console.error('❌ Revocation error:', err);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// Endpoint: Admin - Listar licencias
app.get('/api/admin/licenses', async (req, res) => {
  try {
    const authHeader = req.headers['authorization'];

    if (authHeader !== `Bearer ${process.env.ADMIN_TOKEN}`) {
      return res.status(401).json({ error: 'Unauthorized' });
    }

    const result = await pool.query(
      `SELECT id, key, tier, server_uuid, created_at, expires_at, active, validation_count
       FROM licenses ORDER BY created_at DESC LIMIT 100`
    );

    return res.json(result.rows);

  } catch (err) {
    console.error('❌ Admin query error:', err);
    return res.status(500).json({ error: 'Internal server error' });
  }
});

// Logging function
async function logValidation(key, serverUuid, playerCount, ip, status) {
  try {
    await pool.query(
      `INSERT INTO validation_logs (license_key, server_uuid, player_count, ip_address, status)
       VALUES ($1, $2, $3, $4, $5)`,
      [key, serverUuid, playerCount, ip, status]
    );
  } catch (err) {
    console.error('❌ Logging error:', err);
  }
}

// Health check
app.get('/health', (req, res) => {
  res.json({ status: 'ok', timestamp: new Date() });
});

// Start server
const PORT = process.env.PORT || 3000;
initDatabase().then(() => {
  app.listen(PORT, () => {
    console.log(`🚀 License Server running on port ${PORT}`);
  });
});
