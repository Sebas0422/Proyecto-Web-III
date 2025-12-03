import { Search, Filter, X } from 'lucide-react'

interface ProjectFiltersBarProps {
  searchValue: string
  activoFilter: boolean | undefined
  onSearchChange: (value: string) => void
  onActivoChange: (value: boolean | undefined) => void
  onReset: () => void
  hasActiveFilters: boolean
}

export default function ProjectFiltersBar({
  searchValue,
  activoFilter,
  onSearchChange,
  onActivoChange,
  onReset,
  hasActiveFilters,
}: ProjectFiltersBarProps) {
  return (
    <div className="bg-white rounded-lg shadow-md p-4 mb-6">
      <div className="flex flex-col md:flex-row gap-4">
        <div className="flex-1 relative">
          <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400" size={20} />
          <input
            type="text"
            placeholder="Buscar proyectos por nombre o ubicación..."
            value={searchValue}
            onChange={(e) => onSearchChange(e.target.value)}
            className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500"
          />
        </div>

        <div className="flex items-center gap-2">
          <Filter size={20} className="text-gray-500" />
          <select
            value={activoFilter === undefined ? '' : activoFilter ? 'true' : 'false'}
            onChange={(e) => {
              const val = e.target.value
              onActivoChange(val === '' ? undefined : val === 'true')
            }}
            className="px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-amber-500 bg-white"
          >
            <option value="">Todos los estados</option>
            <option value="true">Activos</option>
            <option value="false">Inactivos</option>
          </select>
        </div>

        {hasActiveFilters && (
          <button
            onClick={onReset}
            className="px-4 py-2 bg-gray-100 hover:bg-gray-200 text-gray-700 rounded-lg flex items-center gap-2 transition-colors"
          >
            <X size={18} />
            <span>Limpiar</span>
          </button>
        )}
      </div>
    </div>
  )
}
