import React, { useState, useEffect } from 'react';
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
    Search
} from 'lucide-react';

// --- Interfaces & Types ---

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

// --- Mock Data ---

const statsData: StatData[] = [
    {
        title: "Proyectos Activos",
        value: 12,
        change: "+2 este mes",
        icon: <Building2 size={24} />,
        iconBgColor: "bg-blue-100 dark:bg-slate-800",
        iconColor: "text-blue-600"
    },
    {
        title: "Lotes Disponibles",
        value: 256,
        change: "15 vendidos recientemente",
        icon: <Grid size={24} />,
        iconBgColor: "bg-green-100 dark:bg-slate-800",
        iconColor: "text-green-600"
    },
    {
        title: "Nuevos Leads",
        value: 89,
        change: "En la última semana",
        icon: <Users size={24} />,
        iconBgColor: "bg-yellow-100 dark:bg-slate-800",
        iconColor: "text-yellow-600"
    },
    {
        title: "Reservas Confirmadas",
        value: 34,
        change: "5 pendientes de pago",
        icon: <BookmarkCheck size={24} />,
        iconBgColor: "bg-purple-100 dark:bg-slate-800",
        iconColor: "text-purple-600"
    }
];

const activityData: ActivityData[] = [
    {
        user: "Carlos López",
        action: "Agregó un nuevo lead",
        element: "Juan Rodríguez",
        elementColorClass: "text-blue-600",
        date: "Hace 5 minutos"
    },
    {
        user: "María García",
        action: "Actualizó el estado del lote",
        element: 'A-12, "El Roble"',
        elementColorClass: "text-blue-600",
        date: "Hace 2 horas"
    },
    {
        user: "Ana Pérez",
        action: "Generó un reporte de ventas",
        element: "Reporte Mensual",
        elementColorClass: "text-blue-600",
        date: "Ayer"
    }
];

// --- Components ---

const SidebarItem = ({ 
    icon, 
    label, 
    active = false, 
    onClick 
    }: { 
        icon: React.ReactNode; 
        label: string; 
        active?: boolean;
        onClick?: () => void;
    }) => (
    <a
        href="#"
        onClick={(e) => { e.preventDefault(); onClick && onClick(); }}
        className={`flex items-center px-4 py-2 text-sm font-medium rounded-md transition-colors ${
        active
            ? "bg-blue-50 dark:bg-slate-800 text-blue-600 dark:text-white"
            : "text-slate-600 dark:text-slate-400 hover:bg-slate-100 dark:hover:bg-slate-800 hover:text-slate-900 dark:hover:text-white"
        }`}
    >
        <span className="mr-3">{icon}</span>
        {label}
    </a>
);

const StatCard = ({ data }: { data: StatData }) => (
    <div className="bg-white dark:bg-slate-900 p-6 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800 transition-all hover:shadow-md">
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

// --- Main App Component ---

export default function App() {
  const [darkMode, setDarkMode] = useState(false);
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("Dashboard");

  // Toggle Dark Mode
  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
  };

  return (
    // Outer wrapper handles the 'dark' class for Tailwind
    <div className={darkMode ? 'dark' : ''}>
      <div className="flex h-screen bg-slate-50 dark:bg-[#0f172a] font-sans text-slate-700 dark:text-slate-300 transition-colors duration-200">
        
        {/* Mobile Sidebar Overlay */}
        {isSidebarOpen && (
          <div 
            className="fixed inset-0 z-20 bg-black/50 lg:hidden"
            onClick={() => setIsSidebarOpen(false)}
          />
        )}

        {/* Sidebar */}
        <aside 
          className={`
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
            <button 
              className="ml-auto lg:hidden text-slate-500"
              onClick={() => setIsSidebarOpen(false)}
            >
              <X size={20} />
            </button>
          </div>

          <nav className="flex-1 px-4 py-4 space-y-2 overflow-y-auto">
            <SidebarItem 
              icon={<LayoutDashboard size={20} />} 
              label="Dashboard" 
              active={activeTab === "Dashboard"} 
              onClick={() => setActiveTab("Dashboard")}
            />
            <SidebarItem 
              icon={<Building2 size={20} />} 
              label="Proyectos" 
              active={activeTab === "Proyectos"}
              onClick={() => setActiveTab("Proyectos")}
            />
            <SidebarItem 
              icon={<Grid size={20} />} 
              label="Lotes" 
              active={activeTab === "Lotes"}
              onClick={() => setActiveTab("Lotes")}
            />
            <SidebarItem 
              icon={<Users size={20} />} 
              label="Leads" 
              active={activeTab === "Leads"}
              onClick={() => setActiveTab("Leads")}
            />
            <SidebarItem 
              icon={<BookmarkCheck size={20} />} 
              label="Reservas" 
              active={activeTab === "Reservas"}
              onClick={() => setActiveTab("Reservas")}
            />
            <SidebarItem 
              icon={<BarChart3 size={20} />} 
              label="Reportes" 
              active={activeTab === "Reportes"}
              onClick={() => setActiveTab("Reportes")}
            />
          </nav>

          <div className="px-4 py-4 border-t border-slate-200 dark:border-slate-800">
            <SidebarItem icon={<LogOut size={20} />} label="Cerrar sesión" />
          </div>
        </aside>

        {/* Main Content */}
        <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
          
          {/* Header */}
          <header className="h-16 bg-white dark:bg-slate-900 border-b border-slate-200 dark:border-slate-800 flex items-center justify-between px-4 sm:px-6 flex-shrink-0 z-10">
            <div className="flex items-center gap-4">
              <button 
                className="lg:hidden p-2 -ml-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-md"
                onClick={() => setIsSidebarOpen(true)}
              >
                <Menu size={24} />
              </button>
              <h1 className="text-xl font-semibold text-slate-800 dark:text-white truncate">
                {activeTab}
              </h1>
            </div>

            <div className="flex items-center gap-2 sm:gap-4">
              {/* Dark Mode Toggle */}
              <button 
                onClick={toggleDarkMode}
                className="p-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800 rounded-full transition-colors"
                title="Cambiar tema"
              >
                {darkMode ? <Sun size={20} /> : <Moon size={20} />}
              </button>

              {/* Notification */}
              <button className="relative p-2 text-slate-500 dark:text-slate-400 hover:text-slate-700 dark:hover:text-slate-200 transition-colors">
                <Bell size={20} />
                <span className="absolute top-1.5 right-1.5 flex h-2.5 w-2.5">
                  <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>
                  <span className="relative inline-flex rounded-full h-2.5 w-2.5 bg-red-500"></span>
                </span>
              </button>

              <div className="h-8 w-px bg-slate-200 dark:bg-slate-700 mx-1 hidden sm:block"></div>

              {/* User Profile */}
              <div className="flex items-center gap-3">
                <img 
                  alt="User avatar" 
                  className="h-9 w-9 rounded-full object-cover border-2 border-white dark:border-slate-700 shadow-sm" 
                  src="https://lh3.googleusercontent.com/aida-public/AB6AXuBohqcrl5VPqyvZldvsJcpWV1mqcQ8IXz12bGAUpbbPBWBD6KJNWNpLkRC5Zh9-DTldKFLSFF-OtukQ5e1E4r403s0EkieBSMkYXHwDvL7X4J66GyXusOcpV1FhQ-07oj5OmOg4pOWA2knloOafVyiiQc1skJhFwoLr_kPEFxa0CJBLxDGoWteA88rkRRLvU7SU6q7IO3alIDKscf3lMM9HHpoy9TCy_hcXvtEwXrJMPXIzQvB06SfwqL9Hl7MTMJSIFiVxVcAsbZI" 
                />
                <div className="hidden sm:block">
                  <p className="text-sm font-medium text-slate-800 dark:text-white leading-none">Ana Pérez</p>
                  <p className="text-xs text-slate-500 dark:text-slate-400 mt-1">Administradora</p>
                </div>
              </div>
            </div>
          </header>

          {/* Main Scrollable Area */}
          <main className="flex-1 p-4 sm:p-6 overflow-y-auto">
            <div className="max-w-7xl mx-auto space-y-6">
              
              {/* Stats Grid */}
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {statsData.map((stat, index) => (
                  <StatCard key={index} data={stat} />
                ))}
              </div>

              {/* Activity Table Section */}
              <div className="bg-white dark:bg-slate-900 rounded-lg shadow-sm border border-slate-200 dark:border-slate-800 overflow-hidden">
                <div className="p-6 border-b border-slate-200 dark:border-slate-800 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                  <h2 className="text-lg font-semibold text-slate-800 dark:text-white">Actividad Reciente</h2>
                  <button className="text-blue-600 dark:text-blue-400 bg-blue-50 dark:bg-slate-800 hover:bg-blue-100 dark:hover:bg-slate-700 px-4 py-2 rounded-md text-sm font-medium transition-colors">
                    Ver todo
                  </button>
                </div>
                
                <div className="overflow-x-auto">
                  <table className="w-full text-sm text-left">
                    <thead className="text-xs text-slate-500 dark:text-slate-400 uppercase bg-slate-50 dark:bg-slate-800/50">
                      <tr>
                        <th className="px-6 py-4 font-semibold">Usuario</th>
                        <th className="px-6 py-4 font-semibold">Acción</th>
                        <th className="px-6 py-4 font-semibold">Elemento</th>
                        <th className="px-6 py-4 font-semibold">Fecha</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-slate-200 dark:divide-slate-800">
                      {activityData.map((item, index) => (
                        <tr key={index} className="hover:bg-slate-50 dark:hover:bg-slate-800/50 transition-colors">
                          <td className="px-6 py-4 font-medium text-slate-800 dark:text-white">
                            {item.user}
                          </td>
                          <td className="px-6 py-4 text-slate-600 dark:text-slate-300">
                            {item.action}
                          </td>
                          <td className={`px-6 py-4 font-medium ${item.elementColorClass}`}>
                            {item.element}
                          </td>
                          <td className="px-6 py-4 text-slate-500 dark:text-slate-400">
                            {item.date}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>

            </div>
          </main>
        </div>
      </div>
    </div>
  );
}