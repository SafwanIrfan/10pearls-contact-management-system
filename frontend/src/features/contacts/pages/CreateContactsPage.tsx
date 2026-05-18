// ─── AddContactPage.tsx ───────────────────────────────────────────────────────

import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Plus, Trash2, ArrowLeft, Mail, Phone } from "lucide-react";
import toast from "react-hot-toast";
import type { ContactRequestDTO, Emails, Phones } from "../types/contact.types";
import { createContact } from "../services/api";
import Button from "../../../shared/components/Button";

// ─── Types ────────────────────────────────────────────────────────────────────

interface FormErrors {
  firstName?: string;
  lastName?: string;
  title?: string;
  emails?: string[];
  phones?: string[];
}

// ─── Constants ────────────────────────────────────────────────────────────────

const EMAIL_LABELS = ["Work", "Personal", "Other"];
const PHONE_LABELS = ["Mobile", "Work", "Home"];
const MAX_ENTRIES = 3;

const EMPTY_EMAIL = (): Emails => ({ email: "", label: "Work" });
const EMPTY_PHONE = (): Phones => ({ phone: "", label: "Mobile" });

// ─── Field Components ─────────────────────────────────────────────────────────

interface InputFieldProps {
  label: string;
  value: string;
  placeholder?: string;
  error?: string;
  onChange: (val: string) => void;
}

const InputField = ({
  label,
  value,
  placeholder,
  error,
  onChange,
}: InputFieldProps) => (
  <div className="flex flex-col gap-1.5">
    <label className="text-sm font-medium text-heading">{label}</label>
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      onChange={(e) => onChange(e.target.value)}
      className={`
        w-full px-3 py-2.5 text-sm rounded-xl border outline-none
        placeholder:text-gray-400 text-text font-poppins
        transition-all duration-150
        ${
          error
            ? "border-red-300 focus:border-red-400 focus:shadow-[0_0_0_3px_rgba(239,68,68,0.1)]"
            : "border-gray-200 focus:border-button focus:shadow-[0_0_0_3px_rgba(52,104,105,0.12)]"
        }
      `}
    />
    {error && <p className="text-xs text-red-400">{error}</p>}
  </div>
);

// ─── Dynamic Entry Row (Email or Phone) ───────────────────────────────────────

// ─── Section Wrapper ──────────────────────────────────────────────────────────

interface SectionProps {
  title: string;
  children: React.ReactNode;
  onAdd: () => void;
  canAdd: boolean;
  addLabel: string;
}

const Section = ({
  title,
  children,
  onAdd,
  canAdd,
  addLabel,
}: SectionProps) => (
  <div className="flex flex-col gap-3">
    <h2 className="text-sm font-semibold text-heading">{title}</h2>
    {children}
    {canAdd && (
      <button
        onClick={onAdd}
        className="self-start flex items-center gap-1.5 text-sm text-button font-medium hover:underline transition-all"
      >
        <Plus size={14} />
        {addLabel}
      </button>
    )}
  </div>
);

// ─── Validation ───────────────────────────────────────────────────────────────

const validate = (
  firstName: string,
  lastName: string,
  title: string,
  emails: Emails[],
  phones: Phones[],
): FormErrors => {
  const errors: FormErrors = {};

  if (!firstName.trim()) errors.firstName = "First name is required.";
  if (!lastName.trim()) errors.lastName = "Last name is required.";
  if (!title.trim()) errors.title = "Title is required.";

  const emailErrors = emails.map((e) => {
    if (!e.email.trim()) return "Email is required.";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(e.email))
      return "Enter a valid email.";
    return "";
  });
  if (emailErrors.some(Boolean)) errors.emails = emailErrors;

  const phoneErrors = phones.map((p) => {
    if (!p.phone.trim()) return "Phone is required.";
    if (!/^\+?[\d\s\-()]{7,15}$/.test(p.phone))
      return "Enter a valid phone number.";
    return "";
  });
  if (phoneErrors.some(Boolean)) errors.phones = phoneErrors;

  return errors;
};

// ─── Page ─────────────────────────────────────────────────────────────────────

export default function CreateContactPage() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [title, setTitle] = useState("");
  const [emails, setEmails] = useState<Emails[]>([EMPTY_EMAIL()]);
  const [phones, setPhones] = useState<Phones[]>([EMPTY_PHONE()]);
  const [errors, setErrors] = useState<FormErrors>({});
  const [submitting, setSubmitting] = useState(false);

  // ── Email helpers ──
  const updateEmail = (i: number, field: keyof Emails, val: string) =>
    setEmails((prev) =>
      prev.map((e, idx) => (idx === i ? { ...e, [field]: val } : e)),
    );

  const addEmail = () => setEmails((prev) => [...prev, EMPTY_EMAIL()]);

  const removeEmail = (i: number) =>
    setEmails((prev) => prev.filter((_, idx) => idx !== i));

  // ── Phone helpers ──
  const updatePhone = (i: number, field: keyof Phones, val: string) =>
    setPhones((prev) =>
      prev.map((p, idx) => (idx === i ? { ...p, [field]: val } : p)),
    );

  const addPhone = () => setPhones((prev) => [...prev, EMPTY_PHONE()]);

  const removePhone = (i: number) =>
    setPhones((prev) => prev.filter((_, idx) => idx !== i));

  // ── Submit ──
  const handleSubmit = async () => {
    const validationErrors = validate(
      firstName,
      lastName,
      title,
      emails,
      phones,
    );

    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      toast.error("Please fix the errors before submitting.");
      return;
    }

    const payload: ContactRequestDTO = {
      firstName,
      lastName,
      title,
      emails,
      phones,
    };

    try {
      setSubmitting(true);
      await createContact(payload);
      toast.success("Contact added successfully!");
      navigate(-1);
    } catch {
      toast.error("Something went wrong. Please try again.");
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="h-full flex flex-col px-4 py-6 font-poppins">
      {/* Header */}
      <div className="flex items-center gap-3 mb-6">
        <button
          onClick={() => navigate(-1)}
          className="p-2 text-gray-500 hover:text-text hover:bg-gray-100 rounded-xl transition-all"
        >
          <ArrowLeft size={18} />
        </button>
        <div>
          <h1 className="text-xl font-semibold text-heading">Add Contact</h1>
          <p className="text-xs text-gray-500 mt-0.5">
            Fill in the details below to create a new contact.
          </p>
        </div>
      </div>

      {/* Form Card */}
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-6 flex flex-col flex-1 overflow-hidden">
        {/* Basic Info */}
        <div className="flex flex-col gap-6 overflow-y-auto flex-1 pr-1">
          <div className="flex flex-col gap-4">
            <h2 className="text-sm font-semibold text-heading">Basic Info</h2>
            <div className="grid grid-cols-2 gap-3">
              <InputField
                label="First Name"
                value={firstName}
                placeholder="John"
                error={errors.firstName}
                onChange={(val) => {
                  setFirstName(val);
                  setErrors((e) => ({ ...e, firstName: undefined }));
                }}
              />
              <InputField
                label="Last Name"
                value={lastName}
                placeholder="Doe"
                error={errors.lastName}
                onChange={(val) => {
                  setLastName(val);
                  setErrors((e) => ({ ...e, lastName: undefined }));
                }}
              />
            </div>
            <InputField
              label="Title"
              value={title}
              placeholder="e.g. Software Engineer"
              error={errors.title}
              onChange={(val) => {
                setTitle(val);
                setErrors((e) => ({ ...e, title: undefined }));
              }}
            />
          </div>

          <hr className="border-gray-100" />
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center justify-end gap-2 pt-4 border-t border-gray-100 mt-4">
        <Button label="Cancel" variant="outline" onClick={() => navigate(-1)} />
        <Button
          label={submitting ? "Saving..." : "Save Contact"}
          variant="primary"
          onClick={handleSubmit}
          disabled={submitting}
        />
      </div>
    </div>
  );
}
