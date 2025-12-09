import { configureStore } from '@reduxjs/toolkit'
import { authApi } from '../../features/auth/services/authApi'
import { dashboardApi } from '../../features/dashboard/services/dashboardApi'
import { lotApi } from '../../features/lots/services/lotApi'
import { lotsApi } from '../../features/lots/services/lotsApi'
import { leadsApi } from '../../features/leads/services/leadsApi'
import { reservationsApi } from '../../features/reservations/services/reservationsApi'
import { projectsApi } from '../../features/projects/services/projectsApi'
import { paymentsApi } from '../../features/payments/services/paymentsApi'
import { companiesApi } from '../../features/companies/services/companiesApi'
import { usersApi } from '../../features/users/services/usersApi'
import { reportsApi } from '../../features/reports/services/reportsApi'

import authReducer from '../../features/auth/model/authSlice'
import { setupListeners } from '@reduxjs/toolkit/query'

export const store = configureStore({
  reducer: {
    auth: authReducer,
    [authApi.reducerPath]: authApi.reducer,
    [dashboardApi.reducerPath]: dashboardApi.reducer,
    [lotApi.reducerPath]: lotApi.reducer,
    [lotsApi.reducerPath]: lotsApi.reducer,
    [leadsApi.reducerPath]: leadsApi.reducer,
    [reservationsApi.reducerPath]: reservationsApi.reducer,
    [projectsApi.reducerPath]: projectsApi.reducer,
    [paymentsApi.reducerPath]: paymentsApi.reducer,
    [companiesApi.reducerPath]: companiesApi.reducer,
    [usersApi.reducerPath]: usersApi.reducer,
    [reportsApi.reducerPath]: reportsApi.reducer, 
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(
      authApi.middleware,
      dashboardApi.middleware,
      lotApi.middleware,
      lotsApi.middleware,
      leadsApi.middleware,
      reservationsApi.middleware,
      projectsApi.middleware,
      paymentsApi.middleware,
      companiesApi.middleware,
      usersApi.middleware,
      reportsApi.middleware
    ),
  devTools: true,
})

setupListeners(store.dispatch)

export type RootState = ReturnType<typeof store.getState>
export type AppDispatch = typeof store.dispatch
