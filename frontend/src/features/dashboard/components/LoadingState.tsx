import React from 'react'

interface LoadingStateProps {
  message?: string
}

const LoadingState: React.FC<LoadingStateProps> = ({ message = 'Cargando datos...' }) => (
  <div className="flex items-center justify-center h-full min-h-[400px]">
    <div className="text-center">
      <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-amber-600"></div>
      <p className="mt-4 text-gray-600">{message}</p>
    </div>
  </div>
)

export default LoadingState
