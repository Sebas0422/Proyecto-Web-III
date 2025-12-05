import React from 'react';
import { useParams } from 'react-router-dom';
import { DashboardLayout } from '@shared/components';
import { useGetLotsQuery } from '@features/lots/services/lotsApi';
import MapCreateLot from '../components/MapCreateLot';
import { Spinner, Alert } from 'react-bootstrap';



export default function LotMapPage() {
    const { proyectoId } = useParams<{ proyectoId: string }>();

    const { data: lotes = [], isLoading, isError, refetch } = useGetLotsQuery(
        { proyectoId: proyectoId ?? '' },
        { skip: !proyectoId }
    );

    if (!proyectoId) {
        return (
        <DashboardLayout>
            <Alert variant="danger">No se proporcionó un ID de proyecto válido en la URL.</Alert>
        </DashboardLayout>
        );
    }

    return (
        <DashboardLayout>
        <div className="">
            <div style={{display:"flex", justifyContent:"space-between"}}>
                <h1 style={{fontSize:"30px", fontWeight:"bolder", marginBottom: "13px"}}>Lotes del proyecto</h1>
                <button
                    style={{
                        backgroundColor: "#fd810dff",
                        color: "white",
                        padding: "8px 16px",
                        borderRadius: "6px",
                        border: "none",
                        cursor: "pointer",
                        fontSize: "14px",
                        fontWeight: "600",
                        margin: "10px",
                        transition: "background-color 0.2s ease",
                    }}
                    onMouseOver={(e) => (e.currentTarget.style.backgroundColor = "#d77b0bff")}
                    onMouseOut={(e) => (e.currentTarget.style.backgroundColor = "#fda90dff")}
                    >
                    Zoom
                </button>
            </div>
            
            {isLoading && <Spinner animation="border" />}

            {isError && <Alert variant="danger">Error al cargar los lotes. Intenta recargar.</Alert>}

            {!isLoading && lotes.length === 0 && (
            <Alert variant="info">No hay lotes registrados para este proyecto.</Alert>
            )}
            
            <MapCreateLot proyectoId={proyectoId} />
        </div>
        </DashboardLayout>
    );
}
