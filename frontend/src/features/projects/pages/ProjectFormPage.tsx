import { useParams } from 'react-router-dom'
import { ArrowLeft, AlertCircle } from 'lucide-react'
import { useGetProjectByIdQuery } from '../services/projectsApi'
import { useProjectForm } from '../hooks/useProjectForm'
import ProjectForm from '../components/ProjectForm'
import DashboardLayout from '@shared/components/DashboardLayout'

export default function ProjectFormPage() {
  const { id } = useParams<{ id: string }>()
  const isEdit = Boolean(id)

  const { data: project, isLoading: isLoadingProject } = useGetProjectByIdQuery(id!, {
    skip: !isEdit,
  })

  const { handleSubmit, handleCancel, isLoading, error } = useProjectForm(id)

  if (isEdit && isLoadingProject) {
    return (
      <DashboardLayout>
        <div className="flex justify-center items-center py-16">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-amber-600"></div>
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
              {isEdit ? 'Editar Proyecto' : 'Nuevo Proyecto'}
            </h1>
            <p className="text-gray-600">
              {isEdit
                ? 'Actualiza la información del proyecto'
                : 'Completa los datos para crear un nuevo proyecto'}
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

          <ProjectForm
            initialValues={
              project
                ? {
                  nombre: project.nombre,
                  descripcion: project.descripcion,
                  ubicacion: project.ubicacion,
                  fechaInicio: project.fechaInicio.split('T')[0],
                  fechaEstimadaFinalizacion: project.fechaEstimadaFinalizacion?.split('T')[0] || '',
                }
                : undefined
            }
            onSubmit={handleSubmit}
            onCancel={handleCancel}
            isLoading={isLoading}
            isEdit={isEdit}
          />
        </div>
      </div>
    </DashboardLayout>
  )
}
