export const LotStatus = {
  DISPONIBLE: 'DISPONIBLE',
  RESERVADO: 'RESERVADO',
  VENDIDO: 'VENDIDO',
} as const

export type LotStatus = typeof LotStatus[keyof typeof LotStatus]

export const PaymentStatus = {
  PENDIENTE: 'PENDIENTE',
  COMPLETADO: 'COMPLETADO',
  FALLIDO: 'FALLIDO',
  RECHAZADO: 'RECHAZADO',
} as const

export type PaymentStatus = typeof PaymentStatus[keyof typeof PaymentStatus]

export const PaymentMethod = {
  QR: 'QR',
  BANK_TRANSFER: 'BANK_TRANSFER',
  CASH: 'CASH',
  CREDIT_CARD: 'CREDIT_CARD',
  DEBIT_CARD: 'DEBIT_CARD',
} as const

export type PaymentMethod = typeof PaymentMethod[keyof typeof PaymentMethod]

// Frontend usa valores en español, backend en inglés
export const LeadStatus = {
  NUEVO: 'NUEVO',
  CONTACTADO: 'CONTACTADO',
  INTERESADO: 'INTERESADO',
  NEGOCIANDO: 'NEGOCIANDO',
  CONVERTIDO: 'CONVERTIDO',
  PERDIDO: 'PERDIDO',
} as const

export type LeadStatus = typeof LeadStatus[keyof typeof LeadStatus]

// Mapeo Frontend (Español) → Backend (Inglés)
export const LeadStatusMap: Record<string, string> = {
  NUEVO: 'NEW',
  CONTACTADO: 'CONTACTED',
  INTERESADO: 'INTERESTED',
  NEGOCIANDO: 'NEGOTIATING',
  CONVERTIDO: 'CONVERTED',
  PERDIDO: 'LOST',
}

// Mapeo Backend (Inglés) → Frontend (Español)
export const LeadStatusMapReverse: Record<string, string> = {
  NEW: 'NUEVO',
  CONTACTED: 'CONTACTADO',
  INTERESTED: 'INTERESADO',
  NEGOTIATING: 'NEGOCIANDO',
  CONVERTED: 'CONVERTIDO',
  LOST: 'PERDIDO',
}

// ==================== RESERVATION STATUS ====================
// Estados de reserva en español (para UI)
export const ReservationStatus = {
  PENDIENTE: 'PENDIENTE',
  CONFIRMADA: 'CONFIRMADA',
  EXPIRADA: 'EXPIRADA',
  CANCELADA: 'CANCELADA',
} as const

export type ReservationStatus = typeof ReservationStatus[keyof typeof ReservationStatus]

// Mapeo Frontend (Español) → Backend (Inglés)
export const ReservationStatusMap: Record<string, string> = {
  PENDIENTE: 'PENDING',
  CONFIRMADA: 'CONFIRMED',
  EXPIRADA: 'EXPIRED',
  CANCELADA: 'CANCELLED',
}

// Mapeo Backend (Inglés) → Frontend (Español)
export const ReservationStatusMapReverse: Record<string, string> = {
  PENDING: 'PENDIENTE',
  CONFIRMED: 'CONFIRMADA',
  EXPIRED: 'EXPIRADA',
  CANCELLED: 'CANCELADA',
}

export interface Project {
  id: string
  tenantId: string
  nombre: string
  descripcion?: string
  ubicacion: string
  fechaInicio: string
  fechaEstimadaFinalizacion?: string
  activo: boolean
  createdAt: string
}

export interface Lot {
  id: string
  proyectoId: string
  numeroLote: string
  manzana?: string
  geometriaWKT: string
  areaCalculada?: number
  centroideWKT?: string
  precio: number
  estado: LotStatus
  observaciones?: string
  createdAt: string
}

export interface Lead {
  id: string
  tenantId: string
  projectId?: string
  lotId?: string
  customerName: string
  customerEmail?: string
  customerPhone: string
  customerDocument?: string
  status: LeadStatus
  source: string
  interestLevel: string
  notes?: string
  assignedTo?: string
  contactedAt?: string
  convertedAt?: string
  createdAt: string
}

export interface Reservation {
  id: string
  tenantId: string
  lotId: string
  projectId: string
  customerName: string
  customerEmail: string
  customerPhone: string
  customerDocument: string
  reservationAmount: number
  status: string
  reservationDate: string
  expirationDate: string
  confirmedAt?: string
  cancelledAt?: string
  notes?: string
  createdBy: string
  createdAt: string
}

export interface CreateReservationRequest {
  lotId: string
  projectId: string
  customerName: string
  customerEmail: string
  customerPhone: string
  customerDocument: string
  reservationAmount: number
  expirationDays: number
  notes?: string
}

export interface Payment {
  id: string
  tenantId: string
  reservationId: string
  customerName: string
  customerEmail: string
  customerPhone: string
  customerDocument: string
  amount: number
  currency: string
  paymentMethod: string
  status: string
  transactionReference?: string
  qrCodeData?: string
  paymentDate?: string
  expirationDate?: string
  confirmedAt?: string
  notes?: string
  createdBy?: string
  createdAt: string
}

export interface Receipt {
  id: string
  paymentId: string
  receiptNumber: string
  amount: number
  currency: string
  issuedAt: string
  pdfUrl?: string
}

export interface Company {
  id: string
  name: string
  ruc: string
  address?: string
  phoneNumber?: string
  email?: string
  logoUrl?: string
  isActive: boolean
}

export interface User {
  id: string
  email: string
  firstName: string
  lastName: string
  phoneNumber?: string
  role: 'ADMIN' | 'EMPRESA' | 'CLIENTE'
  isActive: boolean
  isEmailVerified: boolean
  companyId?: string
  companyName?: string
  createdAt: string
  updatedAt: string
}

export interface PaginationParams {
  page?: number
  size?: number
  sort?: string
}

export interface PaginatedResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export interface Coordinates {
  latitude: number
  longitude: number
}

export interface GeoJSONFeature {
  type: 'Feature'
  geometry: GeoJSON.Geometry
  properties: Record<string, unknown>
}
