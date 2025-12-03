import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react'
import Cookies from 'js-cookie'

const baseUrl = import.meta.env.VITE_API_BASE || 'http://localhost:8000'

export const lotApi = createApi({
  reducerPath: 'lotApi',
  baseQuery: fetchBaseQuery({
    baseUrl,
    prepareHeaders: (headers) => {
      const token = Cookies.get('token')
      if (token) {
        headers.set('Authorization', `Bearer ${token}`)
      }
      headers.set('Accept', 'application/json')
      headers.set('Content-Type', 'application/json')
      return headers
    },
    credentials: 'include',
  }),
  tagTypes: ['Lot', 'Project'],
  endpoints: (builder) => ({
  }),
})
