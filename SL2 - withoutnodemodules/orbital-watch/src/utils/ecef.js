// utils/ecef.js

import { dmsToDecimal } from './dms.js';

function degreesToRadians(deg) {
  return (deg * Math.PI) / 180;
}

export async function convertDebrisCSVToECEFSum(csvPath) {
  const res = await fetch(csvPath);
  const text = await res.text();
  const lines = text.trim().split('\n').slice(1); // Überspringe die Kopfzeile

  const a = 6378.137; // Äquatorradius in km
  const e = 8.1819190842622e-2;

  let sumX = 0, sumY = 0, sumZ = 0;

  for (const line of lines) {
    const parts = line.split(',');
    if (parts.length < 4) {
      console.warn("⚠️ Ungültige Zeile (zu kurz):", line);
      continue;
    }

    const latDMS = parts[1].trim();
    const lonDMS = parts[2].trim();
    const altStr = parts[3].trim();

    const latDecimal = dmsToDecimal(latDMS);
    const lonDecimal = dmsToDecimal(lonDMS);
    const latRad = degreesToRadians(latDecimal);
    const lonRad = degreesToRadians(lonDecimal);
    const alt = parseFloat(altStr); // Höhe in km

    if ([latRad, lonRad, alt].some(v => isNaN(v))) {
      console.warn(`⚠️ Fehlerhafte Werte in Zeile: ${line}`);
      continue;
    }

    const N = a / Math.sqrt(1 - e * e * Math.sin(latRad) ** 2);
    const X = (N + alt) * Math.cos(latRad) * Math.cos(lonRad);
    const Y = (N + alt) * Math.cos(latRad) * Math.sin(lonRad);
    const Z = ((1 - e * e) * N + alt) * Math.sin(latRad);

    sumX += X;
    sumY += Y;
    sumZ += Z;
  }

  return parseFloat((sumX + sumY + sumZ).toFixed(5)); // Gibt eine Zahl zurück
}
