// Tipos para el dashboard
export interface DashboardMetrics {
  totalProjects: number
  activeProjects: number
  totalLots: number
  availableLots: number
  soldLots: number
  reservedLots: number
  totalLeads: number
  activeLeads: number
  convertedLeads: number
  conversionRate: number
  totalReservations: number
  confirmedReservations: number
  pendingReservations: number
  totalRevenue: number
  pendingPayments: number
  confirmedPayments: number
  currency: string
}

export interface ActivityItem {
  user: string
  action: string
  element: string
  elementColorClass: string
  date: string
}
