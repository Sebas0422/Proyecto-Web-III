import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from '@features/auth/pages/LoginPage'
import RegisterPage from '@features/auth/pages/RegisterPage'
import PrivateRoute from '@shared/components/PrivateRoute'
import RoleGuard from '@shared/components/RoleGuard'
import { useAppSelector } from '../store/hooks'
import type { RootState } from '../store/store'

const Dashboard: React.FC = () => {
  const user = useAppSelector((s: RootState) => s.auth.user)
  return (
    <div style={{ padding: 20 }}>
      <h2>Dashboard</h2>
      <pre>{JSON.stringify(user, null, 2)}</pre>
    </div>
  )
}

const AppRoutes: React.FC = () => {
  const token = useAppSelector((s: RootState) => s.auth.token)
  return (
    <Routes>
      <Route path="/" element={token ? <Navigate to="/dashboard" /> : <Navigate to="/login" />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />

      <Route
        path="/admin"
        element={
          <PrivateRoute>
            <RoleGuard allowedRoles={["admin"]}>
              <div style={{ padding: 20 }}>
                <h2>Admin Panel</h2>
              </div>
            </RoleGuard>
          </PrivateRoute>
        }
      />
    </Routes>
  )
}

export default AppRoutes
