import { Formik, Form, Field, ErrorMessage as FormikError } from 'formik'
import * as Yup from 'yup'
import type { CreateLotRequest } from '@shared/types'

interface LotFormProps {
  initialValues?: Partial<CreateLotRequest>
  onSubmit: (values: CreateLotRequest) => void
  onCancel: () => void
  isLoading?: boolean
  isEdit?: boolean
}

const validationSchema = Yup.object({
  numeroLote: Yup.string()
    .required('El número de lote es requerido')
    .min(1, 'Mínimo 1 carácter')
    .max(50, 'Máximo 50 caracteres'),
  manzana: Yup.string().max(50, 'Máximo 50 caracteres'),
  precio: Yup.number()
    .required('El precio es requerido')
    .positive('El precio debe ser mayor a 0')
    .min(1, 'El precio debe ser mayor a 0'),
  geometriaWKT: Yup.string()
    .required('La geometría WKT es requerida')
    .min(10, 'La geometría WKT debe ser válida'),
  observaciones: Yup.string().max(500, 'Máximo 500 caracteres'),
})

const defaultValues: CreateLotRequest = {
  numeroLote: '',
  manzana: '',
  geometriaWKT: 'POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))',
  precio: 0,
  observaciones: '',
}

export default function LotForm({
  initialValues,
  onSubmit,
  onCancel,
  isLoading = false,
  isEdit = false,
}: LotFormProps) {
  return (
    <Formik
      initialValues={{ ...defaultValues, ...initialValues }}
      validationSchema={validationSchema}
      onSubmit={onSubmit}
      enableReinitialize
    >
      {({ errors, touched }) => (
        <Form className="space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label htmlFor="numeroLote" className="block text-sm font-medium text-gray-700 mb-2">
                Número de Lote *
              </label>
              <Field
                id="numeroLote"
                name="numeroLote"
                type="text"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.numeroLote && touched.numeroLote ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="Ej: L-001"
              />
              <FormikError name="numeroLote" component="p" className="mt-1 text-sm text-red-600" />
            </div>

            <div>
              <label htmlFor="manzana" className="block text-sm font-medium text-gray-700 mb-2">
                Manzana
              </label>
              <Field
                id="manzana"
                name="manzana"
                type="text"
                className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.manzana && touched.manzana ? 'border-red-500' : 'border-gray-300'
                  }`}
                placeholder="Ej: A, B, C"
              />
              <FormikError name="manzana" component="p" className="mt-1 text-sm text-red-600" />
            </div>
          </div>

          <div>
            <label htmlFor="precio" className="block text-sm font-medium text-gray-700 mb-2">
              Precio (Bs) *
            </label>
            <Field
              id="precio"
              name="precio"
              type="number"
              step="0.01"
              min="0"
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.precio && touched.precio ? 'border-red-500' : 'border-gray-300'
                }`}
              placeholder="Ej: 50000"
            />
            <FormikError name="precio" component="p" className="mt-1 text-sm text-red-600" />
          </div>

          <div>
            <label htmlFor="geometriaWKT" className="block text-sm font-medium text-gray-700 mb-2">
              Geometría WKT *
            </label>
            <Field
              id="geometriaWKT"
              name="geometriaWKT"
              as="textarea"
              rows={3}
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.geometriaWKT && touched.geometriaWKT ? 'border-red-500' : 'border-gray-300'
                }`}
              placeholder="POLYGON((x1 y1, x2 y2, x3 y3, x1 y1))"
            />
            <FormikError name="geometriaWKT" component="p" className="mt-1 text-sm text-red-600" />
            <p className="mt-1 text-xs text-gray-500">
              Formato WKT para definir la geometría del lote. Ej: POLYGON((0 0, 0 1, 1 1, 1 0, 0 0))
            </p>
          </div>

          <div>
            <label htmlFor="observaciones" className="block text-sm font-medium text-gray-700 mb-2">
              Observaciones
            </label>
            <Field
              id="observaciones"
              name="observaciones"
              as="textarea"
              rows={4}
              className={`w-full px-4 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 ${errors.observaciones && touched.observaciones ? 'border-red-500' : 'border-gray-300'
                }`}
              placeholder="Notas adicionales sobre el lote..."
            />
            <FormikError name="observaciones" component="p" className="mt-1 text-sm text-red-600" />
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="submit"
              disabled={isLoading}
              className="flex-1 bg-amber-600 text-white px-6 py-3 rounded-lg hover:bg-amber-700 disabled:opacity-50 disabled:cursor-not-allowed font-medium transition-colors"
            >
              {isLoading ? 'Guardando...' : isEdit ? 'Actualizar Lote' : 'Crear Lote'}
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
