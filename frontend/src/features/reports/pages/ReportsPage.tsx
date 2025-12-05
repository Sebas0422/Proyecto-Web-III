import { useState } from 'react'
import { useGetFinancialReportQuery, useGetLeadsReportQuery } from '../services/reportsApi'
import FinancialReportExportButtons from '../components/FinancialReportExportButtons'
import { DashboardLayout } from '@shared/components'


export default function ReportsPage() {
    const [startDate, setStartDate] = useState<string>(new Date().toISOString())
    const [endDate, setEndDate] = useState<string>(new Date().toISOString())

    const { data: financial, isLoading: loadingFinancial } =
        useGetFinancialReportQuery({ startDate, endDate })

    const { data: leads, isLoading: loadingLeads } =
        useGetLeadsReportQuery({ startDate, endDate })

    return (
        <DashboardLayout>
        <div className="space-y-8">
        <h1 className="text-2xl font-bold">Reportes</h1>

        {/* Filtros */}
        <div className="bg-white p-4 rounded-lg shadow flex gap-4 items-end">
            <div className="flex flex-col">
            <label className="font-semibold">Fecha Inicio</label>
            <input
                type="datetime-local"
                className="border rounded p-2"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
            />
            </div>

            <div className="flex flex-col">
            <label className="font-semibold">Fecha Fin</label>
            <input
                type="datetime-local"
                className="border rounded p-2"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
            />
            </div>

            <FinancialReportExportButtons startDate={startDate} endDate={endDate} />
        </div>

        {/* Reporte Financiero */}
        <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Reporte Financiero</h2>
            {loadingFinancial ? (
            <p>Cargando...</p>
            ) : (
            <pre>{JSON.stringify(financial, null, 2)}</pre>
            )}
        </div>

        {/* Reporte de Leads */}
        <div className="bg-white p-6 rounded-lg shadow">
            <h2 className="text-xl font-semibold mb-4">Reporte de Leads</h2>
            {loadingLeads ? (
            <p>Cargando...</p>
            ) : (
            <pre>{JSON.stringify(leads, null, 2)}</pre>
            )}
        </div>
        </div>
        </DashboardLayout>
    )
}
