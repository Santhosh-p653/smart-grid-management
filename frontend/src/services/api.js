import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to attach JWT token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle token expiry / errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      if (window.location.pathname !== '/login') {
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export const authService = {
  login: async (username, password) => {
    const response = await api.post('/auth/login', { username, password });
    if (response.data && response.data.token) {
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },
  logout: () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
  },
  getCurrentUser: () => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },
};

export const gridService = {
  getNodes: () => api.get('/grid'),
  getNode: (id) => api.get(`/grid/${id}`),
  createNode: (data) => api.post('/grid', data),
  updateNode: (id, data) => api.put(`/grid/${id}`, data),
  deleteNode: (id) => api.delete(`/grid/${id}`),
  getTelemetries: (nodeId) => api.get(`/power/node/${nodeId}`),
  addTelemetry: (nodeId, data) => api.post(`/power/node/${nodeId}`, data),
};

export const faultService = {
  getFaults: () => api.get('/faults'),
  reportFault: (data) => api.post('/faults', data),
  resolveFault: (id) => api.put(`/faults/${id}/resolve`),
  getOutages: () => api.get('/outages'),
  reportOutage: (data) => api.post('/outages', data),
  resolveOutage: (id) => api.put(`/outages/${id}/restore`),
};

export const analyticsService = {
  getDashboardStats: () => api.get('/analytics/dashboard'),
  getReports: () => api.get('/analytics/reports'),
  getReport: (id) => api.get(`/analytics/reports/${id}`),
  generateReport: (type) => api.post(`/analytics/reports/generate?type=${type}`),
};

export const consumerService = {
  getConsumers: () => api.get('/consumers'),
  createConsumer: (data) => api.post('/consumers', data),
};

export default api;
