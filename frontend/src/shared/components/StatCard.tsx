import React from 'react'

export interface StatData {
  title: string
  value: string | number
  change: string
  icon: React.ReactNode
  iconBgColor: string
  iconColor: string
}

interface StatCardProps {
  data: StatData
}

const StatCard: React.FC<StatCardProps> = ({ data }) => (
  <div className="bg-white dark:bg-slate-900 p-6 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800 transition-all hover:shadow-md">
    <div className="flex justify-between items-start">
      <div>
        <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{data.title}</p>
        <p className="text-3xl font-bold text-slate-800 dark:text-white mt-1">{data.value}</p>
      </div>
      <div className={`p-2 rounded-md ${data.iconBgColor}`}>
        <div className={data.iconColor}>{data.icon}</div>
      </div>
    </div>
    <p className="text-xs text-slate-500 dark:text-slate-400 mt-4">{data.change}</p>
  </div>
)

export default StatCard
