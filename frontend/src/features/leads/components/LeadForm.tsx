import { Formik, Form, Field, ErrorMessage as FormikError } from 'formik'
import * as Yup from 'yup'
import type { CreateLeadRequest } from '@shared/types'

interface LeadFormProps {
  initialValues?: Partial<CreateLeadRequest>
  onSubmit: (values: CreateLeadRequest) => void
  onCancel: () => void
  isLoading?: boolean
  isEdit?: boolean
  preselectedLot?: { id: string; number: string }
  preselectedProject?: { id: string; name: string }
}

const validationSchema = Yup.object({
  customerName: Yup.string()
    .required('El nombre es requerido')
    .min(3, 'Mínimo 3 caracteres')
    .max(200, 'Máximo 200 caracteres'),
  customerEmail: Yup.string()
    .email('Email inválido')
    .max(150, 'Máximo 150 caracteres'),
  customerPhone: Yup.string()
    .required('El teléfono es requerido')
    .min(7, 'Mínimo 7 caracteres')
    .max(50, 'Máximo 50 caracteres'),
  customerDocument: Yup.string().max(50, 'Máximo 50 caracteres'),
  source: Yup.string()
    .required('La fuente es requerida')
    .max(100, 'Máximo 100 caracteres'),
  interestLevel: Yup.string()
    .oneOf(['HIGH', 'MEDIUM', 'LOW'], 'Nivel de interés inválido'),
  notes: Yup.string(),
})

const defaultValues: CreateLeadRequest = {
  customerName: '',
  customerEmail: '',
  customerPhone: '',
  customerDocument: '',
  source: '',
  interestLevel: 'MEDIUM',
  notes: '',
  projectId: undefined,
  lotId: undefined,
}

export default function LeadForm({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false,
  isEdit = false,
  preselectedLot,
  preselectedProject,
}: LeadFormProps) {
  const formValues = {
    ...defaultValues,
    ...initialValues,
    projectId: preselectedProject?.id || initialValues?.projectId,
    lotId: preselectedLot?.id || initialValues?.lotId,
  }

  return (
    <Formik
      initialValues={formValues}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
      enableReinitialize
    >
      {({ errors, touched }) => (
        <Form className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="md:col-span-2">
              <label htmlFor="customerName" className="block text-sm font-medium text-gray-700 mb-2">
                Nombre Completo *
              </label>
              <Field
                id="customerName"
                name="customerName"
                type="text"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.customerName && touched.customerName ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="Ej: Juan Pérez García"
              />
              <FormikError name="customerName" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="customerEmail" className="block text-sm font-medium text-gray-700 mb-2">
                Email
              </label>
              <Field
                id="customerEmail"
                name="customerEmail"
                type="email"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.customerEmail && touched.customerEmail ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="ejemplo@correo.com"
              />
              <FormikError name="customerEmail" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="customerPhone" className="block text-sm font-medium text-gray-700 mb-2">
                Teléfono *
              </label>
              <Field
                id="customerPhone"
                name="customerPhone"
                type="tel"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.customerPhone && touched.customerPhone ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="Ej: 76543210"
              />
              <FormikError name="customerPhone" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="customerDocument" className="block text-sm font-medium text-gray-700 mb-2">
                Documento (CI/NIT)
              </label>
              <Field
                id="customerDocument"
                name="customerDocument"
                type="text"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.customerDocument && touched.customerDocument ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="Ej: 12345678"
              />
              <FormikError name="customerDocument" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="source" className="block text-sm font-medium text-gray-700 mb-2">
                Fuente de Contacto *
              </label>
              <Field
                as="select"
                id="source"
                name="source"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.source && touched.source ? 'border-red-500' : 'border-gray-300'
                  }`}
              >
                <option value="">Seleccionar...</option>
                <option value="Facebook">Facebook</option>
                <option value="Instagram">Instagram</option>
                <option value="WhatsApp">WhatsApp</option>
                <option value="Referido">Referido</option>
                <option value="Walk-in">Visita Directa</option>
                <option value="Llamada">Llamada Telefónica</option>
                <option value="Web">Sitio Web</option>
                <option value="Otro">Otro</option>
              </Field>
              <FormikError name="source" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="interestLevel" className="block text-sm font-medium text-gray-700 mb-2">
                Nivel de Interés
              </label>
              <Field
                as="select"
                id="interestLevel"
                name="interestLevel"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500"
              >
                <option value="HIGH">Alto</option>
                <option value="MEDIUM">Medio</option>
                <option value="LOW">Bajo</option>
              </Field>
            </div>
          </div>

          {(preselectedProject || preselectedLot) && (
            <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
              <h4 className="text-sm font-medium text-blue-900 mb-2">Información Preseleccionada</h4>
              {preselectedProject && (
                <p className="text-sm text-blue-700">Proyecto: <span className="font-semibold">{preselectedProject.name}</span></p>
              )}
              {preselectedLot && (
                <p className="text-sm text-blue-700">Lote: <span className="font-semibold">{preselectedLot.number}</span></p>
              )}
            </div>
          )}

          <div>
            <label htmlFor="notes" className="block text-sm font-medium text-gray-700 mb-2">
              Notas Adicionales
            </label>
            <Field
              id="notes"
              name="notes"
              as="textarea"
              rows={4}
              className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500"
              placeholder="Comentarios, preferencias, horarios de contacto..."
            />
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="submit"
              disabled={isLoading}
              className="flex-1 bg-amber-600 text-white px-6 py-3 rounded-lg hover:bg-amber-700 disabled:opacity-50 disabled:cursor-not-allowed font-medium transition-colors"
            >
              {isLoading ? 'Guardando...' : isEdit ? 'Actualizar Lead' : 'Crear Lead'}
            </button>
            <button
              type="button"
              onClick={onCancel}
              disabled={isLoading}
              className="flex-1 bg-gray-200 text-gray-800 px-6 py-3 rounded-lg hover:bg-gray-300 disabled:opacity-50 font-medium transition-colors"
            >
              Cancelar
            </button>
          </div>
        </Form>
      )}
    </Formik>
  )
}
