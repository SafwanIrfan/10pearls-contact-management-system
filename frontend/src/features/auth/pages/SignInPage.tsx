import { useState } from "react";
import { Link } from "react-router-dom";
import { Lock, Eye, EyeOff, AtSign } from "lucide-react";
import type { SignInRequestDTO } from "../types/auth";
import { useAuth } from "../hooks/useAuth";
import Button from "../../../shared/components/Button";
import appLogo from "./../../../assets/LeadLy.svg";
import { useAuthForm } from "../hooks/useAuthForm";

const EMPTY_FORM: SignInRequestDTO = { identifier: "", password: "" };

export default function SignInPage() {
  const { handleSignIn, loading, error } = useAuth();
  const { form, errors, update, validate } =
    useAuthForm<SignInRequestDTO>(EMPTY_FORM);

  const [showPass, setShowPass] = useState(false);

  const handleSubmit = async () => {
    if (!validate()) return;
    await handleSignIn(form);
  };

  return (
    <div className="min-h-screen bg-bg flex items-center justify-center px-4 font-poppins">
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm w-full max-w-md">
        <div className="flex justify-center items-center p-4 bg-secondary rounded-t-2xl">
          <img src={appLogo} alt="appLogo" className="h-8 w-auto" />
        </div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSubmit();
          }}
          className="p-8 flex flex-col gap-6"
        >
          {/* Header */}
          <div className="flex flex-col gap-1">
            <h1 className="text-xl font-semibold text-heading">Welcome back</h1>
            <p className="text-sm text-gray-500">
              Sign in to your account to continue.
            </p>
          </div>

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
                  type="text"
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
                  type="button"
                  onClick={() => setShowPass((pass) => !pass)}
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

          <Button
            type="submit"
            label={loading ? "Signing in..." : "Sign In"}
            variant="primary"
            onClick={handleSubmit}
            disabled={loading}
            className="w-full justify-center"
          />

          <p className="text-sm text-center text-gray-500">
            Don't have an account?{" "}
            <Link
              to="/auth/signup"
              className="text-button font-medium hover:underline"
            >
              Sign up
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
