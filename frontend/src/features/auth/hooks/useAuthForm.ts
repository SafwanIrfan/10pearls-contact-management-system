import { useState } from "react";
import { validateIdentifier, validatePassword } from "../utils/validation";

interface AuthForm {
  identifier: string;
  password: string;
}

interface AuthFormErrors {
  identifier?: string;
  password?: string;
}

export const useAuthForm = <T extends AuthForm>(initialForm: T) => {
  const [form, setForm] = useState<T>(initialForm);
  const [errors, setErrors] = useState<AuthFormErrors>({});

  const update = (field: keyof T, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
    setErrors((prev) => ({ ...prev, [field]: undefined }));
  };

  const validate = (): boolean => {
    const newErrors: AuthFormErrors = {};
    const identifierError = validateIdentifier(form.identifier);
    const passwordError = validatePassword(form.password);
    if (identifierError) newErrors.identifier = identifierError;
    if (passwordError) newErrors.password = passwordError;

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  return { form, errors, update, validate };
};
