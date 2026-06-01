import { type LucideIcon } from "lucide-react";

interface ActionButtonProps {
  title: string;
  onClick: () => void;
  icon: LucideIcon;
  bgColor?: string;
  variant?: "danger" | "primary";
}

const ActionButton = ({
  title,
  onClick,
  icon: Icon,
  variant = "danger",
}: ActionButtonProps) => {
  const confirmStyles = {
    danger: "bg-red-300 hover:bg-red-200 text-text",
    primary: "hover:bg-gray-50 text-text",
  };
  return (
    <button
      onClick={onClick}
      className={`w-full flex items-center gap-3 px-5 py-4 text-sm font-medium cursor-pointer transition-all rounded-2xl shadow-sm ${confirmStyles[variant]}`}
    >
      {Icon && <Icon size={15} strokeWidth={1.8} />}
      {title}
    </button>
  );
};

export default ActionButton;
