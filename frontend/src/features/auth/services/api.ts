import api from "../../../services/api";
import type {
  AuthResponseDTO,
  SignInRequestDTO,
  SignUpRequestDTO,
} from "../types/auth";

export const API_ENDPOINTS = {
  SIGNIN: "/auth/signin",
  SIGNUP: "/auth/signup",
};

export const TOKEN_KEY = "auth_token";

export const getToken = () => localStorage.getItem(TOKEN_KEY);
export const saveToken = (token: string) =>
  localStorage.setItem(TOKEN_KEY, token);
export const removeToken = () => localStorage.removeItem(TOKEN_KEY);

export const signIn = async (
  payload: SignInRequestDTO,
): Promise<AuthResponseDTO> => {
  const response = await api.post<AuthResponseDTO>(
    API_ENDPOINTS.SIGNIN,
    payload,
  );
  return response.data;
};

export const signUp = async (
  payload: SignUpRequestDTO,
): Promise<AuthResponseDTO> => {
  const response = await api.post<AuthResponseDTO>(
    API_ENDPOINTS.SIGNUP,
    payload,
  );
  return response.data;
};
