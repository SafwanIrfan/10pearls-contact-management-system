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
      const status = err?.response?.status;
      const data = err?.response?.data;
      const message =
        typeof data === "string" ? data : (data?.message ?? data?.error);
      console.log(message);
      if (
        status === 400 ||
        status === 401 ||
        message === "Invalid Credentials"
      ) {
        setError(message ?? "Invalid email or password.");
      } else if (!status) {
        toast.error(
          "Unable to connect. Please check your internet connection.",
        );
      } else {
        toast.error(message ?? "Something went wrong. Please try again.");
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
      const status = err?.response?.status;
      const data = err?.response?.data;
      const message =
        typeof data === "string"
          ? data
          : (data?.message ?? (Object.values(data ?? {})[0] as string));
      if (status === 400 || status === 401) {
        setError(message ?? "Registration failed! Please check your details.");
      } else if (!status) {
        toast.error(
          "Unable to connect. Please check your internet connection.",
        );
      } else {
        toast.error(message ?? "Something went wrong. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return { handleSignIn, handleSignUp, loading, error };
};
