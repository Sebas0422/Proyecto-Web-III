import './App.css'
import Providers from './app/providers/Providers'
import AppRoutes from './app/routes/AppRoutes'

function App() {
  return (
    <Providers>
      <AppRoutes />
    </Providers>
  );
}

export default App;
