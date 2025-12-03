/**
 * Enum centralizado de permisos para evitar errores de tipeo
 * Debe coincidir exactamente con los permisos del backend
 */
export const PERMISSIONS = {
  // Projects
  PROJECTS_VIEW: 'projects:view',
  PROJECTS_CREATE: 'projects:create',
  PROJECTS_UPDATE: 'projects:update',
  PROJECTS_DELETE: 'projects:delete',
  PROJECTS_MANAGE: 'projects:manage',

  // Lots
  LOTS_VIEW: 'lots:view',
  LOTS_CREATE: 'lots:create',
  LOTS_UPDATE: 'lots:update',
  LOTS_DELETE: 'lots:delete',
  LOTS_IMPORT_KML: 'lots:import-kml',
  LOTS_UPLOAD_MEDIA: 'lots:upload-media',
  LOTS_MANAGE: 'lots:manage',

  // Leads
  LEADS_VIEW: 'leads:view',
  LEADS_CREATE: 'leads:create',
  LEADS_UPDATE: 'leads:update',
  LEADS_DELETE: 'leads:delete',
  LEADS_MANAGE: 'leads:manage',

  // Reservations
  RESERVATIONS_VIEW: 'reservations:view',
  RESERVATIONS_CREATE: 'reservations:create',
  RESERVATIONS_UPDATE: 'reservations:update',
  RESERVATIONS_DELETE: 'reservations:delete',
  RESERVATIONS_CONFIRM: 'reservations:confirm',
  RESERVATIONS_CANCEL: 'reservations:cancel',
  RESERVATIONS_MANAGE: 'reservations:manage',

  // Payments
  PAYMENTS_VIEW: 'payments:view',
  PAYMENTS_CREATE: 'payments:create',
  PAYMENTS_PROCESS: 'payments:process',
  PAYMENTS_MANAGE: 'payments:manage',
  PAYMENTS_DOWNLOAD_RECEIPT: 'payments:download-receipt',

  // Reports
  REPORTS_VIEW_OWN: 'reports:view-own',
  REPORTS_VIEW_TENANT: 'reports:view-tenant',
  REPORTS_VIEW_GLOBAL: 'reports:view-global',

  // Companies
  COMPANIES_VIEW: 'companies:view',
  COMPANIES_CREATE: 'companies:create',
  COMPANIES_UPDATE: 'companies:update',
  COMPANIES_DELETE: 'companies:delete',

  // Users
  USERS_VIEW: 'users:view',
  USERS_CREATE: 'users:create',
  USERS_UPDATE: 'users:update',
  USERS_DELETE: 'users:delete',
} as const;

export type Permission = typeof PERMISSIONS[keyof typeof PERMISSIONS];
