import { useState, useRef, useEffect } from "react";
import { Search, X } from "lucide-react";
import type { SearchBarProps } from "../types/search.type";

export default function SearchBar({
  placeholder = "Search contacts...",
  onSearch,
}: Readonly<SearchBarProps>) {
  const [search, setSearch] = useState("");
  const [focused, setFocused] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleClear = () => {
    setSearch("");
    inputRef.current?.focus();
  };

  useEffect(() => {
    const timeoutId = setTimeout(async () => {
      onSearch(search);
    }, 500);
    return () => clearTimeout(timeoutId);
  }, [search]);

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
          value={search}
          onChange={(e) => setSearch(e.target.value)}
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
            ${search ? "opacity-100 scale-100" : "opacity-0 scale-75 pointer-events-none"}
          `}
          tabIndex={search ? 0 : -1}
          aria-label="Clear search"
        >
          <X size={13} strokeWidth={2.5} />
        </button>
      </div>
      {/* Filter Button
      {showFilter && (
        <Button icon={SlidersHorizontal} label="Filter" onClick={onFilter} />
      )} */}
    </div>
  );
}
