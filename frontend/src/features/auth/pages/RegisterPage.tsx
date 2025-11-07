import React from 'react'
import { Formik, Form } from 'formik'
import type { FormikHelpers } from 'formik'
import * as Yup from 'yup'
import { useRegisterMutation } from '../services/authApi'
import { useAppDispatch } from '@app/store/hooks'
import { setCredentials } from '../model/authSlice'
import { useNavigate, Link } from 'react-router-dom'
import Button from '@shared/components/Button'
import Input from '@shared/components/Input'

interface RegisterValues {
  name: string
  email: string
  password: string
}

const RegisterSchema = Yup.object().shape({
  name: Yup.string().required('El nombre es requerido'),
  email: Yup.string().email('Email inválido').required('El email es requerido'),
  password: Yup.string().min(6, 'Mínimo 6 caracteres').required('La contraseña es requerida'),
})

const RegisterPage: React.FC = () => {
  const [register, { isLoading }] = useRegisterMutation()
  const dispatch = useAppDispatch()
  const navigate = useNavigate()

  const handleSubmit = async (
    values: RegisterValues,
    { setSubmitting, setFieldError }: FormikHelpers<RegisterValues>,
  ) => {
    try {
      const res = await register(values).unwrap()
      dispatch(setCredentials({ token: res.token, user: res.user }))
      navigate('/dashboard')
    } catch (err: unknown) {
      const extractMessage = (input: unknown): string => {
        if (typeof input !== 'object' || input === null) return 'Error en el registro'
        const obj = input as Record<string, unknown>
        if ('data' in obj && typeof obj.data === 'object' && obj.data !== null) {
          const data = obj.data as Record<string, unknown>
          if ('message' in data && typeof data.message === 'string') return data.message
        }
        if ('message' in obj && typeof obj.message === 'string') return obj.message
        if ('error' in obj && typeof obj.error === 'string') return obj.error
        return 'Error en el registro'
      }
      setFieldError('email', extractMessage(err))
    } finally {
      setSubmitting(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-50 to-gray-100 p-4">
      <div className="w-full max-w-md">
        {/* Header */}
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

        {/* Card */}
        <div className="bg-white rounded-2xl shadow-xl p-8 border border-gray-100">
          <Formik
            initialValues={{ name: '', email: '', password: '' }}
            validationSchema={RegisterSchema}
            onSubmit={handleSubmit}
          >
            {({ isSubmitting }) => (
              <Form className="space-y-5">
                <Input name="name" label="Nombre completo" type="text" placeholder="Ej: Juan Pérez" />

                <Input name="email" label="Correo electrónico" type="email" placeholder="tu@email.com" />

                <Input name="password" label="Contraseña" type="password" placeholder="Mínimo 6 caracteres" />

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

                <div className="relative my-6">
                  <div className="absolute inset-0 flex items-center">
                    <div className="w-full border-t border-gray-200"></div>
                  </div>
                  <div className="relative flex justify-center text-sm">
                    <span className="px-4 bg-white text-gray-500">o regístrate con</span>
                  </div>
                </div>

                <button
                  type="button"
                  className="w-full flex items-center justify-center gap-3 px-4 py-3 border-2 border-gray-300 rounded-lg text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 hover:border-gray-400 focus:outline-none focus:ring-2 focus:ring-amber-500 transition-all active:scale-95"
                >
                  <svg className="w-5 h-5" viewBox="0 0 24 24">
                    <path
                      fill="#4285F4"
                      d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                    />
                    <path
                      fill="#34A853"
                      d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                    />
                    <path
                      fill="#FBBC05"
                      d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                    />
                    <path
                      fill="#EA4335"
                      d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                    />
                  </svg>
                  Continuar con Google
                </button>
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

        {/* Footer */}
        <p className="text-center text-sm text-gray-500 mt-6">
          © 2025 EstateHub. Todos los derechos reservados.
        </p>
      </div>
    </div>
  )
}

export default RegisterPage
