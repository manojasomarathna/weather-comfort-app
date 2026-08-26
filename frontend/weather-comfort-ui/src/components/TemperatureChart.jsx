import { BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, Cell } from 'recharts'

export default function TemperatureChart({ cities }) {
  return (
    <div className="chart-container">
      <h2>Temperature by City (°C)</h2>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={cities} margin={{ top: 10, right: 20, left: 0, bottom: 60 }}>
          <XAxis dataKey="cityName" angle={-40} textAnchor="end" interval={0} tick={{ fontSize: 12 }} />
          <YAxis unit="°C" />
          <Tooltip formatter={(v) => `${v}°C`} />
          <Bar dataKey="temperatureCelsius" radius={[4, 4, 0, 0]}>
            {cities.map((_, i) => (
              <Cell key={i} fill={`hsl(${200 + i * 15}, 70%, 55%)`} />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  )
}
