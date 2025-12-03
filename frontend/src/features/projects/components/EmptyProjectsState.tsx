import { FolderOpen, PlusCircle } from 'lucide-react'
import { PermissionGuard } from '@shared/components'

interface EmptyProjectsStateProps {
  onCreate?: () => void
}

export default function EmptyProjectsState({ onCreate }: EmptyProjectsStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-16 px-4">
      <div className="bg-amber-50 rounded-full p-6 mb-4">
        <FolderOpen size={48} className="text-amber-600" />
      </div>
      <h3 className="text-xl font-semibold text-gray-900 mb-2">No hay proyectos</h3>
      <p className="text-gray-600 text-center max-w-md mb-6">
        No se encontraron proyectos con los filtros seleccionados. Intenta ajustar tus criterios de búsqueda.
      </p>

      {/* Solo EMPRESA y ADMIN pueden crear proyectos */}
      {onCreate && (
        <PermissionGuard permissions="projects:create">
          <button
            onClick={onCreate}
            className="bg-amber-600 text-white px-6 py-3 rounded-lg font-medium hover:bg-amber-700 flex items-center gap-2 transition-colors"
          >
            <PlusCircle size={20} />
            Crear Primer Proyecto
          </button>
        </PermissionGuard>
      )}
    </div>
  )
}
