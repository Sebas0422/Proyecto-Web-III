import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type {
  Lead,
  CreateLeadRequest,
  UpdateLeadStatusRequest,
  ApiResponse,
  LeadFilters,
} from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const leadsApi = createApi({
  reducerPath: 'leadsApi',
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
  tagTypes: ['Lead', 'LeadsList'],
  endpoints: (builder) => ({
    getLeads: builder.query<Lead[], LeadFilters>({
      query: (params) => {
        const queryParams = new URLSearchParams()
        if (params.status) queryParams.append('status', params.status)
        if (params.projectId) queryParams.append('projectId', params.projectId)
        if (params.lotId) queryParams.append('lotId', params.lotId)

        const queryString = queryParams.toString()
        return `${API_CONFIG.endpoints.leads}${queryString ? `?${queryString}` : ''}`
      },
      transformResponse: (response: ApiResponse<Lead[]>) => response.data,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Lead' as const, id })),
            { type: 'LeadsList', id: 'LIST' },
          ]
          : [{ type: 'LeadsList', id: 'LIST' }],
    }),

    getLeadById: builder.query<Lead, string>({
      query: (id) => `${API_CONFIG.endpoints.leads}/${id}`,
      transformResponse: (response: ApiResponse<Lead>) => response.data,
      providesTags: (_result, _error, id) => [{ type: 'Lead', id }],
    }),

    createLead: builder.mutation<Lead, CreateLeadRequest>({
      query: (data) => ({
        url: API_CONFIG.endpoints.leads,
        method: 'POST',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Lead>) => response.data,
      invalidatesTags: [{ type: 'LeadsList', id: 'LIST' }],
    }),

    updateLeadStatus: builder.mutation<Lead, { id: string; data: UpdateLeadStatusRequest }>({
      query: ({ id, data }) => ({
        url: `${API_CONFIG.endpoints.leads}/${id}/status`,
        method: 'PUT',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Lead>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Lead', id },
        { type: 'LeadsList', id: 'LIST' },
      ],
    }),

    deleteLead: builder.mutation<void, string>({
      query: (id) => ({
        url: `${API_CONFIG.endpoints.leads}/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Lead', id },
        { type: 'LeadsList', id: 'LIST' },
      ],
    }),
  }),
})

export const {
  useGetLeadsQuery,
  useGetLeadByIdQuery,
  useCreateLeadMutation,
  useUpdateLeadStatusMutation,
  useDeleteLeadMutation,
} = leadsApi
