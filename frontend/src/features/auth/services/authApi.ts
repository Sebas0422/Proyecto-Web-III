import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import { VITE_API_BASE } from '@app/config/env'

export interface LoginPayload {
  email: string
  password: string
}

export interface RegisterPayload {
  name: string
  email: string
  password: string
}

export interface AuthResponse {
  token: string
  user: {
    id: string
    name: string
    email: string
    role?: string
  }
}

export const authApi = createApi({
  reducerPath: 'authApi',
  baseQuery: fetchBaseQuery({
    baseUrl: VITE_API_BASE,
    prepareHeaders: (headers) => {
      const token = localStorage.getItem('token')
      if (token) headers.set('Authorization', `Bearer ${token}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    login: builder.mutation<AuthResponse, LoginPayload>({
      query: (body) => ({
        url: '/auth/login',
        method: 'POST',
        body,
      }),
    }),
    register: builder.mutation<AuthResponse, RegisterPayload>({
      query: (body) => ({
        url: '/auth/register',
        method: 'POST',
        body,
      }),
    }),
  }),
})

export const { useLoginMutation, useRegisterMutation } = authApi
