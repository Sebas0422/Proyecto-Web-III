import React, { useCallback, useRef, useState, useEffect } from 'react';
import { GoogleMap, useJsApiLoader, InfoWindow } from '@react-google-maps/api';
import { pathToWKT } from '../utils/wkt';
import { createLote, listLotes, updateLote } from '@features/lots/services/lotes';
import { Modal, Button, Form } from 'react-bootstrap';

const containerStyle = { width: '100%', height: '60vh' };
const defaultCenter = { lat: -17.7833, lng: -63.1821 };

function wktToPath(wkt: string): google.maps.LatLngLiteral[] {
  return wkt
    .replace('POLYGON ((', '')
    .replace('))', '')
    .split(',')
    .map((coord) => {
      const [lng, lat] = coord.trim().split(' ').map(Number);
      return { lat, lng };
    });
}

type Props = { proyectoId: string };

export default function MapCreateLote({ proyectoId }: Props) {
  const { isLoaded } = useJsApiLoader({
    googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_KEY,
    libraries: ['drawing'],
  });

  const mapRef = useRef<google.maps.Map | null>(null);
  const dmRef = useRef<google.maps.drawing.DrawingManager | null>(null);
  const [lotesPolygons, setLotesPolygons] = useState<google.maps.Polygon[]>([]);
  const [loteSeleccionado, setLoteSeleccionado] = useState<any>(null);
  const [showModal, setShowModal] = useState(false);
  const [showInfo, setShowInfo] = useState<{ lat: number; lng: number } | null>(null);
  const [creating, setCreating] = useState(false); // Estado para indicar creación de lote

  // Form del modal de edición
  const [form, setForm] = useState({
    id: '',
    numeroLote: '',
    manzana: '',
    precio: '',
    observaciones: '',
  });

  // Abrir modal para editar
  const abrirModal = (lote: any) => {
    setForm({
      id: lote.id,
      numeroLote: lote.numeroLote,
      manzana: lote.manzana,
      precio: lote.precio,
      observaciones: lote.observaciones,
    });
    setShowModal(true);
  };

  // Guardar cambios del modal
  const guardarCambios = async () => {
    try {
      await updateLote(form.id, {
        numeroLote: form.numeroLote,
        manzana: form.manzana,
        precio: Number(form.precio),
        observaciones: form.observaciones,
      });
      alert('Lote actualizado correctamente');
      setShowModal(false);
      fetchLotes();
    } catch (err) {
      console.error(err);
      alert('Error actualizando lote');
    }
  };

  // Cargar lotes desde API
  const fetchLotes = async () => {
    if (!proyectoId || !mapRef.current) return;
    try {
      const resp = await listLotes(proyectoId);
      const lotes = resp.data?.data ?? [];

      // Limpiar polígonos existentes
      lotesPolygons.forEach((p) => p.setMap(null));

      const newPolygons: google.maps.Polygon[] = [];
      const bounds = new google.maps.LatLngBounds();

      lotes.forEach((lote: any) => {
        const path = wktToPath(lote.geometriaWKT);

        const polygon = new google.maps.Polygon({
          paths: path,
          strokeColor: '#007bff',
          strokeOpacity: 0.8,
          strokeWeight: 2,
          fillColor: '#007bff',
          fillOpacity: 0.35,
          map: mapRef.current!,
        });

        // Click en polígono para mostrar InfoWindow
        polygon.addListener('click', (e: any) => {
          setLoteSeleccionado(lote);
          setShowInfo({ lat: e.latLng.lat(), lng: e.latLng.lng() });
        });

        path.forEach((p) => bounds.extend(p));
        newPolygons.push(polygon);
      });

      setLotesPolygons(newPolygons);
      if (!bounds.isEmpty()) mapRef.current.fitBounds(bounds);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchLotes();
  }, [proyectoId]);

  // Configurar DrawingManager para crear lotes nuevos
  const onLoadMap = useCallback(
    (map: google.maps.Map) => {
      mapRef.current = map;

      const drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: null,
        drawingControl: true,
        drawingControlOptions: {
          position: google.maps.ControlPosition.TOP_CENTER,
          drawingModes: [google.maps.drawing.OverlayType.POLYGON],
        },
        polygonOptions: { editable: true, draggable: false },
      });

      drawingManager.setMap(map);
      dmRef.current = drawingManager;

      google.maps.event.addListener(drawingManager, 'overlaycomplete', async (ev: any) => {
        if (ev.type !== google.maps.drawing.OverlayType.POLYGON) return;

        const polygon: google.maps.Polygon = ev.overlay;
        const path = polygon.getPath().getArray().map((p) => ({ lat: p.lat(), lng: p.lng() }));
        const wkt = pathToWKT(path);

        const numeroLote = prompt('Número de lote (ej. A-101):') || `L-${Date.now()}`;
        const manzana = prompt('Manzana:') || 'Manzana X';
        const precio = Number(prompt('Precio:') || 0);
        const observaciones = prompt('Observaciones:') || 'Creado desde mapa';

        try {
          if (!proyectoId) {
            alert('No hay proyecto seleccionado');
            polygon.setMap(null);
            return;
          }

          setCreating(true);
          const resp = await createLote(proyectoId, { numeroLote, manzana, geometriaWKT: wkt, precio, observaciones });
          console.log('Lote creado:', resp.data);
          console.log('Lote creado:', resp.data);
          console.log('Lote creado:', resp.data);
          console.log('Lote creado:', resp.data);
          polygon.setMap(null); // remover polígono 
          fetchLotes(); // recargar polígonos
        } catch (err) {
          console.error(err);
          alert('Error creando lote (ver consola).');
          polygon.setMap(null);
        } finally {
          setCreating(false);
        }
      });
    },
    [proyectoId]
  );

  if (!isLoaded) return <div>Cargando mapa...</div>;

  return (
    <>
      <div className="mb-2 d-flex align-items-center gap-2">
        <Button
          size="sm"
          variant="outline-primary"
          onClick={() => {
            const dm = dmRef.current;
            if (!dm) return;
            const mode =
              dm.get('drawingMode') === google.maps.drawing.OverlayType.POLYGON
                ? null
                : google.maps.drawing.OverlayType.POLYGON;
            dm.setOptions({ drawingMode: mode as any });
          }}
        >
          Dibujar polígono
        </Button>
        {creating && <span className="text-muted">Creando lote...</span>}
      </div>

      <GoogleMap mapContainerStyle={containerStyle} center={defaultCenter} zoom={14} onLoad={onLoadMap}>
        {loteSeleccionado && showInfo && (
          <InfoWindow position={showInfo} onCloseClick={() => setShowInfo(null)}>
            <div>
              <strong>Lote:</strong> {loteSeleccionado.numeroLote}
              <br />
              <strong>Manzana:</strong> {loteSeleccionado.manzana}
              <br />
              <strong>Precio:</strong> {loteSeleccionado.precio}
              <br />
              <Button variant="primary" size="sm" onClick={() => abrirModal(loteSeleccionado)}>
                Editar
              </Button>
            </div>
          </InfoWindow>
        )}
      </GoogleMap>

      {/* Modal edición lote */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Editar lote</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group>
              <Form.Label>Número de lote</Form.Label>
              <Form.Control value={form.numeroLote} onChange={(e) => setForm({ ...form, numeroLote: e.target.value })} />
            </Form.Group>
            <Form.Group className="mt-3">
              <Form.Label>Manzana</Form.Label>
              <Form.Control value={form.manzana} onChange={(e) => setForm({ ...form, manzana: e.target.value })} />
            </Form.Group>
            <Form.Group className="mt-3">
              <Form.Label>Precio</Form.Label>
              <Form.Control
                type="number"
                value={form.precio}
                onChange={(e) => setForm({ ...form, precio: e.target.value })}
              />
            </Form.Group>
            <Form.Group className="mt-3">
              <Form.Label>Observaciones</Form.Label>
              <Form.Control
                as="textarea"
                value={form.observaciones}
                onChange={(e) => setForm({ ...form, observaciones: e.target.value })}
              />
            </Form.Group>
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Cancelar
          </Button>
          <Button variant="primary" onClick={guardarCambios}>
            Guardar cambios
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
