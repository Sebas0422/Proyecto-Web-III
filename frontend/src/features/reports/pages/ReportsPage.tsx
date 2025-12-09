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
                    ) : financial ? (
                        <div className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div><span className="font-semibold">Periodo:</span> {financial.period}</div>
                                <div><span className="font-semibold">Total Ingresos:</span> {financial.totalIncome?.toLocaleString()} {financial.currency}</div>
                                <div><span className="font-semibold">Total Gastos:</span> {financial.totalExpenses?.toLocaleString()} {financial.currency}</div>
                                <div><span className="font-semibold">Ganancia Neta:</span> {financial.netProfit?.toLocaleString()} {financial.currency}</div>
                                <div><span className="font-semibold">Margen de Ganancia:</span> {financial.profitMargin?.toFixed(2)}%</div>
                                <div><span className="font-semibold">Pagos Pendientes:</span> {financial.pendingPayments?.toLocaleString()} {financial.currency}</div>
                                <div><span className="font-semibold">Pagos Confirmados:</span> {financial.confirmedPayments?.toLocaleString()} {financial.currency}</div>
                            </div>
                            <div>
                                <span className="font-semibold">Ingresos por Fuente:</span>
                                <table className="min-w-full border mt-2">
                                    <thead>
                                        <tr className="bg-gray-100">
                                            <th className="border px-2 py-1 text-left">Fuente</th>
                                            <th className="border px-2 py-1 text-left">Monto</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {(financial.incomeBySource ?? []).map((src) => (
                                            <tr key={src.source}>
                                                <td className="border px-2 py-1">{src.source}</td>
                                                <td className="border px-2 py-1">{src.amount?.toLocaleString()} {src.currency}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                            <div>
                                <span className="font-semibold">Datos Mensuales:</span>
                                <table className="min-w-full border mt-2">
                                    <thead>
                                        <tr className="bg-gray-100">
                                            <th className="border px-2 py-1 text-left">Mes</th>
                                            <th className="border px-2 py-1 text-left">Ingresos</th>
                                            <th className="border px-2 py-1 text-left">Gastos</th>
                                            <th className="border px-2 py-1 text-left">Ganancia</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {(financial.monthlyData ?? []).map((m) => (
                                            <tr key={m.month}>
                                                <td className="border px-2 py-1">{m.month}</td>
                                                <td className="border px-2 py-1">{m.income?.toLocaleString()} {m.currency}</td>
                                                <td className="border px-2 py-1">{m.expenses?.toLocaleString()} {m.currency}</td>
                                                <td className="border px-2 py-1">{m.profit?.toLocaleString()} {m.currency}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    ) : (
                        <p>No hay datos financieros.</p>
                    )}
                </div>

                {/* Reporte de Leads */}
                <div className="bg-white p-6 rounded-lg shadow">
                    <h2 className="text-xl font-semibold mb-4">Reporte de Leads</h2>
                    {loadingLeads ? (
                        <p>Cargando...</p>
                    ) : leads ? (
                        <div className="space-y-4">
                            <div className="grid grid-cols-2 gap-4">
                                <div>
                                    <span className="font-semibold">Periodo:</span> {leads.period}
                                </div>
                                <div>
                                    <span className="font-semibold">Total Leads:</span> {leads.totalLeads}
                                </div>
                                <div>
                                    <span className="font-semibold">Leads Activos:</span> {leads.activeLeads}
                                </div>
                                <div>
                                    <span className="font-semibold">Leads Convertidos:</span> {leads.convertedLeads}
                                </div>
                                <div>
                                    <span className="font-semibold">Leads Perdidos:</span> {leads.lostLeads}
                                </div>
                                <div>
                                    <span className="font-semibold">Tasa de Conversión:</span> {leads.conversionRate?.toFixed(2)}%
                                </div>
                                <div>
                                    <span className="font-semibold">Tiempo Promedio de Conversión:</span> {leads.averageConversionTime} días
                                </div>
                            </div>
                            <div>
                                <span className="font-semibold">Leads por Fuente:</span>
                                <table className="min-w-full border mt-2">
                                    <thead>
                                        <tr className="bg-gray-100">
                                            <th className="border px-2 py-1 text-left">Fuente</th>
                                            <th className="border px-2 py-1 text-left">Total</th>
                                            <th className="border px-2 py-1 text-left">Convertidos</th>
                                            <th className="border px-2 py-1 text-left">Tasa Conversión (%)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {(leads.leadsBySource ?? []).map((src) => (
                                            <tr key={src.source}>
                                                <td className="border px-2 py-1">{src.source}</td>
                                                <td className="border px-2 py-1">{src.count}</td>
                                                <td className="border px-2 py-1">{src.converted}</td>
                                                <td className="border px-2 py-1">{src.conversionRate?.toFixed(2)}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                            <div>
                                <span className="font-semibold">Leads por Estado:</span>
                                <table className="min-w-full border mt-2">
                                    <thead>
                                        <tr className="bg-gray-100">
                                            <th className="border px-2 py-1 text-left">Estado</th>
                                            <th className="border px-2 py-1 text-left">Cantidad</th>
                                            <th className="border px-2 py-1 text-left">Porcentaje (%)</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        {(leads.leadsByStatus ?? []).map((status) => (
                                            <tr key={status.status}>
                                                <td className="border px-2 py-1">{status.status}</td>
                                                <td className="border px-2 py-1">{status.count}</td>
                                                <td className="border px-2 py-1">{status.percentage?.toFixed(2)}</td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    ) : (
                        <p>No hay datos de leads.</p>
                    )}
                </div>
            </div>
        </DashboardLayout>
    )
}
