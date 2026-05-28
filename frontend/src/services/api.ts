import axios from "axios";
import { logout } from "../features/auth/services/authService";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const isAuthRoute = error.config?.url?.includes("/auth/");
    if (error.response?.status === 401 && !isAuthRoute) {
      logout();
    }
    return Promise.reject(error);
  },
);

export default api;
