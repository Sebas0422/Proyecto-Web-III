import { useEffect, useRef, useState } from 'react';
import { MapContainer, TileLayer, useMap } from 'react-leaflet';
import L from 'leaflet';
import '@geoman-io/leaflet-geoman-free';
import '@geoman-io/leaflet-geoman-free/dist/leaflet-geoman.css';
import 'leaflet/dist/leaflet.css';

// Fix para los iconos de Leaflet
import icon from 'leaflet/dist/images/marker-icon.png';
import iconShadow from 'leaflet/dist/images/marker-shadow.png';

const DefaultIcon = L.icon({
  iconUrl: icon,
  shadowUrl: iconShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41]
});

L.Marker.prototype.options.icon = DefaultIcon;

// ============ INTERFACES ============

interface Lote {
  id: string;
  numeroLote: number;
  manzanoId: number;
  tipo: 'normal' | 'esquina' | 'especial';
  area: number;
  coordenadas: Array<[number, number]>;
  lados: number[];
  perimetro: number;
  observaciones?: string;
  propietario?: string;
  layer: L.Polygon;
  marker?: L.Marker;
}

interface Manzano {
  id: number;
  numero: number;
  nombre: string;
  color: string;
}

interface Calle {
  id: string;
  nombre: string;
  coordenadas: Array<[number, number]>;
  ancho?: number;
  layer: L.Polyline;
}

// ============ UTILIDADES ============

function calcularDistancia(lat1: number, lng1: number, lat2: number, lng2: number): number {
  const R = 6378137;
  const toRad = (deg: number) => (deg * Math.PI) / 180;
  const dLat = toRad(lat2 - lat1);
  const dLng = toRad(lng2 - lng1);
  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
    Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return Math.round(R * c * 100) / 100;
}

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
  return { lados, perimetro: Math.round(perimetro * 100) / 100 };
}

function calcularArea(latlngs: L.LatLng[]): number {
  if (latlngs.length < 3) return 0;
  const R = 6378137;
  let area = 0;
  const toRad = (deg: number) => (deg * Math.PI) / 180;
  for (let i = 0; i < latlngs.length; i++) {
    const p1 = latlngs[i];
    const p2 = latlngs[(i + 1) % latlngs.length];
    const lat1 = toRad(p1.lat);
    const lat2 = toRad(p2.lat);
    const lng1 = toRad(p1.lng);
    const lng2 = toRad(p2.lng);
    area += (lng2 - lng1) * (2 + Math.sin(lat1) + Math.sin(lat2));
  }
  area = Math.abs(area * R * R / 2.0);
  return Math.round(area * 100) / 100;
}

// ============ COMPONENTE DE MAPA INTERACTIVO ============

interface MapaInteractivoProps {
  onLoteCreado: (lote: Lote) => void;
  onLoteEditado: (loteId: string, nuevosDatos: Partial<Lote>) => void;
  onLoteEliminado: (loteId: string) => void;
  onCalleCreada: (calle: Calle) => void;
  manzanoActual: number;
  tipoLoteActual: 'normal' | 'esquina' | 'especial';
  colorManzanoActual: string;
  modoActual: 'lotes' | 'calles';
  modoSeleccion: boolean;
  onLoteClick: (loteId: string) => void;
}

function MapaInteractivo({
  onLoteCreado,
  onLoteEditado,
  onLoteEliminado,
  onCalleCreada,
  manzanoActual,
  tipoLoteActual,
  colorManzanoActual,
  modoActual,
  modoSeleccion,
  onLoteClick
}: MapaInteractivoProps) {
  const map = useMap();
  const contadorLotesRef = useRef<{ [key: number]: number }>({});

  useEffect(() => {
    if (!map) return;

    map.pm.addControls({
      position: 'topright',
      drawCircle: false,
      drawCircleMarker: false,
      drawMarker: false,
      drawRectangle: false,
      drawPolygon: modoActual === 'lotes' && !modoSeleccion,
      drawPolyline: modoActual === 'calles' && !modoSeleccion,
      editMode: true,
      dragMode: modoSeleccion,
      cutPolygon: false,
      removalMode: !modoSeleccion,
    });

    map.pm.setGlobalOptions({
      pathOptions: {
        color: colorManzanoActual,
        fillColor: colorManzanoActual,
        fillOpacity: tipoLoteActual === 'esquina' ? 0.5 : 0.3,
        weight: 3
      }
    });

    // Crear lotes
    const handleCreate = (e: L.LeafletEvent & { layer: L.Layer }) => {
      if (modoActual === 'lotes' && e.layer instanceof L.Polygon) {
        const layer = e.layer as L.Polygon;
        const latlngs = layer.getLatLngs()[0] as L.LatLng[];
        const area = calcularArea(latlngs);
        const { lados, perimetro } = calcularLados(latlngs);

        if (!contadorLotesRef.current[manzanoActual]) {
          contadorLotesRef.current[manzanoActual] = 0;
        }
        contadorLotesRef.current[manzanoActual]++;
        const numeroLote = contadorLotesRef.current[manzanoActual];

        // Añadir label en el centro del polígono
        const bounds = layer.getBounds();
        const center = bounds.getCenter();
        const tipoIcon = tipoLoteActual === 'esquina' ? '📐' : tipoLoteActual === 'especial' ? '⭐' : '';

        const marker = L.marker(center, {
          icon: L.divIcon({
            className: 'lote-label',
            html: `<div style="background: white; padding: 4px 8px; border-radius: 4px; font-weight: bold; border: 2px solid ${colorManzanoActual}; font-size: 11px; white-space: nowrap;">
              ${tipoIcon} M${manzanoActual}-L${numeroLote}
            </div>`,
            iconSize: [60, 20]
          })
        }).addTo(map);

        const nuevoLote: Lote = {
          id: `M${manzanoActual}-L${numeroLote}`,
          numeroLote,
          manzanoId: manzanoActual,
          tipo: tipoLoteActual,
          area,
          lados,
          perimetro,
          coordenadas: latlngs.map(ll => [ll.lat, ll.lng] as [number, number]),
          layer,
          marker
        };

        const ladosHTML = lados.map((lado, idx) =>
          `<div style="margin: 2px 0; font-size: 0.85em;">▸ Lado ${idx + 1}: <strong>${lado.toLocaleString()} m</strong></div>`
        ).join('');

        layer.bindPopup(`
          <div style="font-family: sans-serif; min-width: 220px;">
            <h3 style="margin: 0 0 10px 0; color: ${colorManzanoActual}; border-bottom: 2px solid ${colorManzanoActual}; padding-bottom: 5px;">
              ${tipoIcon} M${manzanoActual}-L${numeroLote}
            </h3>
            <div style="background: #f8f9fa; padding: 8px; border-radius: 4px; margin-bottom: 8px;">
              <div style="margin: 3px 0;"><strong>Manzano:</strong> ${manzanoActual}</div>
              <div style="margin: 3px 0;"><strong>Lote:</strong> ${numeroLote}</div>
              <div style="margin: 3px 0;"><strong>Tipo:</strong> ${tipoLoteActual === 'esquina' ? '🔶 Esquina' : tipoLoteActual === 'especial' ? '⭐ Especial' : '📦 Normal'}</div>
            </div>
            <div style="background: #e7f3ff; padding: 8px; border-radius: 4px; margin-bottom: 8px;">
              <div style="margin: 3px 0;"><strong>📐 Área:</strong> ${area.toLocaleString()} m²</div>
              <div style="margin: 3px 0; font-size: 0.85em; color: #666;">(${(area / 10000).toFixed(4)} ha)</div>
              <div style="margin: 3px 0;"><strong>📏 Perímetro:</strong> ${perimetro.toLocaleString()} m</div>
            </div>
            <div style="background: #fff3cd; padding: 8px; border-radius: 4px;">
              <strong style="display: block; margin-bottom: 5px;">📏 Medidas de lados:</strong>
              ${ladosHTML}
            </div>
          </div>
        `);

        // Agregar evento de click para selección
        layer.on('click', (e) => {
          if (modoSeleccion) {
            L.DomEvent.stopPropagation(e);
            onLoteClick(nuevoLote.id);
          }
        });

        onLoteCreado(nuevoLote);
      } else if (modoActual === 'calles' && e.layer instanceof L.Polyline) {
        const layer = e.layer as L.Polyline;
        const latlngs = layer.getLatLngs() as L.LatLng[];
        const nuevaCalle: Calle = {
          id: `CALLE-${Date.now()}`,
          nombre: `Calle ${Date.now()}`,
          coordenadas: latlngs.map(ll => [ll.lat, ll.lng] as [number, number]),
          layer
        };
        onCalleCreada(nuevaCalle);
      }
    };

    map.on('pm:create', handleCreate);

    return () => {
      map.pm.removeControls();
      map.off('pm:create', handleCreate);
    };
  }, [map, manzanoActual, tipoLoteActual, colorManzanoActual, modoActual, modoSeleccion, onLoteCreado, onCalleCreada, onLoteEditado, onLoteEliminado, onLoteClick]);

  return null;
}

// ============ GENERADOR DE URBANIZACIÓN DE EJEMPLO ============

function crearUrbanizacionEjemplo(map: L.Map): {
  manzanos: Manzano[];
  lotes: Lote[];
  calles: Calle[];
} {
  const baseLatitude = -17.915975867119464;
  const baseLongitude = -63.313002976028486;

  // Conversión aproximada de metros a grados (en esta latitud)
  const metersToLat = (m: number) => m / 111320;
  const metersToLng = (m: number) => m / (111320 * Math.cos(baseLatitude * Math.PI / 180));

  const colores = ['#667eea', '#f093fb', '#4facfe', '#43e97b', '#fa709a', '#fee140', '#30cfd0', '#a8edea', '#ff9a9e', '#fecfef'];

  const manzanos: Manzano[] = [];
  const lotes: Lote[] = [];
  const calles: Calle[] = [];

  // Dimensiones
  const manzanoAncho = 60; // metros
  const manzanoAlto = 80; // metros
  const calleAncho = 10; // metros
  const loteAncho = 15; // metros (4 lotes por manzano en ancho)
  const loteAlto = 20; // metros (4 lotes por manzano en alto)

  // Configuración de la cuadrícula: 2x5 = 10 manzanos
  const filasM = 2;
  const columnasM = 5;

  // Crear manzanos en cuadrícula
  for (let fila = 0; fila < filasM; fila++) {
    for (let col = 0; col < columnasM; col++) {
      const manzanoId = fila * columnasM + col + 1;

      // Crear manzano
      const manzano: Manzano = {
        id: manzanoId,
        numero: manzanoId,
        nombre: `Manzano ${manzanoId}`,
        color: colores[(manzanoId - 1) % colores.length]
      };
      manzanos.push(manzano);

      // Posición base del manzano
      const manzanoBaseLat = baseLatitude - metersToLat(fila * (manzanoAlto + calleAncho));
      const manzanoBaseLng = baseLongitude + metersToLng(col * (manzanoAncho + calleAncho));

      // Crear lotes dentro del manzano (4x4 = 16 lotes por manzano)
      const lotesFilas = 4;
      const lotesColumnas = 4;
      let numeroLote = 1;

      for (let lf = 0; lf < lotesFilas; lf++) {
        for (let lc = 0; lc < lotesColumnas; lc++) {
          const latBase = manzanoBaseLat - metersToLat(lf * loteAlto);
          const lngBase = manzanoBaseLng + metersToLng(lc * loteAncho);

          const coordenadas: Array<[number, number]> = [
            [latBase, lngBase],
            [latBase - metersToLat(loteAlto), lngBase],
            [latBase - metersToLat(loteAlto), lngBase + metersToLng(loteAncho)],
            [latBase, lngBase + metersToLng(loteAncho)],
          ];

          const latlngs = coordenadas.map(c => L.latLng(c[0], c[1]));
          const area = calcularArea(latlngs);
          const { lados, perimetro } = calcularLados(latlngs);

          // Determinar tipo de lote (esquinas)
          const esEsquina = (lf === 0 && lc === 0) ||
            (lf === 0 && lc === lotesColumnas - 1) ||
            (lf === lotesFilas - 1 && lc === 0) ||
            (lf === lotesFilas - 1 && lc === lotesColumnas - 1);

          const polygon = L.polygon(latlngs, {
            color: manzano.color,
            fillColor: manzano.color,
            fillOpacity: esEsquina ? 0.5 : 0.3,
            weight: 2
          }).addTo(map);

          // Añadir etiqueta
          const bounds = polygon.getBounds();
          const center = bounds.getCenter();
          const tipoIcon = esEsquina ? '📐' : '';

          const marker = L.marker(center, {
            icon: L.divIcon({
              className: 'lote-label',
              html: `<div style="background: white; padding: 2px 6px; border-radius: 3px; font-weight: bold; border: 1px solid ${manzano.color}; font-size: 9px; white-space: nowrap;">
                ${tipoIcon} M${manzanoId}-L${numeroLote}
              </div>`,
              iconSize: [50, 15]
            })
          }).addTo(map);

          const lote: Lote = {
            id: `M${manzanoId}-L${numeroLote}`,
            numeroLote: numeroLote,
            manzanoId: manzanoId,
            tipo: esEsquina ? 'esquina' : 'normal',
            area,
            lados,
            perimetro,
            coordenadas,
            layer: polygon,
            marker
          };

          const ladosHTML = lados.map((lado, idx) =>
            `<div style="margin: 2px 0; font-size: 0.85em;">▸ Lado ${idx + 1}: <strong>${lado.toLocaleString()} m</strong></div>`
          ).join('');

          polygon.bindPopup(`
            <div style="font-family: sans-serif; min-width: 220px;">
              <h3 style="margin: 0 0 10px 0; color: ${manzano.color}; border-bottom: 2px solid ${manzano.color}; padding-bottom: 5px;">
                ${tipoIcon} M${manzanoId}-L${numeroLote}
              </h3>
              <div style="background: #f8f9fa; padding: 8px; border-radius: 4px; margin-bottom: 8px;">
                <div style="margin: 3px 0;"><strong>Manzano:</strong> ${manzanoId}</div>
                <div style="margin: 3px 0;"><strong>Lote:</strong> ${numeroLote}</div>
                <div style="margin: 3px 0;"><strong>Tipo:</strong> ${esEsquina ? '🔶 Esquina' : '📦 Normal'}</div>
              </div>
              <div style="background: #e7f3ff; padding: 8px; border-radius: 4px; margin-bottom: 8px;">
                <div style="margin: 3px 0;"><strong>📐 Área:</strong> ${area.toLocaleString()} m²</div>
                <div style="margin: 3px 0; font-size: 0.85em; color: #666;">(${(area / 10000).toFixed(4)} ha)</div>
                <div style="margin: 3px 0;"><strong>📏 Perímetro:</strong> ${perimetro.toLocaleString()} m</div>
              </div>
              <div style="background: #fff3cd; padding: 8px; border-radius: 4px;">
                <strong style="display: block; margin-bottom: 5px;">📏 Medidas de lados:</strong>
                ${ladosHTML}
              </div>
            </div>
          `);

          lotes.push(lote);
          numeroLote++;
        }
      }
    }
  }

  // Crear calles horizontales (entre filas de manzanos)
  for (let i = 0; i <= filasM; i++) {
    const calleLatitud = baseLatitude - metersToLat(i * (manzanoAlto + calleAncho) - calleAncho / 2);
    const coordenadas: Array<[number, number]> = [
      [calleLatitud, baseLongitude - metersToLng(calleAncho)],
      [calleLatitud, baseLongitude + metersToLng(columnasM * (manzanoAncho + calleAncho))]
    ];

    const polyline = L.polyline(coordenadas.map(c => L.latLng(c[0], c[1])), {
      color: '#808080',
      weight: 4,
      opacity: 0.7
    }).addTo(map);

    const nombreCalle = i === 0 ? 'Av. Norte' : i === filasM ? 'Av. Sur' : `Calle ${i}`;

    polyline.bindPopup(`<strong>🛣️ ${nombreCalle}</strong>`);

    calles.push({
      id: `CALLE-H-${i}`,
      nombre: nombreCalle,
      coordenadas,
      ancho: calleAncho,
      layer: polyline
    });
  }

  // Crear calles verticales (entre columnas de manzanos)
  for (let i = 0; i <= columnasM; i++) {
    const calleLongitud = baseLongitude + metersToLng(i * (manzanoAncho + calleAncho) - calleAncho / 2);
    const coordenadas: Array<[number, number]> = [
      [baseLatitude + metersToLat(calleAncho), calleLongitud],
      [baseLatitude - metersToLat(filasM * (manzanoAlto + calleAncho)), calleLongitud]
    ];

    const polyline = L.polyline(coordenadas.map(c => L.latLng(c[0], c[1])), {
      color: '#808080',
      weight: 4,
      opacity: 0.7
    }).addTo(map);

    const nombreCalle = i === 0 ? 'Av. Oeste' : i === columnasM ? 'Av. Este' : `Calle ${String.fromCharCode(65 + i - 1)}`;

    polyline.bindPopup(`<strong>🛣️ ${nombreCalle}</strong>`);

    calles.push({
      id: `CALLE-V-${i}`,
      nombre: nombreCalle,
      coordenadas,
      ancho: calleAncho,
      layer: polyline
    });
  }

  return { manzanos, lotes, calles };
}

// ============ COMPONENTE PRINCIPAL ============

export default function MapaUrbanizacion() {
  const [lotes, setLotes] = useState<Lote[]>([]);
  const [manzanos, setManzanos] = useState<Manzano[]>([
    { id: 1, numero: 1, nombre: 'Manzano 1', color: '#667eea' }
  ]);
  const [calles, setCalles] = useState<Calle[]>([]);
  const [manzanoActual, setManzanoActual] = useState(1);
  const [tipoLoteActual, setTipoLoteActual] = useState<'normal' | 'esquina' | 'especial'>('normal');
  const [modoActual, setModoActual] = useState<'lotes' | 'calles'>('lotes');
  const mapRef = useRef<L.Map | null>(null);
  const [ejemploCargado, setEjemploCargado] = useState(false);
  const [lotesSeleccionados, setLotesSeleccionados] = useState<string[]>([]);
  const [modoSeleccion, setModoSeleccion] = useState(false);

  const manzanoSeleccionado = manzanos.find(m => m.id === manzanoActual) || manzanos[0];
  const lotesPorManzano = lotes.filter(l => l.manzanoId === manzanoActual);

  // Cargar ejemplo automáticamente al iniciar
  useEffect(() => {
    if (!ejemploCargado && mapRef.current) {
      const { manzanos: manzanosEjemplo, lotes: lotesEjemplo, calles: callesEjemplo } =
        crearUrbanizacionEjemplo(mapRef.current);
      setManzanos(manzanosEjemplo);
      setLotes(lotesEjemplo);
      setCalles(callesEjemplo);
      setManzanoActual(1);
      setEjemploCargado(true);

      // Ajustar vista del mapa
      setTimeout(() => {
        if (mapRef.current && lotesEjemplo.length > 0) {
          const allCoords = lotesEjemplo.flatMap(l => l.coordenadas.map(c => L.latLng(c[0], c[1])));
          const bounds = L.latLngBounds(allCoords);
          mapRef.current.fitBounds(bounds, { padding: [50, 50] });
        }
      }, 500);
    }
  }, [ejemploCargado]);

  const cargarEjemplo = () => {
    if (mapRef.current) {
      // Limpiar capas existentes (incluyendo markers)
      lotes.forEach(l => {
        l.layer.remove();
        if (l.marker) l.marker.remove();
      });
      calles.forEach(c => c.layer.remove());

      const { manzanos: manzanosEjemplo, lotes: lotesEjemplo, calles: callesEjemplo } =
        crearUrbanizacionEjemplo(mapRef.current);
      setManzanos(manzanosEjemplo);
      setLotes(lotesEjemplo);
      setCalles(callesEjemplo);
      setManzanoActual(1);

      // Ajustar vista del mapa
      setTimeout(() => {
        if (mapRef.current && lotesEjemplo.length > 0) {
          const allCoords = lotesEjemplo.flatMap(l => l.coordenadas.map(c => L.latLng(c[0], c[1])));
          const bounds = L.latLngBounds(allCoords);
          mapRef.current.fitBounds(bounds, { padding: [50, 50] });
        }
      }, 500);
    }
  };

  const limpiarMapa = () => {
    // Limpiar capas existentes (incluyendo markers)
    lotes.forEach(l => {
      l.layer.remove();
      if (l.marker) l.marker.remove();
    });
    calles.forEach(c => c.layer.remove());
    setLotes([]);
    setManzanos([
      { id: 1, numero: 1, nombre: 'Manzano 1', color: '#667eea' }
    ]);
    setCalles([]);
    setManzanoActual(1);
    setLotesSeleccionados([]);
  };

  const toggleSeleccionLote = (loteId: string) => {
    setLotesSeleccionados(prev => {
      if (prev.includes(loteId)) {
        // Deseleccionar
        const lote = lotes.find(l => l.id === loteId);
        if (lote) {
          const manzano = manzanos.find(m => m.id === lote.manzanoId);
          lote.layer.setStyle({
            color: manzano?.color || '#667eea',
            fillColor: manzano?.color || '#667eea',
            fillOpacity: lote.tipo === 'esquina' ? 0.5 : 0.3,
            weight: 2
          });
        }
        return prev.filter(id => id !== loteId);
      } else {
        // Seleccionar
        const lote = lotes.find(l => l.id === loteId);
        if (lote) {
          lote.layer.setStyle({
            color: '#FFD700',
            fillColor: '#FFD700',
            fillOpacity: 0.6,
            weight: 4
          });
        }
        return [...prev, loteId];
      }
    });
  };

  const seleccionarTodos = () => {
    const todosIds = lotes.map(l => l.id);
    setLotesSeleccionados(todosIds);
    lotes.forEach(lote => {
      lote.layer.setStyle({
        color: '#FFD700',
        fillColor: '#FFD700',
        fillOpacity: 0.6,
        weight: 4
      });
    });
  };

  const deseleccionarTodos = () => {
    lotes.forEach(lote => {
      const manzano = manzanos.find(m => m.id === lote.manzanoId);
      lote.layer.setStyle({
        color: manzano?.color || '#667eea',
        fillColor: manzano?.color || '#667eea',
        fillOpacity: lote.tipo === 'esquina' ? 0.5 : 0.3,
        weight: 2
      });
    });
    setLotesSeleccionados([]);
  };

  const moverSeleccionados = (deltaLat: number, deltaLng: number) => {
    const lotesAMover = lotes.filter(l => lotesSeleccionados.includes(l.id));

    lotesAMover.forEach(lote => {
      // Mover polígono
      const nuevasCoordenadas = lote.coordenadas.map(([lat, lng]) =>
        [lat + deltaLat, lng + deltaLng] as [number, number]
      );
      const nuevasLatLngs = nuevasCoordenadas.map(c => L.latLng(c[0], c[1]));
      lote.layer.setLatLngs(nuevasLatLngs);

      // Mover marker
      if (lote.marker) {
        const bounds = lote.layer.getBounds();
        const center = bounds.getCenter();
        lote.marker.setLatLng(center);
      }

      // Actualizar coordenadas en el estado
      lote.coordenadas = nuevasCoordenadas;
    });

    // Actualizar estado
    setLotes([...lotes]);
  };

  const eliminarSeleccionados = () => {
    if (lotesSeleccionados.length === 0) return;

    if (confirm(`¿Eliminar ${lotesSeleccionados.length} lote(s) seleccionado(s)?`)) {
      lotes.forEach(lote => {
        if (lotesSeleccionados.includes(lote.id)) {
          lote.layer.remove();
          if (lote.marker) lote.marker.remove();
        }
      });
      setLotes(lotes.filter(l => !lotesSeleccionados.includes(l.id)));
      setLotesSeleccionados([]);
    }
  };

  const agregarManzano = () => {
    const nuevoNum = Math.max(...manzanos.map(m => m.numero)) + 1;
    const colores = ['#667eea', '#f093fb', '#4facfe', '#43e97b', '#fa709a', '#fee140'];
    const nuevoManzano: Manzano = {
      id: nuevoNum,
      numero: nuevoNum,
      nombre: `Manzano ${nuevoNum}`,
      color: colores[nuevoNum % colores.length]
    };
    setManzanos([...manzanos, nuevoManzano]);
    setManzanoActual(nuevoNum);
  };

  const exportarDatos = () => {
    const data = {
      manzanos: manzanos.map(m => ({
        ...m,
        lotes: lotes.filter(l => l.manzanoId === m.id).map(l => ({
          id: l.id,
          numeroLote: l.numeroLote,
          tipo: l.tipo,
          area: l.area,
          perimetro: l.perimetro,
          lados: l.lados,
          coordenadas: l.coordenadas,
          observaciones: l.observaciones,
          propietario: l.propietario
        }))
      })),
      calles: calles.map(c => ({ id: c.id, nombre: c.nombre, coordenadas: c.coordenadas, ancho: c.ancho }))
    };

    const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'urbanizacion.json';
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);
    URL.revokeObjectURL(url);
  };

  const areaTotal = lotes.reduce((sum, lote) => sum + lote.area, 0);
  const areaManzanoActual = lotesPorManzano.reduce((sum, lote) => sum + lote.area, 0);

  return (
    <div style={{ height: '100vh', display: 'flex', flexDirection: 'column' }}>
      {/* Header */}
      <div style={{
        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        color: 'white',
        padding: '15px 20px',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
      }}>
        <h1 style={{ margin: 0, fontSize: '1.8em' }}>🏗️ Sistema de Urbanización Profesional</h1>
        <p style={{ margin: '5px 0 0 0', opacity: 0.9, fontSize: '0.9em' }}>
          Digitalización de planos arquitectónicos • Manzanos y Lotes
        </p>
      </div>

      {/* Barra de herramientas */}
      <div style={{
        background: '#f8f9fa',
        padding: '12px 20px',
        borderBottom: '2px solid #dee2e6',
        display: 'flex',
        gap: '15px',
        flexWrap: 'wrap',
        alignItems: 'center'
      }}>
        <div style={{ display: 'flex', gap: '10px', alignItems: 'center' }}>
          <label style={{ fontWeight: 'bold', fontSize: '0.9em' }}>Modo:</label>
          <button
            onClick={() => {
              setModoActual('lotes');
              setModoSeleccion(false);
            }}
            style={{
              padding: '8px 16px',
              background: modoActual === 'lotes' && !modoSeleccion ? '#667eea' : 'white',
              color: modoActual === 'lotes' && !modoSeleccion ? 'white' : '#333',
              border: '2px solid #667eea',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            🏘️ Lotes
          </button>
          <button
            onClick={() => {
              setModoActual('calles');
              setModoSeleccion(false);
            }}
            style={{
              padding: '8px 16px',
              background: modoActual === 'calles' && !modoSeleccion ? '#667eea' : 'white',
              color: modoActual === 'calles' && !modoSeleccion ? 'white' : '#333',
              border: '2px solid #667eea',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            🛣️ Calles
          </button>
          <button
            onClick={() => {
              setModoSeleccion(!modoSeleccion);
              if (!modoSeleccion) {
                deseleccionarTodos();
              }
            }}
            style={{
              padding: '8px 16px',
              background: modoSeleccion ? '#ffc107' : 'white',
              color: modoSeleccion ? 'white' : '#333',
              border: '2px solid #ffc107',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            {modoSeleccion ? '✓ Seleccionar' : '👆 Seleccionar'}
          </button>
        </div>

        {modoActual === 'lotes' && (
          <>
            <div style={{ borderLeft: '2px solid #dee2e6', height: '30px' }} />
            <div>
              <label style={{ fontWeight: 'bold', fontSize: '0.85em', display: 'block', marginBottom: '3px' }}>
                Manzano:
              </label>
              <select
                value={manzanoActual}
                onChange={(e) => setManzanoActual(Number(e.target.value))}
                style={{
                  padding: '6px 12px',
                  border: `2px solid ${manzanoSeleccionado?.color || '#667eea'}`,
                  borderRadius: '4px',
                  fontSize: '0.9em',
                  fontWeight: 'bold',
                  background: 'white'
                }}
              >
                {manzanos.map(m => (
                  <option key={m.id} value={m.id}>
                    Manzano {m.numero} ({lotes.filter(l => l.manzanoId === m.id).length} lotes)
                  </option>
                ))}
              </select>
            </div>

            <button
              onClick={agregarManzano}
              style={{
                padding: '6px 12px',
                background: '#28a745',
                color: 'white',
                border: 'none',
                borderRadius: '4px',
                cursor: 'pointer',
                fontWeight: 'bold',
                fontSize: '0.85em'
              }}
            >
              ➕ Nuevo Manzano
            </button>

            <div style={{ borderLeft: '2px solid #dee2e6', height: '30px' }} />

            <div>
              <label style={{ fontWeight: 'bold', fontSize: '0.85em', display: 'block', marginBottom: '3px' }}>
                Tipo de Lote:
              </label>
              <select
                value={tipoLoteActual}
                onChange={(e) => setTipoLoteActual(e.target.value as 'normal' | 'esquina' | 'especial')}
                style={{
                  padding: '6px 12px',
                  border: '2px solid #dee2e6',
                  borderRadius: '4px',
                  fontSize: '0.9em',
                  background: 'white'
                }}
              >
                <option value="normal">📦 Normal</option>
                <option value="esquina">🔶 Esquina</option>
                <option value="especial">⭐ Especial</option>
              </select>
            </div>
          </>
        )}

        {/* Panel de control de selección */}
        {modoSeleccion && (
          <>
            <div style={{ borderLeft: '2px solid #dee2e6', height: '30px' }} />
            <div style={{ display: 'flex', gap: '8px', alignItems: 'center', background: '#fff3cd', padding: '8px 12px', borderRadius: '6px' }}>
              <span style={{ fontSize: '0.85em', fontWeight: 'bold' }}>
                {lotesSeleccionados.length} seleccionado(s)
              </span>
              <button
                onClick={seleccionarTodos}
                style={{
                  padding: '4px 10px',
                  background: '#28a745',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.8em',
                  fontWeight: 'bold'
                }}
              >
                Seleccionar Todos
              </button>
              <button
                onClick={deseleccionarTodos}
                style={{
                  padding: '4px 10px',
                  background: '#6c757d',
                  color: 'white',
                  border: 'none',
                  borderRadius: '4px',
                  cursor: 'pointer',
                  fontSize: '0.8em',
                  fontWeight: 'bold'
                }}
              >
                Deseleccionar
              </button>
              {lotesSeleccionados.length > 0 && (
                <>
                  <div style={{ borderLeft: '2px solid #dee2e6', height: '20px' }} />
                  <button
                    onClick={() => moverSeleccionados(0.0001, 0)}
                    style={{
                      padding: '4px 8px',
                      background: '#007bff',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.8em'
                    }}
                    title="Mover arriba"
                  >
                    ⬆️
                  </button>
                  <button
                    onClick={() => moverSeleccionados(-0.0001, 0)}
                    style={{
                      padding: '4px 8px',
                      background: '#007bff',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.8em'
                    }}
                    title="Mover abajo"
                  >
                    ⬇️
                  </button>
                  <button
                    onClick={() => moverSeleccionados(0, -0.0001)}
                    style={{
                      padding: '4px 8px',
                      background: '#007bff',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.8em'
                    }}
                    title="Mover izquierda"
                  >
                    ⬅️
                  </button>
                  <button
                    onClick={() => moverSeleccionados(0, 0.0001)}
                    style={{
                      padding: '4px 8px',
                      background: '#007bff',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.8em'
                    }}
                    title="Mover derecha"
                  >
                    ➡️
                  </button>
                  <button
                    onClick={eliminarSeleccionados}
                    style={{
                      padding: '4px 10px',
                      background: '#dc3545',
                      color: 'white',
                      border: 'none',
                      borderRadius: '4px',
                      cursor: 'pointer',
                      fontSize: '0.8em',
                      fontWeight: 'bold'
                    }}
                  >
                    🗑️ Eliminar
                  </button>
                </>
              )}
            </div>
          </>
        )}

        <div style={{ marginLeft: 'auto', display: 'flex', gap: '10px' }}>
          <button
            onClick={cargarEjemplo}
            style={{
              padding: '8px 16px',
              background: '#28a745',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            🏘️ Cargar Ejemplo
          </button>
          <button
            onClick={limpiarMapa}
            style={{
              padding: '8px 16px',
              background: '#dc3545',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            🗑️ Limpiar
          </button>
          <button
            onClick={exportarDatos}
            style={{
              padding: '8px 16px',
              background: '#17a2b8',
              color: 'white',
              border: 'none',
              borderRadius: '4px',
              cursor: 'pointer',
              fontWeight: 'bold',
              fontSize: '0.85em'
            }}
          >
            💾 Exportar
          </button>
        </div>
      </div>

      {/* Panel de estadísticas */}
      {lotes.length > 0 && manzanoSeleccionado && (
        <div style={{
          background: '#fff',
          padding: '10px 20px',
          borderBottom: '1px solid #dee2e6',
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
          gap: '15px',
          fontSize: '0.85em'
        }}>
          <div>
            <div style={{ color: '#666', fontSize: '0.9em' }}>Total Manzanos</div>
            <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: '#667eea' }}>{manzanos.length}</div>
          </div>
          <div>
            <div style={{ color: '#666', fontSize: '0.9em' }}>Total Lotes</div>
            <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: '#28a745' }}>{lotes.length}</div>
          </div>
          <div>
            <div style={{ color: '#666', fontSize: '0.9em' }}>Lotes Manzano {manzanoSeleccionado.numero}</div>
            <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: manzanoSeleccionado.color }}>
              {lotesPorManzano.length}
            </div>
          </div>
          <div>
            <div style={{ color: '#666', fontSize: '0.9em' }}>Área Total</div>
            <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: '#fd7e14' }}>
              {areaTotal.toLocaleString()} m²
            </div>
          </div>
          <div>
            <div style={{ color: '#666', fontSize: '0.9em' }}>Área M{manzanoSeleccionado.numero}</div>
            <div style={{ fontSize: '1.5em', fontWeight: 'bold', color: manzanoSeleccionado.color }}>
              {areaManzanoActual.toLocaleString()} m²
            </div>
          </div>
        </div>
      )}

      {/* Mapa */}
      <div style={{ flex: 1, position: 'relative' }}>
        <MapContainer
          center={[-17.915975867119464, -63.313002976028486]}
          zoom={17}
          style={{ height: '100%', width: '100%' }}
          ref={mapRef}
        >
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          <MapaInteractivo
            onLoteCreado={(lote) => setLotes([...lotes, lote])}
            onLoteEditado={(id, datos) => {
              setLotes(lotes.map(l => l.id === id ? { ...l, ...datos } : l));
            }}
            onLoteEliminado={(id) => setLotes(lotes.filter(l => l.id !== id))}
            onCalleCreada={(calle) => setCalles([...calles, calle])}
            manzanoActual={manzanoActual}
            tipoLoteActual={tipoLoteActual}
            colorManzanoActual={manzanoSeleccionado?.color || '#667eea'}
            modoActual={modoActual}
            modoSeleccion={modoSeleccion}
            onLoteClick={toggleSeleccionLote}
          />
        </MapContainer>

        {/* Panel lateral - Lista de lotes del manzano actual */}
        {lotesPorManzano.length > 0 && manzanoSeleccionado && (
          <div style={{
            position: 'absolute',
            top: '10px',
            left: '10px',
            background: 'white',
            borderRadius: '8px',
            boxShadow: '0 2px 12px rgba(0,0,0,0.2)',
            maxWidth: '320px',
            maxHeight: 'calc(100% - 20px)',
            overflow: 'auto',
            zIndex: 1000
          }}>
            <div style={{
              padding: '12px 15px',
              background: manzanoSeleccionado.color,
              color: 'white',
              borderRadius: '8px 8px 0 0',
              fontWeight: 'bold'
            }}>
              📋 Manzano {manzanoSeleccionado.numero} • {lotesPorManzano.length} lotes
            </div>
            <div style={{ padding: '10px' }}>
              {lotesPorManzano.map(lote => (
                <div
                  key={lote.id}
                  style={{
                    padding: '10px',
                    marginBottom: '8px',
                    background: '#f8f9fa',
                    borderRadius: '6px',
                    border: `2px solid ${manzanoSeleccionado.color}`,
                    cursor: 'pointer',
                    fontSize: '0.85em'
                  }}
                  onClick={() => {
                    lote.layer.openPopup();
                    if (mapRef.current) {
                      mapRef.current.fitBounds(lote.layer.getBounds());
                    }
                  }}
                >
                  <div style={{ fontWeight: 'bold', marginBottom: '5px', color: manzanoSeleccionado.color }}>
                    {lote.tipo === 'esquina' ? '🔶' : lote.tipo === 'especial' ? '⭐' : '📦'} {lote.id}
                  </div>
                  <div style={{ fontSize: '0.9em', color: '#495057' }}>
                    <div>Lote {lote.numeroLote} • {lote.tipo}</div>
                    <div style={{ color: '#28a745', fontWeight: 'bold', marginTop: '3px' }}>
                      {lote.area.toLocaleString()} m²
                    </div>
                    <div style={{ fontSize: '0.85em', color: '#6c757d' }}>
                      Perímetro: {lote.perimetro.toLocaleString()} m
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
