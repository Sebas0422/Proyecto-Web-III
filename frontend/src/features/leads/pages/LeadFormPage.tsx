import { useNavigate, useLocation } from 'react-router-dom'
import { ArrowLeft, AlertCircle } from 'lucide-react'
import { useCreateLeadMutation } from '../services/leadsApi'
import LeadForm from '../components/LeadForm'
import { DashboardLayout } from '@shared/components'
import type { CreateLeadRequest } from '@shared/types'
import { useState } from 'react'

export default function LeadFormPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const [error, setError] = useState<string | null>(null)

  const preselectedLot = location.state?.lot
  const preselectedProject = location.state?.project

  const [createLead, { isLoading }] = useCreateLeadMutation()

  const handleSubmit = async (values: CreateLeadRequest) => {
    setError(null)
    try {
      await createLead(values).unwrap()
      navigate('/leads')
    } catch (err) {
      console.error('Error al crear lead:', err)
      const errorMessage = (err as { data?: { message?: string } })?.data?.message || 'Error al crear el lead'
      setError(errorMessage)
    }
  }

  const handleCancel = () => {
    navigate('/leads')
  }

  return (
    <DashboardLayout>
      <div className="max-w-3xl mx-auto p-6">
        <button
          onClick={handleCancel}
          className="flex items-center gap-2 text-gray-600 hover:text-gray-900 mb-6 transition-colors"
        >
          <ArrowLeft size={20} />
          <span>Volver</span>
        </button>

        <div className="bg-white rounded-lg shadow-md p-8">
          <div className="mb-6">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Nuevo Lead</h1>
            <p className="text-gray-600">
              Registra un cliente potencial interesado en tus proyectos
            </p>
          </div>

          {error && (
            <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg flex items-start gap-3">
              <AlertCircle size={20} className="text-red-600 flex-shrink-0 mt-0.5" />
              <div>
                <p className="text-red-800 font-medium">Error al guardar</p>
                <p className="text-red-600 text-sm">{error}</p>
              </div>
            </div>
          )}

          <LeadForm
            onSubmit={handleSubmit}
            onCancel={handleCancel}
            isLoading={isLoading}
            preselectedLot={preselectedLot}
            preselectedProject={preselectedProject}
          />
        </div>
      </div>
    </DashboardLayout>
  )
}
