/**
 * GUMROAD WEBHOOK INTEGRATION
 *
 * En tu cuenta de Gumroad:
 * 1. Ve a Settings → Webhooks
 * 2. Agrega un webhook con URL: https://your-vps-ip:3000/api/licenses/create
 * 3. Selecciona "sale" como evento
 * 4. Usa el token WEBHOOK_TOKEN en Authorization header
 *
 * Payload esperado de Gumroad:
 * {
 *   "id": "evento-id",
 *   "type": "sale",
 *   "timestamp": "2024-01-15T10:30:00Z",
 *   "data": {
 *     "id": "venta-id",
 *     "email": "customer@example.com",
 *     "product_id": "123456",
 *     "product_name": "SurvivalCore Professional",
 *     "license_key": "ABC-XYZ-123",
 *     "custom_fields": {
 *       "tier": "PROFESSIONAL",
 *       "duration_days": 30
 *     }
 *   }
 * }
 *
 * TIERS Y DURACION:
 * STARTER: 30 días, $9.99/mes
 * PROFESSIONAL: 30 días, $24.99/mes
 * ENTERPRISE: 30 días, $49.99/mes
 * LIFETIME: null (nunca expira)
 */

// Este archivo documenta la integración
// El servidor.js ya maneja los webhooks en POST /api/licenses/create
