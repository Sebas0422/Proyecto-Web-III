import React from 'react'
import { Navigate } from 'react-router-dom'
import { useAppSelector } from '@app/store/hooks'
import type { RootState } from '@app/store/store'

interface Props {
  allowedRoles: string[]
  children: React.ReactNode
}

const RoleGuard: React.FC<Props> = ({ allowedRoles, children }) => {
  const user = useAppSelector((s: RootState) => s.auth.user)
  const hasRole = user && allowedRoles.includes(user.role || '')
  if (!user) return <Navigate to="/login" replace />
  if (!hasRole) return <Navigate to="/" replace />
  return <>{children}</>
}

export default RoleGuard
