import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'
import type { FinancialReport, LeadsReport, ReportsFilters } from '../types/reports'
import type { ApiResponse } from '@shared/types'

export const reportsApi = createApi({
    reducerPath: 'reportsApi',
    baseQuery: fetchBaseQuery({
        baseUrl: API_CONFIG.baseURL,
        credentials: 'include',
        prepareHeaders: (headers, { getState }) => {
        const token = (getState() as RootState).auth.token
        if (token) headers.set('Authorization', `Bearer ${token}`)
        return headers
        }
    }),
    tagTypes: ['FinancialReport', 'LeadsReport'],
    endpoints: (builder) => ({

        // ---------- FINANCIAL REPORT ----------
        getFinancialReport: builder.query<FinancialReport, ReportsFilters>({
        query: ({ startDate, endDate }) =>
            `/reports/financial?startDate=${startDate}&endDate=${endDate}`,
        transformResponse: (response: ApiResponse<FinancialReport>) => response.data,
        providesTags: ['FinancialReport']
        }),

        exportFinancialPdf: builder.mutation<Blob, ReportsFilters>({
        query: ({ startDate, endDate }) => ({
            url: `/reports/financial/export/pdf?startDate=${startDate}&endDate=${endDate}`,
            method: 'GET',
            responseHandler: (response) => response.blob()
        })
        }),

        exportFinancialExcel: builder.mutation<Blob, ReportsFilters>({
        query: ({ startDate, endDate }) => ({
            url: `/reports/financial/export/excel?startDate=${startDate}&endDate=${endDate}`,
            method: 'GET',
            responseHandler: (response) => response.blob()
        })
        }),

        // ---------- LEADS REPORT ----------
        getLeadsReport: builder.query<LeadsReport, ReportsFilters>({
        query: ({ startDate, endDate }) =>
            `/reports/leads?startDate=${startDate}&endDate=${endDate}`,
        transformResponse: (response: ApiResponse<LeadsReport>) => response.data,
        providesTags: ['LeadsReport']
        })
    })
})

export const {
    useGetFinancialReportQuery,
    useExportFinancialPdfMutation,
    useExportFinancialExcelMutation,
    useGetLeadsReportQuery
} = reportsApi
