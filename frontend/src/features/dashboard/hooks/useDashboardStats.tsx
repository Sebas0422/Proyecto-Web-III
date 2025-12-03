import { useMemo } from 'react'
import { Building2, Grid, Users, BookmarkCheck, DollarSign, CreditCard, TrendingUp, CheckCircle } from 'lucide-react'
import type { StatData } from '@shared/components/StatCard'
import type { DashboardMetrics } from '../types'

const formatCurrency = (amount: number, currency: string = 'BOB') => {
  return `${currency} ${amount.toFixed(2)}`
}

export const useDashboardStats = (metrics?: DashboardMetrics): StatData[] => {
  return useMemo(() => {
    if (!metrics) return []

    return [
      {
        title: 'Proyectos Totales',
        value: metrics.totalProjects,
        change: `${metrics.activeProjects} activos`,
        icon: <Building2 size={24} />,
        iconBgColor: 'bg-blue-100',
        iconColor: 'text-blue-600',
      },
      {
        title: 'Lotes Totales',
        value: metrics.totalLots,
        change: `${metrics.availableLots} disponibles`,
        icon: <Grid size={24} />,
        iconBgColor: 'bg-green-100',
        iconColor: 'text-green-600',
      },
      {
        title: 'Lotes Vendidos',
        value: metrics.soldLots,
        change: `${metrics.reservedLots} reservados`,
        icon: <CheckCircle size={24} />,
        iconBgColor: 'bg-emerald-100',
        iconColor: 'text-emerald-600',
      },
      {
        title: 'Leads Totales',
        value: metrics.totalLeads,
        change: `${metrics.activeLeads} activos`,
        icon: <Users size={24} />,
        iconBgColor: 'bg-yellow-100',
        iconColor: 'text-yellow-600',
      },
      {
        title: 'Tasa de Conversión',
        value: `${metrics.conversionRate.toFixed(1)}%`,
        change: `${metrics.convertedLeads} convertidos`,
        icon: <TrendingUp size={24} />,
        iconBgColor: 'bg-orange-100',
        iconColor: 'text-orange-600',
      },
      {
        title: 'Reservas Totales',
        value: metrics.totalReservations,
        change: `${metrics.confirmedReservations} confirmadas`,
        icon: <BookmarkCheck size={24} />,
        iconBgColor: 'bg-purple-100',
        iconColor: 'text-purple-600',
      },
      {
        title: 'Reservas Pendientes',
        value: metrics.pendingReservations,
        change: `De ${metrics.totalReservations} totales`,
        icon: <BookmarkCheck size={24} />,
        iconBgColor: 'bg-indigo-100',
        iconColor: 'text-indigo-600',
      },
      {
        title: 'Ingresos Totales',
        value: formatCurrency(metrics.totalRevenue, metrics.currency),
        change: `Confirmados: ${formatCurrency(metrics.confirmedPayments, metrics.currency)}`,
        icon: <DollarSign size={24} />,
        iconBgColor: 'bg-green-100',
        iconColor: 'text-green-600',
      },
      {
        title: 'Pagos Pendientes',
        value: formatCurrency(metrics.pendingPayments, metrics.currency),
        change: `Total: ${formatCurrency(metrics.totalRevenue, metrics.currency)}`,
        icon: <CreditCard size={24} />,
        iconBgColor: 'bg-red-100',
        iconColor: 'text-red-600',
      },
      {
        title: 'Pagos Confirmados',
        value: formatCurrency(metrics.confirmedPayments, metrics.currency),
        change: `Pendientes: ${formatCurrency(metrics.pendingPayments, metrics.currency)}`,
        icon: <CheckCircle size={24} />,
        iconBgColor: 'bg-teal-100',
        iconColor: 'text-teal-600',
      },
    ]
  }, [metrics])
}
