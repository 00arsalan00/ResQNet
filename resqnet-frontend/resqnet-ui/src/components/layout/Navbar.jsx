import React from 'react';
import { ShieldAlert, UserCircle, Sun, Moon, LayoutDashboard, LogOut, User } from 'lucide-react';
import { Link, useNavigate } from 'react-router-dom';
import { useTheme } from '../../store/ThemeContext';
import { useAuth } from '../../store/AuthContext';

export default function Navbar() {
  const { isDarkMode, toggleTheme } = useTheme();
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/');
  };

  return (
    <nav className="fixed top-0 left-0 right-0 h-20 bg-transparent z-50 px-6 md:px-12 flex items-center justify-between pointer-events-none">
      <div className="flex flex-col pointer-events-auto">
        <Link to="/" className="flex flex-col group">
          <h1 className="text-3xl font-bold tracking-tighter text-cello dark:text-peppermint transition-colors">
            ResQ<span className="text-amaranth">Net</span>
          </h1>
          <div className="h-1 w-0 group-hover:w-full bg-aqua-island transition-all duration-300" />
        </Link>
      </div>

      <div className="flex items-center space-x-4 pointer-events-auto">
        <button
          onClick={toggleTheme}
          className="p-2.5 rounded-xl bg-peppermint/80 dark:bg-cello/80 backdrop-blur-md border border-wedgewood/20 text-wedgewood dark:text-aqua-island hover:bg-amaranth hover:text-white transition-all shadow-lg"
          title="Toggle Dark Mode"
        >
          {isDarkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
        </button>

        <Link
          to="/report"
          className="flex items-center space-x-2 px-4 py-2.5 rounded-xl bg-peppermint/80 dark:bg-cello/80 backdrop-blur-md border border-wedgewood/20 text-cello dark:text-peppermint hover:border-amaranth transition-all group shadow-lg"
        >
          <ShieldAlert className="w-5 h-5 text-amaranth" />
          <span className="text-xs font-black uppercase tracking-widest hidden lg:block">Report</span>
        </Link>

        {user ? (
          <div className="flex items-center space-x-2">
            {user.role !== 'CITIZEN' && (
              <Link
                to="/dashboard"
                className="flex items-center space-x-2 px-4 py-2.5 rounded-xl bg-wedgewood text-peppermint hover:bg-cello transition-all shadow-lg"
              >
                <LayoutDashboard className="w-5 h-5" />
                <span className="text-xs font-black uppercase tracking-widest hidden lg:block">Console</span>
              </Link>
            )}

            <div className="relative group">
              <button className="w-11 h-11 rounded-xl bg-aqua-island flex items-center justify-center text-cello border-2 border-wedgewood/20 hover:border-amaranth transition-all shadow-lg overflow-hidden">
                 <User className="w-6 h-6" />
              </button>

              {/* Dropdown Menu */}
              <div className="absolute right-0 mt-2 w-48 bg-white dark:bg-cello-dark rounded-2xl shadow-2xl border border-wedgewood/10 opacity-0 group-hover:opacity-100 pointer-events-none group-hover:pointer-events-auto transition-all p-2 translate-y-2 group-hover:translate-y-0">
                <div className="px-4 py-3 border-b border-wedgewood/10">
                  <p className="text-[10px] font-black text-wedgewood uppercase tracking-widest">Signed in as</p>
                  <p className="text-xs font-bold text-cello dark:text-peppermint truncate mt-1">{user.email || user.phoneNumber}</p>
                  <span className="inline-block mt-1 text-[8px] font-black bg-amaranth/10 text-amaranth px-1.5 py-0.5 rounded uppercase">{user.role}</span>
                </div>
                <button
                  onClick={handleLogout}
                  className="w-full flex items-center space-x-2 px-4 py-3 text-xs font-black text-wedgewood hover:text-amaranth hover:bg-amaranth/5 rounded-xl transition-all uppercase tracking-widest"
                >
                  <LogOut className="w-4 h-4" />
                  <span>Logout</span>
                </button>
              </div>
            </div>
          </div>
        ) : (
          <Link
            to="/login"
            className="flex items-center space-x-2 px-4 py-2.5 rounded-xl bg-wedgewood text-peppermint hover:bg-cello dark:hover:bg-aqua-island dark:hover:text-cello transition-all group shadow-lg"
          >
            <UserCircle className="w-5 h-5" />
            <span className="text-xs font-black uppercase tracking-widest hidden lg:block">Login</span>
          </Link>
        )}
      </div>
    </nav>
  );
}
