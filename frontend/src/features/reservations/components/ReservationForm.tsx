import React, { useState, useEffect } from 'react'
import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from 'yup'
import type { CreateReservationRequest } from '@shared/types'
import { useGetProjectsQuery } from '@features/projects/services/projectsApi'
import { useGetLotsQuery } from '@features/lots/services/lotsApi'

interface ReservationFormProps {
  initialValues?: Partial<CreateReservationRequest>
  onSubmit: (values: CreateReservationRequest) => void | Promise<void>
  isLoading?: boolean
}

const validationSchema = Yup.object({
  lotId: Yup.string().required('El lote es requerido'),
  projectId: Yup.string().required('El proyecto es requerido'),
  customerName: Yup.string()
    .required('El nombre es requerido')
    .min(3, 'El nombre debe tener al menos 3 caracteres'),
  customerEmail: Yup.string()
    .required('El correo es requerido')
    .email('Correo electrónico inválido'),
  customerPhone: Yup.string()
    .required('El teléfono es requerido')
    .min(8, 'El teléfono debe tener al menos 8 dígitos'),
  customerDocument: Yup.string()
    .required('El documento es requerido')
    .min(5, 'El documento debe tener al menos 5 caracteres'),
  reservationAmount: Yup.number()
    .required('El monto es requerido')
    .positive('El monto debe ser positivo')
    .min(1, 'El monto debe ser mayor a 0'),
  expirationDays: Yup.number()
    .required('Los días de expiración son requeridos')
    .integer('Debe ser un número entero')
    .min(1, 'Mínimo 1 día')
    .max(90, 'Máximo 90 días'),
  notes: Yup.string(),
})

const defaultValues: CreateReservationRequest = {
  lotId: '',
  projectId: '',
  customerName: '',
  customerEmail: '',
  customerPhone: '',
  customerDocument: '',
  reservationAmount: 0,
  expirationDays: 7,
  notes: '',
}

const ReservationForm: React.FC<ReservationFormProps> = ({
  initialValues,
  onSubmit,
  isLoading = false,
}) => {
  const [selectedProjectId, setSelectedProjectId] = useState<string>('')

  const { data: projects = [], isLoading: loadingProjects } = useGetProjectsQuery({ activo: true })
  const { data: lots = [], isLoading: loadingLots } = useGetLotsQuery(
    { proyectoId: selectedProjectId },
    { skip: !selectedProjectId }
  )

  return (
    <Formik
      initialValues={{ ...defaultValues, ...initialValues }}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
      enableReinitialize
    >
      {({ isSubmitting }) => (
        <Form className="space-y-6">
          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold text-slate-800 mb-4">
              Información del Lote
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label
                  htmlFor="projectId"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Proyecto *
                </label>
                <Field name="projectId">
                  {({ field, form }: any) => (
                    <select
                      {...field}
                      id="projectId"
                      className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 bg-white"
                      onChange={(e) => {
                        const value = e.target.value
                        form.setFieldValue('projectId', value)
                        form.setFieldValue('lotId', '') // Resetear lote al cambiar proyecto
                        setSelectedProjectId(value)
                      }}
                      disabled={loadingProjects}
                    >
                      <option value="">Seleccione un proyecto</option>
                      {projects.map((project) => (
                        <option key={project.id} value={project.id}>
                          {project.nombre}
                        </option>
                      ))}
                    </select>
                  )}
                </Field>
                <ErrorMessage
                  name="projectId"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>

              <div>
                <label
                  htmlFor="lotId"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Lote *
                </label>
                <Field name="lotId">
                  {({ field }: any) => (
                    <select
                      {...field}
                      id="lotId"
                      className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 bg-white"
                      disabled={!selectedProjectId || loadingLots}
                    >
                      <option value="">
                        {!selectedProjectId
                          ? 'Primero seleccione un proyecto'
                          : loadingLots
                            ? 'Cargando lotes...'
                            : 'Seleccione un lote'}
                      </option>
                      {lots
                        .filter((lot) => lot.estado === 'DISPONIBLE')
                        .map((lot) => (
                          <option key={lot.id} value={lot.id}>
                            Lote {lot.numeroLote} - Manzana {lot.manzana} ({lot.areaCalculada.toFixed(2)} m²)
                          </option>
                        ))}
                    </select>
                  )}
                </Field>
                <ErrorMessage
                  name="lotId"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold text-slate-800 mb-4">
              Datos del Cliente
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label
                  htmlFor="customerName"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Nombre Completo *
                </label>
                <Field
                  id="customerName"
                  name="customerName"
                  type="text"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: Juan Pérez"
                />
                <ErrorMessage
                  name="customerName"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>

              <div>
                <label
                  htmlFor="customerDocument"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Documento *
                </label>
                <Field
                  id="customerDocument"
                  name="customerDocument"
                  type="text"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: 12345678"
                />
                <ErrorMessage
                  name="customerDocument"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>

              <div>
                <label
                  htmlFor="customerEmail"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Email *
                </label>
                <Field
                  id="customerEmail"
                  name="customerEmail"
                  type="email"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: cliente@ejemplo.com"
                />
                <ErrorMessage
                  name="customerEmail"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>

              <div>
                <label
                  htmlFor="customerPhone"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Teléfono *
                </label>
                <Field
                  id="customerPhone"
                  name="customerPhone"
                  type="tel"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: 78945612"
                />
                <ErrorMessage
                  name="customerPhone"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>
            </div>
          </div>

          <div className="bg-white rounded-lg shadow p-6">
            <h3 className="text-lg font-semibold text-slate-800 mb-4">
              Detalles de la Reserva
            </h3>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div>
                <label
                  htmlFor="reservationAmount"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Monto de Reserva (Bs) *
                </label>
                <Field
                  id="reservationAmount"
                  name="reservationAmount"
                  type="number"
                  step="0.01"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: 5000"
                />
                <ErrorMessage
                  name="reservationAmount"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>

              <div>
                <label
                  htmlFor="expirationDays"
                  className="block text-sm font-medium text-slate-700 mb-1"
                >
                  Días de Expiración *
                </label>
                <Field
                  id="expirationDays"
                  name="expirationDays"
                  type="number"
                  className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                  placeholder="Ej: 7"
                />
                <ErrorMessage
                  name="expirationDays"
                  component="div"
                  className="text-red-500 text-sm mt-1"
                />
              </div>
            </div>

            <div className="mt-4">
              <label
                htmlFor="notes"
                className="block text-sm font-medium text-slate-700 mb-1"
              >
                Notas (Opcional)
              </label>
              <Field
                id="notes"
                name="notes"
                as="textarea"
                rows={3}
                className="w-full px-3 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500"
                placeholder="Información adicional sobre la reserva..."
              />
              <ErrorMessage
                name="notes"
                component="div"
                className="text-red-500 text-sm mt-1"
              />
            </div>
          </div>

          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={() => window.history.back()}
              className="px-4 py-2 border border-slate-300 text-slate-700 rounded-lg hover:bg-slate-50 transition-colors"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={isSubmitting || isLoading}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-slate-400 disabled:cursor-not-allowed transition-colors"
            >
              {isSubmitting || isLoading ? 'Guardando...' : 'Crear Reserva'}
            </button>
          </div>
        </Form>
      )}
    </Formik>
  )
}

export default ReservationForm
