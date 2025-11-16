import React from 'react'
import { Provider } from 'react-redux'
import { store } from '../store/store'
import { BrowserRouter } from 'react-router-dom'

const AppProviders: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <Provider store={store}>
      <BrowserRouter>{children}</BrowserRouter>
    </Provider>
  )
}

export default AppProviders
