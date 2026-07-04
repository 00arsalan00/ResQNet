import React, { useEffect, useState } from 'react';
import { incidentService } from '../../services/api';
import { ShieldAlert, Clock, CheckCircle, Truck, MapPin, ChevronRight } from 'lucide-react';

const STATUS_STEPS = [
  { id: 'REPORTED', label: 'Alert Received', icon: ShieldAlert },
  { id: 'DISPATCHED', label: 'Unit En Route', icon: Truck },
  { id: 'ON_SITE', label: 'Responders Arrived', icon: MapPin },
  { id: 'RESOLVED', label: 'Incident Resolved', icon: CheckCircle }
];

export default function MyReports() {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMyReports = async () => {
      try {
        const response = await incidentService.getAll(0, 100); // Temporary: reuse existing until service update
        setReports(response.data.content || []);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchMyReports();
  }, []);

  if (loading) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center">
        <div className="w-12 h-12 border-t-4 border-amaranth rounded-full animate-spin" />
        <p className="mt-4 text-[10px] font-black text-wedgewood uppercase tracking-[0.4em]">Establishing Secure Link...</p>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto space-y-12 animate-in fade-in slide-in-from-bottom-4 duration-700">
      <div className="text-center space-y-2">
        <h2 className="text-4xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">My <span className="text-amaranth">Activity</span></h2>
        <p className="text-xs text-wedgewood font-bold uppercase tracking-widest opacity-60">Live Operational Tracking</p>
      </div>

      <div className="grid grid-cols-1 gap-6">
        {reports.map((report) => (
          <div key={report.id} className="bg-white dark:bg-cello rounded-[2.5rem] border border-wedgewood/10 shadow-2xl overflow-hidden">
            <div className="p-8 md:p-10 flex flex-col md:flex-row gap-8">

              <div className="md:w-1/3 space-y-4">
                <div className="inline-flex items-center px-3 py-1 rounded-full bg-amaranth/10 text-amaranth text-[10px] font-black uppercase tracking-widest">
                  ID: {report.id.substring(0, 8)}
                </div>
                <h3 className="text-2xl font-black text-cello dark:text-peppermint uppercase">{report.type}</h3>
                <div className="flex items-center text-wedgewood space-x-2">
                   <Clock className="w-4 h-4" />
                   <span className="text-xs font-bold uppercase tracking-wider">Reported 2h ago</span>
                </div>
              </div>

              <div className="flex-1 flex items-center">
                <div className="w-full flex justify-between relative">
                  <div className="absolute top-1/2 left-0 w-full h-0.5 bg-wedgewood/10 -translate-y-1/2 z-0" />
                  {STATUS_STEPS.map((step, idx) => {
                    const isCompleted = true; // Temporary logic
                    return (
                      <div key={step.id} className="relative z-10 flex flex-col items-center">
                        <div className={`w-10 h-10 rounded-full flex items-center justify-center transition-all duration-500 ${
                          isCompleted ? 'bg-amaranth text-white scale-110 shadow-lg shadow-amaranth/20' : 'bg-peppermint dark:bg-cello-dark text-wedgewood'
                        }`}>
                          <step.icon className="w-5 h-5" />
                        </div>
                        <p className={`mt-3 text-[8px] font-black uppercase tracking-widest text-center w-20 ${isCompleted ? 'text-cello dark:text-peppermint' : 'text-wedgewood opacity-40'}`}>
                          {step.label}
                        </p>
                      </div>
                    )
                  })}
                </div>
              </div>

              <button className="self-center p-4 bg-aqua-island/10 rounded-2xl text-aqua-island hover:bg-amaranth hover:text-white transition-all group">
                <ChevronRight className="w-6 h-6 group-hover:translate-x-1 transition-transform" />
              </button>
            </div>
          </div>
        ))}

        {reports.length === 0 && (
          <div className="p-20 text-center bg-aqua-island/5 rounded-[3rem] border-2 border-dashed border-wedgewood/20">
            <p className="text-wedgewood font-black uppercase tracking-[0.2em]">No active reports found in your sector.</p>
          </div>
        )}
      </div>
    </div>
  );
}
