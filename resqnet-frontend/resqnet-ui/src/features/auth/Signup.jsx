import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../store/AuthContext';
import { UserCircle, Shield, Mail, Lock, Smartphone, ChevronRight } from 'lucide-react';

export default function Signup() {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    phoneNumber: '',
    role: 'CITIZEN'
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { signup } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await signup(formData);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed. Check network nodes.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-20 bg-peppermint dark:bg-cello transition-colors duration-500">
      <div className="w-full max-w-md bg-white dark:bg-cello-dark rounded-[2.5rem] border border-wedgewood/20 shadow-2xl overflow-hidden p-10 space-y-8 animate-in fade-in zoom-in duration-500">

        <div className="text-center space-y-2">
          <div className="mx-auto w-16 h-16 bg-amaranth/10 rounded-2xl flex items-center justify-center text-amaranth mb-4">
            <Shield className="w-10 h-10" />
          </div>
          <h1 className="text-3xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">Responder <span className="text-amaranth">Registry</span></h1>
          <p className="text-xs text-wedgewood font-bold uppercase tracking-widest opacity-60">Join the Response Network</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-5">
          <div className="space-y-4">
             <div className="relative">
              <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-wedgewood" />
              <input
                type="email" required
                value={formData.email}
                onChange={(e) => setFormData({...formData, email: e.target.value})}
                placeholder="Operational Email"
                className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-3.5 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
              />
            </div>
            <div className="relative">
              <Smartphone className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-wedgewood" />
              <input
                type="tel" required
                value={formData.phoneNumber}
                onChange={(e) => setFormData({...formData, phoneNumber: e.target.value})}
                placeholder="Phone (for OTP node)"
                className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-3.5 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
              />
            </div>
            <div className="relative">
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-4 h-4 text-wedgewood" />
              <input
                type="password" required
                value={formData.password}
                onChange={(e) => setFormData({...formData, password: e.target.value})}
                placeholder="Secure Access Key (Min 8 chars)"
                className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-3.5 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
              />
            </div>

            <div className="space-y-1">
              <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Assigned Role</label>
              <select
                value={formData.role}
                onChange={(e) => setFormData({...formData, role: e.target.value})}
                className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl px-4 py-3.5 text-sm text-cello dark:text-peppermint"
              >
                <option value="CITIZEN">Citizen</option>
                <option value="VOLUNTEER">Volunteer</option>
                <option value="FIELD_RESCUE_TEAM">Rescue Team</option>
                <option value="DISTRICT_COORDINATOR">District Coordinator</option>
              </select>
            </div>
          </div>

          {error && <p className="text-[10px] font-black text-amaranth text-center uppercase tracking-widest bg-amaranth/5 p-2 rounded-lg border border-amaranth/10">{error}</p>}

          <button
            type="submit" disabled={loading}
            className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-4 rounded-2xl font-black uppercase tracking-[0.2em] shadow-xl hover:bg-amaranth dark:hover:bg-amaranth dark:hover:text-white transition-all active:scale-95 flex items-center justify-center space-x-2"
          >
            {loading ? <div className="animate-spin h-5 w-5 border-2 border-peppermint rounded-full border-t-transparent" /> : (
              <>
                <span>Establish Account</span>
                <ChevronRight className="w-4 h-4" />
              </>
            )}
          </button>
        </form>

        <p className="text-center text-[10px] text-wedgewood font-bold uppercase tracking-widest">
          Already Enrolled? <Link to="/login" className="text-amaranth hover:underline">Authentication Node</Link>
        </p>
      </div>
    </div>
  );
}
