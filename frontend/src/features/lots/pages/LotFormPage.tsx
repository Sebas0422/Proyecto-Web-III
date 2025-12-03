import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, AlertCircle } from 'lucide-react'
import { useGetLotByIdQuery, useCreateLotMutation, useUpdateLotMutation } from '../services/lotsApi'
import LotForm from '../components/LotForm'
import { LoadingSpinner, DashboardLayout } from '@shared/components'
import type { CreateLotRequest } from '@shared/types'
import { useState } from 'react'

export default function LotFormPage() {
  const { proyectoId, lotId } = useParams<{ proyectoId: string; lotId?: string }>()
  const navigate = useNavigate()
  const isEdit = Boolean(lotId)
  const [error, setError] = useState<string | null>(null)

  const { data: lot, isLoading: isLoadingLot } = useGetLotByIdQuery(lotId!, {
    skip: !isEdit,
  })

  const [createLot, { isLoading: isCreating }] = useCreateLotMutation()
  const [updateLot, { isLoading: isUpdating }] = useUpdateLotMutation()

  const handleSubmit = async (values: CreateLotRequest) => {
    setError(null)
    try {
      if (isEdit && lotId) {
        await updateLot({ id: lotId, data: values }).unwrap()
      } else {
        await createLot({ proyectoId: proyectoId!, data: values }).unwrap()
      }
      navigate(`/projects/${proyectoId}/lots`)
    } catch (err) {
      console.error('Error al guardar lote:', err)
      const error = err as { data?: { message?: string } }
      setError(error?.data?.message || 'Error al guardar el lote')
    }
  }

  const handleCancel = () => {
    navigate(`/projects/${proyectoId}/lots`)
  }

  if (isEdit && isLoadingLot) {
    return (
      <DashboardLayout>
        <div className="flex justify-center items-center py-16">
          <LoadingSpinner size="lg" />
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
              {isEdit ? 'Editar Lote' : 'Nuevo Lote'}
            </h1>
            <p className="text-gray-600">
              {isEdit
                ? 'Actualiza la información del lote'
                : 'Completa los datos para crear un nuevo lote'}
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

          <LotForm
            initialValues={
              lot
                ? {
                  numeroLote: lot.numeroLote,
                  manzana: lot.manzana,
                  geometriaWKT: lot.geometriaWKT,
                  precio: lot.precio,
                  observaciones: lot.observaciones,
                }
                : undefined
            }
            onSubmit={handleSubmit}
            onCancel={handleCancel}
            isLoading={isCreating || isUpdating}
            isEdit={isEdit}
          />
        </div>
      </div>
    </DashboardLayout>
  )
}
