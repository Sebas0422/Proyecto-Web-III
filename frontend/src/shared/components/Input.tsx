import React from 'react'
import { Field, ErrorMessage } from 'formik'

interface InputProps {
  name: string
  label: string
  type?: string
  placeholder?: string
  icon?: React.ReactNode
}

const Input: React.FC<InputProps> = ({ name, label, type = 'text', placeholder, icon }) => {
  return (
    <div className="mb-4">
      <label htmlFor={name} className="block text-sm font-semibold text-gray-700 mb-2">
        {label}
      </label>
      <div className="relative">
        {icon && <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">{icon}</div>}
        <Field
          id={name}
          name={name}
          type={type}
          placeholder={placeholder}
          className={`w-full px-4 py-3 ${icon ? 'pl-10' : ''} border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all outline-none text-gray-900 placeholder-gray-400 bg-white`}
        />
      </div>
      <ErrorMessage name={name} component="div" className="mt-1 text-sm text-red-600" />
    </div>
  )
}

export default Input

