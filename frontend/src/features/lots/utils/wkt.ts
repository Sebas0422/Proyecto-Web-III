// utils/wkt.ts

export function pathToWKT(path: google.maps.LatLngLiteral[]): string {
    if (!path || path.length === 0) return '';

    const coords = path.map((p) => `${p.lng} ${p.lat}`).join(', ');
    const first = path[0];
    const closedCoords = `${coords}, ${first.lng} ${first.lat}`;

    return `POLYGON((${closedCoords}))`;
}

export function wktToPath(wkt: string): google.maps.LatLngLiteral[] {
    if (!wkt) return [];

    const cleaned = wkt.replace(/^POLYGON\s*\(\(/i, '').replace(/\)\)$/, '');
    return cleaned.split(',').map((c) => {
        const [lng, lat] = c.trim().split(/\s+/).map(Number);
        return { lat, lng };
    });
}
