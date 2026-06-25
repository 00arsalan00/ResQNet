import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../../store/AuthContext';
import { UserCircle, Lock, Mail, ChevronRight, Smartphone } from 'lucide-react';

export default function Login() {
  const [identifier, setIdentifier] = useState('');
  const [password, setPassword] = useState('');
  const [loginType, setLoginType] = useState('password');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await login({ identifier, password });
      navigate('/dashboard');
    } catch (err) {
      setError('Authentication failed. Verify credentials.');
    } finally {
      setLoading(false);
    }
  };

  const handleGoogleLogin = () => {
    window.location.href = 'http://localhost:8072/oauth2/authorization/google';
  };

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-20 bg-peppermint dark:bg-cello transition-colors duration-500">
      <div className="w-full max-w-md bg-white dark:bg-cello-dark rounded-[2.5rem] border border-wedgewood/20 shadow-2xl overflow-hidden p-10 space-y-8 animate-in fade-in zoom-in duration-500">

        <div className="text-center space-y-2">
          <div className="mx-auto w-16 h-16 bg-wedgewood/10 rounded-2xl flex items-center justify-center text-wedgewood mb-4">
            <UserCircle className="w-10 h-10" />
          </div>
          <h1 className="text-3xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">Authorized <span className="text-amaranth">Entry</span></h1>
          <p className="text-xs text-wedgewood font-bold uppercase tracking-widest opacity-60">System Security Layer v2.0</p>
        </div>

        <div className="flex bg-peppermint dark:bg-cello p-1 rounded-2xl border border-wedgewood/10 mb-2">
          <button
            onClick={() => setLoginType('password')}
            className={`flex-1 py-2 rounded-xl text-[10px] font-black uppercase transition-all ${loginType === 'password' ? 'bg-white dark:bg-cello-dark shadow-sm text-amaranth' : 'text-wedgewood opacity-50'}`}
          >
            Standard Key
          </button>
          <button
            onClick={() => setLoginType('otp')}
            className={`flex-1 py-2 rounded-xl text-[10px] font-black uppercase transition-all ${loginType === 'otp' ? 'bg-white dark:bg-cello-dark shadow-sm text-amaranth' : 'text-wedgewood opacity-50'}`}
          >
            Mobile Node
          </button>
        </div>

        {loginType === 'password' ? (
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="space-y-4">
              <div className="relative group">
                <Mail className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-wedgewood opacity-40 group-focus-within:text-amaranth group-focus-within:opacity-100 transition-all" />
                <input
                  type="text" required
                  value={identifier}
                  onChange={(e) => setIdentifier(e.target.value)}
                  placeholder="Email or Phone Number"
                  className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-4 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
                />
              </div>
              <div className="relative group">
                <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-wedgewood opacity-40 group-focus-within:text-amaranth group-focus-within:opacity-100 transition-all" />
                <input
                  type="password" required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  placeholder="Secure Password"
                  className="w-full bg-peppermint/30 dark:bg-cello border border-wedgewood/20 rounded-2xl pl-12 pr-6 py-4 text-sm text-cello dark:text-peppermint outline-none focus:ring-2 focus:ring-amaranth/50 transition-all"
                />
              </div>
            </div>

            {error && <p className="text-[10px] font-black text-amaranth text-center uppercase tracking-widest bg-amaranth/5 p-2 rounded-lg border border-amaranth/10">{error}</p>}

            <button
              type="submit" disabled={loading}
              className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-4 rounded-2xl font-black uppercase tracking-[0.2em] shadow-xl hover:bg-amaranth dark:hover:bg-amaranth dark:hover:text-white transition-all active:scale-95 flex items-center justify-center space-x-2"
            >
              {loading ? <div className="animate-spin h-5 w-5 border-2 border-peppermint rounded-full border-t-transparent" /> : (
                <>
                  <span>Initialize Login</span>
                  <ChevronRight className="w-4 h-4" />
                </>
              )}
            </button>
          </form>
        ) : (
          <div className="space-y-6 animate-in fade-in duration-300">
            <p className="text-center text-xs text-wedgewood font-medium">Passwordless entry for field responders.</p>
            <Link
              to="/otp-login"
              className="w-full bg-cello dark:bg-peppermint text-peppermint dark:text-cello py-4 rounded-2xl font-black uppercase tracking-[0.2em] shadow-xl hover:bg-amaranth flex items-center justify-center space-x-2 transition-all"
            >
              <Smartphone className="w-5 h-5" />
              <span>Use SMS Node</span>
            </Link>
          </div>
        )}

        <div className="relative">
          <div className="absolute inset-0 flex items-center"><div className="w-full border-t border-wedgewood/10"></div></div>
          <div className="relative flex justify-center text-[10px] uppercase font-black"><span className="px-4 bg-white dark:bg-cello-dark text-wedgewood">Secondary Nodes</span></div>
        </div>

        <button
          onClick={handleGoogleLogin}
          className="w-full flex items-center justify-center space-x-2 py-4 rounded-2xl border border-wedgewood/20 text-cello dark:text-peppermint hover:bg-aqua-island/10 transition-all text-xs font-black uppercase tracking-widest"
        >
           <span>Continue with Google</span>
        </button>

        <p className="text-center text-[10px] text-wedgewood font-bold uppercase tracking-widest pt-4 border-t border-wedgewood/10">
          New Responder? <Link to="/signup" className="text-amaranth hover:underline">Register Account</Link>
        </p>
      </div>
    </div>
  );
}
