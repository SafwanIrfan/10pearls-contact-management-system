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
    danger:
      "text-text border border-red-200 hover:bg-red-50 hover:border-red-300",
    primary: "hover:bg-gray-50 border border-gray-200 text-text",
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
