import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { Company, CreateCompanyRequest, UpdateCompanyRequest } from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const companiesApi = createApi({
  reducerPath: 'companiesApi',
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
  tagTypes: ['Company'],
  endpoints: (builder) => ({
    getCompanies: builder.query<Company[], { onlyActive?: boolean }>({
      query: ({ onlyActive = true }) => `/api/admin/companies?onlyActive=${onlyActive}`,
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'Company' as const, id })),
            { type: 'Company', id: 'LIST' },
          ]
          : [{ type: 'Company', id: 'LIST' }],
    }),

    getCompanyById: builder.query<Company, string>({
      query: (id) => `/api/admin/companies/${id}`,
      providesTags: (result, error, id) => [{ type: 'Company', id }],
    }),

    createCompany: builder.mutation<{ message: string }, CreateCompanyRequest>({
      query: (body) => ({
        url: '/api/admin/companies',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'Company', id: 'LIST' }],
    }),

    updateCompany: builder.mutation<Company, { id: string; data: UpdateCompanyRequest }>({
      query: ({ id, data }) => ({
        url: `/api/admin/companies/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'Company', id },
        { type: 'Company', id: 'LIST' },
      ],
    }),

    deactivateCompany: builder.mutation<{ message: string }, string>({
      query: (id) => ({
        url: `/api/admin/companies/${id}`,
        method: 'DELETE',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'Company', id },
        { type: 'Company', id: 'LIST' },
      ],
    }),

    assignUserToCompany: builder.mutation<
      { message: string },
      { userId: string; companyId: string; role: string }
    >({
      query: (body) => ({
        url: '/api/admin/companies/assign-user',
        method: 'POST',
        body,
      }),
      invalidatesTags: [
        { type: 'Company', id: 'LIST' },
      ],
    }),
  }),
})

export const {
  useGetCompaniesQuery,
  useGetCompanyByIdQuery,
  useCreateCompanyMutation,
  useUpdateCompanyMutation,
  useDeactivateCompanyMutation,
  useAssignUserToCompanyMutation,
} = companiesApi
