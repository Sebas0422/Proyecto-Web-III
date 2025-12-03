import type { Config } from 'tailwindcss'

const config: Config = {
  content: [
    './index.html',
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  darkMode: 'class', // Activar modo oscuro basado en clase
  theme: {
    extend: {},
  },
  plugins: [],
}

export default config
