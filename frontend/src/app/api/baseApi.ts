import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';

export const baseApi = createApi({
  reducerPath: 'baseApi',
  baseQuery: fetchBaseQuery({ baseUrl: '/' }),
  endpoints: (build) => ({
    // define endpoints per feature or globally
    ping: build.query<{ ok: boolean }, void>({
      query: () => ({ url: '/ping' }),
    }),
  }),
});

export const { usePingQuery } = baseApi;
