export function pathToWKT(path: Array<{ lat: number } | google.maps.LatLngLiteral | google.maps.LatLng>) {
    const coords = (path as any[]).map((p) => {
        const lat = typeof p.lat === 'function' ? p.lat() : p.lat;
        const lng = typeof p.lng === 'function' ? p.lng() : p.lng;
        return `${lng} ${lat}`;
    });

    if (coords.length === 0) return '';
    if (coords[0] !== coords[coords.length - 1]) coords.push(coords[0]);

    return `POLYGON((${coords.join(', ')}))`;
}
