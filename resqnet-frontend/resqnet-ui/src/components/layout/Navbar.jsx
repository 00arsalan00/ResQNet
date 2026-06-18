import React from 'react';
import { ShieldAlert, UserCircle, Sun, Moon } from 'lucide-react';
import { Link } from 'react-router-dom';
import { useTheme } from '../../store/ThemeContext';

export default function Navbar() {
  const { isDarkMode, toggleTheme } = useTheme();

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
        {/* Theme Toggle with mixed colors */}
        <button
          onClick={toggleTheme}
          className="p-2.5 rounded-xl bg-peppermint/80 dark:bg-cello/80 backdrop-blur-md border border-wedgewood/20 text-wedgewood dark:text-aqua-island hover:bg-amaranth hover:text-white transition-all shadow-lg shadow-cello/5"
          title="Toggle Dark Mode"
        >
          {isDarkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
        </button>

        {/* Report Link */}
        <Link
          to="/report"
          className="flex items-center space-x-2 px-4 py-2.5 rounded-xl bg-peppermint/80 dark:bg-cello/80 backdrop-blur-md border border-wedgewood/20 text-cello dark:text-peppermint hover:border-amaranth transition-all group shadow-lg shadow-cello/5"
        >
          <ShieldAlert className="w-5 h-5 text-amaranth" />
          <span className="text-xs font-black uppercase tracking-widest hidden lg:block">Report</span>
        </Link>

        {/* Account Button */}
        <button
          className="flex items-center space-x-2 px-4 py-2.5 rounded-xl bg-wedgewood text-peppermint hover:bg-cello dark:hover:bg-aqua-island dark:hover:text-cello transition-all group shadow-lg shadow-cello/10"
        >
          <UserCircle className="w-5 h-5" />
          <span className="text-xs font-black uppercase tracking-widest hidden lg:block">Login</span>
        </button>
      </div>
    </nav>
  );
}
