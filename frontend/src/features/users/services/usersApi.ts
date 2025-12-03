import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import type { User, CreateUserRequest, UpdateUserRequest, ChangePasswordRequest } from '@shared/types'
import API_CONFIG from '@app/config/api.config'
import type { RootState } from '@app/store/store'

export const usersApi = createApi({
  reducerPath: 'usersApi',
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
  tagTypes: ['User'],
  endpoints: (builder) => ({
    getUsers: builder.query<User[], { role?: string; isActive?: boolean }>({
      query: ({ role, isActive }) => {
        const params = new URLSearchParams()
        if (role) params.append('role', role)
        if (isActive !== undefined) params.append('isActive', String(isActive))
        return `/api/admin/users${params.toString() ? `?${params.toString()}` : ''}`
      },
      providesTags: (result) =>
        result
          ? [
            ...result.map(({ id }) => ({ type: 'User' as const, id })),
            { type: 'User', id: 'LIST' },
          ]
          : [{ type: 'User', id: 'LIST' }],
    }),

    getUserById: builder.query<User, string>({
      query: (id) => `/api/admin/users/${id}`,
      providesTags: (result, error, id) => [{ type: 'User', id }],
    }),

    createUser: builder.mutation<{ message: string; userId: string }, CreateUserRequest>({
      query: (body) => ({
        url: '/api/admin/users',
        method: 'POST',
        body,
      }),
      invalidatesTags: [{ type: 'User', id: 'LIST' }],
    }),

    updateUser: builder.mutation<{ message: string }, { id: string; data: UpdateUserRequest }>({
      query: ({ id, data }) => ({
        url: `/api/admin/users/${id}`,
        method: 'PUT',
        body: data,
      }),
      invalidatesTags: (result, error, { id }) => [
        { type: 'User', id },
        { type: 'User', id: 'LIST' },
      ],
    }),

    activateUser: builder.mutation<{ message: string }, string>({
      query: (id) => ({
        url: `/api/admin/users/${id}/activate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'User', id },
        { type: 'User', id: 'LIST' },
      ],
    }),

    deactivateUser: builder.mutation<{ message: string }, string>({
      query: (id) => ({
        url: `/api/admin/users/${id}/deactivate`,
        method: 'PUT',
      }),
      invalidatesTags: (result, error, id) => [
        { type: 'User', id },
        { type: 'User', id: 'LIST' },
      ],
    }),

    changePassword: builder.mutation<{ message: string }, { id: string; data: ChangePasswordRequest }>({
      query: ({ id, data }) => ({
        url: `/api/admin/users/${id}/change-password`,
        method: 'PUT',
        body: data,
      }),
    }),
  }),
})

export const {
  useGetUsersQuery,
  useGetUserByIdQuery,
  useCreateUserMutation,
  useUpdateUserMutation,
  useActivateUserMutation,
  useDeactivateUserMutation,
  useChangePasswordMutation,
} = usersApi
