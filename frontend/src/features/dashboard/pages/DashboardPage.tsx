import React from 'react'
import { Building2, Grid, Users, BookmarkCheck } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import StatCard, { type StatData } from '@shared/components/StatCard'

interface ActivityData {
  user: string
  action: string
  element: string
  elementColorClass: string
  date: string
}

const statsData: StatData[] = [
  {
    title: 'Proyectos Activos',
    value: 12,
    change: '+2 este mes',
    icon: <Building2 size={24} />,
    iconBgColor: 'bg-blue-100 dark:bg-slate-800',
    iconColor: 'text-blue-600',
  },
  {
    title: 'Lotes Disponibles',
    value: 256,
    change: '15 vendidos recientemente',
    icon: <Grid size={24} />,
    iconBgColor: 'bg-green-100 dark:bg-slate-800',
    iconColor: 'text-green-600',
  },
  {
    title: 'Nuevos Leads',
    value: 89,
    change: 'En la última semana',
    icon: <Users size={24} />,
    iconBgColor: 'bg-yellow-100 dark:bg-slate-800',
    iconColor: 'text-yellow-600',
  },
  {
    title: 'Reservas Confirmadas',
    value: 34,
    change: '5 pendientes de pago',
    icon: <BookmarkCheck size={24} />,
    iconBgColor: 'bg-purple-100 dark:bg-slate-800',
    iconColor: 'text-purple-600',
  },
]

const activityData: ActivityData[] = [
  {
    user: 'Carlos López',
    action: 'Agregó un nuevo lead',
    element: 'Juan Rodríguez',
    elementColorClass: 'text-blue-600',
    date: 'Hace 5 minutos',
  },
  {
    user: 'María García',
    action: 'Actualizó el estado del lote',
    element: 'A-12, "El Roble"',
    elementColorClass: 'text-blue-600',
    date: 'Hace 2 horas',
  },
  {
    user: 'Ana Pérez',
    action: 'Generó un reporte de ventas',
    element: 'Reporte Mensual',
    elementColorClass: 'text-blue-600',
    date: 'Ayer',
  },
]

const DashboardPage: React.FC = () => {
  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 dark:text-white mb-2">Dashboard</h1>
          <p className="text-slate-600 dark:text-slate-400">Resumen general de actividades</p>
        </div>

        {/* Stats Grid */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          {statsData.map((stat, index) => (
            <StatCard key={index} data={stat} />
          ))}
        </div>

        {/* Activity Table */}
        <div className="bg-white dark:bg-slate-900 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800">
          <div className="p-6 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
            <h2 className="text-lg font-semibold text-slate-800 dark:text-white">Actividad Reciente</h2>
            <button className="text-blue-600 dark:text-blue-400 hover:text-blue-800 text-sm font-medium">
              Ver todo
            </button>
          </div>
          <div className="overflow-x-auto">
            <table className="w-full text-sm text-left">
              <thead className="text-xs text-slate-500 dark:text-slate-400 uppercase bg-slate-50 dark:bg-slate-800/50">
                <tr>
                  <th className="px-6 py-3">Usuario</th>
                  <th className="px-6 py-3">Acción</th>
                  <th className="px-6 py-3">Elemento</th>
                  <th className="px-6 py-3">Fecha</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-200 dark:divide-slate-800">
                {activityData.map((item, index) => (
                  <tr key={index} className="hover:bg-slate-50 dark:hover:bg-slate-800/50">
                    <td className="px-6 py-4 font-medium text-slate-800 dark:text-white">{item.user}</td>
                    <td className="px-6 py-4 text-slate-600 dark:text-slate-300">{item.action}</td>
                    <td className={`px-6 py-4 font-medium ${item.elementColorClass}`}>{item.element}</td>
                    <td className="px-6 py-4 text-slate-500 dark:text-slate-400">{item.date}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </DashboardLayout>
  )
}

export default DashboardPage
