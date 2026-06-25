import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../store/AuthContext';
import { authService } from '../../services/auth';
import { Smartphone, ShieldCheck, ChevronRight, Timer, RefreshCcw } from 'lucide-react';

export default function OtpAuth() {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [code, setCode] = useState('');
  const [step, setStep] = useState('send'); // 'send' or 'verify'
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [timeLeft, setTimeLeft] = useState(300); // 5 minutes in seconds
  const { handleOtpLogin } = useAuth();
  const navigate = useNavigate();

  // Timer Logic
  useEffect(() => {
    let timer;
    if (step === 'verify' && timeLeft > 0) {
      timer = setInterval(() => {
        setTimeLeft((prev) => prev - 1);
      }, 1000);
    } else if (timeLeft === 0) {
      setError('Access Key expired. Please request a new one.');
    }
    return () => clearInterval(timer);
  }, [step, timeLeft]);

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  const handleSend = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    // Normalize phone number (remove spaces)
    const cleanPhone = phoneNumber.replace(/\s/g, '');
    try {
      await authService.sendOtp({ phoneNumber: cleanPhone });
      setStep('verify');
      setTimeLeft(300); // Reset timer
    } catch (err) {
      setError('Mobile node unreachable. Verify phone number format.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerify = async (e) => {
    e.preventDefault();
    if (timeLeft === 0) {
      setError('Access Key expired. Request a new one.');
      return;
    }
    setLoading(true);
    setError('');
    const cleanPhone = phoneNumber.replace(/\s/g, '');
    try {
      const response = await authService.verifyOtp({
        phoneNumber: cleanPhone,
        code: code.trim()
      });
      handleOtpLogin(response.data);
      navigate('/dashboard');
    } catch (err) {
      setError('Invalid or expired node key. Check the code and try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-20 bg-peppermint dark:bg-cello transition-colors duration-500">
      <div className="w-full max-w-md bg-white dark:bg-cello-dark rounded-[2.5rem] border border-wedgewood/20 shadow-2xl overflow-hidden p-10 space-y-8 animate-in fade-in zoom-in duration-500">

        <div className="text-center space-y-2">
          <div className="mx-auto w-16 h-16 bg-aqua-island/10 rounded-2xl flex items-center justify-center text-wedgewood mb-4">
            {step === 'send' ? <Smartphone className="w-10 h-10" /> : <ShieldCheck className="w-10 h-10 text-amaranth" />}
          </div>
          <h1 className="text-3xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">
            {step === 'send' ? 'OTP' : 'Secure'} <span className="text-amaranth">Access</span>
          </h1>
          <p className="text-[10px] text-wedgewood font-black uppercase tracking-[0.3em] opacity-60">
            {step === 'send' ? 'Field Authentication' : 'Verifying Mobile Node'}
          </p>
        </div>

        {step === 'send' ? (
          <form onSubmit={handleSend} className="space-y-6">
            <div className="space-y-2">
              <label className="text-[10px] font-black text-wedgewood uppercase tracking-widest ml-1">Phone Number</label>
              <input
                type="tel" required
                value={phoneNumber}
                onChange={(e) => setPhoneNumber(e.target.value)}
                placeholder="+91 98765 43210"
                className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl px-6 py-4 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
              />
            </div>

            {error && <p className="text-[10px] font-black text-amaranth text-center uppercase tracking-widest bg-amaranth/5 p-2 rounded-lg border border-amaranth/10">{error}</p>}

            <button
              type="submit" disabled={loading}
              className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-4 rounded-2xl font-black uppercase tracking-[0.2em] shadow-xl hover:bg-amaranth dark:hover:bg-amaranth dark:hover:text-white transition-all active:scale-95 flex items-center justify-center space-x-2"
            >
               {loading ? <RefreshCcw className="animate-spin w-5 h-5" /> : <span>Request Access Key</span>}
            </button>
          </form>
        ) : (
          <form onSubmit={handleVerify} className="space-y-6">
            <div className="space-y-2 text-center">
              <p className="text-xs text-wedgewood font-medium italic">Transmission to {phoneNumber}</p>
              <div className="flex justify-center py-4">
                <input
                  type="text" required maxLength="6"
                  value={code}
                  onChange={(e) => setCode(e.target.value)}
                  className="w-48 bg-peppermint/50 dark:bg-cello border-2 border-amaranth/30 rounded-2xl px-6 py-4 text-3xl font-black text-center text-cello dark:text-peppermint tracking-[0.2em] focus:border-amaranth outline-none transition-all shadow-inner"
                  placeholder="------"
                  autoFocus
                />
              </div>
              <div className={`flex items-center justify-center space-x-1 text-[10px] font-black uppercase transition-colors ${timeLeft < 30 ? 'text-amaranth animate-pulse' : 'text-wedgewood'}`}>
                 <Timer className="w-3.5 h-3.5" />
                 <span>Expires in {formatTime(timeLeft)}</span>
              </div>
            </div>

            {error && <p className="text-[10px] font-black text-amaranth text-center uppercase tracking-widest bg-amaranth/5 p-2 rounded-lg border border-amaranth/10">{error}</p>}

            <button
              type="submit" disabled={loading || timeLeft === 0}
              className="w-full bg-amaranth text-white py-4 rounded-2xl font-black uppercase tracking-[0.2em] shadow-xl shadow-amaranth/20 hover:bg-cello transition-all active:scale-95 flex items-center justify-center space-x-2 disabled:opacity-50 disabled:grayscale"
            >
               {loading ? <RefreshCcw className="animate-spin w-5 h-5" /> : <span>Verify Node Connection</span>}
            </button>

            <div className="flex flex-col space-y-3 pt-2">
              <button
                type="button"
                onClick={() => setStep('send')}
                className="text-[10px] font-black text-wedgewood uppercase hover:text-amaranth transition-colors"
              >
                Re-enter Phone Number
              </button>
              {timeLeft === 0 && (
                <button
                  type="button"
                  onClick={handleSend}
                  className="text-[10px] font-black text-amaranth uppercase hover:underline underline-offset-4"
                >
                  Resend Access Key
                </button>
              )}
            </div>
          </form>
        )}

        <p className="text-center text-[10px] text-wedgewood font-bold uppercase tracking-widest pt-4 border-t border-wedgewood/10">
          Prefer Secure Password? <Link to="/login" className="text-amaranth hover:underline">Standard Node</Link>
        </p>
      </div>
    </div>
  );
}
