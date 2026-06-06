import { useState } from "react";
import { useNavigate } from "react-router-dom";
import type { SignInRequestDTO, SignUpRequestDTO } from "../types/auth";
import { saveToken, signIn, signUp } from "../services/authService";
import toast from "react-hot-toast";

export const useAuth = () => {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSignIn = async (payload: SignInRequestDTO) => {
    try {
      setLoading(true);
      setError(null);
      const { token } = await signIn(payload);
      saveToken(token);
      toast.success("Welcome back to Leadly!");
      navigate("/");
    } catch (err: any) {
      const message = err?.response?.data?.message;
      if (err?.response?.status === 400 || err?.response?.status === 401) {
        setError(message ?? "Invalid email or password."); // inline form error
      } else {
        toast.error(message ?? "Something went wrong. Please try again."); // unexpected error
      }
    } finally {
      setLoading(false);
    }
  };

  const handleSignUp = async (payload: SignUpRequestDTO) => {
    try {
      setLoading(true);
      setError(null);
      const { token } = await signUp(payload);
      saveToken(token);
      navigate("/");
      toast.success("Welcome to Leadly! Let's get started.");
    } catch (err: any) {
      const message = err?.response?.data?.message;
      if (err?.response?.status === 400 || err?.response?.status === 401) {
        setError(message ?? "Registration failed. Please check your details.");
      } else {
        toast.error(message ?? "Something went wrong. Please try again."); // unexpected error
      }
    } finally {
      setLoading(false);
    }
  };

  return { handleSignIn, handleSignUp, loading, error };
};
