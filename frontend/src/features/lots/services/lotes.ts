import api from "./lotApi";

export type CreateLoteDTO = {
  numeroLote: string;
  manzana?: string;
  geometriaWKT: string; // e.g. POLYGON((lng lat, lng lat, ...))
  precio?: number;
  observaciones?: string;
};

export const createLote = (proyectoId: string, dto: CreateLoteDTO) =>
  api.post('/api/lotes', dto, { params: { proyectoId } });

export const listLotes = (proyectoId: string, estado?: string) =>
  api.get('/api/lotes', { params: { proyectoId, estado } });

export const updateLote = (id: string, data: any) => {
  api.put(`/api/lotes/${id}`, data);
};

export const importLotesFromKml = (
    file: File,
    proyectoId: string,
    extras?: { manzana?: string; precio?: string }
  ) => {
  const fd = new FormData();
  fd.append('file', file);
  fd.append('proyectoId', proyectoId);
  if (extras?.manzana) fd.append('manzana', extras.manzana);
  if (extras?.precio) fd.append('precio', extras.precio);

  return api.post('/api/lotes/import-kml', fd, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
};
