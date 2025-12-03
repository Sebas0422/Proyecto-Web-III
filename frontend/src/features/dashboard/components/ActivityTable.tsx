import React from 'react'
import type { ActivityItem } from '../types'

interface ActivityTableProps {
  activities: ActivityItem[]
  onViewAll?: () => void
}

const ActivityTable: React.FC<ActivityTableProps> = ({ activities, onViewAll }) => (
  <div className="bg-white rounded-lg shadow-sm border border-gray-200">
    <div className="p-6 border-b border-gray-200 flex justify-between items-center">
      <h2 className="text-lg font-semibold text-gray-800">Actividad Reciente</h2>
      {onViewAll && (
        <button
          onClick={onViewAll}
          className="text-amber-600 hover:text-amber-700 text-sm font-medium transition-colors"
        >
          Ver todo
        </button>
      )}
    </div>
    <div className="overflow-x-auto">
      <table className="w-full text-sm text-left">
        <thead className="text-xs text-gray-500 uppercase bg-gray-50">
          <tr>
            <th className="px-6 py-3">Usuario</th>
            <th className="px-6 py-3">Acción</th>
            <th className="px-6 py-3">Elemento</th>
            <th className="px-6 py-3">Fecha</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-gray-200">
          {activities.length > 0 ? (
            activities.map((item, index) => (
              <tr key={index} className="hover:bg-gray-50 transition-colors">
                <td className="px-6 py-4 font-medium text-gray-800">{item.user}</td>
                <td className="px-6 py-4 text-gray-600">{item.action}</td>
                <td className={`px-6 py-4 font-medium ${item.elementColorClass}`}>{item.element}</td>
                <td className="px-6 py-4 text-gray-500">{item.date}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan={4} className="px-6 py-8 text-center text-gray-500">
                No hay actividad reciente
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  </div>
)

export default ActivityTable
