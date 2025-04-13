import React, { useState } from 'react'
import { convertGPSFileToDecimal } from '../utils/conversion'

export default function CoordinateConverter() {
  const [result, setResult] = useState(null)

  const handleConvert = async () => {
    const res = await convertGPSFileToDecimal('/space_debris_positions.csv')
    setResult(res)
      }
  
  
  return (
    <div>
      <h2>1️⃣ Koordinatenumrechnung (GPS → Dezimal)</h2>
      <button onClick={handleConvert}>Umrechnung starten</button>
      {result !== null && (
        <p>🔢 Ergebnis: {result.toFixed(6)}</p>
      )}
    </div>
  )
}
