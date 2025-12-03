export interface ApiResponse<T> {
  success: boolean
  message: string | null
  data: T
  errorCode: string | null
}

export interface ApiError {
  data?: {
    message?: string
    errorCode?: string
    errors?: Record<string, string[]>
  }
  status?: number
  message?: string
  error?: string
}

export interface CreateProjectRequest {
  nombre: string
  descripcion?: string
  ubicacion: string
  fechaInicio: string
  fechaEstimadaFinalizacion?: string
}

export interface UpdateProjectRequest {
  nombre?: string
  descripcion?: string
  ubicacion?: string
  fechaInicio?: string
  fechaEstimadaFinalizacion?: string
  activo?: boolean
}

export interface CreateLotRequest {
  numeroLote: string
  manzana?: string
  geometriaWKT: string
  precio: number
  observaciones?: string
}

export interface UpdateLotRequest {
  numeroLote?: string
  manzana?: string
  precio?: number
  estado?: string
  observaciones?: string
}

export interface ImportKMLRequest {
  projectId: string
  file: File
  defaultPrice: number
  blockNumber?: string
}

export interface CreateLeadRequest {
  customerName: string
  customerEmail?: string
  customerPhone: string
  customerDocument?: string
  source: string
  interestLevel?: string
  notes?: string
  projectId?: string
  lotId?: string
}

export interface UpdateLeadStatusRequest {
  status: string
  notes?: string
}

export interface CreatePaymentRequest {
  reservationId: string
  customerName: string
  customerEmail: string
  customerPhone: string
  customerDocument: string
  amount: number
  currency: string
  paymentMethod: string
  expirationHours?: number
  notes?: string
}

export interface ConfirmPaymentRequest {
  transactionReference: string
}

export interface CreateCompanyRequest {
  name: string
  ruc: string
  address?: string
  phoneNumber?: string
  email?: string
}

export interface UpdateCompanyRequest {
  name: string
  address?: string
  phoneNumber?: string
  email?: string
  logoUrl?: string
}

export interface AssignUserRequest {
  userId: string
  companyId: string
  role: string
}

export interface ProjectFilters {
  activo?: boolean
  search?: string
}

export interface LotFilters {
  proyectoId?: string
  estado?: string
  manzana?: string
}

export interface LeadFilters {
  status?: string
  projectId?: string
  lotId?: string
  search?: string
}

export interface ReservationFilters {
  status?: string
  lotId?: string
  leadId?: string
}

export interface PaymentFilters {
  status?: string
  method?: string
  reservationId?: string
}

export interface ReportDateRange {
  startDate: string
  endDate: string
}

export interface CreateUserRequest {
  email: string
  password: string
  firstName: string
  lastName: string
  phoneNumber?: string
  role: 'ADMIN' | 'EMPRESA' | 'CLIENTE'
  companyId?: string
}

export interface UpdateUserRequest {
  firstName: string
  lastName: string
  phoneNumber?: string
  role?: 'ADMIN' | 'EMPRESA' | 'CLIENTE'
  companyId?: string
}

export interface ChangePasswordRequest {
  newPassword: string
}
