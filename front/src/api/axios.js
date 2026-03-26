import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

// Interceptor para poner el token en el Header "Authorization"
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor para el Refresh Token (usa tu ruta /auth/refresh)
api.interceptors.response.use(
  (res) => res,
  async (err) => {
    const originalRequest = err.config;
    if (err.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const { data } = await api.get('/auth/refresh');
        localStorage.setItem('token', data.jwt);
        return api(originalRequest);
      } catch (e) {
        localStorage.removeItem('token');
        window.location.href = '/login';
        return Promise.reject(e);
      }
    }
    return Promise.reject(err);
  }
);

export default api;