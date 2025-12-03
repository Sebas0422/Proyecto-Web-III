import { useMemo } from 'react'
import { useAppSelector } from '@app/store/hooks'
import type { RootState } from '@app/store/store'
import { UserRole } from '@features/auth/types'
import { PERMISSIONS, type Permission } from '@shared/constants/permissions'

/**
 * Permisos definidos por rol según los requerimientos:
 * 
 * CLIENTE (Público):
 * - Explorar proyectos y mapa de lotes
 * - Ver detalle de lotes
 * - Registrar interés/leads
 * - Realizar reservas
 * - Completar pagos
 * - Descargar comprobantes
 * 
 * EMPRESA (Inmobiliaria):
 * - Todo lo del cliente +
 * - Crear y gestionar proyectos
 * - Importar lotes desde KML
 * - Editar información de lotes
 * - Cargar fotos/documentos
 * - Gestionar reservas y leads
 * - Consultar reportes de ventas
 * 
 * ADMIN (Plataforma):
 * - Todo lo anterior +
 * - Alta y administración de empresas
 * - Gestión de usuarios
 * - Monitoreo global
 * - Reportes globales
 */

const ROLE_PERMISSIONS: Record<string, Permission[]> = {
  [UserRole.CLIENTE]: [
    // Ver proyectos y lotes
    PERMISSIONS.PROJECTS_VIEW,
    PERMISSIONS.LOTS_VIEW,

    // Crear y gestionar sus propios leads
    PERMISSIONS.LEADS_CREATE,
    PERMISSIONS.LEADS_VIEW,

    // Crear y ver sus propias reservas
    PERMISSIONS.RESERVATIONS_CREATE,
    PERMISSIONS.RESERVATIONS_VIEW,

    // Realizar pagos
    PERMISSIONS.PAYMENTS_CREATE,
    PERMISSIONS.PAYMENTS_VIEW,
    PERMISSIONS.PAYMENTS_DOWNLOAD_RECEIPT,

    // Ver sus propios reportes
    PERMISSIONS.REPORTS_VIEW_OWN,
  ],

  [UserRole.EMPRESA]: [
    // Todo lo del cliente
    PERMISSIONS.PROJECTS_VIEW,
    PERMISSIONS.LOTS_VIEW,
    PERMISSIONS.LEADS_CREATE,
    PERMISSIONS.LEADS_VIEW,
    PERMISSIONS.RESERVATIONS_CREATE,
    PERMISSIONS.RESERVATIONS_VIEW,
    PERMISSIONS.PAYMENTS_CREATE,
    PERMISSIONS.PAYMENTS_VIEW,
    PERMISSIONS.PAYMENTS_DOWNLOAD_RECEIPT,
    PERMISSIONS.REPORTS_VIEW_OWN,

    // Gestión completa de proyectos
    PERMISSIONS.PROJECTS_CREATE,
    PERMISSIONS.PROJECTS_UPDATE,
    PERMISSIONS.PROJECTS_DELETE,
    PERMISSIONS.PROJECTS_MANAGE,

    // Gestión completa de lotes
    PERMISSIONS.LOTS_CREATE,
    PERMISSIONS.LOTS_UPDATE,
    PERMISSIONS.LOTS_DELETE,
    PERMISSIONS.LOTS_IMPORT_KML,
    PERMISSIONS.LOTS_UPLOAD_MEDIA,
    PERMISSIONS.LOTS_MANAGE,

    // Gestión de leads
    PERMISSIONS.LEADS_UPDATE,
    PERMISSIONS.LEADS_DELETE,
    PERMISSIONS.LEADS_MANAGE,

    // Gestión de reservas
    PERMISSIONS.RESERVATIONS_CONFIRM,
    PERMISSIONS.RESERVATIONS_CANCEL,
    PERMISSIONS.RESERVATIONS_MANAGE,

    // Gestión de pagos
    PERMISSIONS.PAYMENTS_PROCESS,
    PERMISSIONS.PAYMENTS_MANAGE,

    // Reportes del tenant
    PERMISSIONS.REPORTS_VIEW_TENANT,
  ],

  [UserRole.ADMIN]: [
    // Todo lo de empresa
    PERMISSIONS.PROJECTS_VIEW,
    PERMISSIONS.PROJECTS_CREATE,
    PERMISSIONS.PROJECTS_UPDATE,
    PERMISSIONS.PROJECTS_DELETE,
    PERMISSIONS.PROJECTS_MANAGE,
    PERMISSIONS.LOTS_VIEW,
    PERMISSIONS.LOTS_CREATE,
    PERMISSIONS.LOTS_UPDATE,
    PERMISSIONS.LOTS_DELETE,
    PERMISSIONS.LOTS_IMPORT_KML,
    PERMISSIONS.LOTS_UPLOAD_MEDIA,
    PERMISSIONS.LOTS_MANAGE,
    PERMISSIONS.LEADS_VIEW,
    PERMISSIONS.LEADS_CREATE,
    PERMISSIONS.LEADS_UPDATE,
    PERMISSIONS.LEADS_DELETE,
    PERMISSIONS.LEADS_MANAGE,
    PERMISSIONS.RESERVATIONS_VIEW,
    PERMISSIONS.RESERVATIONS_CREATE,
    PERMISSIONS.RESERVATIONS_CONFIRM,
    PERMISSIONS.RESERVATIONS_CANCEL,
    PERMISSIONS.RESERVATIONS_MANAGE,
    PERMISSIONS.PAYMENTS_VIEW,
    PERMISSIONS.PAYMENTS_CREATE,
    PERMISSIONS.PAYMENTS_PROCESS,
    PERMISSIONS.PAYMENTS_MANAGE,
    PERMISSIONS.PAYMENTS_DOWNLOAD_RECEIPT,
    PERMISSIONS.REPORTS_VIEW_OWN,
    PERMISSIONS.REPORTS_VIEW_TENANT,

    // Permisos exclusivos de admin
    PERMISSIONS.REPORTS_VIEW_GLOBAL,
    PERMISSIONS.COMPANIES_VIEW,
    PERMISSIONS.COMPANIES_CREATE,
    PERMISSIONS.COMPANIES_UPDATE,
    PERMISSIONS.COMPANIES_DELETE,
    PERMISSIONS.USERS_VIEW,
    PERMISSIONS.USERS_CREATE,
    PERMISSIONS.USERS_UPDATE,
    PERMISSIONS.USERS_DELETE,
  ],
}

export const usePermissions = () => {
  const user = useAppSelector((state: RootState) => state.auth.user)

  const permissions = useMemo(() => {
    if (!user || !user.role) return []
    return ROLE_PERMISSIONS[user.role] || []
  }, [user])

  const hasPermission = (permission: Permission): boolean => {
    return permissions.includes(permission)
  }

  const hasAnyPermission = (permissionList: Permission[]): boolean => {
    return permissionList.some(p => permissions.includes(p))
  }

  const hasAllPermissions = (permissionList: Permission[]): boolean => {
    return permissionList.every(p => permissions.includes(p))
  }

  const isRole = (role: string): boolean => {
    return user?.role === role
  }

  const isAdmin = (): boolean => {
    return user?.role === UserRole.ADMIN
  }

  const isEmpresa = (): boolean => {
    return user?.role === UserRole.EMPRESA
  }

  const isCliente = (): boolean => {
    return user?.role === UserRole.CLIENTE
  }

  return {
    permissions,
    hasPermission,
    hasAnyPermission,
    hasAllPermissions,
    isRole,
    isAdmin,
    isEmpresa,
    isCliente,
    user,
  }
}
