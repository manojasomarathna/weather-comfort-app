import { useAuth0 } from '@auth0/auth0-react'

export default function Login() {
  const { loginWithRedirect } = useAuth0()

  return (
    <div className="login-page">
      <div className="login-card">
        <h1>🌤 Weather Comfort</h1>
        <p>View real-time comfort rankings for cities worldwide.</p>
        <button className="btn-primary btn-large" onClick={() => loginWithRedirect()}>
          Sign In to Continue
        </button>
      </div>
    </div>
  )
}
