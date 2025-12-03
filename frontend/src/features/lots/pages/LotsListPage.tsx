import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, PlusCircle, Pencil, Trash2 } from 'lucide-react'
import { useGetLotsQuery, useDeleteLotMutation } from '../services/lotsApi'
import { useGetProjectByIdQuery } from '@features/projects/services/projectsApi'
import { LoadingSpinner, ErrorMessage, DashboardLayout, PermissionGuard } from '@shared/components'
import { formatCurrency } from '@shared/utils'

export default function LotsListPage() {
  const { proyectoId } = useParams<{ proyectoId: string }>()
  const navigate = useNavigate()

  const { data: project } = useGetProjectByIdQuery(proyectoId!)
  const { data: lots, isLoading, error } = useGetLotsQuery({ proyectoId })
  const [deleteLot] = useDeleteLotMutation()

  const handleDelete = async (lotId: string, numeroLote: string) => {
    const confirmed = window.confirm(
      `¿Estás seguro de eliminar el lote "${numeroLote}"? Esta acción no se puede deshacer.`
    )

    if (confirmed) {
      try {
        await deleteLot(lotId).unwrap()
      } catch (err) {
        console.error('Error al eliminar lote:', err)
        alert('Error al eliminar el lote. Por favor, intenta de nuevo.')
      }
    }
  }

  const getEstadoBadge = (estado: string) => {
    const colors = {
      DISPONIBLE: 'bg-green-100 text-green-800',
      RESERVADO: 'bg-yellow-100 text-yellow-800',
      VENDIDO: 'bg-blue-100 text-blue-800',
    }
    return colors[estado as keyof typeof colors] || 'bg-gray-100 text-gray-800'
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate(`/projects/${proyectoId}`)}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <ArrowLeft size={24} className="text-gray-600" />
          </button>
          <div className="flex-1">
            <h1 className="text-2xl font-bold text-slate-800">
              Lotes - {project?.nombre || 'Proyecto'}
            </h1>
            <p className="text-slate-600">
              Gestión de lotes del proyecto
            </p>
          </div>

          {/* Solo EMPRESA y ADMIN pueden crear lotes */}
          <PermissionGuard permissions="lots:create">
            <button
              onClick={() => navigate(`/projects/${proyectoId}/lots/new`)}
              className="px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 flex items-center gap-2 transition-colors"
            >
              <PlusCircle size={20} />
              Nuevo Lote
            </button>
          </PermissionGuard>
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner size="lg" />
          </div>
        ) : error ? (
          <ErrorMessage
            title="Error al cargar lotes"
            message="No se pudieron cargar los lotes. Por favor, intenta de nuevo."
            onRetry={() => window.location.reload()}
          />
        ) : !lots || lots.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-12 text-center">
            <p className="text-gray-500 mb-4">No hay lotes registrados en este proyecto</p>

            {/* Solo EMPRESA y ADMIN pueden crear lotes */}
            <PermissionGuard permissions="lots:create">
              <button
                onClick={() => navigate(`/projects/${proyectoId}/lots/new`)}
                className="px-6 py-3 bg-amber-600 text-white rounded-lg hover:bg-amber-700 inline-flex items-center gap-2"
              >
                <PlusCircle size={20} />
                Crear Primer Lote
              </button>
            </PermissionGuard>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 border-b">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Nro. Lote
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Manzana
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Área (m²)
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Precio
                    </th>
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Estado
                    </th>
                    <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                      Acciones
                    </th>
                  </tr>
                </thead>
                <tbody className="bg-white divide-y divide-gray-200">
                  {lots.map((lot) => (
                    <tr key={lot.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="font-medium text-gray-900">{lot.numeroLote}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-gray-600">{lot.manzana || '-'}</div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="text-gray-900">
                          {lot.areaCalculada ? lot.areaCalculada.toFixed(2) : '-'}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <div className="font-medium text-gray-900">
                          {formatCurrency(lot.precio)}
                        </div>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getEstadoBadge(lot.estado)}`}>
                          {lot.estado}
                        </span>
                      </td>
                      <td className="px-6 py-4 whitespace-nowrap text-right">
                        <div className="flex items-center justify-end gap-2">
                          {/* Solo EMPRESA y ADMIN pueden editar lotes */}
                          <PermissionGuard permissions="lots:edit">
                            <button
                              onClick={() => navigate(`/projects/${proyectoId}/lots/${lot.id}/edit`)}
                              className="p-2 text-amber-600 hover:bg-amber-50 rounded-lg transition-colors"
                              title="Editar"
                            >
                              <Pencil size={18} />
                            </button>
                          </PermissionGuard>

                          {/* Solo EMPRESA y ADMIN pueden eliminar lotes */}
                          <PermissionGuard permissions="lots:delete">
                            <button
                              onClick={() => handleDelete(lot.id, lot.numeroLote)}
                              className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                              title="Eliminar"
                            >
                              <Trash2 size={18} />
                            </button>
                          </PermissionGuard>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            <div className="px-6 py-4 bg-gray-50 border-t">
              <p className="text-sm text-gray-600">
                Total de lotes: <span className="font-semibold">{lots.length}</span>
              </p>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout >
  )
}
