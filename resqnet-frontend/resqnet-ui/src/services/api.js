import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export const incidentService = {
  register: (data) => api.post('/incidents', data),
  getAll: (page = 0, size = 10) => api.get(`/incidents?page=${page}&size=${size}`),
  getById: (id) => api.get(`/incidents/${id}`),
};

export const teamService = {
  getAll: () => api.get('/rescue-teams'),
};

export const resourceService = {
  getAll: () => api.get('/resources'),
};

export const volunteerService = {
  getAll: () => api.get('/volunteers'),
};

export default api;
