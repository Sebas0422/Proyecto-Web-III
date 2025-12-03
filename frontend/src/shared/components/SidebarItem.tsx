import React from 'react'

interface SidebarItemProps {
  icon: React.ReactNode
  label: string
  active?: boolean
  onClick?: () => void
}

const SidebarItem: React.FC<SidebarItemProps> = ({ icon, label, active = false, onClick }) => (
  <button
    onClick={onClick}
    className={`w-full flex items-center px-4 py-2 text-sm font-medium rounded-md transition-colors ${active
      ? 'bg-amber-50 text-amber-600'
      : 'text-gray-600 hover:bg-gray-100 hover:text-gray-900'
      }`}
  >
    <span className="mr-3">{icon}</span>
    {label}
  </button>
)

export default SidebarItem
