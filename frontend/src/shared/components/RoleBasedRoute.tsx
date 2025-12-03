import React from 'react'
import { Navigate } from 'react-router-dom'
import { usePermissions, type Permission } from '@shared/hooks/usePermissions'

interface RoleBasedRouteProps {
  permissions: Permission | Permission[]
  requireAll?: boolean
  redirectTo?: string
  children: React.ReactNode
}

/**
 * Componente para proteger rutas completas basadas en permisos
 * 
 * @example
 * <Route
 *   path="/projects/new"
 *   element={
 *     <RoleBasedRoute permissions="projects:create">
 *       <ProjectFormPage />
 *     </RoleBasedRoute>
 *   }
 * />
 */
const RoleBasedRoute: React.FC<RoleBasedRouteProps> = ({
  permissions,
  requireAll = false,
  redirectTo = '/dashboard',
  children,
}) => {
  const { hasPermission, hasAnyPermission, hasAllPermissions, user } = usePermissions()

  if (!user) {
    return <Navigate to="/login" replace />
  }

  const hasAccess = (() => {
    if (Array.isArray(permissions)) {
      return requireAll
        ? hasAllPermissions(permissions)
        : hasAnyPermission(permissions)
    }
    return hasPermission(permissions)
  })()

  if (!hasAccess) {
    return <Navigate to={redirectTo} replace />
  }

  return <>{children}</>
}

export default RoleBasedRoute
