# Weather Comfort App

A full-stack weather analytics application that fetches real-time weather data, computes a custom **Comfort Index Score** for each city, and presents ranked insights through a responsive UI with Auth0 authentication.

---

## Setup Instructions

### Prerequisites
- Java 25
- Maven 3.9+
- Node.js 18+
- Auth0 account
- OpenWeatherMap API key

### Backend

```bash
cd backend/weather-comfort-api
```

Copy the example properties file and fill in your values:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

Edit `application.properties`:
```properties
openweather.api.key=YOUR_OPENWEATHER_API_KEY
auth0.audience=https://weather-comfort-api
spring.security.oauth2.resourceserver.jwt.issuer-uri=https://YOUR_AUTH0_DOMAIN/
```

Run the backend:
```bash
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend

```bash
cd frontend/weather-comfort-ui
```

Copy the example env file and fill in your values:
```bash
cp .env.example .env
```

Edit `.env`:
```env
VITE_AUTH0_DOMAIN=your-domain.us.auth0.com
VITE_AUTH0_CLIENT_ID=your-client-id
VITE_AUTH0_AUDIENCE=https://weather-comfort-api
VITE_API_BASE_URL=http://localhost:8080
```

Run the frontend:
```bash
npm install
npm run dev
```

Frontend runs on `http://localhost:5174`

---

## Comfort Index Formula

```
ComfortScore = (TempScore × 0.35) + (HumidityScore × 0.25) + (WindScore × 0.20)
             + (VisibilityScore × 0.10) + (CloudScore × 0.10)
```

### Parameters & Scoring

| Parameter | Weight | Ideal Range | Scoring Logic |
|-----------|--------|-------------|---------------|
| Temperature | 35% | 22°C | `max(0, 100 - abs(temp - 22) × 4)` |
| Humidity | 25% | 40–60% | 100 if in range, else `max(0, 100 - abs(humidity - 50) × 2)` |
| Wind Speed | 20% | 5–15 km/h | 100 if in range, else `max(0, 100 - abs(wind - 10) × 3)` |
| Visibility | 10% | 10000m | `(visibility / 10000) × 100` |
| Cloudiness | 10% | 0% | `max(0, 100 - cloudiness)` |

### Why These Parameters?

**Temperature (35%)** — The most direct indicator of physical comfort. 22°C is widely accepted as the ideal indoor/outdoor comfort temperature. Deviation in either direction reduces comfort significantly.

**Humidity (25%)** — High humidity makes heat feel worse and causes discomfort. The 40–60% range is considered ideal for human comfort. Outside this range, the body struggles to regulate temperature efficiently.

**Wind Speed (20%)** — A gentle breeze (5–15 km/h) enhances comfort by aiding evaporative cooling. Too calm feels stuffy; too windy feels harsh and unpleasant.

**Visibility (10%)** — Poor visibility indicates fog, smog, or heavy rain — all of which reduce the quality of the outdoor experience.

**Cloudiness (10%)** — Clear skies are generally preferred for outdoor comfort. Heavy cloud cover often correlates with rain and reduced UV, affecting mood and activity.

---

## Cache Design

- **Library**: Caffeine (in-memory, high-performance)
- **TTL**: 5 minutes (`expireAfterWrite`)
- **Caches**:
  - `weatherData` — raw API responses per city (keyed by city code)
  - `comfortRankings` — processed ranked list of all cities
- **Debug endpoint**: `GET /api/weather/cache-status` — returns HIT/MISS status and estimated size for each cache (publicly accessible, no auth required)

---

## Trade-offs Considered

| Decision | Trade-off |
|----------|-----------|
| In-memory Caffeine cache | Fast and simple, but cache is lost on restart. Redis would persist across restarts but adds infrastructure complexity. |
| City ID lookup (OpenWeatherMap) | More precise than name-based lookup, but IDs can become stale if OpenWeatherMap updates their database. |
| Comfort score computed server-side | Ensures formula integrity and prevents client-side manipulation, but adds backend processing time per request. |
| Auth0 for authentication | Saves significant development time for MFA/SSO, but introduces a third-party dependency. |
| Caffeine over Redis | Sufficient for a single-instance app. Redis would be needed for horizontal scaling. |

---

## Known Limitations

- Cache is in-memory only — restarting the server clears all cached data
- OpenWeatherMap free tier has rate limits (60 calls/minute) — fetching all 12 cities simultaneously could hit limits under heavy load
- MFA via email requires Auth0 Enterprise plan — OTP (Google Authenticator) is used instead
- No persistent storage — all data is fetched live from OpenWeatherMap

---

## API Endpoints

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | `/api/weather/rankings` | Required | Returns ranked city comfort scores |
| GET | `/api/weather/cache-status` | None | Returns cache HIT/MISS debug info |

---

## Features

- Real-time weather data for 12 cities (3 Sri Lankan + 9 global)
- Custom Comfort Index Score (0–100)
- City ranking from Most to Least Comfortable
- Server-side caching (5 min TTL)
- Auth0 authentication with MFA
- Disabled public signups (whitelist only)
- Dark mode
- Responsive UI (mobile + desktop)
- Search and sort (by rank, score, temperature)
- Temperature bar chart (Recharts)
