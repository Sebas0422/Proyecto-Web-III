import React from 'react'
import { usePermissions, type Permission } from '@shared/hooks/usePermissions'

interface PermissionGuardProps {
  permissions: Permission | Permission[]
  requireAll?: boolean
  fallback?: React.ReactNode
  children: React.ReactNode
}

/**
 * Componente para mostrar contenido solo si el usuario tiene los permisos necesarios
 * 
 * @example
 * // Requiere un permiso
 * <PermissionGuard permissions="projects:create">
 *   <CreateProjectButton />
 * </PermissionGuard>
 * 
 * @example
 * // Requiere cualquiera de los permisos
 * <PermissionGuard permissions={['projects:edit', 'projects:delete']}>
 *   <EditButton />
 * </PermissionGuard>
 * 
 * @example
 * // Requiere todos los permisos
 * <PermissionGuard permissions={['projects:edit', 'lots:edit']} requireAll>
 *   <AdvancedButton />
 * </PermissionGuard>
 * 
 * @example
 * // Con fallback
 * <PermissionGuard permissions="projects:create" fallback={<div>No autorizado</div>}>
 *   <CreateButton />
 * </PermissionGuard>
 */
const PermissionGuard: React.FC<PermissionGuardProps> = ({
  permissions,
  requireAll = false,
  fallback = null,
  children,
}) => {
  const { hasPermission, hasAnyPermission, hasAllPermissions } = usePermissions()

  const hasAccess = (() => {
    if (Array.isArray(permissions)) {
      return requireAll
        ? hasAllPermissions(permissions)
        : hasAnyPermission(permissions)
    }
    return hasPermission(permissions)
  })()

  if (!hasAccess) {
    return <>{fallback}</>
  }

  return <>{children}</>
}

export default PermissionGuard
