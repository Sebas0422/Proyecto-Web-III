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
  <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200 transition-all hover:shadow-md">
    <div className="flex justify-between items-start">
      <div>
        <p className="text-sm font-medium text-gray-500">{data.title}</p>
        <p className="text-3xl font-bold text-gray-800 mt-1">{data.value}</p>
      </div>
      <div className={`p-2 rounded-md ${data.iconBgColor}`}>
        <div className={data.iconColor}>{data.icon}</div>
      </div>
    </div>
    <p className="text-xs text-gray-500 mt-4">{data.change}</p>
  </div>
)

export default StatCard
