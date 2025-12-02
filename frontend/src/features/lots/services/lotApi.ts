// src/services/lotApi.ts
import axios from 'axios'
import Cookies from 'js-cookie'

const BASE = 'http://localhost:8000'

const api = axios.create({
  baseURL: BASE,
  headers: {
    Accept: 'application/json',
    'Content-Type': 'application/json',
  },
  withCredentials: true, // necesario para cookies
})

// Interceptor para añadir el token de cookie en Authorization
api.interceptors.request.use((config) => {
  const token = Cookies.get('token')
  if (token) {
    config.headers = config.headers ?? {}
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

export default api
