package com.proyectoweb.proyectos.domain.services;

import com.proyectoweb.proyectos.domain.value_objects.LoteGeometria;
import de.micromata.opengis.kml.v_2_2_0.*;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class KmlParserService {

    public List<LoteData> parseKml(InputStream kmlInputStream) {
        List<LoteData> lotes = new ArrayList<>();
        
        try {
            Kml kml = Kml.unmarshal(kmlInputStream);
            Feature feature = kml.getFeature();
            
            if (feature instanceof Document) {
                processDocument((Document) feature, lotes);
            } else if (feature instanceof Folder) {
                processFolder((Folder) feature, lotes);
            } else if (feature instanceof Placemark) {
                processPlacemark((Placemark) feature, lotes);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear archivo KML: " + e.getMessage(), e);
        }
        
        return lotes;
    }

    private void processDocument(Document document, List<LoteData> lotes) {
        for (Feature feature : document.getFeature()) {
            if (feature instanceof Folder) {
                processFolder((Folder) feature, lotes);
            } else if (feature instanceof Placemark) {
                processPlacemark((Placemark) feature, lotes);
            }
        }
    }

    private void processFolder(Folder folder, List<LoteData> lotes) {
        for (Feature feature : folder.getFeature()) {
            if (feature instanceof Placemark) {
                processPlacemark((Placemark) feature, lotes);
            }
        }
    }

    private void processPlacemark(Placemark placemark, List<LoteData> lotes) {
        String name = placemark.getName();
        Geometry geometry = placemark.getGeometry();
        
        if (geometry instanceof Polygon) {
            LoteGeometria loteGeometria = processPolygon((Polygon) geometry);
            lotes.add(new LoteData(name, loteGeometria, extractDescription(placemark)));
        } else if (geometry instanceof MultiGeometry) {
            processMultiGeometry((MultiGeometry) geometry, name, lotes);
        }
    }

    private void processMultiGeometry(MultiGeometry multiGeometry, String baseName, List<LoteData> lotes) {
        int index = 1;
        for (Geometry geometry : multiGeometry.getGeometry()) {
            if (geometry instanceof Polygon) {
                LoteGeometria loteGeometria = processPolygon((Polygon) geometry);
                String name = baseName != null ? baseName + "_" + index : "Lote_" + index;
                lotes.add(new LoteData(name, loteGeometria, null));
                index++;
            }
        }
    }

    private LoteGeometria processPolygon(Polygon polygon) {
        Boundary outerBoundary = polygon.getOuterBoundaryIs();
        if (outerBoundary == null || outerBoundary.getLinearRing() == null) {
            throw new IllegalArgumentException("El polígono no tiene un límite exterior válido");
        }

        LinearRing linearRing = outerBoundary.getLinearRing();
        List<de.micromata.opengis.kml.v_2_2_0.Coordinate> kmlCoordinates = linearRing.getCoordinates();
        
        if (kmlCoordinates == null || kmlCoordinates.isEmpty()) {
            throw new IllegalArgumentException("El polígono no tiene coordenadas");
        }

        Coordinate[] jtsCoordinates = new Coordinate[kmlCoordinates.size()];
        for (int i = 0; i < kmlCoordinates.size(); i++) {
            de.micromata.opengis.kml.v_2_2_0.Coordinate kmlCoord = kmlCoordinates.get(i);
            // KML usa (longitud, latitud, altitud)
            jtsCoordinates[i] = new Coordinate(kmlCoord.getLongitude(), kmlCoord.getLatitude());
        }

        return LoteGeometria.fromCoordinates(jtsCoordinates);
    }

    private String extractDescription(Placemark placemark) {
        return placemark.getDescription();
    }

    public record LoteData(
            String nombre,
            LoteGeometria geometria,
            String descripcion
    ) {}
}
