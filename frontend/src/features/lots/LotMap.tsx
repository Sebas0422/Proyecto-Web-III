import 'bootstrap/dist/css/bootstrap.min.css';
import React, { useCallback, useRef, useState } from "react";
import { GoogleMap, useJsApiLoader } from "@react-google-maps/api";
import * as toGeoJSON from "togeojson";
import { Button, Spinner, Card, Alert, Nav, Navbar, Container, Row, Col } from "react-bootstrap";
import type { FeatureCollection } from "geojson";

const GOOGLE_API_KEY = import.meta.env.VITE_GOOGLE_MAPS_KEY as string;

// Estilos de mapa más limpios y con altura ajustada al viewport (vh)
const containerStyle = {
  width: "100%",
  height: "88vh", // Aumentado para ocupar más espacio de la vista
  borderRadius: "0.5rem", // Bordes redondeados para un look moderno
};
const defaultCenter = { lat: -17.7833, lng: -63.1821 }; // Santa Cruz, Bolivia

export default function LotMapDashboard() {
  const { isLoaded, loadError } = useJsApiLoader({
    googleMapsApiKey: GOOGLE_API_KEY,
    libraries: ["drawing", "places"],
  });

  const mapRef = useRef<google.maps.Map | null>(null);
  const dataRef = useRef<google.maps.Data | null>(null);

  const [fileName, setFileName] = useState<string | null>(null);
  const [loadingKml, setLoadingKml] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Estado para controlar la visibilidad de la barra lateral
  const [sidebarShow, setSidebarShow] = useState(true);

  const toggleSidebar = () => setSidebarShow(!sidebarShow);

  const onLoad = useCallback((map: google.maps.Map) => {
    mapRef.current = map;
    // Capa de datos con estilo corporativo
    const dataLayer = new google.maps.Data({ map });
    dataLayer.setStyle({
      fillColor: "#007BFF", // Azul primario de Bootstrap
      fillOpacity: 0.25,
      strokeColor: "#0056b3",
      strokeWeight: 2,
    });
    dataRef.current = dataLayer;
  }, []);

  const handleKmlUpload = async (e: React.ChangeEvent<HTMLInputElement>) => {
    setError(null);
    const file = e.target.files?.[0];
    if (!file) return;

    setFileName(file.name);
    setLoadingKml(true);

    try {
      const text = await file.text();
      const parser = new DOMParser();
      const xml = parser.parseFromString(text, "text/xml");

      // Limpia el input de archivo para permitir subir el mismo archivo
      e.target.value = '';

      try {
        // Se asume que toGeoJSON.kml maneja Document
        const geojson = toGeoJSON.kml(xml as unknown as Document) as FeatureCollection;
        addGeoJsonDirect(geojson);
      } catch {
        setError("El archivo KML no es válido o no contiene geometrías.");
      }
    } catch {
      setError("Error leyendo el archivo.");
    } finally {
      setLoadingKml(false);
    }
  };

  const addGeoJsonDirect = (geojson: FeatureCollection) => {
    if (!dataRef.current) return;

    // Limpia la capa de datos anterior
    dataRef.current.forEach((f) => dataRef.current!.remove(f));

    try {
      dataRef.current.addGeoJson(geojson);
    } catch (err) {
      console.error("Error al agregar GeoJSON:", err);
      setError("Error al procesar GeoJSON.");
      return;
    }

    try {
      const bounds = new google.maps.LatLngBounds();

      dataRef.current.forEach((feature) => {
        const geom = feature.getGeometry();
        if (!geom) return;

        const processGeometry = (g: google.maps.Data.Geometry) => {
          const type = g.getType();

          if (type === "Point") {
            bounds.extend((g as google.maps.Data.Point).get());
          }
          if (type === "Polygon") {
            (g as google.maps.Data.Polygon).getAt(0).getArray().forEach((coord) => bounds.extend(coord));
          }
          if (type === "MultiPolygon") {
            (g as google.maps.Data.MultiPolygon).getArray().forEach((poly) =>
              poly.getAt(0).getArray().forEach((coord) => bounds.extend(coord))
            );
          }
          if (type === "GeometryCollection") {
            (g as google.maps.Data.GeometryCollection).getArray().forEach(processGeometry);
          }
        };

        processGeometry(geom);
      });

      // Ajusta el mapa a las nuevas geometrías
      if (!bounds.isEmpty()) {
        mapRef.current?.fitBounds(bounds);
      } else {
        console.warn("Límites geográficos vacíos, no se ajustó el zoom.");
      }
    } catch (err) {
      console.warn("Error al calcular o ajustar el zoom:", err);
    }
  };

  if (loadError)
    return <Alert variant="danger" className="m-3">Error cargando Google Maps</Alert>;

  if (!isLoaded)
    return (
      <div className="d-flex justify-content-center align-items-center bg-light" style={{ height: "100vh" }}>
        <Spinner animation="border" role="status" variant="primary">
          <span className="visually-hidden">Cargando mapa...</span>
        </Spinner>
      </div>
    );

  return (
    <div className="bg-light min-vh-100">
      {/* HEADER - Navbar oscura y limpia */}
      <Navbar bg="dark" variant="dark" expand={false} className="shadow-lg">
        <Container fluid>
          <Button variant="outline-light" onClick={toggleSidebar} className="border-0 p-2 me-3">
            {/* Símbolo de menú estándar */}
            <span className="h5 mb-0">☰</span>
            <span className="visually-hidden">Alternar Menú</span>
          </Button>
          <Navbar.Brand className="fw-bolder fs-5 text-uppercase">
            Geo-Dashboard Proyectos
          </Navbar.Brand>
        </Container>
      </Navbar>

      <Container fluid className="p-0">
        <Row className="g-0"> {/* g-0 elimina el espaciado entre columnas */}
          {/* SIDEBAR - Panel lateral con estilo moderno */}
          {sidebarShow && (
            <Col
              xs={12}
              md={3}
              lg={2}
              className="vh-100 p-3 sticky-top"
              style={{ backgroundColor: "#343a40" }} // mismo color que el navbar
            >
              <h6
                className="text-uppercase fw-bold mb-3"
                style={{ color: "#ffffff", fontFamily: "Segoe UI, Roboto, sans-serif", fontSize: "0.85rem" }}
              >
                Control de Capas
              </h6>

              <Nav className="flex-column">
                <span
                  className="fw-semibold mt-2 mb-1 pt-2 border-top"
                  style={{ color: "#adb5bd", fontSize: "0.75rem" }}
                >
                  Gestión de Proyectos
                </span>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#495057",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Crear Proyecto */ }}
                >
                  Crear Proyecto
                </Button>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#495057",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Listar Proyectos */ }}
                >
                  Listar Proyectos
                </Button>

                <Button
                  className="mb-3 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#495057",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Gestionar Sectores */ }}
                >
                  Gestionar Sectores
                </Button>

                <span
                  className="fw-semibold mt-3 mb-1 pt-2 border-top"
                  style={{ color: "#adb5bd", fontSize: "0.75rem" }}
                >
                  Herramientas de Lotes
                </span>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#28a745",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Crear Lote */ }}
                >
                  Crear Lote
                </Button>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#28a745",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Listar Lotes */ }}
                >
                  Listar Lotes
                </Button>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#28a745",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Calcular Áreas */ }}
                >
                  Calcular Áreas
                </Button>

                <Button
                  className="mb-2 w-100 text-start py-1"
                  style={{
                    backgroundColor: "#28a745",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => { /* Calcular Centroide */ }}
                >
                  Calcular Centroide
                </Button>

                <Button
                  className="mb-2 w-100 text-start py-1 mt-3"
                  style={{
                    backgroundColor: "#007bff",
                    color: "#ffffff",
                    border: "none",
                    fontSize: "0.8rem",
                    fontFamily: "Segoe UI, Roboto, sans-serif"
                  }}
                  onClick={() => document.getElementById("kmlUpload")?.click()}
                >
                  Importar KML
                </Button>
              </Nav>
            </Col>
          )}


          {/* CONTENIDO PRINCIPAL - Columna del mapa */}
          <Col xs={12} md={sidebarShow ? 9 : 12} lg={sidebarShow ? 10 : 12} className="p-3">
            {/* KML Info y Errores - Barra de estado elegante */}
            <Card className="mb-3 shadow-sm border-0">
              <Card.Body className="p-3 bg-white d-flex align-items-center justify-content-between">
                <span className="fw-semibold text-muted">
                  <span className="text-primary me-2">◉</span> Estado de Capa: {fileName ? fileName : 'Ningún archivo KML cargado'}
                </span>
                {loadingKml && (
                  <div className="d-flex align-items-center text-primary">
                    <Spinner animation="border" size="sm" className="me-2" variant="primary" />
                    Procesando archivo...
                  </div>
                )}
              </Card.Body>
              {error && (
                <Card.Footer className="bg-danger text-white py-2">
                  <span className="fw-bold">Error:</span> {error}
                </Card.Footer>
              )}
            </Card>

            {/* MAPA */}
            <Card className="shadow-lg border-0">
              <GoogleMap
                mapContainerStyle={containerStyle}
                center={defaultCenter}
                zoom={14}
                onLoad={onLoad}
              />
            </Card>
          </Col>
        </Row>
      </Container>

      {/* INPUT OCULTO DEL KML */}
      <input
        type="file"
        accept=".kml"
        onChange={handleKmlUpload}
        id="kmlUpload"
        style={{ display: "none" }}
      />
    </div>
  );
}