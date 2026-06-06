import { Plus } from "lucide-react";

interface CreateContactSectionProps {
  title: string;
  children: React.ReactNode;
  onAdd: () => void;
  canAdd: boolean;
  addLabel: string;
}

const CreateContactSection = ({
  title,
  children,
  onAdd,
  canAdd,
  addLabel,
}: CreateContactSectionProps) => (
  <div className="flex flex-col gap-3">
    <h2 className="text-sm font-semibold text-heading">{title}</h2>
    {children}
    {canAdd && (
      <button
        type="button"
        onClick={onAdd}
        className="self-start flex items-center gap-1.5 text-sm text-button font-medium hover:underline transition-all"
      >
        <Plus size={14} />
        {addLabel}
      </button>
    )}
  </div>
);

export default CreateContactSection;
