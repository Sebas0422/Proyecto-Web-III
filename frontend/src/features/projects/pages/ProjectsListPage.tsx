import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { PlusCircle } from 'lucide-react'
import { useGetProjectsQuery } from '../services/projectsApi'
import { useProjectFilters } from '../hooks/useProjectFilters'
import { useDebounce } from '@shared/hooks'
import ProjectCard from '../components/ProjectCard'
import ProjectFiltersBar from '../components/ProjectFiltersBar'
import EmptyProjectsState from '../components/EmptyProjectsState'
import { LoadingSpinner, ErrorMessage, DashboardLayout, PermissionGuard } from '@shared/components'
import { PERMISSIONS } from '@shared/constants/permissions'

export default function ProjectsListPage() {
  const navigate = useNavigate()
  const { filters, updateFilter, resetFilters, hasActiveFilters } = useProjectFilters()
  const [searchInput, setSearchInput] = useState('')
  const debouncedSearch = useDebounce(searchInput, 300)

  const queryParams = {
    ...filters,
    search: debouncedSearch || undefined,
  }

  const { data: projects, isLoading, error } = useGetProjectsQuery(queryParams)

  const handleSearchChange = (value: string) => {
    setSearchInput(value)
  }

  const handleActivoChange = (value: boolean | undefined) => {
    updateFilter('activo', value)
  }

  const handleReset = () => {
    setSearchInput('')
    resetFilters()
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 mb-2">Proyectos</h1>
            <p className="text-slate-600">Gestión de todos los proyectos</p>
          </div>

          {/* Solo EMPRESA y ADMIN pueden crear proyectos */}
          <PermissionGuard permissions={PERMISSIONS.PROJECTS_CREATE}>
            <button
              onClick={() => navigate('/projects/new')}
              className="bg-amber-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-amber-700 flex items-center gap-2 transition-colors"
            >
              <PlusCircle size={20} />
              Nuevo Proyecto
            </button>
          </PermissionGuard>
        </div>

        <ProjectFiltersBar
          searchValue={searchInput}
          activoFilter={filters.activo}
          onSearchChange={handleSearchChange}
          onActivoChange={handleActivoChange}
          onReset={handleReset}
          hasActiveFilters={hasActiveFilters}
        />

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner size="lg" />
          </div>
        ) : error ? (
          <ErrorMessage
            title="Error al cargar proyectos"
            message="No se pudieron cargar los proyectos. Por favor, intenta de nuevo."
            onRetry={() => window.location.reload()}
          />
        ) : !projects || projects.length === 0 ? (
          <EmptyProjectsState onCreate={() => navigate('/projects/new')} />
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {projects.map((project) => (
              <ProjectCard
                key={project.id}
                project={project}
                onClick={() => navigate(`/projects/${project.id}`)}
              />
            ))}
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}
