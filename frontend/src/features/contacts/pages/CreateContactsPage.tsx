// ─── AddContactPage.tsx ───────────────────────────────────────────────────────

import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Mail, Phone } from "lucide-react";
import toast from "react-hot-toast";
import type { ContactRequestDTO, Emails, Phones } from "../types/contact.types";
import { createContact, updateContact } from "../services/api";
import Button from "../../../shared/components/Button";
import AddContactsEntryRow from "../components/CreateContactsEntryRow";
import CreateContactSection from "../components/CreateContactSection";
import type { FormErrors } from "../types/contact-form-errors.types";
import CreateContactInputField from "../components/CreateContactInputField";
import { useContactById } from "../hooks/useContactById";
import { Spinner } from "../../../shared/components/Spinner";
import { isEmail, isPhone } from "../../auth/utils/validation";
import { ConfirmationModal } from "../../../shared/components/ConfirmationModal";

// Constants
const EMAIL_LABELS = ["Work", "Personal", "Other"];
const PHONE_LABELS = ["Mobile", "Work", "Home"];
const MAX_ENTRIES = 3;

const EMPTY_EMAIL = (): Emails => ({ email: "", label: "Work" });
const EMPTY_PHONE = (): Phones => ({ phone: "", label: "Mobile" });

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
    if (!isEmail(e.email)) return "Enter a valid email.";
    return "";
  });
  if (emailErrors.some(Boolean)) errors.emails = emailErrors;

  const phoneErrors = phones.map((p) => {
    if (!p.phone.trim()) return "Phone is required.";
    if (!isPhone(p.phone)) return "Enter a valid phone number.";
    return "";
  });
  if (phoneErrors.some(Boolean)) errors.phones = phoneErrors;

  return errors;
};

// Main

export default function CreateContactPage() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();

  const isEditMode = Boolean(id);
  const contactId = Number(id);

  const { contact, loading: contactLoading } = useContactById(
    isEditMode ? contactId : null,
  );

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [title, setTitle] = useState("");
  const [emails, setEmails] = useState<Emails[]>([EMPTY_EMAIL()]);
  const [phones, setPhones] = useState<Phones[]>([EMPTY_PHONE()]);
  const [errors, setErrors] = useState<FormErrors>({});
  const [submitting, setSubmitting] = useState(false);
  const [isConfirmationModalOpen, setIsConfirmationModalOpen] = useState(false);

  useEffect(() => {
    if (contact) {
      setFirstName(contact.firstName);
      setLastName(contact.lastName);
      setTitle(contact.title ?? "");
      setEmails(contact.emails?.length > 0 ? contact.emails : [EMPTY_EMAIL()]);
      setPhones(contact.phones?.length > 0 ? contact.phones : [EMPTY_PHONE()]);
    }
  }, [contact]);

  const submitLabel = submitting
    ? "Saving..."
    : isEditMode
      ? "Update Contact"
      : "Save Contact";

  // Email helpers
  const updateEmail = (i: number, field: keyof Emails, val: string) =>
    setEmails((prev) =>
      prev.map((e, idx) => (idx === i ? { ...e, [field]: val } : e)),
    );

  const addEmail = () => setEmails((prev) => [...prev, EMPTY_EMAIL()]);

  const removeEmail = (i: number) =>
    setEmails((prev) => prev.filter((_, idx) => idx !== i));

  // Phone helpers
  const updatePhone = (i: number, field: keyof Phones, val: string) =>
    setPhones((prev) =>
      prev.map((p, idx) => (idx === i ? { ...p, [field]: val } : p)),
    );

  const addPhone = () => setPhones((prev) => [...prev, EMPTY_PHONE()]);

  const removePhone = (i: number) =>
    setPhones((prev) => prev.filter((_, idx) => idx !== i));

  const handleSubmit = async () => {
    const payload: ContactRequestDTO = {
      firstName,
      lastName,
      title,
      emails,
      phones,
    };

    try {
      setSubmitting(true);
      if (isEditMode) {
        await updateContact(contactId, payload);
        toast.success("Contact updated successfully!");
      } else {
        await createContact(payload);
        toast.success("Contact added successfully!");
      }
      navigate(-1);
    } catch {
      toast.error("Something went wrong. Please try again.");
    } finally {
      setSubmitting(false);
    }
  };

  if (isEditMode && contactLoading)
    return (
      <div className="flex justify-center">
        <Spinner size={24} />
      </div>
    );

  return (
    <div className="h-full flex flex-col px-4 py-6 font-poppins">
      {isConfirmationModalOpen && (
        <ConfirmationModal
          title={isEditMode ? "Update Contact" : "Add Contact"}
          message={
            isEditMode
              ? "Are you sure you want to update this contact?"
              : "Are you sure you want to add this contact?"
          }
          confirmLabel={isEditMode ? "Update" : "Add"}
          variant="primary"
          loading={submitting}
          onConfirm={handleSubmit}
          onCancel={() => setIsConfirmationModalOpen(false)}
        />
      )}

      {/* Header */}
      <div className="flex items-center gap-3 mb-6">
        <button
          onClick={() => navigate(-1)}
          className="p-2 text-gray-500 hover:text-text hover:bg-gray-100 cursor-pointer rounded-xl transition-all"
        >
          <ArrowLeft size={18} />
        </button>
        <div>
          {/* Header */}
          <h1 className="text-xl font-semibold text-heading">
            {isEditMode ? "Edit Contact" : "Add Contact"}
          </h1>
          <p className="text-xs text-gray-500 mt-0.5">
            {isEditMode
              ? "Update the contact details below."
              : "Fill in the details below to create a new contact."}
          </p>
        </div>
      </div>

      {/* Form Card */}
      <form
        onSubmit={(e) => {
          e.preventDefault();
          e.preventDefault();
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
          setIsConfirmationModalOpen(true);
        }}
        className="bg-white border border-gray-100 rounded-2xl shadow-sm p-6 flex flex-col flex-1 overflow-hidden"
      >
        <div className="flex flex-col gap-6 overflow-y-auto flex-1 pr-1">
          <div className="flex flex-col gap-4">
            <h2 className="text-md sm:text-xl font-semibold text-heading">
              Basic Info
            </h2>
            <div className="grid grid-cols-2 gap-3">
              <CreateContactInputField
                label="First Name"
                value={firstName}
                placeholder="Safwan"
                error={errors.firstName}
                onChange={(val) => {
                  setFirstName(val);
                  setErrors((e) => ({ ...e, firstName: undefined }));
                }}
              />
              <CreateContactInputField
                label="Last Name"
                value={lastName}
                placeholder="Irfan"
                error={errors.lastName}
                onChange={(val) => {
                  setLastName(val);
                  setErrors((e) => ({ ...e, lastName: undefined }));
                }}
              />
            </div>
            <CreateContactInputField
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

          {/* Emails */}
          <CreateContactSection
            title="Email Addresses"
            onAdd={addEmail}
            canAdd={emails.length < MAX_ENTRIES}
            addLabel="Add another email"
          >
            {emails.map((e, i) => (
              <AddContactsEntryRow
                key={i}
                icon={<Mail size={14} />}
                value={e.email}
                label={e.label}
                labels={EMAIL_LABELS}
                placeholder="safwan@example.com"
                error={errors.emails?.[i]}
                onValueChange={(val) => {
                  updateEmail(i, "email", val);
                  setErrors((err) => ({ ...err, emails: undefined }));
                }}
                onLabelChange={(val) => updateEmail(i, "label", val)}
                onRemove={() => removeEmail(i)}
                showRemove={emails.length > 1}
              />
            ))}
          </CreateContactSection>

          <hr className="border-gray-100" />

          {/* Phones */}
          <CreateContactSection
            title="Phone Numbers"
            onAdd={addPhone}
            canAdd={phones.length < MAX_ENTRIES}
            addLabel="Add another phone"
          >
            {phones.map((p, i) => (
              <AddContactsEntryRow
                key={i}
                icon={<Phone size={14} />}
                value={p.phone}
                label={p.label}
                type="text"
                labels={PHONE_LABELS}
                placeholder="+12345678900"
                error={errors.phones?.[i]}
                onValueChange={(val) => {
                  updatePhone(i, "phone", val);
                  setErrors((err) => ({ ...err, phones: undefined }));
                }}
                onLabelChange={(val) => updatePhone(i, "label", val)}
                onRemove={() => removePhone(i)}
                showRemove={phones.length > 1}
              />
            ))}
          </CreateContactSection>
        </div>

        {/* Actions */}
        <div className="flex items-center justify-end gap-2 pt-4 border-t border-gray-100 mt-4">
          <Button
            type="submit"
            label={submitLabel}
            variant="primary"
            disabled={submitting}
          />
        </div>
      </form>
    </div>
  );
}
