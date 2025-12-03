import React, { useState } from 'react';
import { Button, Form, Alert } from 'react-bootstrap';
import * as toGeoJSON from '@tmcw/togeojson';
import { DOMParser } from 'xmldom';
import { createLote } from '@features/lots/services/lotes';

interface Props {
    proyectoId: string;
}

const KmlUploader: React.FC<Props> = ({ proyectoId }) => {
    const [file, setFile] = useState<File | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            setFile(e.target.files[0]);
            setError(null);
            setSuccess(false);
        }
    };

    const handleUpload = async () => {
        if (!file) return setError('Selecciona un archivo KML');

        setLoading(true);
        setError(null);
        setSuccess(false);

        try {
            const text = await file.text();
            const parser = new DOMParser();
            const kmlDoc = parser.parseFromString(text, 'text/xml');

            const geojson = toGeoJSON.kml(kmlDoc) as any;

            // geojson.features es un array con cada <Placemark>
            for (const feature of geojson.features) {
                const lote = {
                    numeroLote: feature.properties.name || 'Sin nombre',
                    manzana: feature.properties.manzana || 'N/A',
                    geometriaWKT: geojsonFeatureToWKT(feature),
                    centroideWKT: geojsonCentroidToWKT(feature),
                    precio: feature.properties.precio || 0,
                    estado: feature.properties.estado || 'DISPONIBLE',
                    observaciones: feature.properties.observaciones || '',
                    proyectoId
                };

                await createLote(lote);
            }

            setSuccess(true);
        } catch (err: any) {
            console.error(err);
            setError('Error procesando el archivo KML: ' + err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="mb-4">
            <Form.Group>
                <Form.Label>Subir archivo KML</Form.Label>
                <Form.Control type="file" accept=".kml" onChange={handleFileChange} />
            </Form.Group>
            <Button className="mt-2" onClick={handleUpload} disabled={loading}>
                {loading ? 'Cargando...' : 'Importar Lotes'}
            </Button>
            {error && <Alert className="mt-2" variant="danger">{error}</Alert>}
            {success && <Alert className="mt-2" variant="success">Lotes importados correctamente!</Alert>}
        </div>
    );
};

export default KmlUploader;

function geojsonFeatureToWKT(feature: any) {
    if (!feature.geometry) return '';
    const coords = feature.geometry.coordinates;

    if (feature.geometry.type === 'Polygon') {
        const rings = coords.map((ring: any) =>
            ring.map((c: number[]) => `${c[0]} ${c[1]}`).join(', ')
        );
        return `POLYGON ((${rings.join('), (')}))`;
    }
    return '';
}

function geojsonCentroidToWKT(feature: any) {
    if (!feature.geometry) return '';
    const coords = feature.geometry.coordinates;

    if (feature.geometry.type === 'Polygon') {
        const ring = coords[0];
        let sumX = 0, sumY = 0;
        ring.forEach((c: number[]) => {
            sumX += c[0];
            sumY += c[1];
        });
        const n = ring.length;
        return `POINT (${sumX / n} ${sumY / n})`;
    }
    return '';
}
