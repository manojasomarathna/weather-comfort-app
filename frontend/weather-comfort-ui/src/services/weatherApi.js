import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
})

export const fetchRankings = async (getAccessTokenSilently) => {
  try {
    const token = await getAccessTokenSilently()
    console.log('[weatherApi] token acquired:', token?.substring(0, 20) + '...')
    const { data } = await api.get('/api/weather/rankings', {
      headers: { Authorization: `Bearer ${token}` },
    })
    return data
  } catch (err) {
    console.error('[weatherApi] token/request failed:', err)
    const { data } = await api.get('/api/weather/rankings')
    return data
  }
}

export const fetchCacheStatus = async () => {
  const { data } = await api.get('/api/weather/cache-status')
  return data
}
