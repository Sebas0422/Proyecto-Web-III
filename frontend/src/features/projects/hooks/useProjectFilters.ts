import { useState, useMemo } from 'react'
import type { ProjectFilters, PaginationParams } from '@shared/types'
import API_CONFIG from '@app/config/api.config'

export const useProjectFilters = () => {
  const [filters, setFilters] = useState<ProjectFilters & PaginationParams>({
    page: 0,
    size: API_CONFIG.defaultPageSize,
    activo: undefined,
    search: undefined,
  })

  const updateFilter = (key: keyof ProjectFilters, value: string | boolean | undefined) => {
    setFilters(prev => ({
      ...prev,
      [key]: value,
      page: 0,
    }))
  }

  const updatePage = (page: number) => {
    setFilters(prev => ({ ...prev, page }))
  }

  const updatePageSize = (size: number) => {
    setFilters(prev => ({ ...prev, size, page: 0 }))
  }

  const resetFilters = () => {
    setFilters({
      page: 0,
      size: API_CONFIG.defaultPageSize,
      activo: undefined,
      search: undefined,
    })
  }

  const hasActiveFilters = useMemo(
    () => filters.activo !== undefined || Boolean(filters.search),
    [filters]
  )

  return {
    filters,
    updateFilter,
    updatePage,
    updatePageSize,
    resetFilters,
    hasActiveFilters,
  }
}
