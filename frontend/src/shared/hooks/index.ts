import { debounce as _debounce } from '@shared/utils'
import { useRef, useEffect, useMemo, useState } from 'react'

export function useDebounce<T>(value: T, delay: number): T {
  const [debouncedValue, setDebouncedValue] = useState(value)

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedValue(value)
    }, delay)

    return () => {
      clearTimeout(handler)
    }
  }, [value, delay])

  return debouncedValue
}

export { usePermissions } from './usePermissions'
export type { Permission } from './usePermissions'
