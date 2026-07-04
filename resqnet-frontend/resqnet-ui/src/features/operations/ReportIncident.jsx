import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ShieldAlert, MapPin, MessageSquare, Info, ChevronLeft, Send, Globe as GlobeIcon, CheckCircle, Smartphone, Mail, Timer, RefreshCcw, ShieldCheck, User } from 'lucide-react';
import { incidentService } from '../../services/api';
import { authService } from '../../services/auth';
import { useAuth } from '../../store/AuthContext';

const INCIDENT_TYPES = ['FLOOD', 'EARTHQUAKE', 'FIRE', 'ACCIDENT', 'MEDICAL', 'STORM', 'OTHER'];

export default function ReportIncident() {
  const navigate = useNavigate();
  const { user: authUser, handleOtpLogin } = useAuth();
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    type: 'OTHER',
    reporter: '',
    email: '',
    phoneNumber: '',
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

  const [identityInput, setIdentityInput] = useState('');
  const [otpSent, setOtpSent] = useState(false);
  const [otpCode, setCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [timeLeft, setTimeLeft] = useState(0);
  const [isVerified, setIsVerified] = useState(false);

  useEffect(() => {
    let timer;
    if (otpSent && timeLeft > 0 && !isVerified) {
      timer = setInterval(() => setTimeLeft((prev) => prev - 1), 1000);
    } else if (timeLeft === 0 && otpSent && !isVerified) {
      setError('Verification key expired. Please request a new one.');
    }
    return () => clearInterval(timer);
  }, [otpSent, timeLeft, isVerified]);

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const handleNextStep = () => {
    setError('');
    if (step === 1) {
      if (authUser) {
        setFormData(prev => ({
          ...prev,
          email: authUser.email || '',
          phoneNumber: authUser.phoneNumber || '',
          reporter: authUser.name || prev.reporter
        }));
        setStep(3);
      } else {
        setStep(2);
      }
    } else {
      setStep(step + 1);
    }
  };

  const resetVerification = () => {
    setOtpSent(false);
    setTimeLeft(0);
    setCode('');
    setIdentityInput('');
    setError('');
    setFormData(prev => ({ ...prev, email: '', phoneNumber: '' }));
  };

  const handleSendOtp = async () => {
    if (!identityInput) {
      setError('Please provide your Email or Phone Number.');
      return;
    }
    setLoading(true);
    setError('');

    const cleanInput = identityInput.replace(/\s/g, '');
    const isEmail = cleanInput.includes('@');

    setFormData(prev => ({
      ...prev,
      email: isEmail ? cleanInput : '',
      phoneNumber: isEmail ? '' : cleanInput
    }));

    try {
      await authService.sendOtp({ phoneNumber: cleanInput });
      setOtpSent(true);
      setTimeLeft(300);
    } catch (err) {
      setError('Communication node failed. Verify your contact information.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOtp = async () => {
    if (timeLeft === 0) {
      setError('Key expired.');
      return;
    }
    setLoading(true);
    setError('');
    const cleanInput = identityInput.replace(/\s/g, '');
    try {
      const response = await authService.verifyOtp({ phoneNumber: cleanInput, code: otpCode.trim() });
      setIsVerified(true);
      handleOtpLogin(response.data);
      setTimeout(() => setStep(3), 1000);
    } catch (err) {
      setError(err.response?.data?.message || 'Verification key mismatch.');
    } finally {
      setLoading(false);
    }
  };

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
      navigate('/my-reports');
    } catch (err) {
      setError('Transmission failed. Satellite link error.');
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
        <ChevronLeft className="w-4 h-4 mr-1 group-hover:-translate-x-1 transition-transform" /> Back
      </button>

      <div className="bg-white dark:bg-cello rounded-[2.5rem] border border-wedgewood/10 shadow-2xl overflow-hidden transition-colors duration-500 text-cello dark:text-peppermint">
        <div className="bg-amaranth p-8 md:p-12 text-peppermint">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <ShieldAlert className="w-10 h-10" />
              <h1 className="text-3xl md:text-4xl font-black uppercase tracking-tighter">Emergency Report</h1>
            </div>
            <div className="flex space-x-2">
              {[1, 2, 3].map(i => (
                <div key={i} className={`h-1.5 w-8 rounded-full transition-colors ${step >= i ? 'bg-white' : 'bg-white/30'}`} />
              ))}
            </div>
          </div>
        </div>

        <div className="p-8 md:p-12">
          {step === 1 && (
            <div className="space-y-8 animate-in fade-in slide-in-from-right-4 duration-500">
              <div className="flex items-center space-x-3 text-amaranth">
                <MessageSquare className="w-5 h-5" />
                <h2 className="font-black uppercase tracking-widest text-sm">Step 1: The Situation</h2>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                 <div className="space-y-2">
                  <label className="block text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Incident Category</label>
                  <select
                    value={formData.type}
                    onChange={(e) => setFormData({...formData, type: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                  >
                    {INCIDENT_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
                  </select>
                </div>
                <div className="space-y-2">
                  <label className="block text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Your Name</label>
                  <input
                    type="text" required
                    value={formData.reporter}
                    onChange={(e) => setFormData({...formData, reporter: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all"
                    placeholder="Full Name"
                  />
                </div>
              </div>
              <div className="space-y-3">
                <label className="block text-[10px] font-black text-amaranth uppercase tracking-widest ml-1">Narrative Description *</label>
                <textarea
                  required rows="5"
                  value={formData.description}
                  onChange={(e) => setFormData({...formData, description: e.target.value})}
                  className="w-full bg-peppermint/50 dark:bg-cello-dark border border-amaranth/20 rounded-2xl px-6 py-4 text-cello dark:text-peppermint focus:ring-2 focus:ring-amaranth outline-none transition-all resize-none text-lg"
                  placeholder="Example: Large fire spreading across the building..."
                />
              </div>
              <button
                onClick={handleNextStep}
                disabled={!formData.description || !formData.reporter}
                className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-5 rounded-2xl font-black uppercase tracking-widest hover:bg-amaranth dark:hover:bg-amaranth transition-all shadow-xl disabled:opacity-30"
              >
                Continue {authUser ? 'to Location' : 'to Verification'}
              </button>
            </div>
          )}

          {step === 2 && (
            <div className="space-y-8 animate-in fade-in slide-in-from-right-4 duration-500">
              <div className="flex items-center space-x-3 text-wedgewood">
                <ShieldCheck className="w-5 h-5" />
                <h2 className="font-black uppercase tracking-widest text-sm">Step 2: Identity Verification</h2>
              </div>

              <div className="p-8 bg-aqua-island/5 border border-wedgewood/10 rounded-[2rem] space-y-8">
                <div className="space-y-2">
                  <label className="block text-[10px] font-black text-wedgewood uppercase tracking-[0.2em] ml-1">Contact Identity (Email or Phone)</label>
                  <div className="relative group">
                    <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-wedgewood opacity-40 group-focus-within:text-amaranth transition-all" />
                    <input
                      type="text" required
                      value={identityInput}
                      disabled={otpSent}
                      onChange={(e) => setIdentityInput(e.target.value)}
                      className="w-full bg-white dark:bg-cello-dark border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-4 text-sm text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none transition-all disabled:opacity-50"
                      placeholder="e.g. name@mail.com or +91 98765 43210"
                    />
                  </div>
                </div>

                {!isVerified ? (
                  <div className="space-y-8">
                    {!otpSent ? (
                      <button
                        onClick={handleSendOtp}
                        disabled={loading || !identityInput}
                        className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-4 rounded-2xl font-black uppercase tracking-widest hover:bg-amaranth transition-all shadow-xl disabled:opacity-30"
                      >
                        {loading ? <RefreshCcw className="animate-spin w-4 h-4 mr-2" /> : null}
                        <span>Request Verification Key</span>
                      </button>
                    ) : (
                      <div className="space-y-8 text-center animate-in zoom-in duration-300 border-t border-wedgewood/10 pt-8">
                        <div className="space-y-4">
                          <p className="text-xs text-wedgewood font-medium">Enter 6-digit key sent to your identity</p>
                          <div className="flex flex-col items-center">
                            <input
                              type="text" maxLength="6"
                              value={otpCode}
                              onChange={(e) => setCode(e.target.value)}
                              className="w-48 bg-peppermint/50 dark:bg-cello border-2 border-amaranth/30 rounded-2xl px-6 py-4 text-3xl font-black text-center text-cello dark:text-peppermint tracking-[0.2em] focus:border-amaranth outline-none transition-all shadow-inner"
                              placeholder="------"
                              autoFocus
                            />
                            <div className={`flex items-center justify-center space-x-1 mt-4 text-[10px] font-black uppercase transition-colors ${timeLeft < 30 ? 'text-amaranth animate-pulse' : 'text-wedgewood'}`}>
                              <Timer className="w-3.5 h-3.5" />
                              <span>Expires in {formatTime(timeLeft)}</span>
                            </div>
                          </div>
                        </div>

                        <div className="space-y-3">
                          <button
                            onClick={handleVerifyOtp}
                            disabled={loading || otpCode.length < 6 || timeLeft === 0}
                            className="w-full bg-amaranth text-white py-4 rounded-2xl font-black uppercase tracking-widest shadow-xl shadow-amaranth/20 active:scale-95 transition-all"
                          >
                            {loading ? 'Verifying...' : 'Establish Identity'}
                          </button>
                          <button
                            onClick={resetVerification}
                            className="text-[10px] font-black text-amaranth uppercase tracking-widest hover:underline"
                          >
                            Reset & Clear Selection
                          </button>
                        </div>
                      </div>
                    )}
                  </div>
                ) : (
                  <div className="p-12 bg-aqua-island/10 rounded-[2rem] border border-aqua-island/30 flex flex-col items-center space-y-4 animate-in zoom-in duration-500">
                    <div className="w-20 h-20 bg-peppermint dark:bg-cello rounded-full flex items-center justify-center text-aqua-island border-4 border-aqua-island animate-pulse">
                      <CheckCircle className="w-10 h-10" />
                    </div>
                    <h3 className="text-xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">Identity Synchronized</h3>
                    <p className="text-xs text-wedgewood font-bold uppercase tracking-widest opacity-60 italic">Mapping incident to global node...</p>
                  </div>
                )}
              </div>

              {error && <p className="text-[10px] font-black text-amaranth text-center uppercase tracking-widest bg-amaranth/5 p-3 rounded-xl border border-amaranth/10">{error}</p>}
            </div>
          )}

          {step === 3 && (
            <div className="space-y-10 animate-in fade-in slide-in-from-right-4 duration-500">
              <div className="flex items-center space-x-3 text-wedgewood">
                <MapPin className="w-5 h-5" />
                <h2 className="font-black uppercase tracking-widest text-sm">Step 3: Geolocation Details</h2>
              </div>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">House/Building No.</label>
                  <input
                    type="text" placeholder="e.g. 123 Sky Tower"
                    value={formData.address}
                    onChange={(e) => setFormData({...formData, address: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Street / Area</label>
                  <input
                    type="text" placeholder="e.g. Main Street"
                    value={formData.street}
                    onChange={(e) => setFormData({...formData, street: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Landmark</label>
                  <input
                    type="text" placeholder="e.g. Near Metro Station"
                    value={formData.landmark}
                    onChange={(e) => setFormData({...formData, landmark: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">City / Town *</label>
                  <input
                    type="text" required placeholder="Required"
                    value={formData.city}
                    onChange={(e) => setFormData({...formData, city: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">District *</label>
                  <input
                    type="text" required placeholder="Required"
                    value={formData.district}
                    onChange={(e) => setFormData({...formData, district: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
                <div className="space-y-1.5">
                  <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Country *</label>
                  <input
                    type="text" required placeholder="Required"
                    value={formData.country}
                    onChange={(e) => setFormData({...formData, country: e.target.value})}
                    className="w-full bg-peppermint/50 dark:bg-cello-dark border border-wedgewood/20 rounded-xl px-4 py-3 text-cello dark:text-peppermint focus:ring-2 focus:ring-aqua-island outline-none"
                  />
                </div>
              </div>
              <section className="p-6 bg-aqua-island/10 rounded-[2rem] space-y-4 border border-aqua-island/30 shadow-inner">
                <div className="flex items-center space-x-2 text-wedgewood">
                  <GlobeIcon className="w-4 h-4" />
                  <span className="text-[10px] font-black uppercase tracking-widest italic">Optional GPS Override</span>
                </div>
                <div className="grid grid-cols-2 gap-6">
                  <input
                    type="number" step="any" placeholder="Latitude"
                    value={formData.latitude}
                    onChange={(e) => setFormData({...formData, latitude: e.target.value})}
                    className="w-full bg-white dark:bg-cello border border-aqua-island/20 rounded-xl px-4 py-3 text-xs text-cello dark:text-peppermint outline-none"
                  />
                  <input
                    type="number" step="any" placeholder="Longitude"
                    value={formData.longitude}
                    onChange={(e) => setFormData({...formData, longitude: e.target.value})}
                    className="w-full bg-white dark:bg-cello border border-aqua-island/20 rounded-xl px-4 py-3 text-xs text-cello dark:text-peppermint outline-none"
                  />
                </div>
              </section>
              <button
                onClick={handleSubmit}
                disabled={loading}
                className="w-full bg-amaranth text-peppermint py-6 rounded-2xl font-black text-xl uppercase tracking-[0.2em] shadow-2xl hover:bg-cello transition-all active:scale-95 flex items-center justify-center space-x-3"
              >
                {loading ? <RefreshCcw className="animate-spin h-6 w-6" /> : (
                  <>
                    <span>Dispatch Alert</span>
                    <Send className="w-6 h-6" />
                  </>
                )}
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
