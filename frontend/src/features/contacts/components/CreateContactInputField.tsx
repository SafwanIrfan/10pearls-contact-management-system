import type { InputFieldProps } from "../types/contact-form-input-fields.types";

const CreateContactInputField = ({
  label,
  value,
  placeholder,
  error,
  onChange,
  type,
}: InputFieldProps) => (
  <div className="flex flex-col gap-1.5">
    <label className="text-sm font-medium text-heading">{label}</label>
    <input
      type={type || "text"}
      value={value}
      placeholder={placeholder}
      onChange={(e) => onChange(e.target.value)}
      className={`
        w-full px-3 py-2.5 text-sm rounded-xl border outline-none
        placeholder:text-gray-400 text-text font-poppins
        transition-all duration-150
        ${
          error
            ? "border-red-300 focus:border-red-400 focus:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
            : "border-gray-200 focus:border-button focus:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
        }
      `}
    />
    {error && <p className="text-xs text-red-400">{error}</p>}
  </div>
);

export default CreateContactInputField;
