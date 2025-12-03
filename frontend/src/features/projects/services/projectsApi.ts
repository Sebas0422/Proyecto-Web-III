import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type {
  Project,
  CreateProjectRequest,
  UpdateProjectRequest,
  ApiResponse,
  ProjectFilters,
  PaginationParams,
} from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const projectsApi = createApi({
  reducerPath: 'projectsApi',
  baseQuery: fetchBaseQuery({
    baseUrl: API_CONFIG.baseURL,
    credentials: 'include',
    prepareHeaders: (headers, { getState }) => {
      const token = (getState() as RootState).auth.token
      if (token) {
        headers.set('Authorization', `Bearer ${token}`)
      }
      return headers
    },
  }),
  tagTypes: ['Project', 'ProjectsList'],
  endpoints: (builder) => ({
    getProjects: builder.query<Project[], ProjectFilters & PaginationParams>({
      query: (params) => {
        const queryParams = new URLSearchParams()
        if (params.activo !== undefined) queryParams.append('activo', String(params.activo))
        if (params.search) queryParams.append('search', params.search)

        const queryString = queryParams.toString()
        return `${API_CONFIG.endpoints.projects}${queryString ? `?${queryString}` : ''}`
      },
      transformResponse: (response: ApiResponse<Project[]>) => response.data,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Project' as const, id })),
            { type: 'ProjectsList', id: 'LIST' },
          ]
          : [{ type: 'ProjectsList', id: 'LIST' }],
    }),

    getProjectById: builder.query<Project, string>({
      query: (id) => `${API_CONFIG.endpoints.projects}/${id}`,
      transformResponse: (response: ApiResponse<Project>) => response.data,
      providesTags: (_result, _error, id) => [{ type: 'Project', id }],
    }),

    createProject: builder.mutation<Project, CreateProjectRequest>({
      query: (data) => ({
        url: API_CONFIG.endpoints.projects,
        method: 'POST',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Project>) => response.data,
      invalidatesTags: [{ type: 'ProjectsList', id: 'LIST' }],
    }),

    updateProject: builder.mutation<Project, { id: string; data: UpdateProjectRequest }>({
      query: ({ id, data }) => ({
        url: `${API_CONFIG.endpoints.projects}/${id}`,
        method: 'PUT',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Project>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Project', id },
        { type: 'ProjectsList', id: 'LIST' },
      ],
    }),

    deleteProject: builder.mutation<void, string>({
      query: (id) => ({
        url: `${API_CONFIG.endpoints.projects}/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Project', id },
        { type: 'ProjectsList', id: 'LIST' },
      ],
    }),

    getProjectMedia: builder.query<string[], string>({
      query: (id) => `${API_CONFIG.endpoints.projects}/${id}/media`,
      transformResponse: (response: ApiResponse<string[]>) => response.data,
    }),

    uploadProjectMedia: builder.mutation<string, { id: string; file: File }>({
      query: ({ id, file }) => {
        const formData = new FormData()
        formData.append('file', file)

        return {
          url: `${API_CONFIG.endpoints.projects}/${id}/media`,
          method: 'POST',
          body: formData,
        }
      },
      transformResponse: (response: ApiResponse<string>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [{ type: 'Project', id }],
    }),
  }),
})

export const {
  useGetProjectsQuery,
  useGetProjectByIdQuery,
  useCreateProjectMutation,
  useUpdateProjectMutation,
  useDeleteProjectMutation,
  useGetProjectMediaQuery,
  useUploadProjectMediaMutation,
} = projectsApi
