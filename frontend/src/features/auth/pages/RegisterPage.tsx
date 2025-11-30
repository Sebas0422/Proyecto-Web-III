import React from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import type { FormikHelpers } from 'formik'
import * as Yup from 'yup'
import { useRegisterMutation } from '../services/authApi'
import { useNavigate, Link } from 'react-router-dom'
import Button from '@shared/components/Button'
import Input from '@shared/components/Input'
import { UserRole, type RegisterFormValues, type ApiError } from '../types/auth.types'

const RegisterSchema = Yup.object().shape({
  firstName: Yup.string().required('El nombre es requerido'),
  lastName: Yup.string().required('El apellido es requerido'),
  email: Yup.string().email('Email inválido').required('El email es requerido'),
  password: Yup.string().min(6, 'Mínimo 6 caracteres').required('La contraseña es requerida'),
  phoneNumber: Yup.string(),
  role: Yup.string().oneOf(Object.values(UserRole), 'Rol inválido').required('El rol es requerido'),
})

const RegisterPage: React.FC = () => {
  const [register, { isLoading }] = useRegisterMutation()
  const navigate = useNavigate()

  const handleSubmit = async (
    values: RegisterFormValues,
    { setSubmitting, setFieldError }: FormikHelpers<RegisterFormValues>,
  ) => {
    try {
      await register(values).unwrap()
      // Después de registrarse exitosamente, redirigir al login
      navigate('/login', { state: { message: 'Registro exitoso. Por favor inicia sesión.' } })
    } catch (err: unknown) {
      const extractMessage = (error: unknown): string => {
        if (typeof error !== 'object' || error === null) return 'Error en el registro'
        const apiError = error as ApiError
        return apiError.data?.message ?? apiError.message ?? apiError.error ?? 'Error en el registro'
      }
      setFieldError('email', extractMessage(err))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 p-4">
      <div className="w-full max-w-2xl">
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-amber-600 rounded-2xl mb-4 shadow-lg">
            <svg className="w-9 h-9 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z"
              />
            </svg>
          </div>
          <h2 className="text-4xl font-bold text-gray-900 mb-2">Crear cuenta</h2>
          <p className="text-gray-600">Completa tus datos para comenzar</p>
        </div>

        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <Formik
            initialValues={{
              firstName: '',
              lastName: '',
              email: '',
              password: '',
              phoneNumber: '',
              role: UserRole.CLIENTE
            }}
            validationSchema={RegisterSchema}
            onSubmit={handleSubmit}
          >
            {({ isSubmitting }) => (
              <Form className="space-y-5">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input name="firstName" label="Nombre" type="text" placeholder="Ej: Juan" />
                  <Input name="lastName" label="Apellido" type="text" placeholder="Ej: Pérez" />
                </div>

                <Input name="email" label="Correo electrónico" type="email" placeholder="tu@email.com" />

                <Input name="password" label="Contraseña" type="password" placeholder="Mínimo 6 caracteres" />

                <Input
                  name="phoneNumber"
                  label="Teléfono (opcional)"
                  type="tel"
                  placeholder="+591 78945612"
                />

                <div className="mb-4">
                  <label htmlFor="role" className="block text-sm font-semibold text-gray-700 mb-2">
                    Tipo de cuenta
                  </label>
                  <Field
                    as="select"
                    id="role"
                    name="role"
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-amber-500 focus:border-amber-500 transition-all outline-none text-gray-900 bg-white"
                  >
                    <option value={UserRole.CLIENTE}>Cliente</option>
                    <option value={UserRole.EMPRESA}>Empresa</option>
                  </Field>
                  <ErrorMessage name="role" component="div" className="mt-1 text-sm text-red-600" />
                </div>

                <div className="bg-amber-50 border border-amber-200 rounded-lg p-4">
                  <label className="flex items-start cursor-pointer group">
                    <input
                      type="checkbox"
                      required
                      className="mt-1 mr-3 w-4 h-4 rounded border-gray-300 text-amber-600 focus:ring-2 focus:ring-amber-500"
                    />
                    <span className="text-sm text-gray-700 group-hover:text-gray-900 transition-colors">
                      Acepto los{' '}
                      <a href="#" className="text-amber-600 hover:text-amber-700 font-medium underline">
                        términos y condiciones
                      </a>{' '}
                      y la{' '}
                      <a href="#" className="text-amber-600 hover:text-amber-700 font-medium underline">
                        política de privacidad
                      </a>
                    </span>
                  </label>
                </div>

                <Button
                  type="submit"
                  variant="secondary"
                  size="lg"
                  isLoading={isSubmitting || isLoading}
                  className="w-full"
                >
                  Crear cuenta
                </Button>
              </Form>
            )}
          </Formik>

          <p className="mt-6 text-center text-sm text-gray-600">
            ¿Ya tienes una cuenta?{' '}
            <Link to="/login" className="text-amber-600 hover:text-amber-700 font-semibold transition-colors">
              Inicia sesión
            </Link>
          </p>
        </div>

        <p className="text-center text-sm text-gray-500 mt-6">
          © 2025 EstateHub. Todos los derechos reservados.
        </p>
      </div>
    </div>
  )
}

export default RegisterPage
