const env = import.meta.env;

type RawEnv = {
  VITE_API_BASE?: string
}

const raw = (env ?? {}) as RawEnv

export const VITE_API_BASE: string = raw.VITE_API_BASE ?? 'http://localhost:8000'

export default {
  VITE_API_BASE,
}
