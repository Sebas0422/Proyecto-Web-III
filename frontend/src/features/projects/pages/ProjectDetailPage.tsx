import { useParams, useNavigate } from 'react-router-dom'
import { MapPin, Calendar, Edit, Trash2, ArrowLeft, CircleCheck } from 'lucide-react'
import { useGetProjectByIdQuery, useDeleteProjectMutation } from '../services/projectsApi'
import { formatRelativeTime } from '@shared/utils'
import { LoadingSpinner, ErrorMessage, DashboardLayout, PermissionGuard } from '@shared/components'

export default function ProjectDetailPage() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const { data: project, isLoading, error } = useGetProjectByIdQuery(id!)
  const [deleteProject, { isLoading: isDeleting }] = useDeleteProjectMutation()

  const handleDelete = async () => {
    if (!project) return

    const confirmed = window.confirm(
      `¿Estás seguro de eliminar el proyecto "${project.nombre}"? Esta acción no se puede deshacer.`
    )

    if (confirmed) {
      try {
        await deleteProject(project.id).unwrap()
        navigate('/projects')
      } catch (err) {
        console.error('Error al eliminar proyecto:', err)
        alert('Error al eliminar el proyecto. Por favor, intenta de nuevo.')
      }
    }
  }

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex justify-center items-center py-12">
          <LoadingSpinner size="lg" />
        </div>
      </DashboardLayout>
    )
  }

  if (error || !project) {
    return (
      <DashboardLayout>
        <ErrorMessage
          title="Error al cargar proyecto"
          message="No se pudo cargar el proyecto. Por favor, intenta de nuevo."
          onRetry={() => navigate('/projects')}
        />
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate('/projects')}
            className="p-2 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <ArrowLeft size={24} className="text-gray-600" />
          </button>
          <div className="flex-1">
            <h1 className="text-2xl font-bold text-slate-800">{project.nombre}</h1>
            <p className="text-slate-600">Detalles del proyecto</p>
          </div>
          <div className="flex gap-3">
            {/* Solo EMPRESA y ADMIN pueden editar */}
            <PermissionGuard permissions="projects:edit">
              <button
                onClick={() => navigate(`/projects/${project.id}/edit`)}
                className="px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 flex items-center gap-2 transition-colors"
              >
                <Edit size={18} />
                Editar
              </button>
            </PermissionGuard>

            {/* Solo EMPRESA y ADMIN pueden eliminar */}
            <PermissionGuard permissions="projects:delete">
              <button
                onClick={handleDelete}
                disabled={isDeleting}
                className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 flex items-center gap-2 transition-colors disabled:opacity-50"
              >
                <Trash2 size={18} />
                {isDeleting ? 'Eliminando...' : 'Eliminar'}
              </button>
            </PermissionGuard>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 space-y-6">
          <div className="flex items-center justify-between border-b pb-4">
            <div className="flex items-center gap-3">
              <div className={`px-3 py-1 rounded-full text-sm font-medium ${project.activo
                ? 'bg-green-100 text-green-800'
                : 'bg-gray-100 text-gray-800'
                }`}>
                {project.activo ? 'ACTIVO' : 'INACTIVO'}
              </div>
              <span className="text-sm text-gray-500">
                Creado {formatRelativeTime(project.createdAt)}
              </span>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-2">Ubicación</h3>
              <div className="flex items-start gap-2">
                <MapPin size={20} className="text-amber-600 mt-1 flex-shrink-0" />
                <p className="text-gray-900">{project.ubicacion}</p>
              </div>
            </div>

            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-2">Fecha de Inicio</h3>
              <div className="flex items-center gap-2">
                <Calendar size={20} className="text-amber-600" />
                <p className="text-gray-900">
                  {new Date(project.fechaInicio).toLocaleDateString('es-BO', {
                    year: 'numeric',
                    month: 'long',
                    day: 'numeric',
                  })}
                </p>
              </div>
            </div>

            {project.fechaEstimadaFinalizacion && (
              <div>
                <h3 className="text-sm font-medium text-gray-500 mb-2">Fecha Estimada de Finalización</h3>
                <div className="flex items-center gap-2">
                  <CircleCheck size={20} className="text-amber-600" />
                  <p className="text-gray-900">
                    {new Date(project.fechaEstimadaFinalizacion).toLocaleDateString('es-BO', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                    })}
                  </p>
                </div>
              </div>
            )}

            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-2">ID del Proyecto</h3>
              <p className="text-gray-900 font-mono text-sm">{project.id}</p>
            </div>
          </div>

          {project.descripcion && (
            <div>
              <h3 className="text-sm font-medium text-gray-500 mb-2">Descripción</h3>
              <p className="text-gray-900 whitespace-pre-wrap">{project.descripcion}</p>
            </div>
          )}
        </div>

        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-900">Lotes del Proyecto</h2>

            {/* Todos pueden ver lotes */}
            <PermissionGuard permissions="lots:view">
              <button
                onClick={() => navigate(`/projects/${project.id}/lots`)}
                className="px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 transition-colors"
              >
                Ver Lotes
              </button>
            </PermissionGuard>
          </div>
          <p className="text-gray-600">
            Gestiona los lotes asociados a este proyecto
          </p>
        </div>
      </div>
    </DashboardLayout>
  )
}
