import { Formik, Form, Field, ErrorMessage } from 'formik'
import * as Yup from 'yup'
import type { CreateProjectRequest, UpdateProjectRequest } from '@shared/types'
import Button from '@shared/components/Button'

interface ProjectFormProps {
  initialValues?: UpdateProjectRequest & { nombre: string; ubicacion: string; fechaInicio: string }
  onSubmit: (values: CreateProjectRequest | UpdateProjectRequest) => void
  onCancel: () => void
  isLoading?: boolean
  isEdit?: boolean
}

const projectValidationSchema = Yup.object({
  nombre: Yup.string()
    .required('El nombre es obligatorio')
    .min(3, 'El nombre debe tener al menos 3 caracteres')
    .max(200, 'El nombre no puede exceder 200 caracteres'),
  descripcion: Yup.string()
    .max(1000, 'La descripción no puede exceder 1000 caracteres'),
  ubicacion: Yup.string()
    .required('La ubicación es obligatoria')
    .max(500, 'La ubicación no puede exceder 500 caracteres'),
  fechaInicio: Yup.date()
    .required('La fecha de inicio es obligatoria')
    .typeError('Fecha inválida'),
  fechaEstimadaFinalizacion: Yup.date()
    .nullable()
    .min(Yup.ref('fechaInicio'), 'La fecha de finalización debe ser posterior a la fecha de inicio')
    .typeError('Fecha inválida'),
})

export default function ProjectForm({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false,
  isEdit = false,
}: ProjectFormProps) {
  const today = new Date().toISOString().split('T')[0]

  const defaultValues: CreateProjectRequest = {
    nombre: '',
    descripcion: '',
    ubicacion: '',
    fechaInicio: today,
    fechaEstimadaFinalizacion: '',
  }

  return (
    <Formik
      initialValues={initialValues || defaultValues}
      validationSchema={projectValidationSchema}
      onSubmit={onSubmit}
      enableReinitialize
    >
      {({ errors, touched, isSubmitting }) => (
        <Form className="space-y-6">
          <div>
            <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-2">
              Nombre del Proyecto *
            </label>
            <Field
              id="nombre"
              name="nombre"
              type="text"
              placeholder="Ej: Residencial Los Álamos"
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.nombre && touched.nombre ? 'border-red-500' : 'border-gray-300'
                }`}
            />
            <ErrorMessage name="nombre" component="div" className="text-red-500 text-sm mt-1" />
          </div>

          <div>
            <label htmlFor="ubicacion" className="block text-sm font-medium text-gray-700 mb-2">
              Ubicación *
            </label>
            <Field
              id="ubicacion"
              name="ubicacion"
              type="text"
              placeholder="Ej: Av. Cristo Redentor, Santa Cruz"
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.ubicacion && touched.ubicacion ? 'border-red-500' : 'border-gray-300'
                }`}
            />
            <ErrorMessage name="ubicacion" component="div" className="text-red-500 text-sm mt-1" />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label htmlFor="fechaInicio" className="block text-sm font-medium text-gray-700 mb-2">
                Fecha de Inicio *
              </label>
              <Field
                id="fechaInicio"
                name="fechaInicio"
                type="date"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.fechaInicio && touched.fechaInicio ? 'border-red-500' : 'border-gray-300'
                  }`}
              />
              <ErrorMessage name="fechaInicio" component="div" className="text-red-500 text-sm mt-1" />
            </div>

            <div>
              <label htmlFor="fechaEstimadaFinalizacion" className="block text-sm font-medium text-gray-700 mb-2">
                Fecha Estimada de Finalización
              </label>
              <Field
                id="fechaEstimadaFinalizacion"
                name="fechaEstimadaFinalizacion"
                type="date"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.fechaEstimadaFinalizacion && touched.fechaEstimadaFinalizacion ? 'border-red-500' : 'border-gray-300'
                  }`}
              />
              <ErrorMessage name="fechaEstimadaFinalizacion" component="div" className="text-red-500 text-sm mt-1" />
            </div>
          </div>

          <div>
            <label htmlFor="descripcion" className="block text-sm font-medium text-gray-700 mb-2">
              Descripción
            </label>
            <Field
              as="textarea"
              id="descripcion"
              name="descripcion"
              rows={4}
              placeholder="Describe las características principales del proyecto..."
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 resize-none ${errors.descripcion && touched.descripcion ? 'border-red-500' : 'border-gray-300'
                }`}
            />
            <ErrorMessage name="descripcion" component="div" className="text-red-500 text-sm mt-1" />
            <p className="text-gray-500 text-xs mt-1">Máximo 1000 caracteres</p>
          </div>

          <div className="flex items-center justify-end gap-4 pt-4 border-t border-gray-200">
            <Button
              type="button"
              onClick={onCancel}
              disabled={isLoading || isSubmitting}
              className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50"
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              disabled={isLoading || isSubmitting}
              className="px-6 py-2 bg-amber-600 hover:bg-amber-700 text-white rounded-lg transition-colors disabled:opacity-50 flex items-center gap-2"
            >
              {isLoading || isSubmitting ? (
                <>
                  <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                  <span>{isEdit ? 'Actualizando...' : 'Creando...'}</span>
                </>
              ) : (
                <span>{isEdit ? 'Actualizar Proyecto' : 'Crear Proyecto'}</span>
              )}
            </Button>
          </div>
        </Form>
      )}
    </Formik>
  )
}
