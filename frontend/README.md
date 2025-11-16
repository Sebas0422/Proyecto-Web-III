# React + TypeScript + Vite

This template provides a minimal setup to get React working in Vite with HMR and some ESLint rules.

Currently, two official plugins are available:

## Estructura propuesta (Feature-Sliced / Clean-modular)

Se añadió una estructura base modular en `src/` para trabajar con arquitectura limpia / Feature-Sliced Design.

Ejemplo de la estructura creada:

```
src/
 ├── app/
 │   ├── store/           # Redux store, hooks
 │   ├── routes/          # Rutas principales (AppRoutes)
 │   └── providers/       # AppProviders (Redux + Router)
 ├── features/
 │   └── auth/            # Módulo de autenticación
 │       ├── pages/       # LoginPage, RegisterPage
 │       ├── model/       # authSlice
 │       └── services/    # authApi (RTK Query)
 ├── shared/
 │   └── components/      # PrivateRoute, RoleGuard
 └── main.tsx
```

Principales características implementadas:

- Configuración de `tsconfig` con aliases (`@app`, `@features`, `@shared`).
- Redux Toolkit + RTK Query base (store + authApi).
- Páginas `LoginPage` y `RegisterPage` con Formik + Yup.
- Persistencia de JWT en `localStorage` y envío automático en headers (RTK Query `prepareHeaders`).
- `PrivateRoute` y `RoleGuard` para proteger rutas y roles.
- Configs básicas para Prettier, y scripts en `package.json` para `prepare`, `lint`, `lint:fix` y `format`.

Cómo empezar (local):

1. Instala dependencias:

```powershell
npm install
```

2. Inicializa Husky (para hooks):

```powershell
npm run prepare
```

3. Ejecuta en modo desarrollo:

```powershell
npm run dev
```

Notas:
- Aún falta instalar dependencias y ejecutar `npm install` en tu entorno local. Los paquetes y versiones añadidos en `package.json` son sugeridos; ajusta según prefieras.
- Si quieres, puedo también añadir tests básicos o conectar un backend de ejemplo para probar login/register.

Husky + lint-staged
-------------------

Se agregó el hook pre-commit en `.husky/pre-commit` para ejecutar `lint-staged`. Ejecuta `npm run prepare` localmente para instalar los hooks de Husky.


- [@vitejs/plugin-react](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react) uses [Babel](https://babeljs.io/) (or [oxc](https://oxc.rs) when used in [rolldown-vite](https://vite.dev/guide/rolldown)) for Fast Refresh
- [@vitejs/plugin-react-swc](https://github.com/vitejs/vite-plugin-react/blob/main/packages/plugin-react-swc) uses [SWC](https://swc.rs/) for Fast Refresh

## React Compiler

The React Compiler is currently not compatible with SWC. See [this issue](https://github.com/vitejs/vite-plugin-react/issues/428) for tracking the progress.

## Expanding the ESLint configuration

If you are developing a production application, we recommend updating the configuration to enable type-aware lint rules:

```js
export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...

      // Remove tseslint.configs.recommended and replace with this
      tseslint.configs.recommendedTypeChecked,
      // Alternatively, use this for stricter rules
      tseslint.configs.strictTypeChecked,
      // Optionally, add this for stylistic rules
      tseslint.configs.stylisticTypeChecked,

      // Other configs...
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```

You can also install [eslint-plugin-react-x](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-x) and [eslint-plugin-react-dom](https://github.com/Rel1cx/eslint-react/tree/main/packages/plugins/eslint-plugin-react-dom) for React-specific lint rules:

```js
// eslint.config.js
import reactX from 'eslint-plugin-react-x'
import reactDom from 'eslint-plugin-react-dom'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{ts,tsx}'],
    extends: [
      // Other configs...
      // Enable lint rules for React
      reactX.configs['recommended-typescript'],
      // Enable lint rules for React DOM
      reactDom.configs.recommended,
    ],
    languageOptions: {
      parserOptions: {
        project: ['./tsconfig.node.json', './tsconfig.app.json'],
        tsconfigRootDir: import.meta.dirname,
      },
      // other options...
    },
  },
])
```
