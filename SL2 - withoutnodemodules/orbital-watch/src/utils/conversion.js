// In conversion.js und ecef.js
import { dmsToDecimal } from './dms.js';
  
  export async function convertGPSFileToDecimal(csvPath) {
    const res = await fetch(csvPath);
    const text = await res.text();
    const lines = text.trim().split('\n').slice(1);
    let latSum = 0, lonSum = 0;
  
    lines.forEach(line => {
      const parts = line.split(',');
      const lat = parts[1]?.trim();
      const lon = parts[2]?.trim();
    
      const decLat = dmsToDecimal(lat);
      const decLon = dmsToDecimal(lon);
    
      if (isNaN(decLat) || isNaN(decLon)) {
        console.warn(`Fehlerhafte Zeile: ${line}`);
      } else {
        latSum += decLat;
        lonSum += decLon;
      }
    });
    
  
    return latSum * lonSum;
  }
  