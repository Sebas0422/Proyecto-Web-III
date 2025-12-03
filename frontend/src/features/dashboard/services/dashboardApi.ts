import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import Cookies from 'js-cookie'
import type { DashboardMetrics } from '../types'

const baseUrl = import.meta.env.VITE_API_BASE || 'http://localhost:8000'

interface ApiResponse<T> {
  success: boolean
  message: string | null
  data: T
  errorCode: string | null
}

export const dashboardApi = createApi({
  reducerPath: 'dashboardApi',
  baseQuery: fetchBaseQuery({
    baseUrl,
    prepareHeaders: (headers) => {
      const token = Cookies.get('token')
      if (token) {
        headers.set('Authorization', `Bearer ${token}`)
      }
      return headers
    },
  }),
  endpoints: (builder) => ({
    getDashboardMetrics: builder.query<DashboardMetrics, void>({
      query: () => '/api/reports/dashboard/metrics',
      transformResponse: (response: ApiResponse<DashboardMetrics>) => response.data,
    }),
  }),
})

export const { useGetDashboardMetricsQuery } = dashboardApi
