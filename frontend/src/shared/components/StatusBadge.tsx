import React from 'react'

export type ProjectStatus = 'Activo' | 'En Desarrollo' | 'Pausado' | 'Cerrado'

interface StatusBadgeProps {
  status: ProjectStatus
}

const StatusBadge: React.FC<StatusBadgeProps> = ({ status }) => {
  const styles: Record<ProjectStatus, string> = {
    Activo: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300',
    'En Desarrollo': 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300',
    Pausado: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300',
    Cerrado: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300',
  }

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[status]}`}>
      {status}
    </span>
  )
}

export default StatusBadge
