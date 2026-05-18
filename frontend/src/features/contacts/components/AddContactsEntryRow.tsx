import { Trash2 } from "lucide-react";

interface AddContactsEntryRowProps {
  icon: React.ReactNode;
  value: string;
  label: string;
  labels: string[];
  placeholder: string;
  error?: string;
  onValueChange: (val: string) => void;
  onLabelChange: (val: string) => void;
  onRemove: () => void;
  showRemove: boolean;
}

const AddContactsEntryRow = ({
  icon,
  value,
  label,
  labels,
  placeholder,
  error,
  onValueChange,
  onLabelChange,
  onRemove,
  showRemove,
}: AddContactsEntryRowProps) => (
  <div className="flex flex-col gap-1">
    <div className="flex items-center gap-2">
      <div
        className={`
          flex items-center gap-2 flex-1 px-3 py-2.5
          bg-white border rounded-xl transition-all duration-150
          ${
            error
              ? "border-red-300 focus-within:border-red-400 focus-within:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
              : "border-gray-200 focus-within:border-button focus-within:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
          }
        `}
      >
        <span className="text-gray-400 shrink-0">{icon}</span>
        <input
          type="text"
          value={value}
          placeholder={placeholder}
          onChange={(e) => onValueChange(e.target.value)}
          className="flex-1 text-sm text-text placeholder:text-gray-400 outline-none font-poppins bg-transparent"
        />
      </div>

      <select
        value={label}
        onChange={(e) => onLabelChange(e.target.value)}
        className="px-2 py-2.5 text-sm text-text bg-white border border-gray-200 rounded-xl outline-none focus:border-button cursor-pointer font-poppins transition-all"
      >
        {labels.map((l) => (
          <option key={l} value={l}>
            {l}
          </option>
        ))}
      </select>

      {showRemove && (
        <button
          onClick={onRemove}
          className="p-2 text-gray-400 hover:text-red-400 hover:bg-red-50 rounded-xl transition-all"
        >
          <Trash2 size={15} />
        </button>
      )}
    </div>
    {error && <p className="text-xs text-red-400 pl-1">{error}</p>}
  </div>
);

export default AddContactsEntryRow;
