import React, { useState, useMemo, useRef } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Line } from '@react-three/drei';
import * as topojson from 'topojson-client';
import { useTheme } from '../store/ThemeContext';

const latLonToXYZ = (lat, lon, radius) => {
  const phi = (90 - lat) * (Math.PI / 180);
  const theta = (lon + 180) * (Math.PI / 180);
  return [
    -radius * Math.sin(phi) * Math.cos(theta),
    radius * Math.cos(phi),
    radius * Math.sin(phi) * Math.sin(theta),
  ];
};

const MapScene = ({ isUnrolled, isDarkMode }) => {
  const globeRef = useRef();
  const [borders, setBorders] = useState([]);
  const radius = 2;

  useMemo(() => {
    fetch('https://unpkg.com/world-atlas/countries-110m.json')
      .then(res => res.json())
      .then(topology => {
        const countries = topojson.feature(topology, topology.objects.countries).features;
        const allBorders = countries.flatMap(feature => {
          if (feature.geometry.type === "Polygon") {
            return [feature.geometry.coordinates[0]];
          } else if (feature.geometry.type === "MultiPolygon") {
            return feature.geometry.coordinates.map(polygon => polygon[0]);
          }
          return [];
        });
        setBorders(allBorders);
      });
  }, []);

  useFrame((state, delta) => {
    if (globeRef.current && !isUnrolled) {
      globeRef.current.rotation.y += delta * 0.1;
    }
  });

  const wireframeColor = isDarkMode ? "#447a9c" : "#1d3658";
  const borderColor = isDarkMode ? "#a7dadc" : "#447a9c";

  return (
    <group ref={globeRef}>
      <mesh visible={!isUnrolled}>
        <sphereGeometry args={[radius, 64, 64]} />
        <meshBasicMaterial color={wireframeColor} wireframe opacity={0.1} transparent />
      </mesh>

      {borders.map((border, i) => {
        const points = border.map(coord => {
          const [lon, lat] = coord;
          if (isUnrolled) {
            return [lon / 90, lat / 45, 0];
          }
          return latLonToXYZ(lat, lon, radius + 0.01);
        });

        return (
          <Line
            key={i}
            points={points}
            color={borderColor}
            lineWidth={0.5}
            opacity={0.8}
            transparent
          />
        );
      })}
    </group>
  );
};

export default function Globe() {
  const [isUnrolled, setIsUnrolled] = useState(false);
  const { isDarkMode } = useTheme();

  return (
    <div className={`relative w-full h-[600px] flex items-center justify-center rounded-3xl overflow-hidden transition-colors duration-500 ${isDarkMode ? 'bg-cello-dark/50' : 'bg-aqua-island/10'} shadow-inner border border-peppermint/10`}>
      <Canvas camera={{ position: [0, 0, 6], fov: 45 }}>
        <ambientLight intensity={isDarkMode ? 1.5 : 2.0} />
        <pointLight position={[10, 10, 10]} />

        <MapScene isUnrolled={isUnrolled} isDarkMode={isDarkMode} />

        <OrbitControls
          enablePan={false}
          minDistance={3}
          maxDistance={10}
        />
      </Canvas>

      <div className="absolute bottom-8 right-8 flex space-x-3 pointer-events-auto">
        <button
          onClick={() => setIsUnrolled(false)}
          className={`px-6 py-2 font-bold rounded-lg border transition-all active:scale-95 shadow-lg ${
            isDarkMode
              ? 'bg-cello text-peppermint border-wedgewood/30 hover:bg-cello-dark'
              : 'bg-white text-cello border-cello/10 hover:bg-peppermint'
          }`}
        >
          Reset
        </button>
        <button
          onClick={() => setIsUnrolled(!isUnrolled)}
          className={`px-6 py-2 ${isUnrolled ? (isDarkMode ? 'bg-wedgewood' : 'bg-cello') : 'bg-amaranth'} text-white font-bold rounded-lg hover:opacity-90 transition-all active:scale-95 shadow-lg`}
        >
          {isUnrolled ? 'Roll Globe' : 'Unroll Globe'}
        </button>
      </div>

      <div className="absolute top-8 left-8 text-left pointer-events-none">
        <div className={`flex items-center space-x-2 ${isDarkMode ? 'text-aqua-island' : 'text-cello'} opacity-60`}>
          <div className="w-2 h-2 rounded-full bg-amaranth animate-pulse" />
          <span className="text-[10px] font-mono uppercase tracking-[0.2em]">System Live: Global Monitoring</span>
        </div>
      </div>
    </div>
  );
}
