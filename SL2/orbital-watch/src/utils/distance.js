function euclidean(p1, p2) {
    return Math.sqrt(
      (p1.x - p2.x) ** 2 +
      (p1.y - p2.y) ** 2 +
      (p1.z - p2.z) ** 2
    );
  }
  
  function degreesToRadians(deg) {
    return (deg * Math.PI) / 180;
  }
  
  function toECEF(lat, lon, alt) {
    const a = 6378.137;
    const e = 8.1819190842622e-2;
    const radLat = degreesToRadians(lat);
    const radLon = degreesToRadians(lon);
    const h = alt / 1000;
  
    const N = a / Math.sqrt(1 - e ** 2 * Math.sin(radLat) ** 2);
    const x = (N + h) * Math.cos(radLat) * Math.cos(radLon);
    const y = (N + h) * Math.cos(radLat) * Math.sin(radLon);
    const z = ((1 - e ** 2) * N + h) * Math.sin(radLat);
  
    return { x, y, z };
  }
  
  export async function detectCollisions(debrisPath, satellitesPath) {
    const debrisCSV = await fetch(debrisPath).then(res => res.text());
    const satCSV = await fetch(satellitesPath).then(res => res.text());
  
    const debrisLines = debrisCSV.trim().split('\n').slice(1);
    const satLines = satCSV.trim().split('\n').slice(1);
  
    const debrisECEF = debrisLines.map(line => {
      const [lat, lon, alt] = line.split(',').map(Number);
      return toECEF(lat, lon, alt);
    });
  
    const satECEF = satLines.map(line => {
      const [x, y, z] = line.split(',').map(Number);
      return { x, y, z };
    });
  
    let count = 0;
    let totalDistance = 0;
  
    for (const sat of satECEF) {
      for (const debris of debrisECEF) {
        const dist = euclidean(sat, debris);
        if (dist <= 1) {
          count++;
          totalDistance += dist * 1000;
          break;
        }
      }
    }
  
    return { count, totalDistance: Math.round(totalDistance) };
  }
  