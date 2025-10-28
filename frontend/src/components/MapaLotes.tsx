import { useEffect, useRef, useState, useCallback } from 'react';
import { MapContainer, TileLayer, useMap } from 'react-leaflet';
import L from 'leaflet';
import '@geoman-io/leaflet-geoman-free';
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css';
import 'leaflet/dist/leaflet.css';

import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

interface Lote {
  id: string;
  numeroLote: number;
  nombre: string;
  manzanoId: number;
  tipo: 'normal' | 'esquina' | 'especial';
  area: number;
  coordenadas: Array<[number, number]>;
  lados: number[];
  perimetro: number;
  observaciones?: string;
  layer: L.Layer;
}

interface Manzano {
  id: number;
  nombre: string;
  color: string;
  lotes: Lote[];
}

interface Calle {
  id: string;
  nombre: string;
  coordenadas: Array<[number, number]>;
  ancho?: number;
  layer: L.Layer;
}

// Función para calcular la distancia entre dos puntos (fórmula de Haversine)
function calcularDistancia(lat1: number, lng1: number, lat2: number, lng2: number): number {
  const R = 6378137; // Radio de la Tierra en metros (WGS84)
  const toRad = (deg: number) => (deg * Math.PI) / 180;

  const dLat = toRad(lat2 - lat1);
  const dLng = toRad(lng2 - lng1);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
    Math.sin(dLng / 2) * Math.sin(dLng / 2);

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  const distance = R * c;

  return Math.round(distance * 100) / 100; // Redondear a 2 decimales
}

// Función para calcular las longitudes de todos los lados de un polígono
function calcularLados(latlngs: L.LatLng[]): { lados: number[]; perimetro: number } {
  const lados: number[] = [];
  let perimetro = 0;

  for (let i = 0; i < latlngs.length; i++) {
    const p1 = latlngs[i];
    const p2 = latlngs[(i + 1) % latlngs.length];

    const distancia = calcularDistancia(p1.lat, p1.lng, p2.lat, p2.lng);
    lados.push(distancia);
    perimetro += distancia;
  }

  return {
    lados,
    perimetro: Math.round(perimetro * 100) / 100
  };
}

// Función auxiliar para calcular área usando la fórmula geodésica (Vincenty)
// Basado en la fórmula para calcular áreas en una esfera
function calcularArea(latlngs: L.LatLng[]): number {
  if (latlngs.length < 3) return 0;

  const R = 6378137; // Radio de la Tierra en metros (WGS84)
  let area = 0;

  // Convertir a radianes
  const toRad = (deg: number) => (deg * Math.PI) / 180;

  for (let i = 0; i < latlngs.length; i++) {
    const p1 = latlngs[i];
    const p2 = latlngs[(i + 1) % latlngs.length];

    const lat1 = toRad(p1.lat);
    const lat2 = toRad(p2.lat);
    const lng1 = toRad(p1.lng);
    const lng2 = toRad(p2.lng);

    // Fórmula del área para polígonos esféricos
    area += (lng2 - lng1) * (2 + Math.sin(lat1) + Math.sin(lat2));
  }

  area = Math.abs(area * R * R / 2.0);
  return Math.round(area * 100) / 100;
}

// Componente que maneja la lógica del mapa
function MapaInteractivo({ onLotesChange }: { onLotesChange: (lotes: Lote[]) => void }) {
  const map = useMap();
  const lotesRef = useRef<Lote[]>([]);
  const nextIdRef = useRef(1);

  useEffect(() => {
    if (!map) return;

    // Habilitar los controles de dibujo de Geoman
    map.pm.addControls({
      position: 'topright',
      drawCircle: false,
      drawCircleMarker: false,
      drawMarker: false,
      drawPolyline: false,
      drawRectangle: false,
      drawPolygon: true,
      editMode: true,
      dragMode: false,
      cutPolygon: false,
      removalMode: true,
    });

    // Configurar estilo de los polígonos
    map.pm.setGlobalOptions({
      pathOptions: {
        color: '#667eea',
        fillColor: '#667eea',
        fillOpacity: 0.4,
        weight: 3
      }
    });

    // Cuando se crea un nuevo lote
    map.on('pm:create', (e) => {
      const layer = e.layer;

      if (layer instanceof L.Polygon) {
        const latlngs = layer.getLatLngs()[0] as L.LatLng[];
        const area = calcularArea(latlngs);
        const { lados, perimetro } = calcularLados(latlngs);
        const coordenadas = latlngs.map((ll) => [ll.lat, ll.lng] as [number, number]);

        const nuevoLote: Lote = {
          id: `LOTE-${nextIdRef.current}`,
          nombre: `Lote ${nextIdRef.current}`,
          area: area,
          lados: lados,
          perimetro: perimetro,
          coordenadas: coordenadas,
          layer: layer
        };

        // Generar HTML para los lados
        const ladosHTML = lados.map((lado, idx) =>
          `<div style="margin: 2px 0;">Lado ${idx + 1}: ${lado.toLocaleString()} m</div>`
        ).join('');

        // Añadir popup
        layer.bindPopup(`
          <div style="font-family: sans-serif; max-width: 250px;">
            <h3 style="margin: 0 0 10px 0; color: #333;">${nuevoLote.id}</h3>
            <p style="margin: 5px 0;"><strong>Nombre:</strong> ${nuevoLote.nombre}</p>
            <p style="margin: 5px 0;"><strong>Área:</strong> ${nuevoLote.area.toLocaleString()} m²</p>
            <p style="margin: 5px 0; font-size: 0.85em; color: #666;">
              ${(nuevoLote.area / 10000).toFixed(4)} hectáreas
            </p>
            <hr style="margin: 10px 0; border: none; border-top: 1px solid #ddd;">
            <p style="margin: 5px 0;"><strong>Perímetro:</strong> ${perimetro.toLocaleString()} m</p>
            <div style="font-size: 0.9em; color: #495057; margin-top: 5px;">
              <strong>Medidas de los lados:</strong>
              ${ladosHTML}
            </div>
          </div>
        `);

        lotesRef.current = [...lotesRef.current, nuevoLote];
        nextIdRef.current += 1;
        onLotesChange(lotesRef.current);
      }
    });

    // Cuando se edita un lote
    map.on('pm:edit', (e) => {
      const layer = e.layer;

      if (layer instanceof L.Polygon) {
        const loteIndex = lotesRef.current.findIndex(l => l.layer === layer);
        if (loteIndex !== -1) {
          const latlngs = layer.getLatLngs()[0] as L.LatLng[];
          const newArea = calcularArea(latlngs);
          const { lados, perimetro } = calcularLados(latlngs);
          const coordenadas = latlngs.map((ll) => [ll.lat, ll.lng] as [number, number]);

          lotesRef.current[loteIndex] = {
            ...lotesRef.current[loteIndex],
            area: newArea,
            lados: lados,
            perimetro: perimetro,
            coordenadas: coordenadas
          };

          // Generar HTML para los lados
          const ladosHTML = lados.map((lado, idx) =>
            `<div style="margin: 2px 0;">Lado ${idx + 1}: ${lado.toLocaleString()} m</div>`
          ).join('');

          // Actualizar popup
          layer.setPopupContent(`
            <div style="font-family: sans-serif; max-width: 250px;">
              <h3 style="margin: 0 0 10px 0; color: #333;">${lotesRef.current[loteIndex].id}</h3>
              <p style="margin: 5px 0;"><strong>Nombre:</strong> ${lotesRef.current[loteIndex].nombre}</p>
              <p style="margin: 5px 0;"><strong>Área:</strong> ${lotesRef.current[loteIndex].area.toLocaleString()} m²</p>
              <p style="margin: 5px 0; font-size: 0.85em; color: #666;">
                ${(lotesRef.current[loteIndex].area / 10000).toFixed(4)} hectáreas
              </p>
              <hr style="margin: 10px 0; border: none; border-top: 1px solid #ddd;">
              <p style="margin: 5px 0;"><strong>Perímetro:</strong> ${perimetro.toLocaleString()} m</p>
              <div style="font-size: 0.9em; color: #495057; margin-top: 5px;">
                <strong>Medidas de los lados:</strong>
                ${ladosHTML}
              </div>
            </div>
          `);

          onLotesChange(lotesRef.current);
        }
      }
    });

    // Cuando se elimina un lote
    map.on('pm:remove', (e) => {
      const layer = e.layer;
      lotesRef.current = lotesRef.current.filter(lote => lote.layer !== layer);
      onLotesChange(lotesRef.current);
    });

    return () => {
      map.pm.removeControls();
      map.off('pm:create');
      map.off('pm:edit');
      map.off('pm:remove');
    };
  }, [map, onLotesChange]);

  return null;
}

// Función para crear un rectángulo dado un punto central y dimensiones
function crearRectangulo(
  centerLat: number,
  centerLng: number,
  ancho: number,
  alto: number
): Array<[number, number]> {
  // Convertir metros a grados (aproximado)
  const latPerMeter = 1 / 111320;
  const lngPerMeter = 1 / (111320 * Math.cos(centerLat * Math.PI / 180));

  const halfAncho = ancho / 2;
  const halfAlto = alto / 2;

  return [
    [centerLat + halfAlto * latPerMeter, centerLng - halfAncho * lngPerMeter],
    [centerLat + halfAlto * latPerMeter, centerLng + halfAncho * lngPerMeter],
    [centerLat - halfAlto * latPerMeter, centerLng + halfAncho * lngPerMeter],
    [centerLat - halfAlto * latPerMeter, centerLng - halfAncho * lngPerMeter],
  ];
}

// Componente de herramientas rápidas
function HerramientasRapidas({
  map
}: {
  map: L.Map | null;
}) {
  const [mostrarPanel, setMostrarPanel] = useState(false);
  const [ancho, setAncho] = useState(10);
  const [alto, setAlto] = useState(20);

  const crearLoteRapido = (anchoM: number, altoM: number) => {
    if (!map) return;

    const center = map.getCenter();
    const coords = crearRectangulo(center.lat, center.lng, anchoM, altoM);

    // Crear el polígono en el mapa
    const polygon = L.polygon(coords, {
      color: '#667eea',
      fillColor: '#667eea',
      fillOpacity: 0.4,
      weight: 3
    }).addTo(map);

    // Habilitar edición con Geoman
    polygon.pm.enable();

    // Disparar evento de creación
    map.fire('pm:create', { layer: polygon });

    setMostrarPanel(false);
  };

  return (
    <div style={{
      position: 'absolute',
      top: '10px',
      right: '60px',
      zIndex: 1000
    }}>
      <button
        onClick={() => setMostrarPanel(!mostrarPanel)}
        style={{
          padding: '10px 15px',
          background: '#FF9800',
          color: 'white',
          border: 'none',
          borderRadius: '6px',
          cursor: 'pointer',
          fontWeight: 'bold',
          fontSize: '0.9em',
          boxShadow: '0 2px 8px rgba(0,0,0,0.2)',
          display: 'flex',
          alignItems: 'center',
          gap: '5px'
        }}
      >
        ⚡ Dibujo Rápido
      </button>

      {mostrarPanel && (
        <div style={{
          position: 'absolute',
          top: '50px',
          right: '0',
          background: 'white',
          borderRadius: '8px',
          boxShadow: '0 4px 12px rgba(0,0,0,0.2)',
          padding: '20px',
          minWidth: '300px'
        }}>
          <h3 style={{ margin: '0 0 15px 0', color: '#333', fontSize: '1.1em' }}>
            ⚡ Herramientas Rápidas
          </h3>

          {/* Plantillas predefinidas */}
          <div style={{ marginBottom: '20px' }}>
            <h4 style={{ margin: '0 0 10px 0', fontSize: '0.95em', color: '#495057' }}>
              📐 Plantillas de Lotes
            </h4>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '8px' }}>
              <button
                onClick={() => crearLoteRapido(10, 20)}
                style={{
                  padding: '8px',
                  background: '#e9ecef',
                  border: '1px solid #dee2e6',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.85em'
                }}
              >
                10m × 20m
              </button>
              <button
                onClick={() => crearLoteRapido(15, 30)}
                style={{
                  padding: '8px',
                  background: '#e9ecef',
                  border: '1px solid #dee2e6',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.85em'
                }}
              >
                15m × 30m
              </button>
              <button
                onClick={() => crearLoteRapido(20, 40)}
                style={{
                  padding: '8px',
                  background: '#e9ecef',
                  border: '1px solid #dee2e6',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.85em'
                }}
              >
                20m × 40m
              </button>
              <button
                onClick={() => crearLoteRapido(12, 25)}
                style={{
                  padding: '8px',
                  background: '#e9ecef',
                  border: '1px solid #dee2e6',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.85em'
                }}
              >
                12m × 25m
              </button>
            </div>
          </div>

          {/* Dimensiones personalizadas */}
          <div style={{ marginBottom: '15px' }}>
            <h4 style={{ margin: '0 0 10px 0', fontSize: '0.95em', color: '#495057' }}>
              ✏️ Dimensiones Personalizadas
            </h4>
            <div style={{ display: 'grid', gap: '10px' }}>
              <div>
                <label style={{ fontSize: '0.85em', color: '#666', display: 'block', marginBottom: '5px' }}>
                  Ancho (metros):
                </label>
                <input
                  type="number"
                  value={ancho}
                  onChange={(e) => setAncho(Number(e.target.value) || 0)}
                  style={{
                    width: '100%',
                    padding: '8px',
                    border: '1px solid #dee2e6',
                    borderRadius: '4px',
                    fontSize: '0.9em'
                  }}
                  min="1"
                />
              </div>
              <div>
                <label style={{ fontSize: '0.85em', color: '#666', display: 'block', marginBottom: '5px' }}>
                  Alto (metros):
                </label>
                <input
                  type="number"
                  value={alto}
                  onChange={(e) => setAlto(Number(e.target.value) || 0)}
                  style={{
                    width: '100%',
                    padding: '8px',
                    border: '1px solid #dee2e6',
                    borderRadius: '4px',
                    fontSize: '0.9em'
                  }}
                  min="1"
                />
              </div>
              <button
                onClick={() => crearLoteRapido(ancho, alto)}
                disabled={ancho <= 0 || alto <= 0}
                style={{
                  padding: '10px',
                  background: ancho > 0 && alto > 0 ? '#667eea' : '#dee2e6',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: ancho > 0 && alto > 0 ? 'pointer' : 'not-allowed',
                  fontWeight: 'bold',
                  fontSize: '0.9em'
                }}
              >
                🎯 Crear Lote {ancho > 0 && alto > 0 ? `(${ancho}m × ${alto}m)` : ''}
              </button>
            </div>
          </div>

          <div style={{
            fontSize: '0.75em',
            color: '#6c757d',
            borderTop: '1px solid #dee2e6',
            paddingTop: '10px',
            marginTop: '10px'
          }}>
            💡 El lote se creará en el centro del mapa actual. Puedes editarlo después.
          </div>
        </div>
      )}
    </div>
  );
}

/**
 * Componente principal para dibujar y gestionar lotes de inmuebles
 */
export default function MapaLotes() {
  const [lotes, setLotes] = useState<Lote[]>([]);
  const [manzanos, setManzanos] = useState<Manzano[]>([
    { id: 1, nombre: 'Manzano 1', color: '#667eea', lotes: [] },
  ]);
  const [calles, setCalles] = useState<Calle[]>([]);
  const [manzanoActual, setManzanoActual] = useState(1);
  const [tipoLoteActual, setTipoLoteActual] = useState<'normal' | 'esquina' | 'especial'>('normal');
  const [mostrarPanelControl, setMostrarPanelControl] = useState(true);
  const mapRef = useRef<L.Map | null>(null);

  const handleLotesChange = useCallback((nuevosLotes: Lote[]) => {
    setLotes([...nuevosLotes]);
  }, []);

  // Exportar datos de lotes a JSON
  const exportarLotes = () => {
    const data = lotes.map(lote => ({
      id: lote.id,
      nombre: lote.nombre,
      area: lote.area,
      perimetro: lote.perimetro,
      lados: lote.lados,
      coordenadas: lote.coordenadas
    }));

    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'lotes.json';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  // Duplicar un lote
  const duplicarLote = (lote: Lote) => {
    if (!mapRef.current || !(lote.layer instanceof L.Polygon)) return;

    // Obtener coordenadas originales
    const originalCoords = lote.layer.getLatLngs()[0] as L.LatLng[];

    // Desplazar el nuevo lote 20 metros al este
    const offset = 20 / 111320; // aproximadamente 20 metros en grados
    const newCoords = originalCoords.map(coord =>
      L.latLng(coord.lat, coord.lng + offset)
    );

    // Crear el nuevo polígono
    const newPolygon = L.polygon(newCoords, {
      color: '#667eea',
      fillColor: '#667eea',
      fillOpacity: 0.4,
      weight: 3
    }).addTo(mapRef.current);

    // Habilitar edición
    newPolygon.pm.enable();

    // Disparar evento de creación
    mapRef.current.fire('pm:create', { layer: newPolygon });
  };

  const areaTotal = lotes.reduce((sum, lote) => sum + lote.area, 0);

  return (
    <div style={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Header */}
      <div style={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        padding: '20px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
      }}>
        <h1 style={{ margin: 0, fontSize: '2em' }}>🏘️ Gestor de Lotes de Inmuebles</h1>
        <p style={{ margin: '10px 0 0 0', opacity: 0.9 }}>
          Dibuja, edita y gestiona lotes en el mapa
        </p>
      </div>

      {/* Panel de información */}
      {lotes.length > 0 && (
        <div style={{
          background: '#f8f9fa',
          padding: '15px 20px',
          borderBottom: '1px solid #dee2e6',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          flexWrap: 'wrap',
          gap: '10px'
        }}>
          <div style={{ display: 'flex', gap: '30px', fontSize: '0.95em' }}>
            <div>
              <strong style={{ color: '#495057' }}>Total de lotes:</strong>{' '}
              <span style={{ color: '#667eea', fontWeight: 'bold' }}>{lotes.length}</span>
            </div>
            <div>
              <strong style={{ color: '#495057' }}>Área total:</strong>{' '}
              <span style={{ color: '#28a745', fontWeight: 'bold' }}>
                {areaTotal.toLocaleString()} m²
              </span>
              <span style={{ color: '#6c757d', fontSize: '0.9em', marginLeft: '5px' }}>
                ({(areaTotal / 10000).toFixed(4)} ha)
              </span>
            </div>
          </div>
          <button
            onClick={exportarLotes}
            style={{
              padding: '8px 16px',
              background: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontSize: '0.9em',
              fontWeight: 'bold'
            }}
          >
            💾 Exportar JSON
          </button>
        </div>
      )}

      {/* Mapa */}
      <div style={{ flex: 1, position: 'relative' }}>
        <MapContainer
          center={[-17.7833, -63.1821]}
          zoom={13}
          style={{ height: '100%', width: '100%' }}
          ref={mapRef}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          <MapaInteractivo onLotesChange={handleLotesChange} />
        </MapContainer>

        {/* Herramientas rápidas */}
        <HerramientasRapidas map={mapRef.current} />

        {/* Panel lateral con lista de lotes */}
        {lotes.length > 0 && (
          <div style={{
            position: 'absolute',
            top: '10px',
            left: '10px',
            background: 'white',
            borderRadius: '8px',
            boxShadow: '0 2px 10px rgba(0,0,0,0.2)',
            maxWidth: '300px',
            maxHeight: 'calc(100% - 20px)',
            overflow: 'auto',
            zIndex: 1000
          }}>
            <div style={{
              padding: '15px',
              borderBottom: '2px solid #667eea',
              background: '#f8f9fa'
            }}>
              <h3 style={{ margin: 0, color: '#333' }}>📋 Lista de Lotes</h3>
            </div>
            <div style={{ padding: '10px' }}>
              {lotes.map((lote, index) => (
                <div
                  key={lote.id}
                  style={{
                    padding: '12px',
                    marginBottom: '8px',
                    background: '#f8f9fa',
                    borderRadius: '6px',
                    border: '1px solid #dee2e6',
                    cursor: 'pointer',
                    transition: 'all 0.2s'
                  }}
                  onMouseEnter={(e) => {
                    e.currentTarget.style.background = '#e9ecef';
                    e.currentTarget.style.borderColor = '#667eea';
                  }}
                  onMouseLeave={(e) => {
                    e.currentTarget.style.background = '#f8f9fa';
                    e.currentTarget.style.borderColor = '#dee2e6';
                  }}
                  onClick={() => {
                    if (lote.layer instanceof L.Polygon) {
                      lote.layer.openPopup();
                      if (mapRef.current) {
                        mapRef.current.fitBounds(lote.layer.getBounds());
                      }
                    }
                  }}
                >
                  <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    marginBottom: '5px'
                  }}>
                    <strong style={{ color: '#667eea' }}>{lote.id}</strong>
                    <span style={{
                      fontSize: '0.75em',
                      background: '#667eea',
                      color: 'white',
                      padding: '2px 8px',
                      borderRadius: '10px'
                    }}>
                      #{index + 1}
                    </span>
                  </div>
                  <div style={{ fontSize: '0.9em', color: '#495057' }}>
                    <div>{lote.nombre}</div>
                    <div style={{ marginTop: '5px', color: '#28a745', fontWeight: 'bold' }}>
                      📐 {lote.area.toLocaleString()} m²
                    </div>
                    <div style={{ fontSize: '0.85em', color: '#6c757d' }}>
                      {(lote.area / 10000).toFixed(4)} hectáreas
                    </div>
                    <div style={{ fontSize: '0.85em', color: '#495057', marginTop: '5px' }}>
                      📏 Perímetro: {lote.perimetro.toLocaleString()} m
                    </div>
                    <div style={{ fontSize: '0.8em', color: '#6c757d', marginTop: '3px' }}>
                      {lote.lados.length} lados
                    </div>
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        duplicarLote(lote);
                      }}
                      style={{
                        marginTop: '8px',
                        padding: '5px 10px',
                        background: '#17a2b8',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer',
                        fontSize: '0.75em',
                        width: '100%',
                        fontWeight: 'bold'
                      }}
                    >
                      📋 Duplicar
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* Instrucciones */}
        {lotes.length === 0 && (
          <div style={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            background: 'white',
            padding: '30px',
            borderRadius: '12px',
            boxShadow: '0 4px 20px rgba(0,0,0,0.15)',
            maxWidth: '400px',
            textAlign: 'center',
            zIndex: 1000
          }}>
            <div style={{ fontSize: '3em', marginBottom: '15px' }}>🏘️</div>
            <h3 style={{ margin: '0 0 15px 0', color: '#333' }}>¡Comienza a dibujar lotes!</h3>
            <p style={{ color: '#666', lineHeight: '1.6', margin: '0 0 15px 0' }}>
              <strong>Opción 1:</strong> Usa el botón <strong style={{ color: '#FF9800' }}>⚡ Dibujo Rápido</strong> para crear lotes ingresando medidas específicas.
            </p>
            <p style={{ color: '#666', lineHeight: '1.6', margin: '0 0 15px 0' }}>
              <strong>Opción 2:</strong> Usa el botón <strong style={{ color: '#667eea' }}>⬟</strong> para dibujar manualmente polígonos en el mapa.
            </p>
            <div style={{
              background: '#f8f9fa',
              padding: '10px',
              borderRadius: '6px',
              marginTop: '15px'
            }}>
              <p style={{ color: '#495057', fontSize: '0.85em', margin: 0 }}>
                💡 <strong>Tip:</strong> Después de crear un lote, puedes editarlo, duplicarlo o ver sus medidas exactas.
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
