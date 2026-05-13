export interface SearchBarProps {
  placeholder?: string;
  onSearch?: (value: string) => void;
  onFilter?: () => void;
  showFilter?: boolean;
}
