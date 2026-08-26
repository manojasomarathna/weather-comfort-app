import { useAuth0 } from '@auth0/auth0-react'
import { useState, useEffect } from 'react'

export default function Navbar() {
  const { isAuthenticated, loginWithRedirect, logout, user } = useAuth0()
  const [dark, setDark] = useState(() => localStorage.getItem('theme') === 'dark')

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', dark ? 'dark' : 'light')
    localStorage.setItem('theme', dark ? 'dark' : 'light')
  }, [dark])

  return (
    <nav className="navbar">
      <span className="navbar-brand">🌤 Weather Comfort</span>
      <div className="navbar-actions">
        <button className="btn-icon" onClick={() => setDark(d => !d)}>
          {dark ? '☀️' : '🌙'}
        </button>
        {isAuthenticated ? (
          <>
            <span className="navbar-user">{user?.email}</span>
            <button className="btn-outline" onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}>
              Logout
            </button>
          </>
        ) : (
          <button className="btn-primary" onClick={() => loginWithRedirect()}>Login</button>
        )}
      </div>
    </nav>
  )
}
