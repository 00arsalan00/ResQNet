import React, { createContext, useContext, useState, useEffect } from 'react';
import { authService } from '../services/auth';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Initialize user from localStorage (minimal info, security is in cookies)
  useEffect(() => {
    const savedUser = localStorage.getItem('resqnet_user');
    if (savedUser) {
      setUser(JSON.parse(savedUser));
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    const response = await authService.login(credentials);
    const userData = response.data;
    setUser(userData);
    localStorage.setItem('resqnet_user', JSON.stringify(userData));
    return userData;
  };

  const signup = async (data) => {
    const response = await authService.register(data);
    const userData = response.data;
    setUser(userData);
    localStorage.setItem('resqnet_user', JSON.stringify(userData));
    return userData;
  };

  const logout = async () => {
    try {
      await authService.logout();
    } finally {
      setUser(null);
      localStorage.removeItem('resqnet_user');
    }
  };

  const handleOtpLogin = (userData) => {
    setUser(userData);
    localStorage.setItem('resqnet_user', JSON.stringify(userData));
  };

  return (
    <AuthContext.Provider value={{ user, login, signup, logout, loading, handleOtpLogin }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
