import React, { useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from 'yup'
import { ArrowLeft, UserCog, AlertCircle } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import Button from '@shared/components/Button'
import {
  useCreateUserMutation,
  useUpdateUserMutation,
  useGetUserByIdQuery,
} from '../services/usersApi'
import { useGetCompaniesQuery } from '@features/companies/services/companiesApi'
import type { CreateUserRequest, UpdateUserRequest } from '@shared/types'

const userValidationSchema = Yup.object({
  email: Yup.string()
    .email('Email inválido')
    .required('El email es obligatorio'),
  password: Yup.string()
    .when('$isEdit', {
      is: false,
      then: (schema) => schema
        .required('La contraseña es obligatoria')
        .min(6, 'La contraseña debe tener al menos 6 caracteres'),
    }),
  firstName: Yup.string()
    .required('El nombre es obligatorio')
    .min(2, 'El nombre debe tener al menos 2 caracteres'),
  lastName: Yup.string()
    .required('El apellido es obligatorio')
    .min(2, 'El apellido debe tener al menos 2 caracteres'),
  phoneNumber: Yup.string()
    .nullable(),
  role: Yup.string()
    .required('El rol es obligatorio')
    .oneOf(['ADMIN', 'EMPRESA', 'CLIENTE'], 'Rol inválido'),
  companyId: Yup.string()
    .nullable(),
})

const UserFormPage: React.FC = () => {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const isEdit = Boolean(id)
  const [error, setError] = useState<string | null>(null)

  const { data: user, isLoading: isLoadingUser } = useGetUserByIdQuery(id!, {
    skip: !isEdit,
  })
  const { data: companies = [], isLoading: isLoadingCompanies } = useGetCompaniesQuery({ onlyActive: true })
  const [createUser, { isLoading: isCreating }] = useCreateUserMutation()
  const [updateUser, { isLoading: isUpdating }] = useUpdateUserMutation()

  const handleSubmit = async (values: any) => {
    try {
      setError(null)
      if (isEdit && id) {
        const updateData: UpdateUserRequest = {
          firstName: values.firstName,
          lastName: values.lastName,
          phoneNumber: values.phoneNumber || undefined,
          role: values.role,
          companyId: values.companyId || undefined,
        }
        await updateUser({ id, data: updateData }).unwrap()
      } else {
        const createData: CreateUserRequest = {
          email: values.email,
          password: values.password,
          firstName: values.firstName,
          lastName: values.lastName,
          phoneNumber: values.phoneNumber || undefined,
          role: values.role,
          companyId: values.companyId || undefined,
        }
        await createUser(createData).unwrap()
      }
      navigate('/users')
    } catch (err: any) {
      console.error('Error:', err)
      setError(err?.data?.message || 'Error al guardar el usuario')
    }
  }

  const handleCancel = () => {
    navigate('/users')
  }

  const initialValues = user && isEdit
    ? {
      email: user.email,
      password: '',
      firstName: user.firstName,
      lastName: user.lastName,
      phoneNumber: user.phoneNumber || '',
      role: user.role,
      companyId: user.companyId || '',
    }
    : {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      phoneNumber: '',
      role: 'CLIENTE',
      companyId: '',
    }

  if (isEdit && isLoadingUser) {
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
              {isEdit ? 'Editar Usuario' : 'Nuevo Usuario'}
            </h1>
            <p className="text-gray-600">
              {isEdit
                ? 'Actualiza la información del usuario'
                : 'Completa los datos para crear un nuevo usuario'}
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
            validationSchema={userValidationSchema}
            onSubmit={handleSubmit}
            enableReinitialize
            context={{ isEdit }}
          >
            {({ errors, touched, isSubmitting }) => (
              <Form className="space-y-6">
                {/* Email - Solo lectura en edición */}
                <div>
                  <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                    Email *
                  </label>
                  <Field
                    id="email"
                    name="email"
                    type="email"
                    disabled={isEdit}
                    placeholder="usuario@ejemplo.com"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${isEdit ? 'bg-gray-100 cursor-not-allowed' : ''
                      } ${errors.email && touched.email ? 'border-red-500' : 'border-gray-300'}`}
                  />
                  <ErrorMessage name="email" component="div" className="text-red-500 text-sm mt-1" />
                </div>

                {/* Contraseña - Solo en creación */}
                {!isEdit && (
                  <div>
                    <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                      Contraseña *
                    </label>
                    <Field
                      id="password"
                      name="password"
                      type="password"
                      placeholder="Mínimo 6 caracteres"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.password && touched.password ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    <ErrorMessage name="password" component="div" className="text-red-500 text-sm mt-1" />
                  </div>
                )}

                {/* Nombre y Apellido */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 mb-2">
                      Nombre *
                    </label>
                    <Field
                      id="firstName"
                      name="firstName"
                      type="text"
                      placeholder="Ej: Juan"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.firstName && touched.firstName ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    <ErrorMessage name="firstName" component="div" className="text-red-500 text-sm mt-1" />
                  </div>

                  <div>
                    <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 mb-2">
                      Apellido *
                    </label>
                    <Field
                      id="lastName"
                      name="lastName"
                      type="text"
                      placeholder="Ej: Pérez"
                      className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.lastName && touched.lastName ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    <ErrorMessage name="lastName" component="div" className="text-red-500 text-sm mt-1" />
                  </div>
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

                {/* Rol */}
                <div>
                  <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-2">
                    Rol *
                  </label>
                  <Field
                    as="select"
                    id="role"
                    name="role"
                    className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 ${errors.role && touched.role ? 'border-red-500' : 'border-gray-300'
                      }`}
                  >
                    <option value="CLIENTE">Cliente</option>
                    <option value="EMPRESA">Empresa</option>
                    <option value="ADMIN">Administrador</option>
                  </Field>
                  <ErrorMessage name="role" component="div" className="text-red-500 text-sm mt-1" />
                  <p className="text-gray-500 text-xs mt-1">
                    Define los permisos del usuario en el sistema
                  </p>
                </div>

                {/* Empresa */}
                <div>
                  <label htmlFor="companyId" className="block text-sm font-medium text-gray-700 mb-2">
                    Empresa
                  </label>
                  <Field
                    as="select"
                    id="companyId"
                    name="companyId"
                    disabled={isLoadingCompanies}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="">Sin asignar</option>
                    {companies.map((company) => (
                      <option key={company.id} value={company.id}>
                        {company.name}
                      </option>
                    ))}
                  </Field>
                  <p className="text-gray-500 text-xs mt-1">
                    Asigna el usuario a una empresa para establecer el tenantId
                  </p>
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
                    <UserCog size={20} />
                    {isCreating || isUpdating || isSubmitting ? (
                      <span>Guardando...</span>
                    ) : (
                      <span>{isEdit ? 'Actualizar Usuario' : 'Crear Usuario'}</span>
                    )}
                  </Button>
                </div>
              </Form>
            )}
          </Formik>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default UserFormPage
