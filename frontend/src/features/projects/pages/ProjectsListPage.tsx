import React from 'react'
import { useNavigate } from 'react-router-dom'
import { PlusCircle, MoreVertical } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import StatusBadge, { type ProjectStatus } from '@shared/components/StatusBadge'

interface Project {
  id: number
  name: string
  status: ProjectStatus
  lotsAvailable: number
  lotsTotal: number
  date: string
}

const projectsData: Project[] = [
  {
    id: 1,
    name: 'Residencial "El Roble"',
    status: 'Activo',
    lotsAvailable: 45,
    lotsTotal: 50,
    date: '15/05/2023',
  },
  {
    id: 2,
    name: 'Villas "Las Palmeras"',
    status: 'Activo',
    lotsAvailable: 120,
    lotsTotal: 150,
    date: '02/03/2023',
  },
  {
    id: 3,
    name: 'Condominio "Mirador del Valle"',
    status: 'En Desarrollo',
    lotsAvailable: 200,
    lotsTotal: 200,
    date: '10/01/2024',
  },
  {
    id: 4,
    name: 'Altos de la Montaña',
    status: 'Pausado',
    lotsAvailable: 30,
    lotsTotal: 75,
    date: '20/09/2022',
  },
  {
    id: 5,
    name: 'Parque Industrial "El Progreso"',
    status: 'Cerrado',
    lotsAvailable: 0,
    lotsTotal: 100,
    date: '05/06/2021',
  },
]

const ProjectsListPage: React.FC = () => {
  const navigate = useNavigate()

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 dark:text-white mb-2">Proyectos</h1>
            <p className="text-slate-600 dark:text-slate-400">Gestión de todos los proyectos</p>
          </div>
          <button
            onClick={() => navigate('/projects/new')}
            className="bg-blue-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-blue-700 flex items-center gap-2 transition-colors"
          >
            <PlusCircle size={20} />
            Nuevo Proyecto
          </button>
        </div>

        <div className="bg-white dark:bg-slate-900 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800">
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-xs text-slate-500 dark:text-slate-400 uppercase bg-slate-50 dark:bg-slate-800/50">
                <tr>
                  <th className="px-6 py-3">Nombre del Proyecto</th>
                  <th className="px-6 py-3">Estado</th>
                  <th className="px-6 py-3">Lotes Disponibles</th>
                  <th className="px-6 py-3">Fecha de Creación</th>
                  <th className="px-6 py-3">
                    <span className="sr-only">Acciones</span>
                  </th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200 dark:divide-slate-800">
                {projectsData.map((project) => (
                  <tr key={project.id} className="hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors">
                    <td className="px-6 py-4 font-medium text-slate-800 dark:text-white whitespace-nowrap">
                      {project.name}
                    </td>
                    <td className="px-6 py-4">
                      <StatusBadge status={project.status} />
                    </td>
                    <td className="px-6 py-4 text-slate-600 dark:text-slate-300">
                      {project.lotsAvailable} / {project.lotsTotal}
                    </td>
                    <td className="px-6 py-4 text-slate-600 dark:text-slate-300">{project.date}</td>
                    <td className="px-6 py-4 text-right">
                      <button className="text-slate-500 hover:text-blue-600 dark:text-slate-400 dark:hover:text-white transition-colors">
                        <MoreVertical size={20} />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default ProjectsListPage
