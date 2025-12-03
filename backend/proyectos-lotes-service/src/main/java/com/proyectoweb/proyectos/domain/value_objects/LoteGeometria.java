package com.proyectoweb.proyectos.domain.value_objects;

import com.proyectoweb.proyectos.shared_kernel.ValueObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public record LoteGeometria(Polygon polygon) {
    private static final GeometryFactory geometryFactory = new GeometryFactory();

    public LoteGeometria {
        if (polygon == null) {
            throw new IllegalArgumentException("La geometría del lote no puede ser nula");
        }
        if (!polygon.isValid()) {
            throw new IllegalArgumentException("La geometría del lote no es válida");
        }
    }

    public double calcularArea() {
        return polygon.getArea();
    }

    public Point calcularCentroide() {
        return polygon.getCentroid();
    }

    public String toWKT() {
        return polygon.toText();
    }

    public static LoteGeometria fromWKT(String wkt) {
        try {
            org.locationtech.jts.io.WKTReader reader = new org.locationtech.jts.io.WKTReader(geometryFactory);
            Polygon polygon = (Polygon) reader.read(wkt);
            return new LoteGeometria(polygon);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al parsear WKT: " + e.getMessage());
        }
    }

    public static LoteGeometria fromCoordinates(Coordinate[] coordinates) {
        if (coordinates.length < 4) {
            throw new IllegalArgumentException("Se requieren al menos 4 coordenadas para formar un polígono");
        }
        
        if (!coordinates[0].equals(coordinates[coordinates.length - 1])) {
            Coordinate[] closedCoordinates = new Coordinate[coordinates.length + 1];
            System.arraycopy(coordinates, 0, closedCoordinates, 0, coordinates.length);
            closedCoordinates[coordinates.length] = coordinates[0];
            coordinates = closedCoordinates;
        }

        Polygon polygon = geometryFactory.createPolygon(coordinates);
        return new LoteGeometria(polygon);
    }
}
