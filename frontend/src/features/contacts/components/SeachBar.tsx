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
}
