import React, { useEffect, useState } from 'react';
import { dmsToDecimal } from '../utils/dms.js'; // Stellen Sie sicher, dass diese Funktion korrekt implementiert ist

function degreesToRadians(degrees) {
  return (degrees * Math.PI) / 180;
}

function parseSatelliteCSV(text) {
  const lines = text.trim().split('\n').slice(1); // Kopfzeile überspringen
  return lines.map(line => {
    const [id, xStr, yStr, zStr] = line.split(',').map(s => s.trim());
    return {
      id,
      x: parseFloat(xStr) / 1000, // Meter zu Kilometer
      y: parseFloat(yStr) / 1000,
      z: parseFloat(zStr) / 1000,
    };
  });
}

function parseDebrisCSV(text) {
  const lines = text.trim().split('\n').slice(1); // Kopfzeile überspringen
  return lines.map(line => {
    const [id, latDMS, lonDMS, altStr] = line.split(',').map(s => s.trim());
    const latDecimal = dmsToDecimal(latDMS);
    const lonDecimal = dmsToDecimal(lonDMS);
    const latRad = degreesToRadians(latDecimal);
    const lonRad = degreesToRadians(lonDecimal);
    const alt = parseFloat(altStr); // Bereits in Kilometern

    const a = 6378.137; // Äquatorradius in km
    const e = 8.1819190842622e-2;

    const N = a / Math.sqrt(1 - e * e * Math.sin(latRad) ** 2);
    const x = (N + alt) * Math.cos(latRad) * Math.cos(lonRad);
    const y = (N + alt) * Math.cos(latRad) * Math.sin(lonRad);
    const z = ((1 - e * e) * N + alt) * Math.sin(latRad);

    return { id, x, y, z };
  });
}

function calculateDistance(a, b) {
  const dx = a.x - b.x;
  const dy = a.y - b.y;
  const dz = a.z - b.z;
  return Math.sqrt(dx * dx + dy * dy + dz * dz);
}

const CollisionAlert = () => {
  const [dangerousPairs, setDangerousPairs] = useState([]);
  const [totalDistance, setTotalDistance] = useState(0);
  const [message, setMessage] = useState('');

  useEffect(() => {
    const detectCollisions = async () => {
      try {
        const [debrisRes, satelliteRes] = await Promise.all([
          fetch('/space_debris_positions.csv'),
          fetch('/satellite_positions.csv'),
        ]);

        const [debrisText, satelliteText] = await Promise.all([
          debrisRes.text(),
          satelliteRes.text(),
        ]);

        const debrisList = parseDebrisCSV(debrisText);
        const satelliteList = parseSatelliteCSV(satelliteText);

        const pairs = [];

        for (const satellite of satelliteList) {
          for (const debris of debrisList) {
            const distance = calculateDistance(satellite, debris);
            if (distance < 1) { // Weniger als 1 km
              pairs.push({
                satelliteId: satellite.id,
                debrisId: debris.id,
                distance: distance * 1000, // Kilometer zu Meter
              });
            }
          }
        }

        // Gruppieren nach Satelliten-ID
        const satellitesWithCloseDebris = {};
        for (const pair of pairs) {
          if (!satellitesWithCloseDebris[pair.satelliteId]) {
            satellitesWithCloseDebris[pair.satelliteId] = [];
          }
          satellitesWithCloseDebris[pair.satelliteId].push(pair);
        }

        // Überprüfen Sie, ob genau drei Satelliten jeweils ein gefährlich nahes Trümmerteil haben
        const satellites = Object.keys(satellitesWithCloseDebris);
        if (satellites.length === 3 && satellites.every(id => satellitesWithCloseDebris[id].length === 1)) {
          const totalDist = pairs.reduce((sum, pair) => sum + pair.distance, 0);
          if (Math.abs(totalDist - 1500) < 1e-6) {
            setDangerousPairs(pairs);
            setTotalDistance(totalDist);
            setMessage('Mission erfolgreich abgeschlossen!');
            return;
          }
        }

        setDangerousPairs(pairs);
        setTotalDistance(pairs.reduce((sum, pair) => sum + pair.distance, 0));
        setMessage('Kriterien nicht erfüllt.');
      } catch (error) {
        console.error('Fehler beim Laden oder Verarbeiten der Daten:', error);
        setMessage('Fehler beim Laden oder Verarbeiten der Daten.');
      }
    };

    detectCollisions();
  }, []);

  return (
    <div>
      <h2>Kollisionswarnung</h2>
      <p>{message}</p>
      <p>Gesamtdistanz: {totalDistance.toFixed(2)} Meter</p>
      <ul>
        {dangerousPairs.map((pair, index) => (
          <li key={index}>
            Satellit {pair.satelliteId} – Trümmer {pair.debrisId}: {pair.distance.toFixed(2)} m
          </li>
        ))}
      </ul>
    </div>
  );
};

export default CollisionAlert;
