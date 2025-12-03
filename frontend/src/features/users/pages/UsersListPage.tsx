import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { PlusCircle, UserCog, Edit, CheckCircle, XCircle, Key, Lock, Unlock } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import PermissionGuard from '@shared/components/PermissionGuard'
import { PERMISSIONS } from '@shared/constants/permissions'
import { useGetUsersQuery, useActivateUserMutation, useDeactivateUserMutation } from '../services/usersApi'

const UsersListPage: React.FC = () => {
  const navigate = useNavigate()
  const [roleFilter, setRoleFilter] = useState<string>('')
  const [activeFilter, setActiveFilter] = useState<boolean | undefined>(true)

  const { data: users, isLoading, error } = useGetUsersQuery({
    role: roleFilter || undefined,
    isActive: activeFilter
  })
  const [activateUser] = useActivateUserMutation()
  const [deactivateUser] = useDeactivateUserMutation()

  const handleActivate = async (id: string, name: string) => {
    if (window.confirm(`¿Está seguro de activar al usuario "${name}"?`)) {
      try {
        await activateUser(id).unwrap()
      } catch (err) {
        console.error('Error al activar:', err)
        alert('Error al activar el usuario')
      }
    }
  }

  const handleDeactivate = async (id: string, name: string) => {
    if (window.confirm(`¿Está seguro de desactivar al usuario "${name}"?`)) {
      try {
        await deactivateUser(id).unwrap()
      } catch (err) {
        console.error('Error al desactivar:', err)
        alert('Error al desactivar el usuario')
      }
    }
  }

  const getRoleBadge = (role: string) => {
    const colors = {
      ADMIN: 'bg-red-100 text-red-700',
      EMPRESA: 'bg-blue-100 text-blue-700',
      CLIENTE: 'bg-green-100 text-green-700',
    }
    return colors[role as keyof typeof colors] || 'bg-gray-100 text-gray-700'
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 mb-2">Usuarios</h1>
            <p className="text-slate-600">Gestión de usuarios del sistema</p>
          </div>

          <PermissionGuard permissions={PERMISSIONS.USERS_CREATE}>
            <button
              onClick={() => navigate('/users/new')}
              className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition-colors"
            >
              <PlusCircle size={20} />
              Nuevo Usuario
            </button>
          </PermissionGuard>
        </div>

        {/* Filtros */}
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex gap-3 flex-wrap">
            <select
              value={roleFilter}
              onChange={(e) => setRoleFilter(e.target.value)}
              className="px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
            >
              <option value="">Todos los roles</option>
              <option value="ADMIN">Administrador</option>
              <option value="EMPRESA">Empresa</option>
              <option value="CLIENTE">Cliente</option>
            </select>

            <button
              onClick={() => setActiveFilter(true)}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${activeFilter === true
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Activos
            </button>
            <button
              onClick={() => setActiveFilter(undefined)}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${activeFilter === undefined
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Todos
            </button>
          </div>
        </div>

        {/* Tabla */}
        {isLoading ? (
          <div className="text-center py-12">
            <p className="text-slate-600">Cargando usuarios...</p>
          </div>
        ) : error ? (
          <div className="text-center py-12">
            <p className="text-red-600">Error al cargar los usuarios</p>
          </div>
        ) : users && users.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-lg shadow">
            <UserCog size={48} className="mx-auto text-slate-300 mb-4" />
            <p className="text-slate-600">No se encontraron usuarios</p>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-slate-200">
              <thead className="bg-slate-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Usuario
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Email
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Rol
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Empresa
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500 uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-slate-200">
                {users?.map((user) => (
                  <tr key={user.id} className="hover:bg-slate-50">
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="flex items-center">
                        <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                          <UserCog className="text-blue-600" size={20} />
                        </div>
                        <div className="ml-4">
                          <div className="text-sm font-medium text-slate-900">
                            {user.firstName} {user.lastName}
                          </div>
                          {user.phoneNumber && (
                            <div className="text-sm text-slate-500">📞 {user.phoneNumber}</div>
                          )}
                        </div>
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-slate-900">{user.email}</div>
                      {user.isEmailVerified ? (
                        <div className="text-xs text-green-600 flex items-center gap-1">
                          <CheckCircle size={12} /> Verificado
                        </div>
                      ) : (
                        <div className="text-xs text-amber-600">No verificado</div>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${getRoleBadge(user.role)}`}>
                        {user.role}
                      </span>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {user.companyName ? (
                        <div className="text-sm text-slate-900">{user.companyName}</div>
                      ) : (
                        <div className="text-sm text-slate-400 italic">Sin asignar</div>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {user.isActive ? (
                        <span className="flex items-center gap-1 text-green-600 text-sm">
                          <CheckCircle size={16} /> Activo
                        </span>
                      ) : (
                        <span className="flex items-center gap-1 text-red-600 text-sm">
                          <XCircle size={16} /> Inactivo
                        </span>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <PermissionGuard permissions={[PERMISSIONS.USERS_UPDATE, PERMISSIONS.USERS_DELETE]}>
                        <div className="flex gap-2">
                          <button
                            onClick={() => navigate(`/users/${user.id}/edit`)}
                            className="text-blue-600 hover:text-blue-900"
                            title="Editar"
                          >
                            <Edit size={18} />
                          </button>
                          {user.isActive ? (
                            <button
                              onClick={() => handleDeactivate(user.id, `${user.firstName} ${user.lastName}`)}
                              className="text-red-600 hover:text-red-900"
                              title="Desactivar"
                            >
                              <Lock size={18} />
                            </button>
                          ) : (
                            <button
                              onClick={() => handleActivate(user.id, `${user.firstName} ${user.lastName}`)}
                              className="text-green-600 hover:text-green-900"
                              title="Activar"
                            >
                              <Unlock size={18} />
                            </button>
                          )}
                        </div>
                      </PermissionGuard>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Total */}
        {users && users.length > 0 && (
          <div className="text-sm text-slate-600">
            Total: {users.length} usuario(s)
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}

export default UsersListPage
