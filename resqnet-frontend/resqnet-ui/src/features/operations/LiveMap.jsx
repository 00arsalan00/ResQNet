import React, { useState, useMemo } from 'react';
import {
  ComposableMap,
  Geographies,
  Geography,
  Marker
} from "react-simple-maps";
import { MapPin, Info } from 'lucide-react';
import { useTheme } from '../../store/ThemeContext';

const geoUrl = "https://unpkg.com/world-atlas/countries-110m.json";

export default function LiveMap() {
  const { isDarkMode } = useTheme();

  const operationalNodes = [
    { name: 'District HQ - Delhi', lat: 28.61, lng: 77.20, status: 'Active' },
    { name: 'Rescue Node - Mumbai', lat: 19.07, lng: 72.87, status: 'Active' },
    { name: 'Support Hub - London', lat: 51.50, lng: -0.12, status: 'Standby' },
    { name: 'Command Center - NY', lat: 40.71, lng: -74.00, status: 'Active' },
    { name: 'Relief Hub - Tokyo', lat: 35.67, lng: 139.65, status: 'Active' },
  ];

  return (
    <section className={`w-full py-24 relative overflow-hidden transition-colors duration-500 ${isDarkMode ? 'bg-cello-dark' : 'bg-peppermint'}`}>
      <div className="max-w-7xl mx-auto px-6 grid grid-cols-1 lg:grid-cols-3 gap-12 items-center">

        <div className="lg:col-span-1 space-y-6 z-10">
          <div className="flex items-center space-x-2 text-amaranth">
            <div className="w-8 h-[2px] bg-amaranth" />
            <span className="text-xs font-black uppercase tracking-[0.3em]">Operational Reach</span>
          </div>
          <h3 className={`text-4xl md:text-5xl font-black tracking-tight ${isDarkMode ? 'text-peppermint' : 'text-cello'}`}>
            WHERE WE <span className="text-amaranth">RESPOND</span>
          </h3>
          <p className={`text-lg leading-relaxed font-medium ${isDarkMode ? 'text-aqua-island' : 'text-wedgewood'}`}>
            ResQNet nodes are strategically deployed across critical districts. Our decentralized network ensures localized authority with global visibility.
          </p>
          <div className={`flex items-start space-x-4 p-4 rounded-2xl border ${isDarkMode ? 'bg-white/5 border-white/10' : 'bg-aqua-island/10 border-wedgewood/20'}`}>
            <Info className={`w-6 h-6 shrink-0 mt-1 ${isDarkMode ? 'text-wedgewood' : 'text-wedgewood'}`} />
            <p className={`text-sm ${isDarkMode ? 'text-wedgewood' : 'text-cello'}`}>
              Pins represent active District Command Centers. Hover over a location to view live synchronization status.
            </p>
          </div>
        </div>

        <div className={`lg:col-span-2 rounded-3xl border transition-colors duration-500 shadow-2xl relative overflow-hidden p-4 ${
          isDarkMode ? 'border-white/5 bg-cello/30' : 'border-wedgewood/10 bg-white shadow-cello/5'
        }`}>
          <ComposableMap
            projectionConfig={{ scale: 180 }}
            className="w-full h-full"
          >
            <Geographies geography={geoUrl}>
              {({ geographies }) =>
                geographies.map((geo) => (
                  <Geography
                    key={geo.rsmKey}
                    geography={geo}
                    fill={isDarkMode ? "#1d3658" : "#a7dadc"}
                    stroke={isDarkMode ? "#447a9c" : "#f2faef"}
                    strokeWidth={0.5}
                    style={{
                      default: { outline: "none" },
                      hover: { fill: "#e63746", outline: "none" },
                      pressed: { outline: "none" },
                    }}
                  />
                ))
              }
            </Geographies>
            {operationalNodes.map((node, i) => (
              <Marker key={i} coordinates={[node.lng, node.lat]}>
                <g className="group cursor-pointer">
                  <circle r={4} fill="#e63746" />
                  <circle r={8} fill="#e63746" opacity={0.3} className="animate-ping" />
                  <foreignObject x={10} y={-20} width={150} height={50} className="pointer-events-none opacity-0 group-hover:opacity-100 transition-opacity">
                    <div className={`p-2 rounded-lg border shadow-xl backdrop-blur-md ${
                      isDarkMode ? 'bg-cello-dark/90 text-peppermint border-aqua-island/20' : 'bg-white/90 text-cello border-wedgewood/20'
                    }`}>
                      <p className="text-[10px] font-black uppercase tracking-tighter truncate">{node.name}</p>
                      <p className={`text-[8px] font-bold ${isDarkMode ? 'text-aqua-island' : 'text-wedgewood'}`}>{node.status}</p>
                    </div>
                  </foreignObject>
                </g>
              </Marker>
            ))}
          </ComposableMap>

          <div className="absolute top-6 right-6 flex items-center space-x-2">
            <div className="w-2 h-2 rounded-full bg-amaranth animate-pulse" />
            <span className={`text-[10px] font-mono uppercase tracking-widest ${isDarkMode ? 'text-white/40' : 'text-cello/40'}`}>
              Grid System: Active
            </span>
          </div>
        </div>
      </div>
    </section>
  );
}
