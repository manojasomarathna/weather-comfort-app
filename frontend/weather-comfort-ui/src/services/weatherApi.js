import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

export const fetchRankings = async (getAccessTokenSilently) => {
  const token = await getAccessTokenSilently()
  const { data } = await api.get('/api/weather/rankings', {
    headers: { Authorization: `Bearer ${token}` },
  })
  return data
}

export const fetchCacheStatus = async () => {
  const { data } = await api.get('/api/weather/cache-status')
  return data
}
