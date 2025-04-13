// components/ECEFConverter.jsx

import React, { useState } from 'react';
import { convertDebrisCSVToECEFSum } from '../utils/ecef';

export default function ECEFConverter() {
  const [result, setResult] = useState(null);

  const handleConvert = async () => {
    try {
      const res = await convertDebrisCSVToECEFSum('/space_debris_positions.csv');
      setResult(res);
    } catch (error) {
      console.error("Fehler beim Laden der CSV-Datei:", error);
      setResult(null);
    }
  };

  return (
    <div>
      <h2>2️⃣ ECEF-Konvertierung</h2>
      <button onClick={handleConvert}>ECEF berechnen</button>
      {typeof result === 'number' && (
        <p>🧮 Summe X+Y+Z: {result.toFixed(5)} km</p>
      )}
    </div>
  );
}
