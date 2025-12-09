import {
    useExportFinancialPdfMutation,
    useExportFinancialExcelMutation
} from '../services/reportsApi'

interface Props {
    startDate: string
    endDate: string
}

export default function FinancialReportExportButtons({ startDate, endDate }: Props) {
    const [exportPdf] = useExportFinancialPdfMutation()
    const [exportExcel] = useExportFinancialExcelMutation()

    const handleDownload = async (blob: Blob, filename: string) => {
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = filename
        a.click()
        window.URL.revokeObjectURL(url)
    }

    const handlePdf = async () => {
        const blob = await exportPdf({ startDate, endDate }).unwrap()
        handleDownload(blob, 'financial-report.pdf')
    }

    const handleExcel = async () => {
        const blob = await exportExcel({ startDate, endDate }).unwrap()
        handleDownload(blob, 'financial-report.xlsx')
    }

    return (
        <div className="flex gap-3">
        <button
            onClick={handlePdf}
            className="px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
        >
            Descargar PDF
        </button>
        <button
            onClick={handleExcel}
            className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
        >
            Descargar Excel
        </button>
        </div>
    )
}
