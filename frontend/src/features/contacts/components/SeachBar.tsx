import { useState, useRef } from "react";
import { Search, X, SlidersHorizontal } from "lucide-react";
import type { SearchBarProps } from "../types/search.type";

export default function SearchBar({
  placeholder = "Search contacts...",
  onSearch,
  onFilter,
  showFilter = true,
}: SearchBarProps) {
  const [value, setValue] = useState("");
  const [focused, setFocused] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValue(e.target.value);
    onSearch?.(e.target.value);
  };

  const handleClear = () => {
    setValue("");
    onSearch?.("");
    inputRef.current?.focus();
  };

  return (
    <div className="flex items-center gap-2 w-full max-w-md mt-10 font-poppins">
      {/* Search Input */}
      <div
        className={`
          relative flex items-center flex-1 gap-2
          bg-white border rounded-xl px-3 py-2.5
          transition-all duration-200 ease-in-out
          ${
            focused
              ? "border-button shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
              : "border-gray-200 shadow-sm hover:border-gray-300"
          }
        `}
      >
        {/* Search Icon */}
        <Search
          size={15}
          strokeWidth={2}
          className={`shrink-0 transition-colors duration-200 ${
            focused ? "text-button" : "text-gray-400"
          }`}
        />

        {/* Input */}
        <input
          ref={inputRef}
          type="text"
          value={value}
          onChange={handleChange}
          onFocus={() => setFocused(true)}
          onBlur={() => setFocused(false)}
          placeholder={placeholder}
          className="
            flex-1 bg-transparent text-sm text-text
            placeholder:text-gray-400 outline-none
            font-poppins min-w-0
          "
        />

        {/* Clear Button */}
        <button
          onClick={handleClear}
          className={`
            shrink-0 p-0.5 rounded-full transition-all duration-150
            text-gray-400 hover:text-text hover:bg-gray-100
            ${value ? "opacity-100 scale-100" : "opacity-0 scale-75 pointer-events-none"}
          `}
          tabIndex={value ? 0 : -1}
          aria-label="Clear search"
        >
          <X size={13} strokeWidth={2.5} />
        </button>
      </div>
      {/* Filter Button */}
      {showFilter && (
        <button
          onClick={onFilter}
          className="
            flex items-center gap-2 px-3 py-2.5
            text-sm font-medium text-text
            bg-white border border-gray-200 rounded-xl
            shadow-sm hover:border-gray-300 hover:bg-gray-50
            active:scale-95 transition-all duration-150
          "
        >
          <SlidersHorizontal size={15} strokeWidth={1.8} />
          <span className="hidden sm:inline">Filter</span>
        </button>
      )}
    </div>
  );
}
