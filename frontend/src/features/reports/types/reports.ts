export interface FinancialReport {
    period: string;
    startDate: string;
    endDate: string;
    totalIncome: number;
    totalExpenses: number;
    netProfit: number;
    profitMargin: number;
    pendingPayments: number;
    confirmedPayments: number;
    currency: string;
    incomeBySource: Array<{
        source: string;
        amount: number;
        currency: string;
    }>;
    monthlyData: Array<{
        month: string;
        income: number;
        expenses: number;
        profit: number;
        currency: string;
    }>;
}

export interface LeadsReport {
    period: string;
    startDate: string;
    endDate: string;
    totalLeads: number;
    activeLeads: number;
    convertedLeads: number;
    lostLeads: number;
    conversionRate: number;
    averageConversionTime: number;
    leadsBySource: Array<{
        source: string;
        count: number;
        converted: number;
        conversionRate: number;
    }>;
    leadsByStatus: Array<{
        status: string;
        count: number;
        percentage: number;
    }>;
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
