import { jwtDecode } from "jwt-decode";
import api from "../../../services/api";
import type {
  AuthResponseDTO,
  DecodedToken,
  SignInRequestDTO,
  SignUpRequestDTO,
} from "../types/auth";

export const API_ENDPOINTS = {
  SIGNIN: "/auth/login",
  SIGNUP: "/auth/register",
};

export const TOKEN_KEY = "auth_token";

export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const saveToken = (token: string) =>
  localStorage.setItem(TOKEN_KEY, token);
export const removeToken = () => localStorage.removeItem(TOKEN_KEY);

export const signIn = async (
  payload: SignInRequestDTO,
): Promise<AuthResponseDTO> => {
  const endpoint = API_ENDPOINTS.SIGNIN;
  const response = await api.post<AuthResponseDTO>(`${endpoint}`, payload);
  return response.data;
};

export const signUp = async (
  payload: SignUpRequestDTO,
): Promise<AuthResponseDTO> => {
  const endpoint = API_ENDPOINTS.SIGNUP;
  const response = await api.post<AuthResponseDTO>(`${endpoint}`, payload);
  return response.data;
};

export const isTokenExpired = (): boolean => {
  const token = getToken();
  if (!token) return true;
  try {
    const { expiration } = jwtDecode<DecodedToken>(token);
    return Date.now() >= expiration * 1000;
  } catch {
    return true;
  }
};

export const logout = () => {
  removeToken();
  window.location.href = "/auth/signin";
};
