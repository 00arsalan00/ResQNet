import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../store/AuthContext';

export default function ProtectedRoute({ children, roles }) {
  const { user, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return (
      <div className="min-h-screen flex flex-col items-center justify-center bg-peppermint dark:bg-cello">
        <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-amaranth"></div>
        <p className="mt-4 text-[10px] font-black text-wedgewood uppercase tracking-[0.4em]">Verifying Security Credentials...</p>
      </div>
    );
  }

  if (!user) {
    // Redirect to login but save the current location
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  if (roles && !roles.includes(user.role)) {
    // Role not authorized for this route
    return <Navigate to="/" replace />;
  }

  return children;
}
