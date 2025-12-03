import React, { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { PlusCircle, CreditCard, XCircle } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import PermissionGuard from '@shared/components/PermissionGuard'
import { useGetReservationsQuery, useCancelReservationMutation } from '../services/reservationsApi'
import { useCreatePaymentMutation } from '@features/payments/services/paymentsApi'
import PaymentConfirmationModal from '@features/payments/components/PaymentConfirmationModal'
import { ReservationStatusMap, ReservationStatusMapReverse } from '@shared/types'
import type { Reservation, Payment } from '@shared/types'

const ReservationsListPage: React.FC = () => {
  const navigate = useNavigate()
  const [statusFilter, setStatusFilter] = useState<string>('')
  const [showPaymentModal, setShowPaymentModal] = useState(false)
  const [currentPayment, setCurrentPayment] = useState<Payment | null>(null)
  const [selectedReservation, setSelectedReservation] = useState<Reservation | null>(null)

  // Convertir filtro español → inglés
  const backendStatusFilter = statusFilter ? ReservationStatusMap[statusFilter] : undefined

  const { data: reservations, isLoading, error } = useGetReservationsQuery({ status: backendStatusFilter })
  const [createPayment, { isLoading: isCreatingPayment }] = useCreatePaymentMutation()
  const [cancelReservation] = useCancelReservationMutation()

  // Transformar reservas inglés → español
  const reservationsInSpanish = useMemo(() => {
    if (!reservations) return []
    return reservations.map((reservation) => ({
      ...reservation,
      status: ReservationStatusMapReverse[reservation.status] || reservation.status,
    }))
  }, [reservations])

  const handlePayReservation = async (reservation: Reservation, e: React.MouseEvent) => {
    e.stopPropagation()
    setSelectedReservation(reservation)

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

  const handlePaymentSuccess = () => {
    setShowPaymentModal(false)
    setCurrentPayment(null)
    setSelectedReservation(null)
    // Refrescar la lista automáticamente gracias a RTK Query
  }

  const handleCancel = async (id: string) => {
    if (window.confirm('¿Cancelar esta reserva? Esta acción no se puede deshacer.')) {
      try {
        await cancelReservation(id).unwrap()
      } catch (err) {
        console.error('Error al cancelar:', err)
        alert('Error al cancelar la reserva')
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
      <span className={`inline-flex items-center px-2.5 py-1 rounded text-xs font-semibold ${config.bg} ${config.text}`}>
        {config.label}
      </span>
    )
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('es-BO', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    })
  }

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('es-BO', {
      style: 'currency',
      currency: 'BOB',
    }).format(amount)
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 mb-2">Reservas</h1>
            <p className="text-slate-600">
              Gestión de todas las reservas de lotes
            </p>
          </div>

          {/* Todos pueden crear reservas */}
          <PermissionGuard permissions="reservations:create">
            <button
              onClick={() => navigate('/reservations/new')}
              className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition-colors"
            >
              <PlusCircle size={20} />
              Nueva Reserva
            </button>
          </PermissionGuard>
        </div>

        {/* Filtros */}
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex gap-3 flex-wrap">
            <button
              onClick={() => setStatusFilter('')}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${statusFilter === ''
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Todas
            </button>
            <button
              onClick={() => setStatusFilter('PENDIENTE')}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${statusFilter === 'PENDIENTE'
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Pendientes
            </button>
            <button
              onClick={() => setStatusFilter('CONFIRMADA')}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${statusFilter === 'CONFIRMADA'
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Confirmadas
            </button>
            <button
              onClick={() => setStatusFilter('EXPIRADA')}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${statusFilter === 'EXPIRADA'
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Expiradas
            </button>
            <button
              onClick={() => setStatusFilter('CANCELADA')}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${statusFilter === 'CANCELADA'
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Canceladas
            </button>
          </div>
        </div>

        {/* Lista */}
        {isLoading ? (
          <div className="text-center py-12">
            <p className="text-slate-600 ">Cargando reservas...</p>
          </div>
        ) : error ? (
          <div className="text-center py-12">
            <p className="text-red-600 ">Error al cargar las reservas</p>
          </div>
        ) : reservationsInSpanish.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-lg shadow">
            <p className="text-slate-600 ">No se encontraron reservas</p>
          </div>
        ) : (
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-slate-200">
              <thead className="bg-slate-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Cliente
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Contacto
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Monto
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Estado
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Fecha Expiración
                  </th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-slate-500  uppercase tracking-wider">
                    Acciones
                  </th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-slate-200">
                {reservationsInSpanish.map((reservation) => (
                  <tr
                    key={reservation.id}
                    className="hover:bg-slate-50 cursor-pointer transition-colors"
                    onClick={() => navigate(`/reservations/${reservation.id}`)}
                  >
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-slate-900 ">
                        {reservation.customerName}
                      </div>
                      <div className="text-sm text-slate-500 ">
                        {reservation.customerDocument}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm text-slate-900 ">
                        {reservation.customerEmail}
                      </div>
                      <div className="text-sm text-slate-500 ">
                        {reservation.customerPhone}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      <div className="text-sm font-medium text-slate-900 ">
                        {formatCurrency(reservation.reservationAmount)}
                      </div>
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap">
                      {getStatusBadge(reservation.status)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-slate-900 ">
                      {formatDate(reservation.expirationDate)}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <div className="flex gap-2" onClick={(e) => e.stopPropagation()}>
                        {/* Solo EMPRESA y ADMIN pueden pagar/cancelar */}
                        <PermissionGuard permissions={['reservations:confirm', 'reservations:cancel']}>
                          {reservation.status === 'PENDIENTE' && (
                            <>
                              <button
                                onClick={(e) => handlePayReservation(reservation, e)}
                                disabled={isCreatingPayment}
                                className="text-blue-600 hover:text-blue-900 disabled:text-slate-400"
                                title="Pagar con QR"
                              >
                                <CreditCard size={20} />
                              </button>
                              <button
                                onClick={(e) => {
                                  e.stopPropagation()
                                  handleCancel(reservation.id)
                                }}
                                className="text-red-600 hover:text-red-900"
                                title="Cancelar"
                              >
                                <XCircle size={20} />
                              </button>
                            </>
                          )}
                        </PermissionGuard>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div >
        )}

        {/* Total */}
        {
          reservationsInSpanish.length > 0 && (
            <div className="text-sm text-slate-600 ">
              Total: {reservationsInSpanish.length} reserva(s)
            </div>
          )
        }
      </div >

      {/* Modal de Pago */}
      {showPaymentModal && currentPayment && selectedReservation && (
        <PaymentConfirmationModal
          payment={currentPayment}
          onClose={() => setShowPaymentModal(false)}
          onSuccess={handlePaymentSuccess}
        />
      )}
    </DashboardLayout >
  )
}

export default ReservationsListPage
