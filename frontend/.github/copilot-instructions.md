<!-- .github/copilot-instructions.md - Guidance for AI coding agents working on this repo -->

# Quick orientation for AI coding agents

This project is a small React + TypeScript frontend scaffolded with Vite and organized with a Feature-Sliced / clean-modular layout. The instructions below summarize the "why" and the concrete, discoverable patterns an agent should follow to be immediately productive.

Key files to reference while editing:
- `package.json` (scripts, deps, lint-staged)
- `vite.config.ts` (path aliases)
- `src/main.tsx`, `src/App.tsx` (app entry)
- `src/app/providers/AppProviders.tsx` (Redux + Router wiring)
- `src/app/store/store.ts` (RTK store + RTK Query registration)
- `src/features/auth/services/authApi.ts` (RTK Query API + prepareHeaders)
- `src/features/auth/model/authSlice.ts` (auth state + localStorage persistence)
- `src/app/config/env.ts` (env var usage: VITE_API_BASE)
- `src/app/routes/AppRoutes.tsx`, `src/shared/components/PrivateRoute.tsx`, `src/shared/components/RoleGuard.tsx`
- Feature folders under `src/features/*` and shared UI under `src/shared/components`.

Core architecture (brief)
- Vite + React + TypeScript. Path aliases: `@app`, `@features`, `@shared`, `@assets`, `@types` (see `vite.config.ts`).
- State: Redux Toolkit. RTK Query is used for server communication; each API is registered in the store and its middleware is applied (`authApi` is an example).
- Auth flow: `authApi` defines `login` / `register` endpoints. `prepareHeaders` reads `localStorage.token` and sets `Authorization: Bearer <token>` automatically. `authSlice` persists token and user to `localStorage` on `setCredentials` and clears them on `logout`.
- Routing: `AppRoutes` uses a top-level token check to redirect; `PrivateRoute` enforces authentication and `RoleGuard` enforces role-based access.
- Forms: Formik + Yup for forms (see `LoginPage.tsx`) and RTK Query `unwrap()` is used to get the resolved payload and handle errors.

Concrete conventions and patterns to follow
- Imports: Use path aliases, e.g. `import X from '@features/auth/...` or `@app/store/hooks`.
- Adding a feature: add folder in `src/features/<feature>` with `pages/`, `model/` (slices), and `services/` (RTK Query APIs). Register new APIs in `src/app/store/store.ts` and add reducer entry using `[api.reducerPath]: api.reducer` and `middleware.concat(api.middleware)`.
- Auth handling: After successful login, dispatch `setCredentials({ token, user })` from `authSlice`. The token will be persisted automatically.
- API base URL: Use `VITE_API_BASE` from `src/app/config/env.ts`. Note the code reads `import.meta.env` there; ensure env vars start with `VITE_` and are available to Vite.
- Error handling pattern: RTK Query mutations are called then `.unwrap()` is used; on error, `LoginPage` extracts a message and calls `setFieldError('email', message)`. Follow this pattern when surfacing API errors to forms.

Developer workflows / useful commands
- Install deps: `npm install`
- Start dev server (HMR): `npm run dev`
- Build (TypeScript build + Vite): `npm run build` (runs `tsc -b && vite build`)
- Preview production build: `npm run preview`
- Lint: `npm run lint` (formats/lints via lint-staged hooks on commit)
- Fix lint: `npm run lint:fix`
- Format: `npm run format`
- Husky (install hooks locally): `npm run setup-husky` (project provides this script)

Local environment gotchas
- `VITE_API_BASE` fallback in `src/app/config/env.ts` is `'localhost:3000/api'` (no protocol). When running locally, make sure env var includes protocol (for example `VITE_API_BASE=http://localhost:3000/api`) or update `env.ts` when necessary.
- The build script runs `tsc -b` first. If you add new TypeScript project references, ensure `tsconfig.app.json`/`tsconfig.node.json` remain valid.

Small examples (where to look)
- Persisting auth: `src/features/auth/model/authSlice.ts` — token/user saved to `localStorage` inside `setCredentials`.
- RTK Query base + auth header: `src/features/auth/services/authApi.ts` — `baseQuery.fetchBaseQuery({ baseUrl: VITE_API_BASE, prepareHeaders: ... })`.
- Route protection: `src/shared/components/PrivateRoute.tsx` and `src/shared/components/RoleGuard.tsx` (use `useAppSelector` to read auth state).
- Login flow: `src/features/auth/pages/LoginPage.tsx` — Formik + Yup, calling `useLoginMutation`, `dispatch(setCredentials(...))`, and `navigate(...)`.

Editing and PR guidance for agents
- Keep existing import aliases. When adding a new API slice, update `src/app/store/store.ts` to register it.
- Prefer reusing `Input` and `Button` components from `src/shared/components` for consistent UI.
- Stick to the existing error extraction and form handling pattern used in `LoginPage.tsx` when adding form pages.
- If you change env handling or token persistence, update `authApi` and `authSlice` together to avoid breaking auth flows.

What this file is NOT: do not add high-level policies or aspirational items. Only document patterns you can confirm by reading the code.

If anything above is unclear or you'd like more examples (e.g., how to add a new RTK Query API + tests, or a checklist to add a new protected route), tell me which area to expand and I'll iterate.
