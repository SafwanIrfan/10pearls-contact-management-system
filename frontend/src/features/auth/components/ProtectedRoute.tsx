import { Outlet } from "react-router-dom";
import { getToken, isTokenExpired, logout } from "../services/authService";

export const ProtectedRoute = () => {
  if (!getToken() || isTokenExpired()) {
    logout();
    return null;
  }

  return <Outlet />;
};
