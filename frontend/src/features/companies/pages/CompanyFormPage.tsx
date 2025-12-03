import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from 'yup'
import { ArrowLeft, Building2, AlertCircle, UserPlus, Trash2, UserCog } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import {
  useCreateCompanyMutation,
  useUpdateCompanyMutation,
  useGetCompanyByIdQuery,
  useAssignUserToCompanyMutation,
} from '../services/companiesApi'
import { useGetUsersQuery } from '@features/users/services/usersApi'
import type { CreateCompanyRequest, UpdateCompanyRequest } from '@shared/types'
import Button from '@shared/components/Button'

const companyValidationSchema = Yup.object({
  name: Yup.string()
    .required('El nombre es obligatorio')
    .min(3, 'El nombre debe tener al menos 3 caracteres')
    .max(200, 'El nombre no puede exceder 200 caracteres'),
  ruc: Yup.string()
    .when('$isEdit', {
      is: false,
      then: (schema) => schema.required('El RUC es obligatorio'),
    }),
  email: Yup.string()
    .email('Email inválido')
    .nullable(),
  phoneNumber: Yup.string()
    .nullable(),
  address: Yup.string()
    .max(500, 'La dirección no puede exceder 500 caracteres')
    .nullable(),
  logoUrl: Yup.string()
    .url('URL inválida')
    .nullable(),
})

const CompanyFormPage: React.FC = () => {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const isEdit = Boolean(id)
  const [error, setError] = useState<string | null>(null)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [selectedUserId, setSelectedUserId] = useState('')
  const [selectedRole, setSelectedRole] = useState('EMPRESA')

  const { data: company, isLoading: isLoadingCompany } = useGetCompanyByIdQuery(id!, {
    skip: !isEdit,
  })
  const { data: allUsers = [], isLoading: isLoadingUsers } = useGetUsersQuery({ isActive: true })
  const [createCompany, { isLoading: isCreating }] = useCreateCompanyMutation()
  const [updateCompany, { isLoading: isUpdating }] = useUpdateCompanyMutation()
  const [assignUser, { isLoading: isAssigning }] = useAssignUserToCompanyMutation()

  const handleSubmit = async (values: any) => {
    try {
      setError(null)
      if (isEdit && id) {
        const updateData: UpdateCompanyRequest = {
          name: values.name,
          address: values.address || undefined,
          phoneNumber: values.phoneNumber || undefined,
          email: values.email || undefined,
          logoUrl: values.logoUrl || undefined,
        }
        await updateCompany({ id, data: updateData }).unwrap()
      } else {
        const createData: CreateCompanyRequest = {
          name: values.name,
          ruc: values.ruc,
          address: values.address || undefined,
          phoneNumber: values.phoneNumber || undefined,
          email: values.email || undefined,
        }
        await createCompany(createData).unwrap()
      }
      navigate('/companies')
    } catch (err: any) {
      console.error('Error:', err)
      setError(err?.data?.message || 'Error al guardar la empresa')
    }
  }

  const handleCancel = () => {
    navigate('/companies')
  }

  const handleAssignUser = async () => {
    if (!selectedUserId || !id) return
    try {
      await assignUser({
        userId: selectedUserId,
        companyId: id,
        role: selectedRole,
      }).unwrap()
      setShowAssignModal(false)
      setSelectedUserId('')
      setSelectedRole('EMPRESA')
    } catch (err: any) {
      console.error('Error al asignar usuario:', err)
      setError(err?.data?.message || 'Error al asignar usuario')
    }
  }

  // Usuarios ya asignados a esta empresa
  const assignedUsers = allUsers.filter(user => user.companyId === id)

  // Usuarios disponibles para asignar (no tienen empresa asignada)
  const availableUsers = allUsers.filter(user => !user.companyId)

  const initialValues = company && isEdit
    ? {
      name: company.name,
      ruc: company.ruc,
      address: company.address || '',
      phoneNumber: company.phoneNumber || '',
      email: company.email || '',
      logoUrl: company.logoUrl || '',
    }
    : {
      name: '',
      ruc: '',
      address: '',
      phoneNumber: '',
      email: '',
      logoUrl: '',
    }

  if (isEdit && isLoadingCompany) {
    return (
      <DashboardLayout>
        <div className="flex justify-center items-center py-16">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
        </div>
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <div className="max-w-3xl mx-auto p-6">
        <button
          onClick={handleCancel}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6 transition-colors"
        >
          <ArrowLeft size={20} />
          <span>Volver</span>
        </button>

        <div className="bg-white rounded-lg shadow-md p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              {isEdit ? 'Editar Empresa' : 'Nueva Empresa'}
            </h1>
            <p className="text-gray-600">
              {isEdit
                ? 'Actualiza la información de la empresa'
                : 'Completa los datos para crear una nueva empresa'}
            </p>
          </div>

          {error && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start gap-3">
              <AlertCircle size={20} className="text-red-600 flex-shrink-0 mt-0.5" />
              <div>
                <p className="text-red-800 font-medium">Error al guardar</p>
                <p className="text-red-600 text-sm">{error}</p>
              </div>
            </div>
          )}

          <Formik
            initialValues={initialValues}
            validationSchema={companyValidationSchema}
            onSubmit={handleSubmit}
            enableReinitialize
            context={{ isEdit }}
          >
            {({ errors, touched, isSubmitting }) => (
              <Form className="space-y-6">
                {/* Nombre */}
                <div>
                  <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-2">
                    Nombre de la Empresa *
                  </label>
                  <Field
                    id="name"
                    name="name"
                    type="text"
                    placeholder="Ej: Constructora ABC"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.name && touched.name ? 'border-red-500' : 'border-gray-300'
                      }`}
                  />
                  <ErrorMessage name="name" component="div" className="text-red-500 text-sm mt-1" />
                </div>

                {/* RUC - Solo en creación */}
                {!isEdit && (
                  <div>
                    <label htmlFor="ruc" className="block text-sm font-medium text-gray-700 mb-2">
                      RUC *
                    </label>
                    <Field
                      id="ruc"
                      name="ruc"
                      type="text"
                      placeholder="Ej: 1234567890"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.ruc && touched.ruc ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    <ErrorMessage name="ruc" component="div" className="text-red-500 text-sm mt-1" />
                  </div>
                )}

                {/* Email */}
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                    Email
                  </label>
                  <Field
                    id="email"
                    name="email"
                    type="email"
                    placeholder="empresa@ejemplo.com"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.email && touched.email ? 'border-red-500' : 'border-gray-300'
                      }`}
                  />
                  <ErrorMessage name="email" component="div" className="text-red-500 text-sm mt-1" />
                </div>

                {/* Teléfono */}
                <div>
                  <label htmlFor="phoneNumber" className="block text-sm font-medium text-gray-700 mb-2">
                    Teléfono
                  </label>
                  <Field
                    id="phoneNumber"
                    name="phoneNumber"
                    type="tel"
                    placeholder="Ej: +591 70123456"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  />
                </div>

                {/* Dirección */}
                <div>
                  <label htmlFor="address" className="block text-sm font-medium text-gray-700 mb-2">
                    Dirección
                  </label>
                  <Field
                    as="textarea"
                    id="address"
                    name="address"
                    rows={3}
                    placeholder="Dirección completa"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none ${errors.address && touched.address ? 'border-red-500' : 'border-gray-300'
                      }`}
                  />
                  <ErrorMessage name="address" component="div" className="text-red-500 text-sm mt-1" />
                  <p className="text-gray-500 text-xs mt-1">Máximo 500 caracteres</p>
                </div>

                {/* Logo URL */}
                <div>
                  <label htmlFor="logoUrl" className="block text-sm font-medium text-gray-700 mb-2">
                    URL del Logo
                  </label>
                  <Field
                    id="logoUrl"
                    name="logoUrl"
                    type="url"
                    placeholder="https://ejemplo.com/logo.png"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.logoUrl && touched.logoUrl ? 'border-red-500' : 'border-gray-300'
                      }`}
                  />
                  <ErrorMessage name="logoUrl" component="div" className="text-red-500 text-sm mt-1" />
                  <p className="text-gray-500 text-xs mt-1">URL de la imagen del logo de la empresa</p>
                </div>

                {/* Botones */}
                <div className="flex items-center justify-end gap-4 pt-4 border-t border-gray-200">
                  <Button
                    type="button"
                    onClick={handleCancel}
                    disabled={isCreating || isUpdating || isSubmitting}
                    className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50"
                  >
                    Cancelar
                  </Button>
                  <Button
                    type="submit"
                    disabled={isCreating || isUpdating || isSubmitting}
                    className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors disabled:opacity-50 flex items-center gap-2"
                  >
                    <Building2 size={20} />
                    {isCreating || isUpdating || isSubmitting ? (
                      <span>Guardando...</span>
                    ) : (
                      <span>{isEdit ? 'Actualizar Empresa' : 'Crear Empresa'}</span>
                    )}
                  </Button>
                </div>
              </Form>
            )}
          </Formik>
        </div>

        {/* Sección de Usuarios Asignados - Solo en modo edición */}
        {isEdit && (
          <div className="bg-white rounded-lg shadow-md p-8 mt-6">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h2 className="text-2xl font-bold text-gray-900 mb-1">Usuarios Asignados</h2>
                <p className="text-gray-600 text-sm">
                  Gestiona los usuarios que pertenecen a esta empresa
                </p>
              </div>
              <Button
                onClick={() => setShowAssignModal(true)}
                disabled={availableUsers.length === 0}
                className="flex items-center gap-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                <UserPlus size={18} />
                Asignar Usuario
              </Button>
            </div>

            {/* Lista de usuarios asignados */}
            {isLoadingUsers ? (
              <div className="text-center py-8">
                <p className="text-gray-600">Cargando usuarios...</p>
              </div>
            ) : assignedUsers.length === 0 ? (
              <div className="text-center py-12 bg-gray-50 rounded-lg">
                <UserCog size={48} className="mx-auto text-gray-300 mb-3" />
                <p className="text-gray-600 mb-2">No hay usuarios asignados</p>
                <p className="text-gray-500 text-sm">
                  {availableUsers.length > 0
                    ? 'Haz clic en "Asignar Usuario" para comenzar'
                    : 'No hay usuarios disponibles para asignar'}
                </p>
              </div>
            ) : (
              <div className="space-y-3">
                {assignedUsers.map(user => (
                  <div
                    key={user.id}
                    className="flex items-center justify-between p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition-colors"
                  >
                    <div className="flex items-center gap-4">
                      <div className="w-10 h-10 bg-blue-100 rounded-full flex items-center justify-center">
                        <UserCog className="text-blue-600" size={20} />
                      </div>
                      <div>
                        <h3 className="font-medium text-gray-900">
                          {user.firstName} {user.lastName}
                        </h3>
                        <div className="flex items-center gap-3 text-sm text-gray-600">
                          <span>{user.email}</span>
                          <span className="px-2 py-0.5 bg-blue-100 text-blue-700 rounded text-xs font-medium">
                            {user.role}
                          </span>
                        </div>
                      </div>
                    </div>
                    <button
                      onClick={() => {
                        if (window.confirm('¿Desasignar este usuario de la empresa?')) {
                          // TODO: Implementar desasignación
                          console.log('Desasignar usuario:', user.id)
                        }
                      }}
                      className="text-red-600 hover:text-red-800 p-2 hover:bg-red-50 rounded transition-colors"
                      title="Desasignar usuario"
                    >
                      <Trash2 size={18} />
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* Modal para asignar usuario */}
        {showAssignModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-lg shadow-xl max-w-md w-full p-6">
              <h3 className="text-xl font-bold text-gray-900 mb-4">Asignar Usuario a Empresa</h3>

              <div className="space-y-4 mb-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Usuario *
                  </label>
                  <select
                    value={selectedUserId}
                    onChange={(e) => setSelectedUserId(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Seleccionar usuario</option>
                    {availableUsers.map(user => (
                      <option key={user.id} value={user.id}>
                        {user.firstName} {user.lastName} ({user.email})
                      </option>
                    ))}
                  </select>
                  <p className="text-gray-500 text-xs mt-1">
                    Solo se muestran usuarios sin empresa asignada
                  </p>
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">
                    Rol *
                  </label>
                  <select
                    value={selectedRole}
                    onChange={(e) => setSelectedRole(e.target.value)}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="EMPRESA">Empresa</option>
                    <option value="CLIENTE">Cliente</option>
                    <option value="ADMIN">Administrador</option>
                  </select>
                </div>
              </div>

              <div className="flex justify-end gap-3">
                <Button
                  onClick={() => {
                    setShowAssignModal(false)
                    setSelectedUserId('')
                    setSelectedRole('EMPRESA')
                  }}
                  disabled={isAssigning}
                  className="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
                >
                  Cancelar
                </Button>
                <Button
                  onClick={handleAssignUser}
                  disabled={!selectedUserId || isAssigning}
                  className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors disabled:opacity-50"
                >
                  {isAssigning ? 'Asignando...' : 'Asignar'}
                </Button>
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}

export default CompanyFormPage
