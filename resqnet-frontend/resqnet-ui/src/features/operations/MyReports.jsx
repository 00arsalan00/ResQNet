import React, { useEffect, useState } from 'react';
import { incidentService } from '../../services/api';
import { ShieldAlert, Clock, CheckCircle, Truck, MapPin, ChevronRight, AlertCircle } from 'lucide-react';

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
        const response = await incidentService.getMyReports();
        setReports(response.data);
      } catch (err) {
        console.error("Link to operational node failed", err);
      } finally {
        setLoading(false);
      }
    };
    fetchMyReports();
  }, []);

  const getStatusIndex = (status) => {
    return STATUS_STEPS.findIndex(step => step.id === status);
  };

  if (loading) {
    return (
      <div className="min-h-[60vh] flex flex-col items-center justify-center">
        <div className="w-12 h-12 border-t-4 border-amaranth rounded-full animate-spin" />
        <p className="mt-4 text-[10px] font-black text-wedgewood uppercase tracking-[0.4em]">Establishing Secure Link...</p>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto space-y-12 animate-in fade-in slide-in-from-bottom-4 duration-700 pb-20">
      <div className="text-center space-y-2">
        <h2 className="text-5xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">My <span className="text-amaranth">Activity</span></h2>
        <p className="text-xs text-wedgewood font-bold uppercase tracking-widest opacity-60">Live Operational Tracking</p>
      </div>

      <div className="grid grid-cols-1 gap-8">
        {reports.map((report) => {
          const currentIndex = getStatusIndex(report.status);

          return (
            <div key={report.id} className="group bg-white dark:bg-cello rounded-[2.5rem] border border-wedgewood/10 shadow-2xl overflow-hidden hover:border-amaranth/20 transition-all duration-500">
              <div className="p-8 md:p-10 flex flex-col md:flex-row gap-10 items-center">

                {/* ID and Type */}
                <div className="md:w-1/4 space-y-3">
                  <div className="inline-flex items-center px-3 py-1 rounded-full bg-amaranth/5 text-amaranth text-[10px] font-black uppercase tracking-widest border border-amaranth/10">
                    ID: {report.id.substring(0, 8)}
                  </div>
                  <h3 className="text-3xl font-black text-cello dark:text-peppermint uppercase tracking-tighter leading-none">{report.type}</h3>
                  <div className="flex items-center text-wedgewood space-x-2">
                     <Clock className="w-4 h-4" />
                     <span className="text-[10px] font-black uppercase tracking-wider">Reported 2h ago</span>
                  </div>
                </div>

                {/* The Timeline Map */}
                <div className="flex-1 w-full px-4">
                  <div className="relative flex justify-between">
                    {/* Connecting Line Background */}
                    <div className="absolute top-5 left-0 w-full h-0.5 bg-peppermint-dark dark:bg-cello-dark z-0" />

                    {/* Connecting Line Active */}
                    <div
                      className="absolute top-5 left-0 h-0.5 bg-amaranth z-0 transition-all duration-1000 ease-out"
                      style={{ width: `${(currentIndex / (STATUS_STEPS.length - 1)) * 100}%` }}
                    />

                    {STATUS_STEPS.map((step, idx) => {
                      const isActive = idx <= currentIndex;
                      const isCurrent = idx === currentIndex;

                      return (
                        <div key={step.id} className="relative z-10 flex flex-col items-center">
                          <div className={`w-11 h-11 rounded-full flex items-center justify-center transition-all duration-700 ${
                            isActive
                              ? 'bg-amaranth text-white scale-110 shadow-lg shadow-amaranth/30'
                              : 'bg-peppermint dark:bg-cello-dark text-wedgewood/40'
                          }`}>
                            <step.icon className={`w-5 h-5 ${isCurrent ? 'animate-pulse' : ''}`} />
                          </div>
                          <p className={`mt-4 text-[9px] font-black uppercase tracking-tighter text-center w-24 transition-colors duration-500 ${
                            isActive ? 'text-cello dark:text-peppermint' : 'text-wedgewood/40'
                          }`}>
                            {step.label}
                          </p>
                        </div>
                      )
                    })}
                  </div>
                </div>

                {/* Details Trigger */}
                <button className="p-5 bg-aqua-island/5 dark:bg-peppermint/5 rounded-3xl text-wedgewood hover:bg-amaranth hover:text-white transition-all transform active:scale-90 border border-wedgewood/5">
                  <ChevronRight className="w-6 h-6" />
                </button>
              </div>
            </div>
          )
        })}

        {reports.length === 0 && (
          <div className="p-20 text-center bg-aqua-island/5 rounded-[4rem] border-2 border-dashed border-wedgewood/10 flex flex-col items-center space-y-4">
            <AlertCircle className="w-12 h-12 text-wedgewood opacity-20" />
            <p className="text-wedgewood font-black uppercase tracking-[0.2em] text-sm">No active reports detected in your sector.</p>
          </div>
        )}
      </div>
    </div>
  );
}