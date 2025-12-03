/**
 * IMPORTANTE: Los permisos del frontend deben coincidir EXACTAMENTE
 * con los permisos configurados en el backend.
 * 
 * Backend: auth-service/src/main/resources/data.sql
 * 
 * Mapeo de permisos por rol:
 * 
 * ADMIN:
 * - Todos los permisos de EMPRESA
 * - companies:view, companies:create, companies:update, companies:delete
 * - users:view, users:create, users:update, users:delete
 * 
 * EMPRESA:
 * - projects:view, projects:create, projects:update, projects:delete
 * - lots:view, lots:create, lots:update, lots:delete
 * - leads:view, leads:create, leads:update, leads:delete
 * - reservations:view, reservations:create, reservations:update, reservations:delete
 * 
 * CLIENTE:
 * - leads:view (solo sus propios leads)
 * - reservations:view (solo sus propias reservas)
 * 
 * NOTA: Si agregas un nuevo permiso, debes:
 * 1. Actualizar el backend (data.sql)
 * 2. Actualizar PERMISSIONS en permissions.ts
 * 3. Reiniciar auth-service para aplicar cambios
 */

export const PERMISSION_DOCS = {
  warning: 'Los permisos deben coincidir exactamente con el backend',
  backendLocation: 'auth-service/src/main/resources/data.sql',
};
