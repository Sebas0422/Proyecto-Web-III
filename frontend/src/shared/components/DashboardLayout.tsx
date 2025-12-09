import React, { useState, useMemo, type ReactNode } from 'react'
import {
  LayoutDashboard,
  Building2,
  Users,
  BookmarkCheck,
  BarChart3,
  LogOut,
  Bell,
  Menu,
  X,
  Home,
  Building,
  UserCog
} from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import { useAppDispatch, useAppSelector } from '@app/store/hooks'
import { logout } from '@features/auth/model/authSlice'
import SidebarItem from './SidebarItem'
import type { RootState } from '@app/store/store'
import { usePermissions } from '@shared/hooks'
import { PERMISSIONS } from '@shared/constants/permissions'

interface DashboardLayoutProps {
  children: ReactNode
}

const DashboardLayout: React.FC<DashboardLayoutProps> = ({ children }) => {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false)
  const navigate = useNavigate()
  const dispatch = useAppDispatch()
  const user = useAppSelector((s: RootState) => s.auth.user)
  const { hasPermission } = usePermissions()

  const handleLogout = () => {
    dispatch(logout())
    navigate('/login')
  }

  const menuItems = useMemo(() => {
    const allItems = [
      {
        icon: <LayoutDashboard size={20} />,
        label: 'Dashboard',
        path: '/dashboard',
        show: true
      },
      {
        icon: <Building size={20} />,
        label: 'Empresas',
        path: '/companies',
        show: hasPermission(PERMISSIONS.COMPANIES_VIEW)
      },
      {
        icon: <UserCog size={20} />,
        label: 'Usuarios',
        path: '/users',
        show: hasPermission(PERMISSIONS.USERS_VIEW)
      },
      {
        icon: <Building2 size={20} />,
        label: 'Proyectos',
        path: '/projects',
        show: hasPermission(PERMISSIONS.PROJECTS_VIEW)
      },
      {
        icon: <Users size={20} />,
        label: 'Leads',
        path: '/leads',
        show: hasPermission(PERMISSIONS.LEADS_VIEW)
      },
      {
        icon: <BookmarkCheck size={20} />,
        label: 'Reservas',
        path: '/reservations',
        show: hasPermission(PERMISSIONS.RESERVATIONS_VIEW)
      },
      {
        icon: <BarChart3 size={20} />,
        label: 'Reportes',
        path: '/reports',
        show: hasPermission(PERMISSIONS.REPORTS_VIEW_OWN)
      }
    ]

    return allItems.filter(item => item.show)
  }, [hasPermission])

  return (
    <div className="flex h-screen bg-gray-50 font-sans text-gray-700">
      {isSidebarOpen && (
        <div className="fixed inset-0 z-20 bg-black/50 lg:hidden" onClick={() => setIsSidebarOpen(false)} />
      )}

      <aside
        className={`
            fixed lg:static inset-y-0 left-0 z-30
            w-64 flex-shrink-0 bg-white 
            border-r border-gray-200 flex flex-col
            transform transition-transform duration-200 ease-in-out
            ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
          `}
      >
        <div className="h-16 flex items-center px-6 border-b border-gray-200">
          <div className="bg-amber-600 p-2 rounded-lg">
            <Home className="text-white" size={24} />
          </div>
          <span className="ml-3 text-lg font-bold text-gray-800">EstateHub</span>
          <button className="ml-auto lg:hidden text-gray-500" onClick={() => setIsSidebarOpen(false)}>
            <X size={20} />
          </button>
        </div>

        <nav className="flex-1 px-4 py-4 space-y-2 overflow-y-auto">
          {menuItems.map((item) => (
            <SidebarItem
              key={item.path}
              icon={item.icon}
              label={item.label}
              active={window.location.pathname === item.path}
              onClick={() => {
                navigate(item.path)
                setIsSidebarOpen(false)
              }}
            />
          ))}
        </nav>

        <div className="px-4 py-4 border-t border-gray-200">
          <SidebarItem icon={<LogOut size={20} />} label="Cerrar sesión" onClick={handleLogout} />
        </div>
      </aside>

      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-4 sm:px-6 flex-shrink-0 z-10">
          <div className="flex items-center gap-4">
            <button
              className="lg:hidden p-2 -ml-2 text-gray-500 hover:bg-gray-100 rounded-md"
              onClick={() => setIsSidebarOpen(true)}
            >
              <Menu size={24} />
            </button>
          </div>

          <div className="flex items-center gap-2 sm:gap-4">
            <button className="relative p-2 text-gray-500 hover:text-gray-700 transition-colors">
              <Bell size={20} />
              <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
                <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500"></span>
              </span>
            </button>

            <div className="h-8 w-px bg-gray-200 mx-1 hidden sm:block"></div>

            <div className="flex items-center gap-3">
              <div className="h-9 w-9 rounded-full bg-blue-600 flex items-center justify-center text-white font-semibold">
                {user?.fullName?.charAt(0).toUpperCase() || 'U'}
              </div>
              <div className="hidden sm:block">
                <p className="text-sm font-medium text-gray-800 leading-none">
                  {user?.fullName || 'Usuario'}
                </p>
                <p className="text-xs text-gray-500 mt-1">{user?.role || 'Usuario'}</p>
              </div>
            </div>
          </div>
        </header>

        <main className="flex-1 p-4 sm:p-6 overflow-y-auto">
          <div className="max-w-7xl mx-auto">{children}</div>
        </main>
      </div>
    </div>
  )
}

export default DashboardLayout
