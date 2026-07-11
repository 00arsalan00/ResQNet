mport React, { useEffect, useState } from 'react'; import { incidentService } from '../../services/api'; import { MapPin, Navigation, ArrowRight } from 'lucide-react';
export default function ResponderDashboard() { const [incidents, setIncidents] = useState([]); const [loading, setLoading] = useState(true);
useEffect(() => { navigator.geolocation.getCurrentPosition(async (pos) => { try { const { latitude, longitude } = pos.coords; const response = await incidentService.getNearest(latitude, longitude); setIncidents(response.data); } finally { setLoading(false); } }); }, []);
return ( <div className="max-w-5xl mx-auto space-y-8 animate-in fade-in duration-700 pb-20"> <div className="bg-cello p-12 rounded-[3rem] text-peppermint relative overflow-hidden shadow-2xl"> <Navigation className="absolute top-0 right-0 w-64 h-64 opacity-5 -translate-y-20 translate-x-20" /> <h2 className="text-5xl font-black uppercase tracking-tighter">Responder <span className="text-amaranth">Node</span></h2> <p className="text-aqua-island/60 font-bold uppercase tracking-[0.3em] text-xs mt-2">Sorting by Global Proximity</p> </div>
  <div className="grid grid-cols-1 gap-6">
    {incidents.map((incident) => (
      <div key={incident.id} className="bg-white dark:bg-cello rounded-[2.5rem] p-8 border border-wedgewood/10 shadow-xl flex flex-col md:flex-row justify-between items-center group hover:border-amaranth/30 transition-all">
        <div className="space-y-3 text-center md:text-left">
          <span className="px-3 py-1 rounded-full bg-peppermint dark:bg-cello-dark text-[10px] font-black uppercase tracking-widest text-wedgewood">Priority Alert</span>
          <h3 className="text-3xl font-black text-cello dark:text-peppermint uppercase">{incident.type}</h3>
          <div className="flex items-center justify-center md:justify-start space-x-6 text-wedgewood">
            <div className="flex items-center font-black text-[10px] uppercase">
                <MapPin className="w-4 h-4 mr-2 text-amaranth" /> 1.2 KM AWAY
                            </div>
                            <span className="text-[10px] font-black uppercase opacity-40">{incident.city}</span>
                          </div>
                        </div>
                        <button className="mt-6 md:mt-0 px-10 py-4 bg-amaranth text-white font-black rounded-2xl hover:bg-cello transition-all text-sm uppercase tracking-widest active:scale-95 shadow-lg shadow-amaranth/20">
                          Respond Now
                        </button>
                      </div>
                    ))}
                  </div>
                </div>
                ); }