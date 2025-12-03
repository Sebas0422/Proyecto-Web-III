import type { Project } from '@shared/types'
import { MapPin, Calendar, CircleCheck } from 'lucide-react'
import { formatRelativeTime } from '@shared/utils'
import StatusBadge from '@shared/components/StatusBadge'

interface ProjectCardProps {
  project: Project
  onClick?: () => void
}

export default function ProjectCard({ project, onClick }: ProjectCardProps) {
  const statusColors = project.activo
    ? 'bg-green-100 text-green-800'
    : 'bg-gray-100 text-gray-800'

  const statusText = project.activo ? 'ACTIVO' : 'INACTIVO'

  return (
    <div
      className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow cursor-pointer border border-gray-200"
      onClick={onClick}
    >
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-xl font-semibold text-gray-900 mb-1">{project.nombre}</h3>
          <div className="flex items-center text-gray-600 text-sm">
            <MapPin size={16} className="mr-1" />
            <span>{project.ubicacion}</span>
          </div>
        </div>
        <StatusBadge
          status={statusText}
          className={statusColors}
        />
      </div>

      {project.descripcion && (
        <p className="text-gray-600 text-sm mb-4 line-clamp-2">{project.descripcion}</p>
      )}

      <div className="grid grid-cols-2 gap-4 mb-4">
        <div className="flex items-center text-gray-700">
          <Calendar size={18} className="text-amber-600 mr-2" />
          <div>
            <p className="text-xs text-gray-500">Inicio</p>
            <p className="font-semibold text-sm">{new Date(project.fechaInicio).toLocaleDateString('es-BO')}</p>
          </div>
        </div>
        {project.fechaEstimadaFinalizacion && (
          <div className="flex items-center text-gray-700">
            <CircleCheck size={18} className="text-amber-600 mr-2" />
            <div>
              <p className="text-xs text-gray-500">Finalización Est.</p>
              <p className="font-semibold text-sm">{new Date(project.fechaEstimadaFinalizacion).toLocaleDateString('es-BO')}</p>
            </div>
          </div>
        )}
      </div>

      <div className="pt-4 border-t border-gray-200 flex items-center justify-between">
        <p className="text-xs text-gray-500">
          Creado {formatRelativeTime(project.createdAt)}
        </p>
        <button
          onClick={(e) => {
            e.stopPropagation()
            onClick?.()
          }}
          className="text-amber-600 hover:text-amber-700 font-medium text-sm"
        >
          Ver Detalles →
        </button>
      </div>
    </div>
  )
}
