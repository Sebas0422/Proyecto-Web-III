import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import { VITE_API_BASE } from '@app/config/env'
import Cookies from 'js-cookie'
import type { LoginRequest, RegisterRequest, LoginResponse, RegisterResponse } from '../types/auth.types'

export const authApi = createApi({
  reducerPath: 'authApi',
  baseQuery: fetchBaseQuery({
    baseUrl: VITE_API_BASE,
    prepareHeaders: (headers) => {
      const token = Cookies.get('token')
      if (token) headers.set('Authorization', `Bearer ${token}`)
      return headers
    },
  }),
  endpoints: (builder) => ({
    login: builder.mutation<LoginResponse, LoginRequest>({
      query: (body) => ({
        url: '/api/auth/login',
        method: 'POST',
        body,
      }),
    }),
    register: builder.mutation<RegisterResponse, RegisterRequest>({
      query: (body) => ({
        url: '/api/auth/register',
        method: 'POST',
        body,
      }),
    }),
  }),
})

export const { useLoginMutation, useRegisterMutation } = authApi
