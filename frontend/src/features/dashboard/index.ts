export { default as DashboardHeader } from './components/DashboardHeader'
export { default as StatsGrid } from './components/StatsGrid'
export { default as ActivityTable } from './components/ActivityTable'
export { default as LoadingState } from './components/LoadingState'
export { default as ErrorState } from './components/ErrorState'

export { useDashboardStats } from './hooks'

export { dashboardApi, useGetDashboardMetricsQuery } from './services/dashboardApi'

export type { DashboardMetrics, ActivityItem } from './types'

export { default as DashboardPage } from './pages/DashboardPage'
