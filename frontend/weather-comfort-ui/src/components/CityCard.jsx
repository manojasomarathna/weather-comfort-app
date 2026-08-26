export default function CityCard({ city }) {
  const scoreColor = city.comfortScore >= 70 ? '#22c55e' : city.comfortScore >= 40 ? '#f59e0b' : '#ef4444'

  return (
    <div className="city-card">
      <div className="city-rank">#{city.rank}</div>
      <div className="city-info">
        <h3 className="city-name">{city.cityName}</h3>
        <p className="city-desc">{city.description}</p>
      </div>
      <div className="city-stats">
        <span>🌡 {city.temperatureCelsius}°C</span>
        <span>💧 {city.humidity}%</span>
        <span>💨 {city.windSpeed} km/h</span>
        <span>☁️ {city.cloudiness}%</span>
      </div>
      <div className="city-score" style={{ color: scoreColor }}>
        <span className="score-value">{city.comfortScore}</span>
        <span className="score-label">Comfort Score</span>
      </div>
    </div>
  )
}
