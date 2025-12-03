import React from 'react'

interface ErrorStateProps {
  title?: string
  message?: string
  onRetry?: () => void
}

const ErrorState: React.FC<ErrorStateProps> = ({
  title = 'Error al cargar los datos',
  message = 'Por favor, intenta recargar la página',
  onRetry
}) => (
  <div className="flex items-center justify-center h-full min-h-[400px]">
    <div className="text-center max-w-md">
      <div className="mb-4">
        <svg className="mx-auto h-12 w-12 text-red-500" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
        </svg>
      </div>
      <p className="text-red-600 text-lg font-semibold mb-2">{title}</p>
      <p className="text-gray-600 mb-4">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="px-4 py-2 bg-amber-600 text-white rounded-lg hover:bg-amber-700 transition-colors"
        >
          Reintentar
        </button>
      )}
    </div>
  </div>
)

export default ErrorState
