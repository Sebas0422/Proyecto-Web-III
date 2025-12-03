import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, Mail, Phone, FileText, User, Calendar, Trash2, AlertCircle } from 'lucide-react'
import { useGetLeadByIdQuery, useDeleteLeadMutation, useUpdateLeadStatusMutation } from '../services/leadsApi'
import { DashboardLayout, LoadingSpinner } from '@shared/components'
import type { LeadStatus } from '@shared/types'
import { LeadStatusMap, LeadStatusMapReverse } from '@shared/types'
import { useState } from 'react'
const statusOptions: { value: LeadStatus; label: string; color: string }[] = [
  { value: 'NUEVO', label: 'Nuevo', color: 'bg-blue-100 text-blue-800' },
  { value: 'CONTACTADO', label: 'Contactado', color: 'bg-yellow-100 text-yellow-800' },
  { value: 'INTERESADO', label: 'Interesado', color: 'bg-purple-100 text-purple-800' },
  { value: 'NEGOCIANDO', label: 'Negociando', color: 'bg-indigo-100 text-indigo-800' },
  { value: 'CONVERTIDO', label: 'Convertido', color: 'bg-green-100 text-green-800' },
  { value: 'PERDIDO', label: 'Perdido', color: 'bg-gray-100 text-gray-800' }
]
const interestLevelLabels: Record<string, string> = {
  ALTO: 'Alto',
  MEDIO: 'Medio',
  BAJO: 'Bajo'
}
const interestLevelColors: Record<string, string> = {
  ALTO: 'bg-red-100 text-red-800',
  MEDIO: 'bg-orange-100 text-orange-800',
  BAJO: 'bg-blue-100 text-blue-800'
}
export default function LeadDetailPage() {
  const { leadId } = useParams<{ leadId: string }>()
  const navigate = useNavigate()
  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const { data: leadData, isLoading, error: fetchError } = useGetLeadByIdQuery(leadId!)
  const [deleteLead, { isLoading: isDeleting }] = useDeleteLeadMutation()
  const [updateStatus, { isLoading: isUpdatingStatus }] = useUpdateLeadStatusMutation()
  // Convertir status del backend (inglés) a frontend (español)
  const lead = leadData ? {
    ...leadData,
    status: LeadStatusMapReverse[leadData.status] || leadData.status,
  } : undefined
  const handleDelete = async () => {
    try {
      await deleteLead(leadId!).unwrap()
      navigate('/leads')
    } catch (err) {
      console.error('Error al eliminar lead:', err)
      setError('No se pudo eliminar el lead')
      setShowDeleteConfirm(false)
    }
  }
  const handleStatusChange = async (newStatus: LeadStatus) => {
    if (!lead || lead.status === newStatus) return
    setError(null)
    try {
      // Convertir de español a inglés para el backend
      const backendStatus = LeadStatusMap[newStatus]
      await updateStatus({ id: leadId!, data: { status: backendStatus } }).unwrap()
    } catch (err) {
      console.error('Error al actualizar estado:', err)
      setError('No se pudo actualizar el estado del lead')
    }
  }
  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="flex items-center justify-center min-h-[400px]">
          <LoadingSpinner size="lg" />
        </div>
      </DashboardLayout>
    )
  }
  if (fetchError || !lead) {
    return (
      <DashboardLayout>
        <div className="max-w-4xl mx-auto p-6">
          <div className="bg-red-50 border border-red-200 rounded-lg p-6 text-center">
            <AlertCircle size={48} className="text-red-500 mx-auto mb-4" />
            <h2 className="text-xl font-semibold text-red-800 mb-2">Lead no encontrado</h2>
            <p className="text-red-600 mb-4">El lead que buscas no existe o fue eliminado.</p>
            <button
              onClick={() => navigate('/leads')}
              className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
            >
              Volver a Leads
            </button>
          </div>
        </div>
      </DashboardLayout>
    )
  }
  const currentStatusOption = statusOptions.find(opt => opt.value === lead.status)
  return (
    <DashboardLayout>
      <div className="max-w-4xl mx-auto p-6">
        <button
          onClick={() => navigate('/leads')}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6 transition-colors"
        >
          <ArrowLeft size={20} />
          <span>Volver a Leads</span>
        </button>
        {error && (
          <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start gap-3">
            <AlertCircle size={20} className="text-red-600 flex-shrink-0 mt-0.5" />
            <div>
              <p className="text-red-800 font-medium">Error</p>
              <p className="text-red-600 text-sm">{error}</p>
            </div>
          </div>
        )}
        <div className="bg-white rounded-lg shadow-md p-8">
          <div className="flex items-start justify-between mb-6">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 mb-2">{lead.customerName}</h1>
              <div className="flex items-center gap-3">
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${currentStatusOption?.color}`}>
                  {currentStatusOption?.label}
                </span>
                <span className={`px-3 py-1 rounded-full text-sm font-medium ${interestLevelColors[lead.interestLevel]}`}>
                  Interés {interestLevelLabels[lead.interestLevel]}
                </span>
              </div>
            </div>
            <button
              onClick={() => setShowDeleteConfirm(true)}
              className="p-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
              title="Eliminar lead"
            >
              <Trash2 size={20} />
            </button>
          </div>
          <div className="mb-8">
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Cambiar Estado
            </label>
            <div className="flex gap-2 flex-wrap">
              {statusOptions.map(option => (
                <button
                  key={option.value}
                  onClick={() => handleStatusChange(option.value)}
                  disabled={isUpdatingStatus || lead.status === option.value}
                  className={`px-4 py-2 rounded-lg font-medium transition-all ${lead.status === option.value
                    ? option.color + ' ring-2 ring-offset-2 ring-blue-500'
                    : 'bg-gray-100 text-gray-700 hover:bg-gray-200'
                    } disabled:opacity-50 disabled:cursor-not-allowed`}
                >
                  {option.label}
                </button>
              ))}
            </div>
          </div>
          <div className="grid md:grid-cols-2 gap-6 mb-8">
            <div className="space-y-4">
              <h2 className="text-lg font-semibold text-gray-900 mb-3">Información de Contacto</h2>
              {lead.customerEmail && (
                <div className="flex items-start gap-3">
                  <Mail size={20} className="text-gray-400 mt-0.5" />
                  <div>
                    <p className="text-sm text-gray-500">Email</p>
                    <a
                      href={`mailto:${lead.customerEmail}`}
                      className="text-blue-600 hover:underline"
                    >
                      {lead.customerEmail}
                    </a>
                  </div>
                </div>
              )}
              {lead.customerPhone && (
                <div className="flex items-start gap-3">
                  <Phone size={20} className="text-gray-400 mt-0.5" />
                  <div>
                    <p className="text-sm text-gray-500">Teléfono</p>
                    <a
                      href={`tel:${lead.customerPhone}`}
                      className="text-blue-600 hover:underline"
                    >
                      {lead.customerPhone}
                    </a>
                  </div>
                </div>
              )}
              {lead.customerDocument && (
                <div className="flex items-start gap-3">
                  <User size={20} className="text-gray-400 mt-0.5" />
                  <div>
                    <p className="text-sm text-gray-500">Documento</p>
                    <p className="text-gray-900">{lead.customerDocument}</p>
                  </div>
                </div>
              )}
            </div>
            <div className="space-y-4">
              <h2 className="text-lg font-semibold text-gray-900 mb-3">Detalles</h2>
              <div className="flex items-start gap-3">
                <FileText size={20} className="text-gray-400 mt-0.5" />
                <div>
                  <p className="text-sm text-gray-500">Fuente</p>
                  <p className="text-gray-900">{lead.source}</p>
                </div>
              </div>
              <div className="flex items-start gap-3">
                <Calendar size={20} className="text-gray-400 mt-0.5" />
                <div>
                  <p className="text-sm text-gray-500">Registrado</p>
                  <p className="text-gray-900">
                    {new Date(lead.createdAt).toLocaleDateString('es-BO', {
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: '2-digit',
                      minute: '2-digit'
                    })}
                  </p>
                </div>
              </div>
              {lead.contactedAt && (
                <div className="flex items-start gap-3">
                  <Calendar size={20} className="text-gray-400 mt-0.5" />
                  <div>
                    <p className="text-sm text-gray-500">Contactado</p>
                    <p className="text-gray-900">
                      {new Date(lead.contactedAt).toLocaleDateString('es-BO', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </p>
                  </div>
                </div>
              )}
              {lead.convertedAt && (
                <div className="flex items-start gap-3">
                  <Calendar size={20} className="text-gray-400 mt-0.5" />
                  <div>
                    <p className="text-sm text-gray-500">Convertido</p>
                    <p className="text-gray-900">
                      {new Date(lead.convertedAt).toLocaleDateString('es-BO', {
                        year: 'numeric',
                        month: 'long',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </p>
                  </div>
                </div>
              )}
            </div>
          </div>
          {lead.notes && (
            <div className="border-t pt-6">
              <h2 className="text-lg font-semibold text-gray-900 mb-3">Notas</h2>
              <p className="text-gray-700 whitespace-pre-wrap">{lead.notes}</p>
            </div>
          )}
        </div>
        {showDeleteConfirm && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
            <div className="bg-white rounded-lg max-w-md w-full p-6">
              <h3 className="text-xl font-semibold text-gray-900 mb-3">
                ¿Eliminar Lead?
              </h3>
              <p className="text-gray-600 mb-6">
                Esta acción no se puede deshacer. Se eliminará el lead de <strong>{lead.customerName}</strong> permanentemente.
              </p>
              <div className="flex gap-3 justify-end">
                <button
                  onClick={() => setShowDeleteConfirm(false)}
                  disabled={isDeleting}
                  className="px-4 py-2 border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors disabled:opacity-50"
                >
                  Cancelar
                </button>
                <button
                  onClick={handleDelete}
                  disabled={isDeleting}
                  className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors disabled:opacity-50 flex items-center gap-2"
                >
                  {isDeleting ? (
                    <>
                      <LoadingSpinner size="sm" />
                      Eliminando...
                    </>
                  ) : (
                    'Eliminar'
                  )}
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}
