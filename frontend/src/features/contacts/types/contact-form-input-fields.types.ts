export interface InputFieldProps {
  label: string;
  value: string;
  placeholder?: string;
  error?: string;
  onChange: (val: string) => void;
  type?: string;
}
