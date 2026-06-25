import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Shield, AlertTriangle, ChevronRight, LogIn } from 'lucide-react';

export default function AuthPortal() {
  const navigate = useNavigate();

  const options = [
    {
      title: "Authorized Login",
      desc: "Access your existing account via Password, OTP, or Google.",
      icon: <LogIn className="w-8 h-8 text-wedgewood" />,
      action: () => navigate('/login'),
      color: "border-wedgewood"
    },
    {
      title: "Join the Network",
      desc: "Register as a Volunteer or Field Responder.",
      icon: <Shield className="w-8 h-8 text-aqua-island" />,
      action: () => navigate('/signup'),
      color: "border-aqua-island"
    },
    {
      title: "Report as Citizen",
      desc: "Submit an incident. Account will be created automatically.",
      icon: <AlertTriangle className="w-8 h-8 text-amaranth" />,
      action: () => navigate('/report'),
      color: "border-amaranth"
    }
  ];

  return (
    <div className="min-h-screen flex items-center justify-center px-6 py-20 bg-peppermint dark:bg-cello transition-colors duration-500">
      <div className="w-full max-w-2xl space-y-12">
        <div className="text-center space-y-4">
          <h1 className="text-5xl font-black text-cello dark:text-peppermint uppercase tracking-tighter">Gateway <span className="text-amaranth">Access</span></h1>
          <p className="text-wedgewood dark:text-aqua-island font-bold uppercase tracking-widest text-sm">Select your entry point to the ResQNet Ecosystem</p>
        </div>

        <div className="grid grid-cols-1 gap-6">
          {options.map((opt, i) => (
            <button
              key={i}
              onClick={opt.action}
              className={`group flex items-center p-8 bg-white dark:bg-cello-dark border-l-8 ${opt.color} rounded-[2rem] shadow-xl hover:-translate-y-1 transition-all text-left space-x-6`}
            >
              <div className="p-4 bg-peppermint dark:bg-cello rounded-2xl group-hover:scale-110 transition-transform">
                {opt.icon}
              </div>
              <div className="flex-1">
                <h3 className="text-xl font-black text-cello dark:text-peppermint uppercase tracking-tight">{opt.title}</h3>
                <p className="text-sm text-wedgewood dark:text-aqua-island/60 font-medium">{opt.desc}</p>
              </div>
              <ChevronRight className="w-6 h-6 text-wedgewood group-hover:translate-x-2 transition-transform" />
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
