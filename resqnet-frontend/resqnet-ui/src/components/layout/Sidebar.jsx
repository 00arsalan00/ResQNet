import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  LayoutDashboard,
  AlertTriangle,
  Users,
  Shield,
  Package,
  Map as MapIcon,
  Settings,
  Menu
} from 'lucide-react';
import { clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

function cn(...inputs) {
  return twMerge(clsx(inputs));
}

const navItems = [
  { name: 'Dashboard', icon: LayoutDashboard, path: '/' },
  { name: 'Incidents', icon: AlertTriangle, path: '/incidents' },
  { name: 'Rescue Teams', icon: Shield, path: '/teams' },
  { name: 'Volunteers', icon: Users, path: '/volunteers' },
  { name: 'Resources', icon: Package, path: '/resources' },
  { name: 'Relief Camps', icon: MapIcon, path: '/camps' },
];

export default function Sidebar({ isOpen, setIsOpen }) {
  return (
    <aside
      className={cn(
        "fixed left-0 top-0 h-full z-40 w-64 bg-cello transition-transform duration-300 ease-in-out transform",
        !isOpen && "-translate-x-full lg:translate-x-0"
      )}
    >
      <div className="flex flex-col h-full">
        {/* Logo */}
        <div className="flex items-center justify-center h-16 bg-cello-dark">
          <span className="text- pepperminit font-bold text-2xl tracking-wider text-peppermint">
            ResQ<span className="text-amaranth">Net</span>
          </span>
        </div>

        {/* Navigation */}
        <nav className="flex-1 px-4 py-6 space-y-2 overflow-y-auto">
          {navItems.map((item) => (
            <NavLink
              key={item.name}
              to={item.path}
              className={({ isActive }) =>
                cn(
                  "flex items-center px-4 py-3 rounded-lg transition-colors duration-200 group",
                  isActive
                    ? "bg-wedgewood text-peppermint"
                    : "text-aqua-island hover:bg-wedgewood/20 hover:text-peppermint"
                )
              }
            >
              <item.icon className="w-5 h-5 mr-3" />
              <span className="font-medium">{item.name}</span>
            </NavLink>
          ))}
        </nav>

        {/* Bottom Actions */}
        <div className="px-4 py-6 border-t border-wedgewood/30">
          <NavLink
            to="/settings"
            className="flex items-center px-4 py-3 text-aqua-island rounded-lg hover:bg-wedgewood/20 hover:text-peppermint transition-all"
          >
            <Settings className="w-5 h-5 mr-3" />
            <span className="font-medium">Settings</span>
          </NavLink>
        </div>
      </div>
    </aside>
  );
}
