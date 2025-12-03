export const UserRole = {
  ADMIN: 'ADMIN',
  EMPRESA: 'EMPRESA',
  CLIENTE: 'CLIENTE',
} as const

export type UserRole = typeof UserRole[keyof typeof UserRole]

export interface User {
  id: string
  fullName: string
  email: string
  role: UserRole
  tenantId?: string | null
}

export interface AuthState {
  token: string | null
  user: User | null
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  firstName: string
  lastName: string
  phoneNumber?: string
  role: UserRole
}

export interface LoginResponse {
  token: string
  userId: string
  email: string
  fullName: string
  role: UserRole
  tenantId?: string | null
}

export interface RegisterResponse {
  userId: string
  email: string
  fullName: string
  role: UserRole
  message: string
}

export interface LoginFormValues {
  email: string
  password: string
}

export interface RegisterFormValues {
  firstName: string
  lastName: string
  email: string
  password: string
  phoneNumber: string
  role: UserRole
}

export type ApiError = {
  data?: {
    message?: string
  }
  message?: string
  error?: string
}
