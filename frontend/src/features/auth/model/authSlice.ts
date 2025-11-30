import { createSlice, type PayloadAction } from '@reduxjs/toolkit'
import Cookies from 'js-cookie'
import type { User, AuthState } from '../types/auth.types'

const tokenFromCookie = Cookies.get('token')
const userFromCookie = Cookies.get('user')

const initialState: AuthState = {
  token: tokenFromCookie ?? null,
  user: userFromCookie ? JSON.parse(userFromCookie) : null,
}

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (state, action: PayloadAction<{ token: string; user: User }>) => {
      state.token = action.payload.token
      state.user = action.payload.user

      // Guardar en cookies con opciones seguras
      Cookies.set('token', action.payload.token, {
        expires: 1, // 1 día
        secure: false, // Cambiar a true en producción con HTTPS
        sameSite: 'strict'
      })
      Cookies.set('user', JSON.stringify(action.payload.user), {
        expires: 1,
        secure: false,
        sameSite: 'strict'
      })
    },
    logout: (state) => {
      state.token = null
      state.user = null

      // Eliminar cookies
      Cookies.remove('token')
      Cookies.remove('user')
    },
  },
})

export const { setCredentials, logout } = authSlice.actions
export default authSlice.reducer
