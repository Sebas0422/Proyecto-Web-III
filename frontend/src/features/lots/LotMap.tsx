import React, { useState, useEffect } from 'react';
import { DashboardLayout } from '@shared/components';
import KmlUploader from '@shared/components/KmlUploader';
import MapCreateLote from '@shared/components/MapCreateLote';
import { createProyecto, listProyectos } from './services/proyectos';
import { Modal, Button, Form, Spinner, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';

interface Proyecto {
  id: string;
  nombre: string;
}

export default function ProyectosLotesPage() {
  const [proyectoIdActivo, setProyectoIdActivo] = useState<string | null>(null);
  const [proyectos, setProyectos] = useState<Proyecto[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [showModal, setShowModal] = useState(false);
  const [nuevoProyecto, setNuevoProyecto] = useState({
    nombre: '',
    descripcion: '',
    ubicacion: '',
    fechaInicio: '',
    fechaEstimadaFinalizacion: ''
  });

  // Cargar proyectos
  const fetchProyectos = async () => {
    setLoading(true);
    setError(null);
    try {
      const resp = await listProyectos(true);
      setProyectos(resp.data?.data ?? []);
    } catch (err: any) {
      console.error('Error cargando proyectos:', err);
      setError(err?.response?.data?.message || err?.message || 'Error desconocido al listar proyectos.');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProyectos();
  }, []);

  // Crear proyecto según backend
  const handleCreateProyecto = async () => {
    if (!nuevoProyecto.nombre.trim()) return alert('El nombre del proyecto es obligatorio');
    if (!nuevoProyecto.fechaInicio || !nuevoProyecto.fechaEstimadaFinalizacion) {
      return alert('Debe ingresar fechas de inicio y finalización');
    }

    setLoading(true);
    setError(null);

    try {
      const resp = await createProyecto(nuevoProyecto);

      const id = resp.data?.data?.id ?? resp.data?.id ?? resp.data;
      if (!id) throw new Error('No se pudo obtener el ID del proyecto creado.');

      const proyectoCreado: Proyecto = { id: String(id), nombre: nuevoProyecto.nombre };
      setProyectos(prev => [...prev, proyectoCreado]);
      setProyectoIdActivo(proyectoCreado.id);

      // Reset del form
      setNuevoProyecto({
        nombre: '',
        descripcion: '',
        ubicacion: '',
        fechaInicio: '',
        fechaEstimadaFinalizacion: ''
      });
      setShowModal(false);

    } catch (err: any) {
      console.error('Error creando proyecto:', err);
      setError(err?.response?.data?.message || err?.message || 'Error desconocido al crear proyecto.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <DashboardLayout>
      <div className="p-6">
        <div className="d-flex justify-content-between align-items-center mb-4">
          <h3>Proyectos</h3>
          <Button variant="primary" onClick={() => setShowModal(true)}>
            Crear nuevo proyecto
          </Button>
        </div>

        {error && <Alert variant="danger">{error}</Alert>}

        {loading && proyectos.length === 0 ? (
          <div className="text-center py-4"><Spinner animation="border" /></div>
        ) : proyectos.length === 0 ? (
          <Alert variant="info">No tienes proyectos activos.</Alert>
        ) : (
          <Form.Group className="mb-4">
            <Form.Label>Selecciona un proyecto</Form.Label>
            <Form.Select
              value={proyectoIdActivo ?? ''}
              onChange={(e) => setProyectoIdActivo(e.target.value)}
            >
              <option value="">-- Selecciona --</option>
              {proyectos.map(p => (
                <option key={p.id} value={p.id}>{p.nombre}</option>
              ))}
            </Form.Select>
          </Form.Group>
        )}

        {proyectoIdActivo && (
        <div className="mb-4">
          {proyectos
            .filter(p => p.id === proyectoIdActivo)
            .map(p => (
              <div key={p.id} className="card mb-3">
                <div className="card-body">
                  <h5 className="card-title">{p.nombre}</h5>
                  <p className="card-text">
                    <strong>Descripción:</strong> {p.descripcion || 'Generado automáticamente desde frontend'}<br/>
                    <strong>Ubicación:</strong> {p.ubicacion || 'Ubicación por defecto'}<br/>
                    <strong>Fecha inicio:</strong> {p.fechaInicio || '2025-01-01T00:00:00'}<br/>
                    <strong>Fecha estimada de finalización:</strong> {p.fechaEstimadaFinalizacion || '2025-12-31T00:00:00'}
                  </p>
                </div>
              </div>
            ))
          }

          {/* Herramientas del proyecto seleccionado */}
          <KmlUploader proyectoId={proyectoIdActivo} />
          <MapCreateLote proyectoId={proyectoIdActivo} />
        </div>
      )}


        {/* Modal para crear proyecto */}
        <Modal show={showModal} onHide={() => setShowModal(false)}>
          <Modal.Header closeButton>
            <Modal.Title>Crear nuevo proyecto</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <Form.Group className="mb-2">
              <Form.Label>Nombre del proyecto</Form.Label>
              <Form.Control
                type="text"
                value={nuevoProyecto.nombre}
                onChange={(e) => setNuevoProyecto({ ...nuevoProyecto, nombre: e.target.value })}
                placeholder="Ej. Proyecto Los Alamos"
              />
            </Form.Group>

            <Form.Group className="mb-2">
              <Form.Label>Descripción</Form.Label>
              <Form.Control
                as="textarea"
                value={nuevoProyecto.descripcion}
                onChange={(e) => setNuevoProyecto({ ...nuevoProyecto, descripcion: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-2">
              <Form.Label>Ubicación</Form.Label>
              <Form.Control
                type="text"
                value={nuevoProyecto.ubicacion}
                onChange={(e) => setNuevoProyecto({ ...nuevoProyecto, ubicacion: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-2">
              <Form.Label>Fecha inicio</Form.Label>
              <Form.Control
                type="date"
                value={nuevoProyecto.fechaInicio}
                onChange={(e) => setNuevoProyecto({ ...nuevoProyecto, fechaInicio: e.target.value })}
              />
            </Form.Group>

            <Form.Group className="mb-2">
              <Form.Label>Fecha estimada de finalización</Form.Label>
              <Form.Control
                type="date"
                value={nuevoProyecto.fechaEstimadaFinalizacion}
                onChange={(e) => setNuevoProyecto({ ...nuevoProyecto, fechaEstimadaFinalizacion: e.target.value })}
              />
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowModal(false)}>Cancelar</Button>
            <Button variant="primary" onClick={handleCreateProyecto} disabled={loading}>
              {loading ? 'Creando...' : 'Crear'}
            </Button>
          </Modal.Footer>
        </Modal>
      </div>
    </DashboardLayout>
  );
}
