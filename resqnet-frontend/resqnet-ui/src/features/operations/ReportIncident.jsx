import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ShieldAlert, MapPin, MessageSquare, Info, ChevronLeft, Send, Globe as GlobeIcon } from 'lucide-react';
import { incidentService } from '../../services/api';

const INCIDENT_TYPES = ['FLOOD', 'EARTHQUAKE', 'FIRE', 'ACCIDENT', 'MEDICAL', 'STORM', 'OTHER'];

export default function ReportIncident() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    type: 'OTHER',
    reporter: '',
    description: '',
    address: '',
    street: '',
    landmark: '',
    city: '',
    district: '',
    country: '',
    latitude: '',
    longitude: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const payload = {
      ...formData,
      latitude: formData.latitude ? parseFloat(formData.latitude) : null,
      longitude: formData.longitude ? parseFloat(formData.longitude) : null,
    };

    try {
      await incidentService.register(payload);
      alert('Your emergency report has been received and is being processed by our network.');
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit report. Ensure all required fields are filled.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen pt-24 pb-20 px-6 max-w-4xl mx-auto animate-in fade-in slide-in-from-bottom-8 duration-700">
      <button
        onClick={() => navigate(-1)}
        className="flex items-center text-wedgewood hover:text-amaranth mb-8 font-bold uppercase tracking-widest text-xs transition-colors group"
      >
        <ChevronLeft className="w-4 h-4 mr-1 group-hover:-translate-x-1 transition-transform" /> Back to Home
      </button>

      <div className="bg-white dark:bg-cello rounded-[2.5rem] border border-wedgewood/10 shadow-2xl overflow-hidden">
        {/* Header Header */}
        <div className="bg-amaranth p-8 md:p-12 text-peppermint">
          <div className="flex items-center space-x-4 mb-4">
            <ShieldAlert className="w-10 h-10" />
            <h1 className="text-3xl md:text-4xl font-black uppercase tracking-tighter">Emergency Report</h1>
          </div>
          <p className="text-peppermint/80 font-medium max-w-xl">
            Provide as much detail as possible. Our system uses advanced processing to analyze your report and dispatch resources immediately.
          </p>
        </div>

        <form onSubmit={handleSubmit} className="p-8 md:p-12 space-y-12">

          {/* Section 1: The Situation (NLP Focus) */}
          <section className="space-y-6">
            <div className="flex items-center space-x-3 text-amaranth">
              <div className="p-2 bg-amaranth/10 rounded-lg">
                <MessageSquare className="w-5 h-5" />
              </div>
              <h2 className="font-black uppercase tracking-widest text-sm">Describe the Situation</h2>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
               <div className="space-y-2">
                <label className="block text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Incident Category</label>
                <select
                  value={formData.type}
                  onChange={(e) => setFormData({...formData, type: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all cursor-pointer"
                >
                  {INCIDENT_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                </select>
              </div>
              <div className="space-y-2">
                <label className="block text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Reporter Name</label>
                <input
                  type="text" required
                  value={formData.reporter}
                  onChange={(e) => setFormData({...formData, reporter: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                  placeholder="Enter full name"
                />
              </div>
            </div>

            <div className="space-y-3">
              <label className="block text-[10px] font-black text-amaranth uppercase tracking-widest ml-1">Briefly describe what is happening *</label>
              <textarea
                required rows="5"
                value={formData.description}
                onChange={(e) => setFormData({...formData, description: e.target.value})}
                className="w-full bg-peppermint/50 dark:bg-cello-dark border border-amaranth/20 rounded-2xl px-6 py-4 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all resize-none text-lg leading-relaxed placeholder:opacity-30"
                placeholder="Example: Large fire spreading across the building, three people trapped on 4th floor. Requesting immediate aerial support..."
              />
              <div className="flex items-start space-x-2 px-1">
                <Info className="w-3.5 h-3.5 text-wedgewood shrink-0 mt-0.5" />
                <p className="text-[10px] text-wedgewood font-medium italic leading-tight">
                  Note: Our AI engine will analyze your description to automatically determine severity and prioritize response.
                </p>
              </div>
            </div>
          </section>

          {/* Section 2: Precise Location */}
          <section className="space-y-6 pt-10 border-t border-wedgewood/10">
            <div className="flex items-center space-x-3 text-wedgewood">
              <div className="p-2 bg-aqua-island/20 rounded-lg">
                <MapPin className="w-5 h-5 text-wedgewood" />
              </div>
              <h2 className="font-black uppercase tracking-widest text-sm text-cello dark:text-peppermint">Location Identity</h2>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">House/Building No.</label>
                <input
                  type="text" placeholder="e.g. 123 Sky Tower"
                  value={formData.address}
                  onChange={(e) => setFormData({...formData, address: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Street / Area</label>
                <input
                  type="text" placeholder="e.g. Main Street"
                  value={formData.street}
                  onChange={(e) => setFormData({...formData, street: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Landmark</label>
                <input
                  type="text" placeholder="e.g. Near Metro Station"
                  value={formData.landmark}
                  onChange={(e) => setFormData({...formData, landmark: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">City / Town *</label>
                <input
                  type="text" required placeholder="Required"
                  value={formData.city}
                  onChange={(e) => setFormData({...formData, city: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">District *</label>
                <input
                  type="text" required placeholder="Required"
                  value={formData.district}
                  onChange={(e) => setFormData({...formData, district: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Country *</label>
                <input
                  type="text" required placeholder="Required"
                  value={formData.country}
                  onChange={(e) => setFormData({...formData, country: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island transition-all outline-none"
                />
              </div>
            </div>
          </section>

          {/* Section 3: GPS Override */}
          <section className="p-8 bg-aqua-island/10 rounded-[2rem] space-y-6 border border-aqua-island/30 shadow-inner">
            <div className="flex items-center space-x-3 text-cello dark:text-peppermint">
              <div className="p-2 bg-white dark:bg-cello rounded-lg shadow-sm">
                <GlobeIcon className="w-5 h-5 text-wedgewood" />
              </div>
              <h2 className="font-black uppercase tracking-widest text-sm">Remote GPS Override</h2>
            </div>

            <div className="flex items-start space-x-4 bg-white/40 dark:bg-black/10 p-4 rounded-xl border border-white/20">
              <Info className="w-5 h-5 text-amaranth shrink-0 mt-0.5" />
              <p className="text-xs text-cello dark:text-peppermint/80 leading-relaxed font-bold italic">
                Are you at a location where a standard address cannot be defined (e.g. dense forest, high mountains, or open ocean)?
                <span className="block mt-1 text-amaranth underline underline-offset-2">Please provide precise GPS coordinates below.</span>
              </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood dark:text-aqua-island uppercase tracking-widest ml-1">Latitude</label>
                <input
                  type="number" step="any" placeholder="0.0000"
                  value={formData.latitude}
                  onChange={(e) => setFormData({...formData, latitude: e.target.value})}
                  className="w-full bg-white dark:bg-cello border border-aqua-island/30 rounded-xl px-4 py-3 text-cello dark:text-peppermint shadow-sm outline-none focus:ring-2 focus:ring-amaranth transition-all"
                />
              </div>
              <div className="space-y-1.5">
                <label className="text-[10px] font-black text-wedgewood dark:text-aqua-island uppercase tracking-widest ml-1">Longitude</label>
                <input
                  type="number" step="any" placeholder="0.0000"
                  value={formData.longitude}
                  onChange={(e) => setFormData({...formData, longitude: e.target.value})}
                  className="w-full bg-white dark:bg-cello border border-aqua-island/30 rounded-xl px-4 py-3 text-cello dark:text-peppermint shadow-sm outline-none focus:ring-2 focus:ring-amaranth transition-all"
                />
              </div>
            </div>
          </section>

          {error && (
            <div className="flex items-center space-x-2 text-amaranth font-black text-xs justify-center bg-amaranth/5 p-4 rounded-2xl border border-amaranth/20 uppercase tracking-widest">
              <Info className="w-4 h-4" />
              <span>{error}</span>
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            className="group w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-6 rounded-2xl font-black text-xl uppercase tracking-[0.2em] hover:bg-amaranth dark:hover:bg-amaranth dark:hover:text-white transition-all shadow-2xl active:scale-95 disabled:opacity-50 flex items-center justify-center space-x-3"
          >
            {loading ? (
              <div className="animate-spin h-6 w-6 border-3 border-peppermint rounded-full border-t-transparent" />
            ) : (
              <>
                <span>Transmit Emergency Alert</span>
                <Send className="w-6 h-6 group-hover:translate-x-2 group-hover:-translate-y-2 transition-transform" />
              </>
            )}
          </button>
        </form>
      </div>
    </div>
  );
}
