import React, { useState } from 'react';
import { 
  LayoutDashboard, 
  Building2, 
  Grid, 
  Users, 
  BookmarkCheck, 
  BarChart3, 
  LogOut, 
  Bell, 
  Menu, 
  X,
  Home,
  Moon,
  Sun,
  PlusCircle,
  MoreVertical,
  ArrowLeft,
  Save
} from 'lucide-react';

// --- Interfaces ---

interface Project {
  id: number;
  name: string;
  status: 'Activo' | 'En Desarrollo' | 'Pausado' | 'Cerrado';
  lotsAvailable: number;
  lotsTotal: number;
  date: string;
}

interface StatData {
  title: string;
  value: string | number;
  change: string;
  icon: React.ReactNode;
  iconBgColor: string;
  iconColor: string;
}

interface ActivityData {
  user: string;
  action: string;
  element: string;
  elementColorClass: string;
  date: string;
}

// --- Mock Data (Datos de prueba) ---

const projectsData: Project[] = [
  { id: 1, name: 'Residencial "El Roble"', status: 'Activo', lotsAvailable: 45, lotsTotal: 50, date: '15/05/2023' },
  { id: 2, name: 'Villas "Las Palmeras"', status: 'Activo', lotsAvailable: 120, lotsTotal: 150, date: '02/03/2023' },
  { id: 3, name: 'Condominio "Mirador del Valle"', status: 'En Desarrollo', lotsAvailable: 200, lotsTotal: 200, date: '10/01/2024' },
  { id: 4, name: 'Altos de la Montaña', status: 'Pausado', lotsAvailable: 30, lotsTotal: 75, date: '20/09/2022' },
  { id: 5, name: 'Parque Industrial "El Progreso"', status: 'Cerrado', lotsAvailable: 0, lotsTotal: 100, date: '05/06/2021' },
];

const statsData: StatData[] = [
  { title: "Proyectos Activos", value: 12, change: "+2 este mes", icon: <Building2 size={24} />, iconBgColor: "bg-blue-100 dark:bg-slate-800", iconColor: "text-blue-600" },
  { title: "Lotes Disponibles", value: 256, change: "15 vendidos recientemente", icon: <Grid size={24} />, iconBgColor: "bg-green-100 dark:bg-slate-800", iconColor: "text-green-600" },
  { title: "Nuevos Leads", value: 89, change: "En la última semana", icon: <Users size={24} />, iconBgColor: "bg-yellow-100 dark:bg-slate-800", iconColor: "text-yellow-600" },
  { title: "Reservas Confirmadas", value: 34, change: "5 pendientes de pago", icon: <BookmarkCheck size={24} />, iconBgColor: "bg-purple-100 dark:bg-slate-800", iconColor: "text-purple-600" }
];

const activityData: ActivityData[] = [
  { user: "Carlos López", action: "Agregó un nuevo lead", element: "Juan Rodríguez", elementColorClass: "text-blue-600", date: "Hace 5 minutos" },
  { user: "María García", action: "Actualizó el estado del lote", element: 'A-12, "El Roble"', elementColorClass: "text-blue-600", date: "Hace 2 horas" },
  { user: "Ana Pérez", action: "Generó un reporte de ventas", element: "Reporte Mensual", elementColorClass: "text-blue-600", date: "Ayer" }
];

// --- Components ---

const SidebarItem = ({ icon, label, active = false, onClick }: { icon: React.ReactNode; label: string; active?: boolean; onClick?: () => void }) => (
  <button
    onClick={onClick}
    className={`w-full flex items-center px-4 py-2 text-sm font-medium rounded-md transition-colors ${
      active
        ? "bg-blue-50 dark:bg-slate-800 text-blue-600 dark:text-white"
        : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-white"
    }`}
  >
    <span className="mr-3">{icon}</span>
    {label}
  </button>
);

const StatCard = ({ data }: { data: StatData }) => (
  <div className="bg-white dark:bg-slate-900 p-6 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800">
    <div className="flex justify-between items-start">
      <div>
        <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{data.title}</p>
        <p className="text-3xl font-bold text-slate-800 dark:text-white mt-1">{data.value}</p>
      </div>
      <div className={`p-2 rounded-md ${data.iconBgColor}`}>
        <div className={data.iconColor}>{data.icon}</div>
      </div>
    </div>
    <p className="text-xs text-slate-500 dark:text-slate-400 mt-4">{data.change}</p>
  </div>
);

const StatusBadge = ({ status }: { status: Project['status'] }) => {
  const styles = {
    'Activo': 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300',
    'En Desarrollo': 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300',
    'Pausado': 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300',
    'Cerrado': 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300'
  };

  return (
    <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${styles[status]}`}>
      {status}
    </span>
  );
};

// --- Views ---

const DashboardView = () => (
  <div className="space-y-6">
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
      {statsData.map((stat, index) => <StatCard key={index} data={stat} />)}
    </div>

    <div className="bg-white dark:bg-slate-900 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800">
      <div className="p-6 border-b border-slate-200 dark:border-slate-800 flex justify-between items-center">
        <h2 className="text-lg font-semibold text-slate-800 dark:text-white">Actividad Reciente</h2>
        <button className="text-blue-600 dark:text-blue-400 hover:text-blue-800 text-sm font-medium">Ver todo</button>
      </div>
      <div className="overflow-x-auto">
        <table className="w-full text-sm text-left">
          <thead className="text-xs text-slate-500 dark:text-slate-400 uppercase bg-slate-50 dark:bg-slate-800/50">
            <tr>
              <th className="px-6 py-3">Usuario</th>
              <th className="px-6 py-3">Acción</th>
              <th className="px-6 py-3">Elemento</th>
              <th className="px-6 py-3">Fecha</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-200 dark:divide-slate-800">
            {activityData.map((item, index) => (
              <tr key={index} className="hover:bg-slate-50 dark:hover:bg-slate-800/50">
                <td className="px-6 py-4 font-medium text-slate-800 dark:text-white">{item.user}</td>
                <td className="px-6 py-4 text-slate-600 dark:text-slate-300">{item.action}</td>
                <td className={`px-6 py-4 font-medium ${item.elementColorClass}`}>{item.element}</td>
                <td className="px-6 py-4 text-slate-500 dark:text-slate-400">{item.date}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  </div>
);

// Nuevo Componente: Formulario de Crear Proyecto
const CreateProjectView = ({ onCancel }: { onCancel: () => void }) => (
  <div className="bg-white dark:bg-slate-900 p-8 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800 max-w-4xl mx-auto">
    <h2 className="text-lg font-semibold text-slate-800 dark:text-white mb-6">Información del Proyecto</h2>
    <form className="space-y-6" onSubmit={(e) => { e.preventDefault(); onCancel(); }}>
      <div>
        <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="project-name">Nombre del Proyecto</label>
        <input 
          className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
          id="project-name" 
          placeholder="Ej: Residencial El Roble" 
          type="text"
        />
      </div>
      <div>
        <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="project-description">Descripción</label>
        <textarea 
          className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
          id="project-description" 
          placeholder="Añada una descripción detallada del proyecto..." 
          rows={4}
        ></textarea>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="project-location">Ubicación</label>
          <input 
            className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
            id="project-location" 
            placeholder="Ej: San José, Costa Rica" 
            type="text"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="project-status">Estado</label>
          <select 
            className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
            id="project-status"
          >
            <option>Activo</option>
            <option>En Desarrollo</option>
            <option>Pausado</option>
            <option>Completado</option>
            <option>Cerrado</option>
          </select>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="start-date">Fecha de Inicio</label>
          <input 
            className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
            id="start-date" 
            type="date"
          />
        </div>
        <div>
          <label className="block text-sm font-medium text-slate-600 dark:text-slate-400 mb-1" htmlFor="end-date">Fecha de Finalización</label>
          <input 
            className="w-full px-3 py-2 border border-slate-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 dark:bg-slate-800 dark:border-slate-700 dark:text-white transition-shadow" 
            id="end-date" 
            type="date"
          />
        </div>
      </div>
      <div className="border-t border-slate-200 dark:border-slate-800 pt-6 flex justify-end gap-3">
        <button 
          onClick={onCancel}
          type="button" 
          className="bg-slate-100 text-slate-700 px-4 py-2 rounded-md text-sm font-medium hover:bg-slate-200 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-slate-500 dark:bg-slate-800 dark:text-slate-300 dark:hover:bg-slate-700 dark:focus:ring-offset-slate-900 transition-colors"
        >
          Cancelar
        </button>
        <button 
          type="submit" 
          className="bg-blue-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-600 dark:focus:ring-offset-slate-900 flex items-center gap-2 transition-colors"
        >
          <Save size={20} />
          Guardar Proyecto
        </button>
      </div>
    </form>
  </div>
);

// Componente: Lista de Proyectos (Modificado para recibir onCreate)
const ProjectsView = ({ onCreate }: { onCreate: () => void }) => (
  <div className="bg-white dark:bg-slate-900 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800">
    <div className="p-6 border-b border-slate-200 dark:border-slate-800 flex flex-col sm:flex-row justify-between items-center gap-4">
      <h2 className="text-lg font-semibold text-slate-800 dark:text-white">Lista de Proyectos</h2>
      <button 
        onClick={onCreate}
        className="bg-blue-600 text-white px-4 py-2 rounded-md text-sm font-medium hover:bg-blue-700 flex items-center gap-2 transition-colors"
      >
        <PlusCircle size={20} />
        Nuevo Proyecto
      </button>
    </div>
    <div className="overflow-x-auto">
      <table className="w-full text-sm text-left">
        <thead className="text-xs text-slate-500 dark:text-slate-400 uppercase bg-slate-50 dark:bg-slate-800/50">
          <tr>
            <th className="px-6 py-3">Nombre del Proyecto</th>
            <th className="px-6 py-3">Estado</th>
            <th className="px-6 py-3">Lotes Disponibles</th>
            <th className="px-6 py-3">Fecha de Creación</th>
            <th className="px-6 py-3"><span className="sr-only">Acciones</span></th>
          </tr>
        </thead>
        <tbody className="divide-y divide-slate-200 dark:divide-slate-800">
          {projectsData.map((project) => (
            <tr key={project.id} className="hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors">
              <td className="px-6 py-4 font-medium text-slate-800 dark:text-white whitespace-nowrap">
                {project.name}
              </td>
              <td className="px-6 py-4">
                <StatusBadge status={project.status} />
              </td>
              <td className="px-6 py-4 text-slate-600 dark:text-slate-300">
                {project.lotsAvailable} / {project.lotsTotal}
              </td>
              <td className="px-6 py-4 text-slate-600 dark:text-slate-300">
                {project.date}
              </td>
              <td className="px-6 py-4 text-right">
                <button className="text-slate-500 hover:text-blue-600 dark:text-slate-400 dark:hover:text-white transition-colors">
                  <MoreVertical size={20} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  </div>
);

// --- Main App Component ---

export default function App() {
  const [darkMode, setDarkMode] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("Proyectos");
  const [isCreatingProject, setIsCreatingProject] = useState(false); // Nuevo estado para controlar la vista de creación

  const toggleDarkMode = () => setDarkMode(!darkMode);

  // Maneja el cambio de pestañas y resetea el estado de creación
  const handleTabChange = (tab: string) => {
    setActiveTab(tab);
    setIsCreatingProject(false);
    setIsSidebarOpen(false);
  };

  return (
    <div className={darkMode ? 'dark' : ''}>
      <div className="flex h-screen bg-slate-50 dark:bg-[#0f172a] font-sans text-slate-700 dark:text-slate-300 transition-colors duration-200">
        
        {/* Mobile Sidebar Overlay */}
        {isSidebarOpen && (
          <div className="fixed inset-0 z-20 bg-black/50 lg:hidden" onClick={() => setIsSidebarOpen(false)} />
        )}

        {/* Sidebar */}
        <aside className={`
            fixed lg:static inset-y-0 left-0 z-30
            w-64 flex-shrink-0 bg-white dark:bg-slate-900 
            border-r border-slate-200 dark:border-slate-800 flex flex-col
            transform transition-transform duration-200 ease-in-out
            ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
          `}
        >
          <div className="h-16 flex items-center px-6 border-b border-slate-200 dark:border-slate-800">
            <div className="bg-blue-600 p-2 rounded-lg">
              <Home className="text-white" size={24} />
            </div>
            <span className="ml-3 text-lg font-bold text-slate-800 dark:text-white">EstateHub</span>
            <button className="ml-auto lg:hidden text-slate-500" onClick={() => setIsSidebarOpen(false)}>
              <X size={20} />
            </button>
          </div>

          <nav className="flex-1 px-4 py-4 space-y-2 overflow-y-auto">
            <SidebarItem icon={<LayoutDashboard size={20} />} label="Dashboard" active={activeTab === "Dashboard"} onClick={() => handleTabChange("Dashboard")} />
            <SidebarItem icon={<Building2 size={20} />} label="Proyectos" active={activeTab === "Proyectos"} onClick={() => handleTabChange("Proyectos")} />
            <SidebarItem icon={<Grid size={20} />} label="Lotes" active={activeTab === "Lotes"} onClick={() => handleTabChange("Lotes")} />
            <SidebarItem icon={<Users size={20} />} label="Leads" active={activeTab === "Leads"} onClick={() => handleTabChange("Leads")} />
            <SidebarItem icon={<BookmarkCheck size={20} />} label="Reservas" active={activeTab === "Reservas"} onClick={() => handleTabChange("Reservas")} />
            <SidebarItem icon={<BarChart3 size={20} />} label="Reportes" active={activeTab === "Reportes"} onClick={() => handleTabChange("Reportes")} />
          </nav>

          <div className="px-4 py-4 border-t border-slate-200 dark:border-slate-800">
            <SidebarItem icon={<LogOut size={20} />} label="Cerrar sesión" />
          </div>
        </aside>

        {/* Main Content */}
        <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
          <header className="h-16 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-4 sm:px-6 flex-shrink-0 z-10">
            <div className="flex items-center gap-4">
              <button className="lg:hidden p-2 -ml-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-md" onClick={() => setIsSidebarOpen(true)}>
                <Menu size={24} />
              </button>
              
              {/* Lógica del Título Dinámico con Botón Volver */}
              {isCreatingProject ? (
                <div className="flex items-center gap-2">
                   <button 
                    onClick={() => setIsCreatingProject(false)}
                    className="p-1 -ml-1 text-slate-500 hover:text-blue-600 dark:text-slate-400 dark:hover:text-white rounded-full transition-colors"
                   >
                     <ArrowLeft size={24} />
                   </button>
                   <h1 className="text-xl font-semibold text-slate-800 dark:text-white truncate">Crear Nuevo Proyecto</h1>
                </div>
              ) : (
                <h1 className="text-xl font-semibold text-slate-800 dark:text-white truncate">{activeTab}</h1>
              )}
            </div>

            <div className="flex items-center gap-2 sm:gap-4">
              <button onClick={toggleDarkMode} className="p-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-full transition-colors">
                {darkMode ? <Sun size={20} /> : <Moon size={20} />}
              </button>
              <button className="relative p-2 text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 transition-colors">
                <Bell size={20} />
                <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500"></span>
                </span>
              </button>
              <div className="h-8 w-px bg-slate-200 dark:bg-slate-700 mx-1 hidden sm:block"></div>
              <div className="flex items-center gap-3">
                <img alt="User avatar" className="h-9 w-9 rounded-full object-cover border-2 border-white dark:border-slate-700 shadow-sm" src="https://lh3.googleusercontent.com/aida-public/AB6AXuBohqcrl5VPqyvZldvsJcpWV1mqcQ8IXz12bGAUpbbPBWBD6KJNWNpLkRC5Zh9-DTldKFLSFF-OtukQ5e1E4r403s0EkieBSMkYXHwDvL7X4J66GyXusOcpV1FhQ-07oj5OmOg4pOWA2knloOafVyiiQc1skJhFwoLr_kPEFxa0CJBLxDGoWteA88rkRRLvU7SU6q7IO3alIDKscf3lMM9HHpoy9TCy_hcXvtEwXrJMPXIzQvB06SfwqL9Hl7MTMJSIFiVxVcAsbZI" />
                <div className="hidden sm:block">
                  <p className="text-sm font-medium text-slate-800 dark:text-white leading-none">Ana Pérez</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">Administradora</p>
                </div>
              </div>
            </div>
          </header>

          <main className="flex-1 p-4 sm:p-6 overflow-y-auto">
            <div className="max-w-7xl mx-auto">
              
              {/* Lógica de Renderizado de Vistas */}
              {activeTab === "Dashboard" && <DashboardView />}
              
              {activeTab === "Proyectos" && (
                isCreatingProject 
                  ? <CreateProjectView onCancel={() => setIsCreatingProject(false)} /> 
                  : <ProjectsView onCreate={() => setIsCreatingProject(true)} />
              )}
              
              {activeTab !== "Dashboard" && activeTab !== "Proyectos" && (
                <div className="text-center py-20 text-slate-500">
                  <p>La vista de {activeTab} está en construcción.</p>
                </div>
              )}

            </div>
          </main>
        </div>
      </div>
    </div>
  );
}