export const isEmail = (val: string) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val);
export const isPhone = (val: string) => /^\+?[\d]{7,15}$/.test(val);

export const validateIdentifier = (val: string): string | undefined => {
  if (!val.trim()) return "Email or phone number is required.";
  if (!isEmail(val) && !isPhone(val))
    return "Enter a valid email or phone number.";
};

export const validatePassword = (
  val: string,
  requireStrength = false,
): string | undefined => {
  if (!val.trim()) return "Password is required.";
  if (requireStrength && val.length < 6)
    return "Password must be at least 6 characters.";
};
