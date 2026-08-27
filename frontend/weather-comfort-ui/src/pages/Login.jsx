import { useAuth0 } from '@auth0/auth0-react'

export default function Login() {
  const { loginWithRedirect } = useAuth0()

  return (
    <div className="login-page">
      <div className="login-bg">
        <span>🌤</span><span>⛅</span><span>🌦</span>
        <span>🌍</span><span>🌡</span><span>💨</span>
        <span>🌤</span><span>⛅</span><span>🌈</span>
      </div>
      <div className="login-card">
        <div className="login-icon">🌤</div>
        <h1>Weather Comfort</h1>
        <p className="login-subtitle">Real-time comfort rankings for cities worldwide</p>

        <div className="login-features">
          <div className="login-feature"><span>🏙</span> 12 Global Cities</div>
          <div className="login-feature"><span>📊</span> Comfort Index Score</div>
          <div className="login-feature"><span>🌡</span> Live Weather Data</div>
        </div>

        <button className="btn-primary btn-large login-btn" onClick={() => loginWithRedirect()}>
          Sign In to Continue →
        </button>
        <p className="login-note">Secure login powered by Auth0</p>
      </div>
    </div>
  )
}
