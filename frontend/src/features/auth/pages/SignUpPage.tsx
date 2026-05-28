import { useState } from "react";
import { Link } from "react-router-dom";
import { Lock, Eye, EyeOff, AtSign } from "lucide-react";
import type { SignUpRequestDTO } from "../types/auth";
import { validateIdentifier, validatePassword } from "../utils/validation";
import { useAuth } from "../hooks/useAuth";
import Button from "../../../shared/components/Button";
import appLogo from "./../../../assets/LeadLy.svg";

const EMPTY_FORM: SignUpRequestDTO = {
  identifier: "",
  password: "",
};

interface FormErrors {
  identifier?: string;
  password?: string;
}

export default function SignUpPage() {
  const { handleSignUp, loading, error } = useAuth();

  const [form, setForm] = useState<SignUpRequestDTO>(EMPTY_FORM);
  const [errors, setErrors] = useState<FormErrors>({});
  const [showPass, setShowPass] = useState(false);

  const update = (field: keyof SignUpRequestDTO, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
    setErrors((prev) => ({ ...prev, [field]: undefined }));
  };

  const validate = (form: SignUpRequestDTO): FormErrors => ({
    identifier: validateIdentifier(form.identifier),
    password: validatePassword(form.password, true),
  });

  const handleSubmit = async () => {
    const validationErrors = validate(form);
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }
    await handleSignUp(form);
  };

  return (
    <div className="min-h-screen bg-bg flex items-center justify-center px-4 font-poppins">
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm w-full max-w-md">
        <div className="flex justify-center items-center p-4 bg-secondary rounded-t-2xl">
          <img src={appLogo} alt="appLogo" className="h-8 w-auto" />
        </div>
        <div className="p-8 flex flex-col gap-6">
          {/* Header */}
          <div className="flex flex-col gap-1">
            <h1 className="text-xl font-semibold text-heading">
              Create an account
            </h1>
            <p className="text-sm text-gray-500">
              Fill in the details below to get started.
            </p>
          </div>

          {/* API Error */}
          {error && (
            <div className="px-4 py-3 bg-red-50 border border-red-100 rounded-xl text-sm text-red-500">
              {error}
            </div>
          )}

          {/* Fields */}
          <div className="flex flex-col gap-4">
            {/* Identifier */}
            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-heading">
                Email or Phone number
              </label>
              <div
                className={`flex items-center gap-2 px-3 py-2.5 bg-white border rounded-xl transition-all ${errors.identifier ? "border-red-300 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]" : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"}`}
              >
                <AtSign size={15} className="text-gray-400 shrink-0" />
                <input
                  type="identifier"
                  value={form.identifier}
                  placeholder="Email or phone number"
                  onChange={(e) => update("identifier", e.target.value)}
                  className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none bg-transparent"
                />
              </div>
              {errors.identifier && (
                <p className="text-xs text-red-400">{errors.identifier}</p>
              )}
            </div>

            {/* Password */}
            <div className="flex flex-col gap-1.5">
              <label className="text-sm font-medium text-heading">
                Password
              </label>
              <div
                className={`flex items-center gap-2 px-3 py-2.5 bg-white border rounded-xl transition-all ${errors.password ? "border-red-300 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]" : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"}`}
              >
                <Lock size={15} className="text-gray-400 shrink-0" />
                <input
                  type={showPass ? "text" : "password"}
                  value={form.password}
                  placeholder="••••••••"
                  onChange={(e) => update("password", e.target.value)}
                  className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none bg-transparent"
                />
                <button
                  onClick={() => setShowPass((p) => !p)}
                  className="text-gray-400 hover:text-text transition-colors cursor-pointer"
                >
                  {showPass ? <EyeOff size={15} /> : <Eye size={15} />}
                </button>
              </div>
              {errors.password && (
                <p className="text-xs text-red-400">{errors.password}</p>
              )}
            </div>
          </div>

          {/* Submit */}
          <Button
            label={loading ? "Creating account..." : "Sign Up"}
            variant="primary"
            onClick={handleSubmit}
            disabled={loading}
            className="w-full justify-center"
          />

          {/* Footer */}
          <p className="text-sm text-center text-gray-500">
            Already have an account?{" "}
            <Link
              to="/signin"
              className="text-button font-medium hover:underline"
            >
              Sign in
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
}
