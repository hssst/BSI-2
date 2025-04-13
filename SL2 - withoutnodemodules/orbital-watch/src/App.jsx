import React from 'react'
import CoordinateConverter from './components/CoordinateConverter'
import ECEFConverter from './components/ECEFConverter'
import CollisionAlert from './components/CollisionAlert'

export default function App() {
  return (
    <div className="container">
      <h1>🛰️ Orbital Watch – Space Debris Alert</h1>
      <CoordinateConverter />
      <ECEFConverter />
      <CollisionAlert />
    </div>
  )
}
