import type { LucideIcon } from "lucide-react";

type ButtonVariant = "primary" | "outline" | "danger";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  label?: string;
  icon?: LucideIcon;
  variant?: ButtonVariant;
  hideLabel?: boolean;
}

export default function Button({
  label,
  icon: Icon,
  variant = "outline",
  hideLabel = false,
  className = "",
  type = "button",
  ...props
}: Readonly<ButtonProps>) {
  const base = `
    flex items-center gap-0 sm:gap-2 px-3 sm:px-4 py-2.5 text-sm font-medium
    rounded-xl font-poppins transition-all duration-150 active:scale-95
    disabled:opacity-50 disabled:pointer-events-none cursor-pointer
  `;

  const variants: Record<ButtonVariant, string> = {
    primary: "bg-button text-white shadow-sm hover:bg-button/90",
    outline:
      "bg-white text-text border border-gray-200 shadow-sm hover:bg-gray-50 hover:border-gray-300",
    danger: "bg-red-500 text-white shadow-sm hover:bg-red-600",
  };

  return (
    <button
      type={type}
      className={`${base} ${variants[variant]} ${className}`}
      {...props}
    >
      {Icon && <Icon size={15} strokeWidth={1.8} />}
      <span className={hideLabel ? "hidden sm:inline" : ""}>{label}</span>
    </button>
  );
}
