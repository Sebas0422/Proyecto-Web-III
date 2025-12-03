import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type {
  Payment,
  CreatePaymentRequest,
  ConfirmPaymentRequest,
  ApiResponse,
  PaymentFilters,
} from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const paymentsApi = createApi({
  reducerPath: 'paymentsApi',
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
  tagTypes: ['Payment', 'PaymentsList'],
  endpoints: (builder) => ({
    getPayments: builder.query<Payment[], PaymentFilters>({
      query: (params) => {
        const queryParams = new URLSearchParams()
        if (params.status) queryParams.append('status', params.status)
        if (params.method) queryParams.append('method', params.method)
        if (params.reservationId) queryParams.append('reservationId', params.reservationId)

        const queryString = queryParams.toString()
        return `/api/payments${queryString ? `?${queryString}` : ''}`
      },
      transformResponse: (response: ApiResponse<Payment[]>) => response.data,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Payment' as const, id })),
            { type: 'PaymentsList', id: 'LIST' },
          ]
          : [{ type: 'PaymentsList', id: 'LIST' }],
    }),

    getPaymentById: builder.query<Payment, string>({
      query: (id) => `/api/payments/${id}`,
      transformResponse: (response: ApiResponse<Payment>) => response.data,
      providesTags: (_result, _error, id) => [{ type: 'Payment', id }],
    }),

    getPaymentQR: builder.query<Blob, string>({
      query: (paymentId) => ({
        url: `/api/payments/${paymentId}/qr`,
        responseHandler: (response) => response.blob(),
      }),
    }),

    createPayment: builder.mutation<Payment, CreatePaymentRequest>({
      query: (payment) => ({
        url: '/api/payments',
        method: 'POST',
        body: payment,
      }),
      transformResponse: (response: ApiResponse<Payment>) => response.data,
      invalidatesTags: [{ type: 'PaymentsList', id: 'LIST' }],
    }),

    confirmPayment: builder.mutation<Payment, { paymentId: string; data: ConfirmPaymentRequest }>({
      query: ({ paymentId, data }) => ({
        url: `/api/payments/${paymentId}/confirm`,
        method: 'PUT',
        body: data,
      }),
      transformResponse: (response: ApiResponse<Payment>) => response.data,
      invalidatesTags: (_result, _error, { paymentId }) => [
        { type: 'Payment', id: paymentId },
        { type: 'PaymentsList', id: 'LIST' },
      ],
    }),
  }),
})

export const {
  useGetPaymentsQuery,
  useGetPaymentByIdQuery,
  useGetPaymentQRQuery,
  useCreatePaymentMutation,
  useConfirmPaymentMutation,
} = paymentsApi
