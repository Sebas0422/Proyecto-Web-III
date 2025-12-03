import { useMemo } from 'react'
import { useGetProjectsQuery } from '../services/projectsApi'
import type { ProjectFilters, PaginationParams } from '@shared/types'

export const useProjects = (filters: ProjectFilters & PaginationParams) => {
  const { data, isLoading, isFetching, error, refetch } = useGetProjectsQuery(filters)

  const projects = useMemo(() => data ?? [], [data])
  const totalProjects = useMemo(() => data?.length ?? 0, [data])
  const isEmpty = useMemo(() => projects.length === 0 && !isLoading, [projects, isLoading])

  return {
    projects,
    totalProjects,
    isEmpty,
    isLoading,
    isFetching,
    error,
    refetch,
  }
}
