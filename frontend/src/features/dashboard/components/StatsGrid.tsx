import React from 'react'
import StatCard, { type StatData } from '@shared/components/StatCard'

interface StatsGridProps {
  stats: StatData[]
}

const StatsGrid: React.FC<StatsGridProps> = ({ stats }) => (
  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6">
    {stats.map((stat, index) => (
      <StatCard key={index} data={stat} />
    ))}
  </div>
)

export default StatsGrid
