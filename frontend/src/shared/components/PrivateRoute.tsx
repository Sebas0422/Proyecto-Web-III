import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAppSelector } from '@app/store/hooks'
import type { RootState } from '@app/store/store'

interface Props {
  children: React.ReactNode
}

const PrivateRoute: React.FC<Props> = ({ children }) => {
  const token = useAppSelector((s: RootState) => s.auth.token)
  if (!token) return <Navigate to="/login" replace />
  return <>{children}</>
}

export default PrivateRoute
