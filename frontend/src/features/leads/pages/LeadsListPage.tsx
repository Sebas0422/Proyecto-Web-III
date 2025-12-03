import { useNavigate } from 'react-router-dom'
import { PlusCircle, Mail, Phone, User, Trash2 } from 'lucide-react'
import { useGetLeadsQuery, useDeleteLeadMutation } from '../services/leadsApi'
import { LoadingSpinner, ErrorMessage, DashboardLayout, PermissionGuard } from '@shared/components'
import { formatRelativeTime } from '@shared/utils'
import { LeadStatusMap, LeadStatusMapReverse } from '@shared/types'
import { useState, useMemo } from 'react'

export default function LeadsListPage() {
  const navigate = useNavigate()
  const [statusFilter, setStatusFilter] = useState<string | undefined>(undefined)

  const { data: leads, isLoading, error } = useGetLeadsQuery({ status: statusFilter })
  const [deleteLead] = useDeleteLeadMutation()

  const handleDelete = async (leadId: string, customerName: string) => {
    const confirmed = window.confirm(
      `¿Estás seguro de eliminar el lead de "${customerName}"? Esta acción no se puede deshacer.`
    )

    if (confirmed) {
      try {
        await deleteLead(leadId).unwrap()
      } catch (err) {
        console.error('Error al eliminar lead:', err)
        alert('Error al eliminar el lead. Por favor, intenta de nuevo.')
      }
    }
  }

  const getStatusBadge = (status: string) => {
    const colors = {
      NUEVO: 'bg-blue-100 text-blue-800',
      CONTACTADO: 'bg-yellow-100 text-yellow-800',
      INTERESADO: 'bg-purple-100 text-purple-800',
      NEGOCIANDO: 'bg-indigo-100 text-indigo-800',
      CONVERTIDO: 'bg-green-100 text-green-800',
      PERDIDO: 'bg-gray-100 text-gray-800',
    }
    return colors[status as keyof typeof colors] || 'bg-gray-100 text-gray-800'
  }

  // Convertir leads del backend (inglés) a frontend (español)
  const leadsInSpanish = useMemo(() => {
    if (!leads) return []
    return leads.map(lead => ({
      ...lead,
      status: LeadStatusMapReverse[lead.status] || lead.status
    }))
  }, [leads])

  const getInterestBadge = (level: string) => {
    const colors = {
      HIGH: 'bg-red-100 text-red-800',
      MEDIUM: 'bg-orange-100 text-orange-800',
      LOW: 'bg-blue-100 text-blue-800',
    }
    const labels = {
      HIGH: 'Alto',
      MEDIUM: 'Medio',
      LOW: 'Bajo',
    }
    return { color: colors[level as keyof typeof colors] || 'bg-gray-100 text-gray-800', label: labels[level as keyof typeof labels] || level }
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 mb-2">Leads</h1>
            <p className="text-slate-600">
              Gestión de clientes potenciales
            </p>
          </div>

          {/* Todos pueden crear leads */}
          <PermissionGuard permissions="leads:create">
            <button
              onClick={() => navigate('/leads/new')}
              className="px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 flex items-center gap-2 transition-colors"
            >
              <PlusCircle size={20} />
              Nuevo Lead
            </button>
          </PermissionGuard>
        </div>

        <div className="bg-white rounded-lg shadow-md p-4">
          <div className="flex gap-2">
            <button
              onClick={() => setStatusFilter(undefined)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === undefined
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Todos
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.NUEVO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.NUEVO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Nuevos
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.CONTACTADO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.CONTACTADO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Contactados
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.INTERESADO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.INTERESADO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Interesados
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.NEGOCIANDO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.NEGOCIANDO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Negociando
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.CONVERTIDO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.CONVERTIDO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Convertidos
            </button>
            <button
              onClick={() => setStatusFilter(LeadStatusMap.PERDIDO)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors ${statusFilter === LeadStatusMap.PERDIDO
                ? 'bg-amber-600 text-white'
                : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                }`}
            >
              Perdidos
            </button>
          </div>
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <LoadingSpinner size="lg" />
          </div>
        ) : error ? (
          <ErrorMessage
            title="Error al cargar leads"
            message="No se pudieron cargar los leads. Por favor, intenta de nuevo."
            onRetry={() => window.location.reload()}
          />
        ) : !leadsInSpanish || leadsInSpanish.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-12 text-center">
            <User size={48} className="mx-auto text-gray-400 mb-4" />
            <p className="text-gray-500 mb-4">No hay leads registrados</p>
            <button
              onClick={() => navigate('/leads/new')}
              className="px-6 py-3 bg-amber-600 text-white rounded-lg hover:bg-amber-700 inline-flex items-center gap-2"
            >
              <PlusCircle size={20} />
              Crear Primer Lead
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-4">
            {leadsInSpanish.map((lead) => {
              const interest = getInterestBadge(lead.interestLevel)
              return (
                <div
                  key={lead.id}
                  className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg transition-shadow cursor-pointer border border-gray-200"
                  onClick={() => navigate(`/leads/${lead.id}`)}
                >
                  <div className="flex items-start justify-between mb-4">
                    <div className="flex-1">
                      <h3 className="text-lg font-semibold text-gray-900 mb-1">
                        {lead.customerName}
                      </h3>
                      <div className="flex gap-2 flex-wrap">
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusBadge(lead.status)}`}>
                          {lead.status}
                        </span>
                        <span className={`px-2 py-1 rounded-full text-xs font-medium ${interest.color}`}>
                          {interest.label}
                        </span>
                      </div>
                    </div>

                    {/* Solo EMPRESA y ADMIN pueden eliminar leads */}
                    <PermissionGuard permissions="leads:delete">
                      <button
                        onClick={(e) => {
                          e.stopPropagation()
                          handleDelete(lead.id, lead.customerName)
                        }}
                        className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
                        title="Eliminar"
                      >
                        <Trash2 size={18} />
                      </button>
                    </PermissionGuard>
                  </div>

                  <div className="space-y-2 text-sm">
                    {lead.customerEmail && (
                      <div className="flex items-center gap-2 text-gray-600">
                        <Mail size={16} className="text-gray-400" />
                        <span className="truncate">{lead.customerEmail}</span>
                      </div>
                    )}
                    <div className="flex items-center gap-2 text-gray-600">
                      <Phone size={16} className="text-gray-400" />
                      <span>{lead.customerPhone}</span>
                    </div>
                    {lead.source && (
                      <div className="text-gray-500 text-xs">
                        Fuente: <span className="font-medium">{lead.source}</span>
                      </div>
                    )}
                  </div>

                  {lead.notes && (
                    <p className="mt-3 text-sm text-gray-600 line-clamp-2">
                      {lead.notes}
                    </p>
                  )}

                  <div className="mt-4 pt-4 border-t border-gray-200 text-xs text-gray-500">
                    Creado {formatRelativeTime(lead.createdAt)}
                  </div>
                </div>
              )
            })}
          </div>
        )}

        {leadsInSpanish && leadsInSpanish.length > 0 && (
          <div className="bg-white rounded-lg shadow-md p-4">
            <p className="text-sm text-gray-600">
              Total de leads: <span className="font-semibold">{leadsInSpanish.length}</span>
            </p>
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}
