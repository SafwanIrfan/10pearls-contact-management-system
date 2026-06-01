import { useState } from "react";
import toast from "react-hot-toast";
import { changePassword, getCurrentUser } from "../services/user-service";
import type { ChangePasswordRequestDTO } from "../types/user";

export const useProfile = () => {
  const user = getCurrentUser();

  const [loading, setLoading] = useState(false);

  const handleChangePassword = async (
    payload: ChangePasswordRequestDTO,
    onSuccess: () => void,
  ) => {
    try {
      setLoading(true);
      await changePassword(payload);
      toast.success("Password changed successfully.");
      onSuccess();
    } catch (err: any) {
      const message = err?.response?.data?.message;
      if (err?.response?.status === 400 || err?.response?.status === 401) {
        toast.error(message ?? "Current password is incorrect.");
      } else {
        toast.error(message ?? "Something went wrong. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  return { user, loading, handleChangePassword };
};
