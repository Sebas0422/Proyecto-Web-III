import React, { useCallback, useRef, useState } from 'react';
import { GoogleMap, useJsApiLoader, InfoWindow, Polygon } from '@react-google-maps/api';
import {
    useGetLotsQuery,
    useCreateLotMutation,
    useUpdateLotMutation,
} from '@features/lots/services/lotsApi';
import { pathToWKT } from '../utils/wkt';

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

export default function MapCreateLot({ proyectoId }: Props) {
    const { isLoaded } = useJsApiLoader({
        googleMapsApiKey: import.meta.env.VITE_GOOGLE_MAPS_KEY,
        libraries: ['drawing'],
    });

    const mapRef = useRef<google.maps.Map | null>(null);
    const dmRef = useRef<google.maps.drawing.DrawingManager | null>(null);

    const [loteSeleccionado, setLoteSeleccionado] = useState<any>(null);
    const [showInfo, setShowInfo] = useState<{ lat: number; lng: number } | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [nuevaGeometria, setNuevaGeometria] = useState<google.maps.LatLngLiteral[] | null>(null);

    const [form, setForm] = useState({
        id: '',
        numeroLote: '',
        manzana: '',
        precio: '',
        observaciones: '',
    });

    const { data: lotes = [], refetch } = useGetLotsQuery({ proyectoId });
    const [createLot] = useCreateLotMutation();
    const [updateLot] = useUpdateLotMutation();

    const abrirModal = (lote?: any, geometria?: google.maps.LatLngLiteral[]) => {
        if (lote) {
        setForm({
            id: lote.id,
            numeroLote: lote.numeroLote,
            manzana: lote.manzana,
            precio: lote.precio,
            observaciones: lote.observaciones,
        });
        setLoteSeleccionado(lote);
        } else {
        setForm({
            id: '',
            numeroLote: '',
            manzana: '',
            precio: '',
            observaciones: '',
        });
        setNuevaGeometria(geometria || null);
        }

        setShowModal(true);
    };


    const guardarCambios = async () => {
    try {
        let result;

        if (loteSeleccionado?.id) {
        result = await updateLot({
            id: form.id,
            data: {
            numeroLote: form.numeroLote,
            manzana: form.manzana,
            precio: Number(form.precio),
            observaciones: form.observaciones,
            },
        });
        } else if (nuevaGeometria) {
        const wkt = pathToWKT(nuevaGeometria);

        result = await createLot({
            proyectoId,
            data: {
            numeroLote: form.numeroLote,
            manzana: form.manzana,
            geometriaWKT: wkt,
            precio: Number(form.precio),
            observaciones: form.observaciones,
            },
        });
        }

        if ("error" in result && result.error) {
        console.error(result.error);
        alert("Error guardando lote");
        return;
        }

        alert(loteSeleccionado ? "Lote actualizado correctamente" : "Lote creado correctamente");
        
    } catch (err) {
        console.error(err);
        alert("Error guardando lote");
    } finally {
        setShowModal(false);
        setLoteSeleccionado(null);
        setNuevaGeometria(null);

        setTimeout(() => {
        refetch();
        }, 200);
    }
    };


    const onLoadMap = useCallback((map: google.maps.Map) => {
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

        google.maps.event.addListener(drawingManager, 'overlaycomplete', (ev: any) => {
        if (ev.type !== google.maps.drawing.OverlayType.POLYGON) return;

        const polygon: google.maps.Polygon = ev.overlay;
        const path = polygon.getPath().getArray().map((p) => ({ lat: p.lat(), lng: p.lng() }));

        setNuevaGeometria(path);
        abrirModal(undefined, path);

        polygon.setMap(null);
        });
    }, []);

    if (!isLoaded) return <div>Cargando mapa...</div>;

    return (
        <>
        <GoogleMap
            mapContainerStyle={containerStyle}
            center={defaultCenter}
            zoom={14}
            onLoad={onLoadMap}
        >
            {lotes.map((lote) => {
            const path = wktToPath(lote.geometriaWKT);

            return (
                <Polygon
                key={lote.id}
                paths={path}
                options={{
                    strokeColor: '#007bff',
                    strokeOpacity: 0.9,
                    strokeWeight: 2,
                    fillColor: '#007bff',
                    fillOpacity: 0.35,
                }}
                onClick={(e) => {
                    setLoteSeleccionado(lote);
                    setShowInfo({
                    lat: e.latLng?.lat() || 0,
                    lng: e.latLng?.lng() || 0,
                    });
                }}
                />
            );
            })}

            {loteSeleccionado && showInfo && (
            <InfoWindow position={showInfo} onCloseClick={() => setShowInfo(null)}>
                <div className="flex flex-col gap-2">
                <span>
                    <strong>Lote:</strong> {loteSeleccionado.numeroLote}
                </span>
                <span>
                    <strong>Manzana:</strong> {loteSeleccionado.manzana}
                </span>
                <span>
                    <strong>Precio:</strong> {loteSeleccionado.precio}
                </span>

                <button
                    className="mt-2 px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700"
                    onClick={() => abrirModal(loteSeleccionado)}
                >
                    Editar
                </button>
                </div>
            </InfoWindow>
            )}
        </GoogleMap>

        {/* MODAL */}
        {showModal && (
            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
            <div className="bg-white rounded-lg shadow-lg w-full max-w-md p-6">
                <h2 className="text-xl font-bold mb-4">
                {loteSeleccionado ? 'Editar Lote' : 'Nuevo Lote'}
                </h2>

                <div className="space-y-3">
                <input
                    className="w-full border px-3 py-2 rounded"
                    placeholder="Número de lote"
                    value={form.numeroLote}
                    onChange={(e) => setForm({ ...form, numeroLote: e.target.value })}
                />
                <input
                    className="w-full border px-3 py-2 rounded"
                    placeholder="Manzana"
                    value={form.manzana}
                    onChange={(e) => setForm({ ...form, manzana: e.target.value })}
                />
                <input
                    type="number"
                    className="w-full border px-3 py-2 rounded"
                    placeholder="Precio"
                    value={form.precio}
                    onChange={(e) => setForm({ ...form, precio: e.target.value })}
                />
                <textarea
                    className="w-full border px-3 py-2 rounded"
                    placeholder="Observaciones"
                    value={form.observaciones}
                    onChange={(e) => setForm({ ...form, observaciones: e.target.value })}
                />
                </div>

                <div className="mt-4 flex justify-end gap-2">
                <button
                    className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400"
                    onClick={() => {
                    setShowModal(false);
                    setNuevaGeometria(null);
                    setLoteSeleccionado(null);
                    }}
                >
                    Cancelar
                </button>

                <button
                    className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                    onClick={guardarCambios}
                >
                    Guardar
                </button>
                </div>
            </div>
            </div>
        )}
        </>
    );
}
