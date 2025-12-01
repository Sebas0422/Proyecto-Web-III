import api from './lotApi';

export type CreateProyectoDTO = {
    nombre: string;
    descripcion?: string;
    ubicacion?: string;
    fechaInicio?: string; // yyyy-mm-dd
    fechaEstimadaFinalizacion?: string;
};

// Crear proyecto
export const createProyecto = (dto: CreateProyectoDTO) =>
    api.post('/api/proyectos', dto);

// Listar proyectos
export const listProyectos = (activo?: boolean) =>
    api.get('/api/proyectos',  { params: { activo }});
