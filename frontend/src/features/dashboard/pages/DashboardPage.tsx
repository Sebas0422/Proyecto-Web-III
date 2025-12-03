import React from 'react'
import DashboardLayout from '@shared/components/DashboardLayout'
import DashboardHeader from '../components/DashboardHeader'
import StatsGrid from '../components/StatsGrid'
import LoadingState from '../components/LoadingState'
import ErrorState from '../components/ErrorState'
import { useGetDashboardMetricsQuery } from '../services/dashboardApi'
import { useDashboardStats } from '../hooks'

const DashboardPage: React.FC = () => {
  const { data: metrics, isLoading, error, refetch } = useGetDashboardMetricsQuery()
  const statsData = useDashboardStats(metrics)

  if (isLoading) {
    return (
      <DashboardLayout>
        <LoadingState />
      </DashboardLayout>
    )
  }

  if (error) {
    return (
      <DashboardLayout>
        <ErrorState onRetry={refetch} />
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <DashboardHeader
          title="Dashboard"
          subtitle="Resumen general de métricas y estadísticas"
        />

        <StatsGrid stats={statsData} />
      </div>
    </DashboardLayout>
  )
}

export default DashboardPage
