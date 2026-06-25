import axios from 'axios';

const authApi = axios.create({
  baseURL: '/api/auth',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Crucial for cookie-based tokens
});

export const authService = {
  login: (data) => authApi.post('/login', data),
  register: (data) => authApi.post('/register', data),
  logout: () => authApi.post('/logout'),
  refresh: (data) => authApi.post('/refresh', data),
  sendOtp: (data) => authApi.post('/otp/send', data),
  verifyOtp: (data) => authApi.post('/otp/verify', data),
};

export default authApi;
