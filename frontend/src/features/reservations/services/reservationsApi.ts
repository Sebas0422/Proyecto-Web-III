import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Reservation, CreateReservationRequest } from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const reservationsApi = createApi({
  reducerPath: 'reservationsApi',
  baseQuery: fetchBaseQuery({
    baseUrl: `${API_CONFIG.baseURL}/api/reservations`,
    credentials: 'include',
    prepareHeaders: (headers, { getState }) => {
      const token = (getState() as RootState).auth.token
      if (token) {
        headers.set('Authorization', `Bearer ${token}`)
      }
      return headers
    },
  }),
  tagTypes: ['Reservations'],
  endpoints: (builder) => ({
    getReservations: builder.query<Reservation[], { status?: string }>({
      query: ({ status }) => {
        const params = new URLSearchParams()
        if (status) params.append('status', status)
        return `?${params.toString()}`
      },
      transformResponse: (response: { data: Reservation[] }) => response.data,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Reservations' as const, id })),
            { type: 'Reservations', id: 'LIST' },
          ]
          : [{ type: 'Reservations', id: 'LIST' }],
    }),

    getReservationById: builder.query<Reservation, string>({
      query: (id) => `/${id}`,
      transformResponse: (response: { data: Reservation }) => response.data,
      providesTags: (_result, _error, id) => [{ type: 'Reservations', id }],
    }),

    createReservation: builder.mutation<Reservation, CreateReservationRequest>({
      query: (reservation) => ({
        url: '',
        method: 'POST',
        body: reservation,
      }),
      transformResponse: (response: { data: Reservation }) => response.data,
      invalidatesTags: [{ type: 'Reservations', id: 'LIST' }],
    }),

    confirmReservation: builder.mutation<Reservation, string>({
      query: (id) => ({
        url: `/${id}/confirm`,
        method: 'PUT',
      }),
      transformResponse: (response: { data: Reservation }) => response.data,
      invalidatesTags: (_result, _error, id) => [
        { type: 'Reservations', id },
        { type: 'Reservations', id: 'LIST' },
      ],
    }),

    cancelReservation: builder.mutation<Reservation, string>({
      query: (id) => ({
        url: `/${id}`,
        method: 'DELETE',
      }),
      transformResponse: (response: { data: Reservation }) => response.data,
      invalidatesTags: (_result, _error, id) => [
        { type: 'Reservations', id },
        { type: 'Reservations', id: 'LIST' },
      ],
    }),
  }),
})

export const {
  useGetReservationsQuery,
  useGetReservationByIdQuery,
  useCreateReservationMutation,
  useConfirmReservationMutation,
  useCancelReservationMutation,
} = reservationsApi
