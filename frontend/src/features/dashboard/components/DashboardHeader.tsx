import React from 'react'

interface DashboardHeaderProps {
  title: string
  subtitle: string
}

const DashboardHeader: React.FC<DashboardHeaderProps> = ({ title, subtitle }) => (
  <div>
    <h1 className="text-2xl font-bold text-gray-800 mb-2">{title}</h1>
    <p className="text-gray-600">{subtitle}</p>
  </div>
)

export default DashboardHeader
