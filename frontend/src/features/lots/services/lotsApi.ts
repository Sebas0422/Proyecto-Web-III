import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type {
  Lot,
  CreateLotRequest,
  UpdateLotRequest,
  ApiResponse,
  LotFilters,
} from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const lotsApi = createApi({
  reducerPath: 'lotsApi',
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
  tagTypes: ['Lot', 'LotsList'],
  endpoints: (builder) => ({
    getLots: builder.query<Lot[], LotFilters>({
      query: (params) => {
        const queryParams = new URLSearchParams()
        if (params.proyectoId) queryParams.append('proyectoId', params.proyectoId)
        if (params.estado) queryParams.append('estado', params.estado)
        if (params.manzana) queryParams.append('manzana', params.manzana)

        const queryString = queryParams.toString()
        return `${API_CONFIG.endpoints.lots}${queryString ? `?${queryString}` : ''}`
      },
      transformResponse: (response: ApiResponse<Lot[]>) => response.data,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Lot' as const, id })),
            { type: 'LotsList', id: 'LIST' },
          ]
          : [{ type: 'LotsList', id: 'LIST' }],
    }),

    getLotById: builder.query<Lot, string>({
      query: (id) => `${API_CONFIG.endpoints.lots}/${id}`,
      transformResponse: (response: ApiResponse<Lot>) => response.data,
      providesTags: (_result, _error, id) => [{ type: 'Lot', id }],
    }),

    createLot: builder.mutation<Lot, { proyectoId: string; data: CreateLotRequest }>({
      query: ({ proyectoId, data }) => ({
        url: `${API_CONFIG.endpoints.lots}?proyectoId=${proyectoId}`,
        method: 'POST',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Lot>) => response.data,
      invalidatesTags: [{ type: 'LotsList', id: 'LIST' }],
    }),

    updateLot: builder.mutation<Lot, { id: string; data: UpdateLotRequest }>({
      query: ({ id, data }) => ({
        url: `${API_CONFIG.endpoints.lots}/${id}`,
        method: 'PUT',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Lot>) => response.data,
      invalidatesTags: (_result, _error, { id }) => [
        { type: 'Lot', id },
        { type: 'LotsList', id: 'LIST' },
      ],
    }),

    deleteLot: builder.mutation<void, string>({
      query: (id) => ({
        url: `${API_CONFIG.endpoints.lots}/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (_result, _error, id) => [
        { type: 'Lot', id },
        { type: 'LotsList', id: 'LIST' },
      ],
    }),
  }),
})

export const {
  useGetLotsQuery,
  useGetLotByIdQuery,
  useCreateLotMutation,
  useUpdateLotMutation,
  useDeleteLotMutation,
} = lotsApi
