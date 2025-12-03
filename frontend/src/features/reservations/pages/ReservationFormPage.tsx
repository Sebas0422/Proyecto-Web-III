import React from 'react'
import { useNavigate } from 'react-router-dom'
import DashboardLayout from '@shared/components/DashboardLayout'
import ReservationForm from '../components/ReservationForm'
import { useCreateReservationMutation } from '../services/reservationsApi'
import type { CreateReservationRequest } from '@shared/types'

const ReservationFormPage: React.FC = () => {
  const navigate = useNavigate()
  const [createReservation, { isLoading }] = useCreateReservationMutation()

  const handleSubmit = async (values: CreateReservationRequest) => {
    try {
      await createReservation(values).unwrap()
      alert('Reserva creada exitosamente')
      navigate('/reservations')
    } catch (err: any) {
      console.error('Error al crear reserva:', err)
      alert(err?.data?.message || 'Error al crear la reserva')
    }
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div>
          <h1 className="text-2xl font-bold text-slate-800 mb-2">
            Nueva Reserva
          </h1>
          <p className="text-slate-600">
            Crear una nueva reserva de lote
          </p>
        </div>

        <ReservationForm onSubmit={handleSubmit} isLoading={isLoading} />
      </div>
    </DashboardLayout>
  )
}

export default ReservationFormPage
