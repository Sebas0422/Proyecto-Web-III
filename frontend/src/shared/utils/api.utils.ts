import type { ApiError } from '@shared/types'

export const extractErrorMessage = (error: unknown): string => {
  if (typeof error !== 'object' || error === null) {
    return 'Ha ocurrido un error inesperado'
  }

  const apiError = error as ApiError

  if (apiError.data?.message) return apiError.data.message
  if (apiError.message) return apiError.message
  if (apiError.error) return apiError.error

  if (apiError.status) {
    switch (apiError.status) {
      case 400:
        return 'Solicitud inválida. Verifica los datos ingresados'
      case 401:
        return 'No autorizado. Por favor, inicia sesión nuevamente'
      case 403:
        return 'No tienes permisos para realizar esta acción'
      case 404:
        return 'Recurso no encontrado'
      case 409:
        return 'Conflicto. El recurso ya existe'
      case 422:
        return 'Datos de validación incorrectos'
      case 500:
        return 'Error del servidor. Intenta nuevamente más tarde'
      case 503:
        return 'Servicio no disponible. Intenta más tarde'
      default:
        return `Error ${apiError.status}: Ocurrió un problema`
    }
  }

  return 'Ha ocurrido un error inesperado'
}

export const extractFieldErrors = (error: unknown): Record<string, string> | null => {
  if (typeof error !== 'object' || error === null) return null

  const apiError = error as ApiError
  if (!apiError.data?.errors) return null

  const fieldErrors: Record<string, string> = {}

  Object.entries(apiError.data.errors).forEach(([field, messages]) => {
    if (Array.isArray(messages) && messages.length > 0) {
      fieldErrors[field] = messages[0]
    }
  })

  return Object.keys(fieldErrors).length > 0 ? fieldErrors : null
}

export const isAuthError = (error: unknown): boolean => {
  if (typeof error !== 'object' || error === null) return false
  const apiError = error as ApiError
  return apiError.status === 401
}

export const isPermissionError = (error: unknown): boolean => {
  if (typeof error !== 'object' || error === null) return false
  const apiError = error as ApiError
  return apiError.status === 403
}

export const isValidationError = (error: unknown): boolean => {
  if (typeof error !== 'object' || error === null) return false
  const apiError = error as ApiError
  return apiError.status === 422 || apiError.status === 400
}
export const formatCurrency = (amount: number, currency: string = 'BOB'): string => {
  return new Intl.NumberFormat('es-BO', {
    style: 'currency',
    currency: currency,
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  }).format(amount)
}

export const formatArea = (area: number): string => {
  return `${area.toLocaleString('es-BO', { maximumFractionDigits: 2 })} m²`
}

export const formatRelativeTime = (date: string | Date): string => {
  const now = new Date()
  const past = new Date(date)
  const diffInMs = now.getTime() - past.getTime()
  const diffInMinutes = Math.floor(diffInMs / 60000)
  const diffInHours = Math.floor(diffInMinutes / 60)
  const diffInDays = Math.floor(diffInHours / 24)

  if (diffInMinutes < 1) return 'Hace un momento'
  if (diffInMinutes < 60) return `Hace ${diffInMinutes} minuto${diffInMinutes > 1 ? 's' : ''}`
  if (diffInHours < 24) return `Hace ${diffInHours} hora${diffInHours > 1 ? 's' : ''}`
  if (diffInDays < 7) return `Hace ${diffInDays} día${diffInDays > 1 ? 's' : ''}`

  return past.toLocaleDateString('es-BO', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

export const formatDate = (date: string | Date): string => {
  return new Date(date).toLocaleDateString('es-BO', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

export const validateFile = (
  file: File,
  maxSize: number,
  allowedTypes: string[]
): { valid: boolean; error?: string } => {
  if (file.size > maxSize) {
    return {
      valid: false,
      error: `El archivo es demasiado grande. Máximo ${Math.floor(maxSize / 1024 / 1024)}MB`,
    }
  }

  const isValidType = allowedTypes.some(type => {
    if (type.startsWith('.')) {
      return file.name.toLowerCase().endsWith(type)
    }
    return file.type === type
  })

  if (!isValidType) {
    return {
      valid: false,
      error: 'Tipo de archivo no permitido',
    }
  }

  return { valid: true }
}

export const buildQueryParams = (params: Record<string, unknown>): string => {
  const searchParams = new URLSearchParams()

  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null && value !== '') {
      searchParams.append(key, String(value))
    }
  })

  const queryString = searchParams.toString()
  return queryString ? `?${queryString}` : ''
}

export const debounce = <T extends (...args: unknown[]) => unknown>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: ReturnType<typeof setTimeout> | null = null

  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout)
    timeout = setTimeout(() => func(...args), wait)
  }
}
