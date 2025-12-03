import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { PlusCircle, Building2, Edit, Trash2, CheckCircle, XCircle } from 'lucide-react'
import DashboardLayout from '@shared/components/DashboardLayout'
import PermissionGuard from '@shared/components/PermissionGuard'
import { PERMISSIONS } from '@shared/constants/permissions'
import { useGetCompaniesQuery, useDeactivateCompanyMutation } from '../services/companiesApi'

const CompaniesListPage: React.FC = () => {
  const navigate = useNavigate()
  const [showInactive, setShowInactive] = useState(false)

  const { data: companies, isLoading, error } = useGetCompaniesQuery({ onlyActive: !showInactive })
  const [deactivateCompany] = useDeactivateCompanyMutation()

  const handleDeactivate = async (id: string, name: string) => {
    if (window.confirm(`¿Está seguro de desactivar la empresa "${name}"?`)) {
      try {
        await deactivateCompany(id).unwrap()
      } catch (err) {
        console.error('Error al desactivar:', err)
        alert('Error al desactivar la empresa')
      }
    }
  }

  return (
    <DashboardLayout>
      <div className="space-y-6">
        {/* Header */}
        <div className="flex justify-between items-center">
          <div>
            <h1 className="text-2xl font-bold text-slate-800 mb-2">Empresas</h1>
            <p className="text-slate-600">
              Gestión de empresas del sistema
            </p>
          </div>

          <PermissionGuard permissions={PERMISSIONS.COMPANIES_CREATE}>
            <button
              onClick={() => navigate('/companies/new')}
              className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg transition-colors"
            >
              <PlusCircle size={20} />
              Nueva Empresa
            </button>
          </PermissionGuard>
        </div>

        {/* Filtros */}
        <div className="bg-white rounded-lg shadow p-4">
          <div className="flex gap-3 flex-wrap">
            <button
              onClick={() => setShowInactive(false)}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${!showInactive
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Activas
            </button>
            <button
              onClick={() => setShowInactive(true)}
              className={`px-5 py-2.5 rounded-lg font-medium transition-colors ${showInactive
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-700 hover:bg-slate-200'
                }`}
            >
              Todas
            </button>
          </div>
        </div>

        {/* Lista */}
        {isLoading ? (
          <div className="text-center py-12">
            <p className="text-slate-600">Cargando empresas...</p>
          </div>
        ) : error ? (
          <div className="text-center py-12">
            <p className="text-red-600">Error al cargar las empresas</p>
          </div>
        ) : companies && companies.length === 0 ? (
          <div className="text-center py-12 bg-white rounded-lg shadow">
            <Building2 size={48} className="mx-auto text-slate-300 mb-4" />
            <p className="text-slate-600">No se encontraron empresas</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {companies?.map((company) => (
              <div
                key={company.id}
                className="bg-white rounded-lg shadow hover:shadow-lg transition-shadow p-6"
              >
                {/* Logo o ícono */}
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center gap-3">
                    {company.logoUrl ? (
                      <img
                        src={company.logoUrl}
                        alt={company.name}
                        className="w-12 h-12 rounded-lg object-cover"
                      />
                    ) : (
                      <div className="w-12 h-12 bg-blue-100 rounded-lg flex items-center justify-center">
                        <Building2 className="text-blue-600" size={24} />
                      </div>
                    )}
                    <div>
                      <h3 className="font-semibold text-slate-800">{company.name}</h3>
                      <p className="text-sm text-slate-500">RUC: {company.ruc}</p>
                    </div>
                  </div>
                  {company.isActive ? (
                    <CheckCircle className="text-green-500" size={20} />
                  ) : (
                    <XCircle className="text-red-500" size={20} />
                  )}
                </div>

                {/* Información */}
                <div className="space-y-2 mb-4">
                  {company.email && (
                    <p className="text-sm text-slate-600">
                      📧 {company.email}
                    </p>
                  )}
                  {company.phoneNumber && (
                    <p className="text-sm text-slate-600">
                      📞 {company.phoneNumber}
                    </p>
                  )}
                  {company.address && (
                    <p className="text-sm text-slate-600">
                      📍 {company.address}
                    </p>
                  )}
                </div>

                {/* Acciones */}
                <PermissionGuard permissions={[PERMISSIONS.COMPANIES_UPDATE, PERMISSIONS.COMPANIES_DELETE]}>
                  <div className="flex gap-2 pt-4 border-t">
                    <button
                      onClick={() => navigate(`/companies/${company.id}/edit`)}
                      className="flex-1 flex items-center justify-center gap-2 bg-slate-100 hover:bg-slate-200 text-slate-700 px-3 py-2 rounded-lg transition-colors"
                    >
                      <Edit size={16} />
                      Editar
                    </button>
                    {company.isActive && (
                      <button
                        onClick={() => handleDeactivate(company.id, company.name)}
                        className="flex items-center justify-center gap-2 bg-red-100 hover:bg-red-200 text-red-700 px-3 py-2 rounded-lg transition-colors"
                      >
                        <Trash2 size={16} />
                      </button>
                    )}
                  </div>
                </PermissionGuard>
              </div>
            ))}
          </div>
        )}

        {/* Total */}
        {companies && companies.length > 0 && (
          <div className="text-sm text-slate-600">
            Total: {companies.length} empresa(s)
          </div>
        )}
      </div>
    </DashboardLayout>
  )
}

export default CompaniesListPage
