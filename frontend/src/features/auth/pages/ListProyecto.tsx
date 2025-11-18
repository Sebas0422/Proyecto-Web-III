import React from 'react'
import { useNavigate, Link } from 'react-router-dom'
import Button from '@shared/components/Button'

const dummy = [
	{ id: '1', title: 'Sitio web corporativo', status: 'En progreso' },
	{ id: '2', title: 'Aplicación móvil', status: 'Planificado' },
]

const ListProyecto: React.FC = () => {
	const navigate = useNavigate()

	return (
		<div className="min-h-screen p-6 bg-gray-50">
			<div className="max-w-5xl mx-auto">
				<div className="flex items-center justify-between mb-6">
					<h1 className="text-2xl font-semibold">Proyectos</h1>
					<div className="flex items-center gap-3">
						<Button variant="primary" size="md" onClick={() => navigate('/projects/new')}>
							Nuevo proyecto
						</Button>
						<Link to="/dashboard" className="text-sm text-gray-600 hover:text-gray-800">
							Volver
						</Link>
					</div>
				</div>

				<div className="bg-white rounded-lg shadow p-4 border border-gray-100">
					{/* TODO: Replace dummy data with RTK Query hook (e.g. useGetProjectsQuery) and map results */}
					<ul className="space-y-3">
						{dummy.map((p) => (
							<li key={p.id} className="flex items-center justify-between p-3 rounded hover:bg-gray-50">
								<div>
									<div className="font-medium">{p.title}</div>
									<div className="text-sm text-gray-500">{p.status}</div>
								</div>
								<div className="flex items-center gap-2">
									<Link to={`/projects/${p.id}`} className="text-sm text-blue-600 hover:text-blue-700">
										Ver
									</Link>
								</div>
							</li>
						))}
					</ul>
				</div>
			</div>
		</div>
	)
}

export default ListProyecto

