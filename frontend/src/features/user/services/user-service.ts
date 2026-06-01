import { jwtDecode } from "jwt-decode";
import type { ChangePasswordRequestDTO, DecodedUser } from "../types/user";
import { getToken } from "../../auth/services/authService";
import api from "../../../services/api";

export const API_USER_ENDPOINTS = {
  CHANGE_PASSWORD: "/auth/password/update",
};

export const getCurrentUser = (): DecodedUser | null => {
  const token = getToken();
  if (!token) return null;
  try {
    return jwtDecode<DecodedUser>(token);
  } catch {
    return null;
  }
};

export const changePassword = async (payload: ChangePasswordRequestDTO) => {
  const response = await api.put(API_USER_ENDPOINTS.CHANGE_PASSWORD, {
    oldPassword: payload.oldPassword,
    newPassword: payload.newPassword,
  });
  return response.data;
};
