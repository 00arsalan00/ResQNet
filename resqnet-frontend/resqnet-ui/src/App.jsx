import React from 'react';
import { Routes, Route, useLocation } from 'react-router-dom';
import Navbar from './components/layout/Navbar';
import Globe from './components/Globe';
import Dashboard from './features/operations/Dashboard';
import LiveMap from './features/operations/LiveMap';
import ReportIncident from './features/operations/ReportIncident';
import Login from './features/auth/Login';
import Signup from './features/auth/Signup';
import OtpAuth from './features/auth/OtpAuth';
import ProtectedRoute from './components/ProtectedRoute';
import { ThemeProvider } from './store/ThemeContext';
import { AuthProvider } from './store/AuthContext';

function LandingPage() {
  return (
    <div className="flex flex-col">
      <section className="min-h-screen flex flex-col items-center justify-center px-6 pt-24 pb-12 bg-peppermint dark:bg-cello transition-colors duration-500">
        <div className="text-center mb-10 space-y-4">
          <h2 className="text-5xl md:text-8xl font-black tracking-tighter text-cello dark:text-peppermint uppercase">
            Global <span className="text-amaranth drop-shadow-sm">Response</span>
          </h2>
          <p className="text-base md:text-lg text-wedgewood dark:text-aqua-island/80 font-medium max-w-2xl mx-auto leading-relaxed">
            Unified platform for real-time disaster tracking, resource allocation, and emergency field operations coordination.
          </p>
        </div>

        <div className="w-full max-w-6xl relative">
          <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-64 h-64 bg-aqua-island/20 rounded-full blur-[100px] pointer-events-none" />
          <Globe />
        </div>

        <div className="mt-12 animate-bounce hidden md:block">
          <div className="w-6 h-10 border-2 border-wedgewood/30 rounded-full flex justify-center p-1">
            <div className="w-1 h-3 bg-amaranth rounded-full" />
          </div>
        </div>
      </section>

      <LiveMap />

      <footer className="py-16 bg-peppermint dark:bg-cello border-t border-wedgewood/10 flex flex-col items-center space-y-4 transition-colors text-center px-6">
        <div className="h-10 w-[2px] bg-amaranth/30" />
        <p className="text-[10px] font-mono text-wedgewood dark:text-aqua-island opacity-50 uppercase tracking-[1em] ml-[1em]">
          Precision. Efficiency. ResQNet.
        </p>
      </footer>
    </div>
  );
}

function MainLayout() {
  const location = useLocation();

  return (
    <div className={`min-h-screen bg-peppermint dark:bg-cello transition-colors duration-500 flex flex-col`}>
      <Navbar />
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/report" element={<ReportIncident />} />
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/otp-login" element={<OtpAuth />} />

        {/* Protected Dashboard - Accessible by all roles except CITIZEN for the full ops view */}
        <Route path="/dashboard" element={
          <ProtectedRoute roles={['SUPER_ADMIN', 'DISTRICT_COORDINATOR', 'FIELD_RESCUE_TEAM', 'VOLUNTEER']}>
            <div className="pt-28 px-6 pb-12 flex-1 overflow-y-auto">
              <Dashboard />
            </div>
          </ProtectedRoute>
        } />
      </Routes>
    </div>
  );
}

function App() {
  return (
    <ThemeProvider>
      <AuthProvider>
        <MainLayout />
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
