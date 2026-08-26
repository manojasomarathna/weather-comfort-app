import { useEffect, useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { fetchRankings } from '../services/weatherApi'
import CityCard from '../components/CityCard'
import TemperatureChart from '../components/TemperatureChart'
import LoadingSpinner from '../components/LoadingSpinner'

export default function Dashboard() {
  const { getAccessTokenSilently } = useAuth0()
  const [cities, setCities] = useState([])
  const [filtered, setFiltered] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [search, setSearch] = useState('')
  const [sortBy, setSortBy] = useState('rank')

  useEffect(() => {
    fetchRankings(getAccessTokenSilently)
      .then(data => { setCities(data); setFiltered(data) })
      .catch(() => setError('Failed to load weather data.'))
      .finally(() => setLoading(false))
  }, [getAccessTokenSilently])

  useEffect(() => {
    let result = cities.filter(c =>
      c.cityName.toLowerCase().includes(search.toLowerCase())
    )
    if (sortBy === 'score') result = [...result].sort((a, b) => b.comfortScore - a.comfortScore)
    else if (sortBy === 'temp') result = [...result].sort((a, b) => b.temperatureCelsius - a.temperatureCelsius)
    else result = [...result].sort((a, b) => a.rank - b.rank)
    setFiltered(result)
  }, [search, sortBy, cities])

  if (loading) return <LoadingSpinner />
  if (error) return <div className="error-msg">{error}</div>

  return (
    <main className="dashboard">
      <h1 className="dashboard-title">City Comfort Rankings</h1>

      <div className="controls">
        <input
          className="search-input"
          placeholder="Search city..."
          value={search}
          onChange={e => setSearch(e.target.value)}
        />
        <select className="sort-select" value={sortBy} onChange={e => setSortBy(e.target.value)}>
          <option value="rank">Sort by Rank</option>
          <option value="score">Sort by Score</option>
          <option value="temp">Sort by Temperature</option>
        </select>
      </div>

      <TemperatureChart cities={filtered} />

      <div className="city-grid">
        {filtered.map(city => <CityCard key={city.cityName} city={city} />)}
      </div>
    </main>
  )
}
