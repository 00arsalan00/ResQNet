import React, { useEffect, useState } from 'react';
import { incidentService, teamService, resourceService } from '../../services/api';
import { AlertTriangle, Users, Package, Activity, MapPin, Search } from 'lucide-react';

export default function Dashboard() {
  const [stats, setStats] = useState({
    incidents: [],
    teams: [],
    resources: [],
    loading: true
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [incRes, teamRes, resRes] = await Promise.all([
          incidentService.getAll(0, 5),
          teamService.getAll(),
          resourceService.getAll()
        ]);
        setStats({
          incidents: incRes.data.content || [],
          teams: teamRes.data.content || [],
          resources: resRes.data.content || [],
          loading: false
        });
      } catch (err) {
        setStats(prev => ({ ...prev, loading: false }));
      }
    };
    fetchData();
  }, []);

  if (stats.loading) {
    return (
      <div className="flex flex-col items-center justify-center min-h-[60vh] space-y-4">
        <div className="relative">
          <div className="animate-spin rounded-full h-16 w-16 border-t-4 border-b-4 border-amaranth"></div>
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 h-8 w-8 bg-aqua-island rounded-full animate-pulse" />
        </div>
        <p className="text-wedgewood font-black animate-pulse uppercase tracking-[0.4em] text-xs">Accessing Command Layer...</p>
      </div>
    );
  }

  return (
    <div className="w-full max-w-7xl mx-auto space-y-10 pb-20 animate-in fade-in duration-1000">

      {/* Top Header */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-wedgewood/10 pb-8">
        <div>
          <h2 className="text-4xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">Command <span className="text-amaranth">Console</span></h2>
          <p className="text-wedgewood font-bold text-xs uppercase tracking-widest mt-1">Status: System Wide Synchronized</p>
        </div>
        <div className="flex items-center bg-aqua-island/10 border border-wedgewood/20 rounded-2xl px-4 py-2 text-cello dark:text-peppermint">
          <Search className="w-4 h-4 mr-3 opacity-50" />
          <input type="text" placeholder="Search Incidents..." className="bg-transparent border-none outline-none text-xs font-bold uppercase tracking-widest placeholder:text-wedgewood/50 w-48" />
        </div>
      </div>

      {/* KPI Grid */}
      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <StatCard icon={<Activity />} label="Threat Alerts" value={stats.incidents.length} color="bg-amaranth" />
        <StatCard icon={<Users />} label="Deployed Units" value={stats.teams.length} color="bg-wedgewood" />
        <StatCard icon={<Package />} label="Resource Units" value={stats.resources.length} color="bg-aqua-island" />
        <StatCard icon={<MapPin />} label="Active Nodes" value="24" color="bg-cello" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-10">
        {/* Alerts Section */}
        <section className="bg-peppermint/50 dark:bg-cello rounded-[2.5rem] border border-wedgewood/10 p-10 shadow-2xl relative overflow-hidden group">
          <div className="absolute top-0 right-0 p-8 opacity-5 group-hover:rotate-12 transition-transform duration-700">
            <AlertTriangle className="w-32 h-32" />
          </div>
          <div className="flex justify-between items-center mb-10 relative z-10">
            <h3 className="text-xl font-black text-cello dark:text-peppermint flex items-center gap-3">
              <div className="w-2 h-8 bg-amaranth rounded-full" />
              LATEST ALERTS
            </h3>
          </div>
          <div className="space-y-4 relative z-10">
            {stats.incidents.map(inc => (
              <div key={inc.id} className="p-5 rounded-3xl bg-white dark:bg-cello-dark border border-wedgewood/5 hover:border-amaranth/40 hover:translate-x-1 transition-all flex justify-between items-center">
                <div>
                  <h4 className="font-black text-cello dark:text-peppermint text-sm uppercase tracking-wide">{inc.type}</h4>
                  <p className="text-[10px] text-wedgewood font-bold uppercase mt-1">Node: {inc.district || 'GLOBAL'}</p>
                </div>
                <div className="flex items-center space-x-3">
                  <span className={`h-1.5 w-8 rounded-full ${inc.severity > 3 ? 'bg-amaranth' : 'bg-aqua-island'}`} />
                  <span className="text-[10px] font-black text-wedgewood uppercase">LVL {inc.severity}</span>
                </div>
              </div>
            ))}
          </div>
        </section>

        {/* Resources Section */}
        <section className="bg-white dark:bg-cello-dark rounded-[2.5rem] border border-wedgewood/10 p-10 shadow-2xl group">
           <h3 className="text-xl font-black text-cello dark:text-peppermint flex items-center gap-3 mb-10">
            <div className="w-2 h-8 bg-aqua-island rounded-full" />
            FIELD INVENTORY
          </h3>
          <div className="grid grid-cols-2 gap-4">
             {stats.resources.map(res => (
              <div key={res.id} className="p-6 rounded-3xl bg-peppermint/30 dark:bg-cello border border-wedgewood/5 group-hover:border-aqua-island/30 transition-colors">
                <p className="text-[10px] font-black text-wedgewood uppercase tracking-widest mb-2">{res.type}</p>
                <div className="flex items-end justify-between">
                   <p className="text-2xl font-black text-cello dark:text-peppermint">{res.quantity}</p>
                   <span className="text-[10px] font-bold text-aqua-island">Units</span>
                </div>
              </div>
            ))}
            {stats.resources.length === 0 && <p className="col-span-2 text-center text-wedgewood italic py-10 opacity-40 uppercase tracking-widest text-[10px]">Depleted</p>}
          </div>
        </section>
      </div>
    </div>
  );
}

function StatCard({ icon, label, value, color }) {
  return (
    <div className="relative group cursor-default">
      <div className="bg-white dark:bg-cello p-8 rounded-[2rem] border border-wedgewood/10 shadow-xl transition-all group-hover:shadow-2xl group-hover:-translate-y-1 overflow-hidden">
        <div className={`absolute top-0 right-0 w-24 h-24 ${color} opacity-5 -translate-y-12 translate-x-12 rounded-full group-hover:scale-150 transition-transform duration-700`} />
        <div className="relative z-10 flex flex-col items-center">
          <div className={`p-4 rounded-2xl bg-peppermint dark:bg-cello-dark border border-wedgewood/10 mb-4 text-cello dark:text-peppermint group-hover:bg-amaranth group-hover:text-white transition-all`}>
            {icon}
          </div>
          <p className="text-[10px] font-black text-wedgewood uppercase tracking-[0.2em] mb-1">{label}</p>
          <p className="text-4xl font-black text-cello dark:text-peppermint tracking-tighter">{value}</p>
        </div>
      </div>
    </div>
  );
}
