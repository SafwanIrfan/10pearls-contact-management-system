import { Eye, EyeOff, Lock } from "lucide-react";

interface PasswordFieldProps {
  label: string;
  value: string;
  error?: string;
  show: boolean;
  onToggle: () => void;
  onChange: (val: string) => void;
}

const PasswordField = ({
  label,
  value,
  error,
  show,
  onToggle,
  onChange,
}: PasswordFieldProps) => (
  <div className="flex flex-col gap-1.5">
    <label className="text-sm font-medium text-heading">{label}</label>
    <div
      className={`flex items-center gap-2 px-3 py-2.5 bg-white border rounded-xl transition-all
      ${
        error
          ? "border-red-300 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
          : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
      }`}
    >
      <Lock size={15} className="text-gray-400 shrink-0" />
      <input
        type={show ? "text" : "password"}
        value={value}
        placeholder="••••••••"
        onChange={(e) => onChange(e.target.value)}
        className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none bg-transparent"
      />
      <button
        type="button"
        onClick={onToggle}
        className="text-gray-400 hover:text-text transition-colors"
      >
        {show ? <EyeOff size={15} /> : <Eye size={15} />}
      </button>
    </div>
    {error && <p className="text-xs text-red-400">{error}</p>}
  </div>
);

export default PasswordField;
