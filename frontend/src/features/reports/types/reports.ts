export interface FinancialReport {
    totalVentas: number
    totalReservas: number
    totalComisiones: number
    proyectos: Array<{
        proyectoId: string
        nombre: string
        ventas: number
        reservas: number
        ingresos: number
    }>
}

export interface LeadsReport {
    totalLeads: number
    leadsPorCanal: Array<{
        canal: string
        cantidad: number
    }>
    leadsPorProyecto: Array<{
        proyectoId: string
        nombreProyecto: string
        cantidad: number
    }>
}

export interface ReportsFilters {
    startDate: string 
    endDate: string   
}

export interface ApiResponse<T> {
    success: boolean
    message: string | null
    errorCode?: string | null
    data: T
}
