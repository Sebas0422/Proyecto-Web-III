import React, { useMemo, useState } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { ArrowLeft, CheckCircle, XCircle, Calendar, DollarSign, User, FileText, CreditCard } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import PermissionGuard from '@shared/components/PermissionGuard'
import { useGetReservationByIdQuery, useConfirmReservationMutation, useCancelReservationMutation } from '../services/reservationsApi'
import { useCreatePaymentMutation, useGetPaymentsQuery } from '@features/payments/services/paymentsApi'
import PaymentConfirmationModal from '@features/payments/components/PaymentConfirmationModal'
import { ReservationStatusMapReverse } from '@shared/types'
import type { Payment } from '@shared/types'

const ReservationDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const [showPaymentModal, setShowPaymentModal] = useState(false)
  const [currentPayment, setCurrentPayment] = useState<Payment | null>(null)

  const { data: reservationData, isLoading, error, refetch } = useGetReservationByIdQuery(id!)
  const { data: payments = [] } = useGetPaymentsQuery({ reservationId: id })
  const [createPayment, { isLoading: isCreatingPayment }] = useCreatePaymentMutation()
  const [confirmReservation, { isLoading: isConfirming }] = useConfirmReservationMutation()
  const [cancelReservation, { isLoading: isCancelling }] = useCancelReservationMutation()

  const reservation = useMemo(() => {
    if (!reservationData) return undefined
    return {
      ...reservationData,
      status: ReservationStatusMapReverse[reservationData.status] || reservationData.status,
    }
  }, [reservationData])

  // Buscar pago pendiente asociado a esta reserva
  const pendingPayment = useMemo(() => {
    return payments.find(p => p.status === 'PENDIENTE')
  }, [payments])

  const handleCreatePayment = async () => {
    if (!reservation) return

    try {
      const payment = await createPayment({
        reservationId: reservation.id,
        customerName: reservation.customerName,
        customerEmail: reservation.customerEmail,
        customerPhone: reservation.customerPhone,
        customerDocument: reservation.customerDocument,
        amount: reservation.reservationAmount,
        currency: 'BOB',
        paymentMethod: 'QR',
        expirationHours: 24,
        notes: `Pago de reserva - Lote ${reservation.lotId}`,
      }).unwrap()

      setCurrentPayment(payment)
      setShowPaymentModal(true)
    } catch (err: any) {
      console.error('Error al crear pago:', err)
      alert(err?.data?.message || 'Error al crear el pago')
    }
  }

  const handleShowPaymentModal = () => {
    if (pendingPayment) {
      setCurrentPayment(pendingPayment)
      setShowPaymentModal(true)
    } else {
      handleCreatePayment()
    }
  }

  const handlePaymentSuccess = () => {
    refetch() // Recargar la reserva para ver el estado actualizado
  }

  const handleConfirm = async () => {
    if (window.confirm('¿Confirmar esta reserva?')) {
      try {
        await confirmReservation(id!).unwrap()
        alert('Reserva confirmada exitosamente')
      } catch (err: unknown) {
        const error = err as { data?: { message?: string } }
        console.error('Error al confirmar:', error)
        alert(error?.data?.message || 'Error al confirmar la reserva')
      }
    }
  }

  const handleCancel = async () => {
    if (window.confirm('¿Cancelar esta reserva? Esta acción no se puede deshacer.')) {
      try {
        await cancelReservation(id!).unwrap()
        alert('Reserva cancelada')
        navigate('/reservations')
      } catch (err: unknown) {
        const error = err as { data?: { message?: string } }
        console.error('Error al cancelar:', error)
        alert(error?.data?.message || 'Error al cancelar la reserva')
      }
    }
  }

  const getStatusBadge = (status: string) => {
    const statusConfig: Record<string, { bg: string; text: string; label: string }> = {
      PENDIENTE: { bg: 'bg-orange-500', text: 'text-white', label: 'Pendiente' },
      CONFIRMADA: { bg: 'bg-green-500', text: 'text-white', label: 'Confirmada' },
      EXPIRADA: { bg: 'bg-red-500', text: 'text-white', label: 'Expirada' },
      CANCELADA: { bg: 'bg-gray-500', text: 'text-white', label: 'Cancelada' },
    }
    const config = statusConfig[status] || { bg: 'bg-gray-500', text: 'text-white', label: status }
    return (
      <span className={`inline-flex items-center px-3 py-1.5 rounded text-sm font-semibold ${config.bg} ${config.text}`}>
        {config.label}
      </span>
    )
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-BO', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-BO', {
      style: 'currency',
      currency: 'BOB',
    }).format(amount)
  }

  if (isLoading) {
    return (
      <DashboardLayout>
        <div className="text-center py-12">
          <p className="text-slate-600">Cargando reserva...</p>
        </div>
      </DashboardLayout>
    )
  }

  if (error || !reservation) {
    return (
      <DashboardLayout>
        <div className="text-center py-12">
          <p className="text-red-600">Error al cargar la reserva</p>
          <button
            onClick={() => navigate('/reservations')}
            className="mt-4 text-blue-600 hover:text-blue-800"
          >
            Volver a reservas
          </button>
        </div>
      </DashboardLayout>
    )
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button
              onClick={() => navigate('/reservations')}
              className="text-slate-600 hover:text-slate-800"
            >
              <ArrowLeft size={24} />
            </button>
            <div>
              <h1 className="text-2xl font-bold text-slate-800">
                Detalle de Reserva
              </h1>
              <p className="text-slate-600">ID: {reservation.id}</p>
            </div>
          </div>
          <div>{getStatusBadge(reservation.status)}</div>
        </div>

        {/* Solo EMPRESA y ADMIN pueden confirmar/cancelar */}
        <PermissionGuard permissions={['reservations:confirm', 'reservations:cancel']}>
          {reservation.status === 'PENDIENTE' && (
            <div className="bg-white rounded-lg shadow p-4">
              <div className="flex flex-wrap gap-3">
                {/* Botón para pagar con QR */}
                <button
                  onClick={handleShowPaymentModal}
                  disabled={isCreatingPayment}
                  className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 disabled:bg-slate-400 text-white px-4 py-2 rounded-lg transition-colors"
                >
                  <CreditCard size={20} />
                  {isCreatingPayment ? 'Generando...' : pendingPayment ? 'Ver Código QR' : 'Pagar Reserva'}
                </button>

                <button
                  onClick={handleCancel}
                  disabled={isCancelling}
                  className="flex items-center gap-2 bg-red-600 hover:bg-red-700 disabled:bg-slate-400 text-white px-4 py-2 rounded-lg transition-colors"
                >
                  <XCircle size={20} />
                  {isCancelling ? 'Cancelando...' : 'Cancelar Reserva'}
                </button>
              </div>

              {pendingPayment && (
                <div className="mt-3 p-3 bg-blue-50 border border-blue-200 rounded-lg">
                  <p className="text-sm text-blue-800">
                    💡 <strong>Pago pendiente:</strong> Ya existe un QR generado para esta reserva.
                    Haz clic en "Ver Código QR" para completar el pago.
                  </p>
                </div>
              )}
            </div>
          )}
        </PermissionGuard>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center gap-2 mb-4">
            <User className="text-blue-600" size={24} />
            <h2 className="text-xl font-semibold text-slate-800">
              Información del Cliente
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p className="text-sm text-slate-500">Nombre Completo</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.customerName}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">Documento</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.customerDocument}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">Email</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.customerEmail}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">Teléfono</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.customerPhone}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center gap-2 mb-4">
            <DollarSign className="text-green-600" size={24} />
            <h2 className="text-xl font-semibold text-slate-800">
              Detalles Financieros
            </h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p className="text-sm text-slate-500">Monto de Reserva</p>
              <p className="text-2xl font-bold text-green-600">
                {formatCurrency(reservation.reservationAmount)}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">ID de Lote</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.lotId}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">ID de Proyecto</p>
              <p className="text-base font-medium text-slate-800">
                {reservation.projectId}
              </p>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <div className="flex items-center gap-2 mb-4">
            <Calendar className="text-purple-600" size={24} />
            <h2 className="text-xl font-semibold text-slate-800">Fechas</h2>
          </div>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <p className="text-sm text-slate-500">Fecha de Reserva</p>
              <p className="text-base font-medium text-slate-800">
                {formatDate(reservation.reservationDate)}
              </p>
            </div>
            <div>
              <p className="text-sm text-slate-500">Fecha de Expiración</p>
              <p className="text-base font-medium text-slate-800">
                {formatDate(reservation.expirationDate)}
              </p>
            </div>
            {reservation.confirmedAt && (
              <div>
                <p className="text-sm text-slate-500">Confirmada el</p>
                <p className="text-base font-medium text-green-600">
                  {formatDate(reservation.confirmedAt)}
                </p>
              </div>
            )}
            {reservation.cancelledAt && (
              <div>
                <p className="text-sm text-slate-500">Cancelada el</p>
                <p className="text-base font-medium text-red-600">
                  {formatDate(reservation.cancelledAt)}
                </p>
              </div>
            )}
            <div>
              <p className="text-sm text-slate-500">Creada el</p>
              <p className="text-base font-medium text-slate-800">
                {formatDate(reservation.createdAt)}
              </p>
            </div>
          </div>
        </div>

        {reservation.notes && (
          <div className="bg-white rounded-lg shadow p-6">
            <div className="flex items-center gap-2 mb-4">
              <FileText className="text-slate-600" size={24} />
              <h2 className="text-xl font-semibold text-slate-800">Notas</h2>
            </div>
            <p className="text-slate-700 whitespace-pre-wrap">
              {reservation.notes}
            </p>
          </div>
        )}
      </div>

      {/* Modal de confirmación de pago */}
      {showPaymentModal && currentPayment && (
        <PaymentConfirmationModal
          payment={currentPayment}
          onClose={() => setShowPaymentModal(false)}
          onSuccess={handlePaymentSuccess}
        />
      )}
    </DashboardLayout>
  )
}

export default ReservationDetailPage
