import { useState } from "react";
import { AtSign, Lock, Eye, EyeOff } from "lucide-react";

interface AuthFieldsProps {
  identifier: string;
  password: string;
  errors: { identifier?: string; password?: string };
  onIdentifierChange: (val: string) => void;
  onPasswordChange: (val: string) => void;
}

const AuthFields = ({
  identifier,
  password,
  errors,
  onIdentifierChange,
  onPasswordChange,
}: AuthFieldsProps) => {
  const [showPass, setShowPass] = useState(false);

  return (
    <div className="flex flex-col gap-4">
      {/* Identifier */}
      <div className="flex flex-col gap-1.5">
        <label
          htmlFor="identifier"
          className="text-sm font-medium text-heading"
        >
          Email or Phone number
        </label>
        <div
          className={`flex items-center gap-2 px-3 py-2.5 bg-white border rounded-xl transition-all
          ${
            errors.identifier
              ? "border-red-300 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
              : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
          }`}
        >
          <AtSign size={15} className="text-gray-400 shrink-0" />
          <input
            id="identifier"
            type="text"
            value={identifier}
            placeholder="Email or phone number"
            onChange={(e) => onIdentifierChange(e.target.value)}
            className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none bg-transparent"
          />
        </div>
        {errors.identifier && (
          <p className="text-xs text-red-400">{errors.identifier}</p>
        )}
      </div>

      {/* Password */}
      <div className="flex flex-col gap-1.5">
        <label htmlFor="password" className="text-sm font-medium text-heading">
          Password
        </label>
        <div
          className={`flex items-center gap-2 px-3 py-2.5 bg-white border rounded-xl transition-all
          ${
            errors.password
              ? "border-red-300 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
              : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
          }`}
        >
          <Lock size={15} className="text-gray-400 shrink-0" />
          <input
            id="password"
            type={showPass ? "text" : "password"}
            value={password}
            placeholder="••••••••"
            onChange={(e) => onPasswordChange(e.target.value)}
            className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none bg-transparent"
          />
          <button
            type="button"
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
  );
};

export default AuthFields;
