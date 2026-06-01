// ─── ChangePasswordModal.tsx ──────────────────────────────────────────────────

import { useState } from "react";
import type { ChangePasswordRequestDTO } from "../types/user";
import Button from "../../../shared/components/Button";
import PasswordField from "./PasswordField";

type ChangePasswordFormErrors = {
  oldPassword?: string;
  newPassword?: string;
  confirmPassword?: string;
};

const EMPTY_FORM: ChangePasswordRequestDTO = {
  oldPassword: "",
  newPassword: "",
  confirmPassword: "",
};

const validate = (form: ChangePasswordRequestDTO): ChangePasswordFormErrors => {
  const errors: ChangePasswordFormErrors = {};
  if (!form.oldPassword.trim())
    errors.oldPassword = "Current password is required.";
  if (!form.newPassword.trim())
    errors.newPassword = "New password is required.";
  else if (form.newPassword.length < 6)
    errors.newPassword = "Password must be at least 6 characters.";
  if (!form.confirmPassword.trim())
    errors.confirmPassword = "Please confirm your new password.";
  else if (form.newPassword !== form.confirmPassword)
    errors.confirmPassword = "Passwords do not match.";
  return errors;
};

interface ChangePasswordModalProps {
  onSubmit: (payload: ChangePasswordRequestDTO) => Promise<void>;
  onCancel: () => void;
  loading: boolean;
}

export const ChangePasswordModal = ({
  onSubmit,
  onCancel,
  loading,
}: ChangePasswordModalProps) => {
  const [form, setForm] = useState<ChangePasswordRequestDTO>(EMPTY_FORM);
  const [errors, setErrors] = useState<ChangePasswordFormErrors>({});
  const [show, setShow] = useState({
    current: false,
    new: false,
    confirm: false,
  });

  const update = (field: keyof ChangePasswordRequestDTO, val: string) => {
    setForm((prev) => ({ ...prev, [field]: val }));
    setErrors((prev) => ({ ...prev, [field]: undefined }));
  };

  const handleSubmit = async () => {
    const validationErrors = validate(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    await onSubmit(form);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div
        className="absolute inset-0 bg-black/30 backdrop-blur-sm"
        onClick={onCancel}
      />
      <div className="relative bg-white rounded-2xl shadow-xl p-6 w-full max-w-sm mx-4 flex flex-col gap-5 font-poppins">
        <div className="flex flex-col gap-1">
          <h3 className="text-base font-semibold text-heading">
            Change Password
          </h3>
          <p className="text-sm text-gray-500">
            Enter your current password and choose a new one.
          </p>
        </div>

        <PasswordField
          label="Current Password"
          value={form.oldPassword}
          error={errors.oldPassword}
          show={show.current}
          onToggle={() => setShow((s) => ({ ...s, current: !s.current }))}
          onChange={(val) => update("oldPassword", val)}
        />
        <PasswordField
          label="New Password"
          value={form.newPassword}
          error={errors.newPassword}
          show={show.new}
          onToggle={() => setShow((s) => ({ ...s, new: !s.new }))}
          onChange={(val) => update("newPassword", val)}
        />
        <PasswordField
          label="Confirm New Password"
          value={form.confirmPassword}
          error={errors.confirmPassword}
          show={show.confirm}
          onToggle={() => setShow((s) => ({ ...s, confirm: !s.confirm }))}
          onChange={(val) => update("confirmPassword", val)}
        />

        <div className="flex items-center justify-end gap-2 pt-1">
          <Button
            type="button"
            label="Cancel"
            variant="outline"
            onClick={onCancel}
            disabled={loading}
          />
          <Button
            type="button"
            label={loading ? "Saving..." : "Reset Password"}
            variant="primary"
            onClick={handleSubmit}
            disabled={loading}
          />
        </div>
      </div>
    </div>
  );
};
