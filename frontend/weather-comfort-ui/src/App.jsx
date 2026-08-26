import { useAuth0 } from '@auth0/auth0-react'
import Dashboard from './pages/Dashboard'
import Login from './pages/Login'
import Navbar from './components/Navbar'
import LoadingSpinner from './components/LoadingSpinner'
import './App.css'

export default function App() {
  const { isAuthenticated, isLoading } = useAuth0()

  if (isLoading) return <LoadingSpinner />

  return (
    <div className="app">
      <Navbar />
      {isAuthenticated ? <Dashboard /> : <Login />}
    </div>
  )
}
