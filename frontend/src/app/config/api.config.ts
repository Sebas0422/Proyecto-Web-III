const BASE_URL = import.meta.env.VITE_API_BASE || 'http://localhost:8000'

export const API_CONFIG = {
  baseURL: BASE_URL,
  timeout: 30000,

  endpoints: {
    auth: '/api/auth',
    projects: '/api/proyectos',
    lots: '/api/lotes',
    leads: '/api/leads',
    reservations: '/api/reservations',
    payments: '/api/payments',
    receipts: '/api/receipts',
    reports: '/api/reports',
    admin: '/api/admin',
  },

  googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_API_KEY || '',

  maxFileSize: Number(import.meta.env.VITE_MAX_FILE_SIZE) || 10485760,
  allowedFileTypes: {
    images: ['image/jpeg', 'image/png', 'image/webp'],
    documents: ['application/pdf'],
    kml: ['application/vnd.google-earth.kml+xml', '.kml'],
  },

  defaultPageSize: 20,
  pageSizeOptions: [10, 20, 50, 100],

  cacheTime: {
    short: 60,
    medium: 300,
    long: 3600,
  },
} as const

export default API_CONFIG