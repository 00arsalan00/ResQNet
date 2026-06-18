import React, { useState } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { Fragment } from 'react';
import { ShieldAlert, X } from 'lucide-react';
import { incidentService } from '../../services/api';

const INCIDENT_TYPES = ['FLOOD', 'EARTHQUAKE', 'FIRE', 'ACCIDENT', 'MEDICAL', 'STORM', 'OTHER'];

export default function IncidentForm({ isOpen, onClose }) {
  const [formData, setFormData] = useState({
    type: 'OTHER',
    severity: 3,
    reporter: '',
    latitude: 0,
    longitude: 0,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await incidentService.register(formData);
      alert('Incident reported successfully!');
      onClose();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to report incident. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Transition show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-[60]" onClose={onClose}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-cello/80 backdrop-blur-sm" />
        </Transition.Child>

        <div className="fixed inset-0 overflow-y-auto">
          <div className="flex min-h-full items-center justify-center p-4">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 scale-95"
              enterTo="opacity-100 scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 scale-100"
              leaveTo="opacity-0 scale-95"
            >
              <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-peppermint dark:bg-cello p-8 text-left align-middle shadow-xl transition-all border border-aqua-island/20">
                <div className="flex justify-between items-center mb-6">
                  <div className="flex items-center space-x-3">
                    <div className="p-2 bg-amaranth/10 rounded-lg">
                      <ShieldAlert className="w-6 h-6 text-amaranth" />
                    </div>
                    <Dialog.Title as="h3" className="text-xl font-bold text-cello dark:text-peppermint">
                      Report Incident
                    </Dialog.Title>
                  </div>
                  <button onClick={onClose} className="text-wedgewood hover:text-amaranth transition-colors">
                    <X className="w-6 h-6" />
                  </button>
                </div>

                <form onSubmit={handleSubmit} className="space-y-4">
                  <div>
                    <label className="block text-sm font-bold text-cello dark:text-aqua-island mb-1 uppercase tracking-wider">
                      Incident Type
                    </label>
                    <select
                      value={formData.type}
                      onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                      className="w-full bg-white dark:bg-cello-dark border border-aqua-island/30 rounded-lg px-4 py-2 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                    >
                      {INCIDENT_TYPES.map((type) => (
                        <option key={type} value={type}>{type}</option>
                      ))}
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-bold text-cello dark:text-aqua-island mb-1 uppercase tracking-wider">
                      Severity (1-5)
                    </label>
                    <input
                      type="range"
                      min="1"
                      max="5"
                      value={formData.severity}
                      onChange={(e) => setFormData({ ...formData, severity: parseInt(e.target.value) })}
                      className="w-full accent-amaranth"
                    />
                    <div className="flex justify-between text-xs text-wedgewood font-bold">
                      <span>LOW</span>
                      <span>HIGH</span>
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-bold text-cello dark:text-aqua-island mb-1 uppercase tracking-wider">
                      Reporter Name
                    </label>
                    <input
                      type="text"
                      required
                      value={formData.reporter}
                      onChange={(e) => setFormData({ ...formData, reporter: e.target.value })}
                      placeholder="Enter your name"
                      className="w-full bg-white dark:bg-cello-dark border border-aqua-island/30 rounded-lg px-4 py-2 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                    />
                  </div>

                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-bold text-cello dark:text-aqua-island mb-1 uppercase tracking-wider">
                        Latitude
                      </label>
                      <input
                        type="number"
                        step="any"
                        required
                        value={formData.latitude}
                        onChange={(e) => setFormData({ ...formData, latitude: parseFloat(e.target.value) })}
                        className="w-full bg-white dark:bg-cello-dark border border-aqua-island/30 rounded-lg px-4 py-2 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-bold text-cello dark:text-aqua-island mb-1 uppercase tracking-wider">
                        Longitude
                      </label>
                      <input
                        type="number"
                        step="any"
                        required
                        value={formData.longitude}
                        onChange={(e) => setFormData({ ...formData, longitude: parseFloat(e.target.value) })}
                        className="w-full bg-white dark:bg-cello-dark border border-aqua-island/30 rounded-lg px-4 py-2 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                      />
                    </div>
                  </div>

                  {error && (
                    <p className="text-amaranth text-xs font-bold bg-amaranth/5 p-2 rounded border border-amaranth/20">
                      {error}
                    </p>
                  )}

                  <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-amaranth hover:bg-amaranth-dark text-white font-bold py-3 rounded-lg shadow-lg transition-all active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed mt-4 uppercase tracking-widest"
                  >
                    {loading ? 'Submitting...' : 'Send Emergency Report'}
                  </button>
                </form>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
}
