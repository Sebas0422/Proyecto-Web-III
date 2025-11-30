import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from '@features/auth/pages/LoginPage'
import RegisterPage from '@features/auth/pages/RegisterPage'
import PrivateRoute from '@shared/components/PrivateRoute'
import { useAppSelector } from '../store/hooks'
import type { RootState } from '../store/store'
import DashboardPage from '@features/dashboard/pages/DashboardPage'
import ProjectsListPage from '@features/projects/pages/ProjectsListPage'
import ProjectFormPage from '@features/projects/pages/ProjectFormPage'
import LotMapDashboard from '@features/lots/LotMap'

const AppRoutes: React.FC = () => {
  const token = useAppSelector((s: RootState) => s.auth.token)
  return (
    <Routes>
      <Route path="/" element={token ? <Navigate to="/dashboard" /> : <Navigate to="/login" />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path='/mapas' element={<LotMapDashboard />} />

      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <DashboardPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/projects"
        element={
          <PrivateRoute>
            <ProjectsListPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/new"
        element={
          <PrivateRoute>
            <ProjectFormPage />
          </PrivateRoute>
        }
      />

      {/* Placeholder routes */}
      <Route path="/lots" element={<PrivateRoute><div>Lotes - En construcción</div></PrivateRoute>} />
      <Route path="/leads" element={<PrivateRoute><div>Leads - En construcción</div></PrivateRoute>} />
      <Route path="/reservations" element={<PrivateRoute><div>Reservas - En construcción</div></PrivateRoute>} />
      <Route path="/reports" element={<PrivateRoute><div>Reportes - En construcción</div></PrivateRoute>} />
    </Routes>
  )
}

export default AppRoutes
