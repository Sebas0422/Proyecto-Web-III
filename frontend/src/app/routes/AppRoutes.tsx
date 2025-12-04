import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import LoginPage from '@features/auth/pages/LoginPage'
import RegisterPage from '@features/auth/pages/RegisterPage'
import PrivateRoute from '@shared/components/PrivateRoute'
import RoleBasedRoute from '@shared/components/RoleBasedRoute'
import { useAppSelector } from '../store/hooks'
import type { RootState } from '../store/store'
import { PERMISSIONS } from '@shared/constants/permissions'
import DashboardPage from '@features/dashboard/pages/DashboardPage'
import ProjectsListPage from '@features/projects/pages/ProjectsListPage'
import ProjectFormPage from '@features/projects/pages/ProjectFormPage'
import ProjectDetailPage from '@features/projects/pages/ProjectDetailPage'
import LotsListPage from '@features/lots/pages/LotsListPage'
import LotFormPage from '@features/lots/pages/LotFormPage'
import LeadsListPage from '@features/leads/pages/LeadsListPage'
import LeadFormPage from '@features/leads/pages/LeadFormPage'
import LeadDetailPage from '@features/leads/pages/LeadDetailPage'
import ReservationsListPage from '@features/reservations/pages/ReservationsListPage'
import ReservationFormPage from '@features/reservations/pages/ReservationFormPage'
import ReservationDetailPage from '@features/reservations/pages/ReservationDetailPage'
import CompaniesListPage from '@features/companies/pages/CompaniesListPage'
import CompanyFormPage from '@features/companies/pages/CompanyFormPage'
import UsersListPage from '@features/users/pages/UsersListPage'
import UserFormPage from '@features/users/pages/UserFormPage'
import LotMapPage from '@features/lots/pages/LotMapPage'

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
            <DashboardPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/projects"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.PROJECTS_VIEW}>
              <ProjectsListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.PROJECTS_CREATE}>
              <ProjectFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/:id"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="projects:view">
              <ProjectDetailPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/:id/edit"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.PROJECTS_UPDATE}>
              <ProjectFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      {/* Lotes - Todos pueden ver, solo EMPRESA y ADMIN pueden crear/editar */}
      <Route
        path="/projects/:proyectoId/lots"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="lots:view">
              <LotsListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/:proyectoId/lots/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="lots:create">
              <LotFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/:proyectoId/lotsMap"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="lots:view">
              <LotMapPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/projects/:proyectoId/lots/:lotId/edit"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="lots:edit">
              <LotFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route path="/lots" element={<PrivateRoute><RoleBasedRoute permissions="lots:view"><div>Lotes - En construcción</div></RoleBasedRoute></PrivateRoute>} />

      {/* Leads - Todos pueden crear, solo EMPRESA y ADMIN pueden gestionar */}
      <Route
        path="/leads"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="leads:view">
              <LeadsListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/leads/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="leads:create">
              <LeadFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/leads/:leadId"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="leads:view">
              <LeadDetailPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      {/* Reservas - Todos pueden crear, solo EMPRESA y ADMIN pueden gestionar */}
      <Route
        path="/reservations"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="reservations:view">
              <ReservationsListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/reservations/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="reservations:create">
              <ReservationFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/reservations/:id"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions="reservations:view">
              <ReservationDetailPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/companies"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.COMPANIES_VIEW}>
              <CompaniesListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/companies/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.COMPANIES_CREATE}>
              <CompanyFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/companies/:id/edit"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.COMPANIES_UPDATE}>
              <CompanyFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/users"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.USERS_VIEW}>
              <UsersListPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/users/new"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.USERS_CREATE}>
              <UserFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/users/:id/edit"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={PERMISSIONS.USERS_UPDATE}>
              <UserFormPage />
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />

      <Route
        path="/reports"
        element={
          <PrivateRoute>
            <RoleBasedRoute permissions={['reports:view-own', 'reports:view-tenant', 'reports:view-global']}>
              <div>Reportes - En construcción</div>
            </RoleBasedRoute>
          </PrivateRoute>
        }
      />
    </Routes>
  )
}

export default AppRoutes
