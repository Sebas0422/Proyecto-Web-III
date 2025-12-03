import React, { useState, useEffect } from 'react'
import { X, CheckCircle, Loader2, QrCode } from 'lucide-react'
import { useGetPaymentQRQuery, useConfirmPaymentMutation } from '../services/paymentsApi'
import type { Payment } from '@shared/types'

interface PaymentConfirmationModalProps {
  payment: Payment
  onClose: () => void
  onSuccess: () => void
}

const PaymentConfirmationModal: React.FC<PaymentConfirmationModalProps> = ({
  payment,
  onClose,
  onSuccess,
}) => {
  const [transactionRef, setTransactionRef] = useState('')
  const [showQR, setShowQR] = useState(true)
  const [qrImageUrl, setQrImageUrl] = useState<string>('')

  const { data: qrBlob, isLoading: loadingQR } = useGetPaymentQRQuery(payment.id)
  const [confirmPayment, { isLoading: confirming }] = useConfirmPaymentMutation()

  // Convertir Blob a URL de imagen
  useEffect(() => {
    if (qrBlob) {
      const url = URL.createObjectURL(qrBlob)
      setQrImageUrl(url)
      return () => URL.revokeObjectURL(url)
    }
  }, [qrBlob])

  const handleConfirm = async () => {
    if (!transactionRef.trim()) {
      alert('Por favor ingrese una referencia de transacción')
      return
    }

    try {
      await confirmPayment({
        paymentId: payment.id,
        data: { transactionReference: transactionRef },
      }).unwrap()

      alert('✅ Pago confirmado exitosamente. La reserva ha sido actualizada.')
      onSuccess()
      onClose()
    } catch (err: any) {
      console.error('Error al confirmar pago:', err)
      alert(err?.data?.message || 'Error al confirmar el pago')
    }
  }

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg shadow-xl max-w-2xl w-full max-h-[90vh] overflow-y-auto">
        {/* Header */}
        <div className="flex items-center justify-between p-6 border-b border-slate-200">
          <div className="flex items-center gap-3">
            <div className="p-2 bg-blue-100 rounded-lg">
              <QrCode className="text-blue-600" size={24} />
            </div>
            <div>
              <h2 className="text-xl font-bold text-slate-800">Confirmar Pago</h2>
              <p className="text-sm text-slate-600">Escanea el QR y confirma tu pago</p>
            </div>
          </div>
          <button
            onClick={onClose}
            className="p-2 hover:bg-slate-100 rounded-lg transition-colors"
            disabled={confirming}
          >
            <X size={20} className="text-slate-600" />
          </button>
        </div>

        {/* Content */}
        <div className="p-6 space-y-6">
          {/* Información del pago */}
          <div className="bg-slate-50 rounded-lg p-4 space-y-2">
            <div className="flex justify-between">
              <span className="text-sm text-slate-600">Monto a pagar:</span>
              <span className="text-lg font-bold text-slate-800">
                {payment.currency} {payment.amount.toFixed(2)}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="text-sm text-slate-600">Cliente:</span>
              <span className="text-sm font-medium text-slate-800">{payment.customerName}</span>
            </div>
            <div className="flex justify-between">
              <span className="text-sm text-slate-600">Email:</span>
              <span className="text-sm text-slate-800">{payment.customerEmail}</span>
            </div>
            {payment.expirationDate && (
              <div className="flex justify-between">
                <span className="text-sm text-slate-600">Válido hasta:</span>
                <span className="text-sm text-amber-600 font-medium">
                  {new Date(payment.expirationDate).toLocaleString('es-BO')}
                </span>
              </div>
            )}
          </div>

          {/* QR Code Section */}
          {showQR && (
            <div className="space-y-4">
              <div className="text-center">
                <h3 className="text-lg font-semibold text-slate-800 mb-2">
                  Escanea el código QR
                </h3>
                <p className="text-sm text-slate-600">
                  Utiliza tu aplicación de banca móvil para escanear el código y realizar el pago
                </p>
              </div>

              {/* QR Image */}
              <div className="flex justify-center p-6 bg-white border-2 border-dashed border-slate-300 rounded-lg">
                {loadingQR ? (
                  <div className="flex flex-col items-center gap-3 py-12">
                    <Loader2 className="animate-spin text-blue-600" size={48} />
                    <p className="text-sm text-slate-600">Generando código QR...</p>
                  </div>
                ) : qrImageUrl ? (
                  <img
                    src={qrImageUrl}
                    alt="Código QR de pago"
                    className="w-64 h-64 object-contain"
                  />
                ) : (
                  <div className="text-center py-12">
                    <QrCode className="mx-auto text-slate-400 mb-2" size={48} />
                    <p className="text-sm text-slate-600">No se pudo cargar el código QR</p>
                  </div>
                )}
              </div>

              <div className="bg-amber-50 border border-amber-200 rounded-lg p-4">
                <p className="text-sm text-amber-800">
                  <strong>Nota:</strong> Una vez que hayas realizado el pago, ingresa la referencia
                  de transacción y haz clic en "Ya realicé el pago" para confirmar.
                </p>
              </div>
            </div>
          )}

          {/* Divider */}
          <div className="relative">
            <div className="absolute inset-0 flex items-center">
              <div className="w-full border-t border-slate-300"></div>
            </div>
            <div className="relative flex justify-center text-sm">
              <span className="px-2 bg-white text-slate-500">Después de realizar el pago</span>
            </div>
          </div>

          {/* Confirmation Section */}
          <div className="space-y-4">
            <div>
              <label
                htmlFor="transactionRef"
                className="block text-sm font-medium text-slate-700 mb-2"
              >
                Referencia de Transacción *
              </label>
              <input
                id="transactionRef"
                type="text"
                value={transactionRef}
                onChange={(e) => setTransactionRef(e.target.value)}
                placeholder="Ej: TX-123456789"
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                disabled={confirming}
              />
              <p className="text-xs text-slate-500 mt-1">
                Ingresa el número de referencia que te proporcionó tu banco
              </p>
            </div>

            {payment.qrCodeData && (
              <div className="bg-blue-50 border border-blue-200 rounded-lg p-3">
                <p className="text-xs text-blue-800">
                  <strong>Datos del QR:</strong> {payment.qrCodeData}
                </p>
              </div>
            )}
          </div>
        </div>

        {/* Footer */}
        <div className="flex gap-3 p-6 border-t border-slate-200 bg-slate-50">
          <button
            onClick={onClose}
            className="flex-1 px-4 py-2 border border-slate-300 text-slate-700 rounded-lg hover:bg-slate-100 transition-colors"
            disabled={confirming}
          >
            Cancelar
          </button>
          <button
            onClick={handleConfirm}
            disabled={confirming || !transactionRef.trim()}
            className="flex-1 px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 disabled:bg-slate-300 disabled:cursor-not-allowed transition-colors flex items-center justify-center gap-2"
          >
            {confirming ? (
              <>
                <Loader2 className="animate-spin" size={20} />
                Confirmando...
              </>
            ) : (
              <>
                <CheckCircle size={20} />
                Ya realicé el pago
              </>
            )}
          </button>
        </div>
      </div>
    </div>
  )
}

export default PaymentConfirmationModal
