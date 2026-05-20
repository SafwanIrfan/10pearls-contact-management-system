// ─── ContactDetailsPage.tsx ───────────────────────────────────────────────────

import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Pencil, Trash2, Mail, Phone } from "lucide-react";
import toast from "react-hot-toast";
import type { Emails, Phones } from "../types/contact.types";
import { useContactById } from "../hooks/useContactById";
import Button from "../../../shared/components/Button";
import { Spinner } from "../../../shared/components/Spinner";
import Avatar from "../components/Avatar";
import { ConfirmationModal } from "../../../shared/components/ConfirmationModal";
import { useContacts } from "../hooks/useContact";

//  Info Card
interface InfoCardProps {
  title: string;
  children: React.ReactNode;
}

const InfoCard = ({ title, children }: InfoCardProps) => (
  <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-5 flex flex-col gap-4 flex-1">
    <h2 className="text-xs font-semibold text-gray-400 uppercase tracking-wide">
      {title}
    </h2>
    {children}
  </div>
);

// Contact Entry Row
interface EntryItemProps {
  icon: React.ReactNode;
  value: string;
  label: string;
}

const EntryItem = ({ icon, value, label }: EntryItemProps) => (
  <div className="flex items-center gap-3">
    <div className="w-8 h-8 rounded-xl bg-gray-50 border border-gray-100 flex items-center justify-center text-gray-400 shrink-0">
      {icon}
    </div>
    <div className="min-w-0">
      <p className="text-sm font-medium text-text truncate">{value}</p>
      <p className="text-xs text-gray-400 capitalize">{label}</p>
    </div>
  </div>
);

// Main
export default function ContactDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { collapsed } = useContacts();

  const { contact, loading, error } = useContactById(Number(id!));

  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const fullName = contact ? `${contact.firstName} ${contact.lastName}` : "";

  const handleEdit = () => navigate(`/contacts/edit/${id}`);

  const handleDelete = async () => {
    try {
      setDeleting(true);
      //   await deleteContact(id!);
      toast.success("Contact deleted successfully.");
      navigate(-1);
    } catch {
      toast.error("Failed to delete contact. Please try again.");
      setDeleting(false);
    }
  };

  if (loading)
    return (
      <div className="h-full flex items-center justify-center">
        <Spinner size={28} />
      </div>
    );

  if (error || !contact)
    return (
      <div className="h-full flex flex-col items-center justify-center gap-3">
        <p className="text-sm font-medium text-gray-400">
          Failed to load contact.
        </p>
        <Button
          label="Go Back"
          variant="outline"
          icon={ArrowLeft}
          onClick={() => navigate(-1)}
        />
      </div>
    );

  return (
    <>
      {showDeleteModal && (
        <ConfirmationModal
          title="Delete Contact"
          message={
            <>
              Are you sure you want to delete{" "}
              <span className="font-medium text-heading">{fullName}</span>? This
              action cannot be undone.
            </>
          }
          confirmLabel="Delete"
          variant="danger"
          loading={deleting}
          onConfirm={handleDelete}
          onCancel={() => setShowDeleteModal(false)}
        />
      )}

      <div className="h-full flex flex-col px-4 py-6 font-poppins gap-6">
        {/* ── Header ── */}
        <div className="flex items-center justify-between gap-3">
          <div className="flex items-center gap-3">
            <button
              onClick={() => navigate(-1)}
              className="p-2 cursor-pointer text-gray-500 hover:text-text hover:bg-gray-100 rounded-xl transition-all"
            >
              <ArrowLeft size={18} />
            </button>
            <div>
              <h1 className="text-xl font-semibold text-heading">
                Contact Details
              </h1>
            </div>
          </div>

          <div className="flex items-center gap-2">
            <Button
              label={collapsed ? "" : "Edit"}
              icon={Pencil}
              variant="outline"
              onClick={handleEdit}
            />
            <Button
              label={collapsed ? "" : "Delete"}
              icon={Trash2}
              variant="outline"
              onClick={() => setShowDeleteModal(true)}
              className="text-red-400 border-red-200 hover:bg-red-50 hover:border-red-300"
            />
          </div>
        </div>

        {/* ── Content ── */}
        <div className="flex flex-col gap-4 flex-1 overflow-y-auto">
          {/* Profile Banner */}
          <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-6 flex items-center justify-between">
            <div className="flex items-center gap-4">
              <Avatar name={fullName} size="lg" />
              <div>
                <h2 className="text-lg font-semibold text-heading">
                  {fullName}
                </h2>
                <p className="text-sm text-gray-500 mt-0.5">
                  {contact.title || "—"}
                </p>
              </div>
            </div>
          </div>

          {/* Details */}
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <InfoCard title="Email Addresses">
              {contact.emails?.length > 0 ? (
                contact.emails.map((e: Emails, i: number) => (
                  <EntryItem
                    key={i}
                    icon={<Mail size={14} />}
                    value={e.email}
                    label={e.label}
                  />
                ))
              ) : (
                <p className="text-sm text-gray-400">No emails added.</p>
              )}
            </InfoCard>

            <InfoCard title="Phone Numbers">
              {contact.phones?.length > 0 ? (
                contact.phones.map((p: Phones, i: number) => (
                  <EntryItem
                    key={i}
                    icon={<Phone size={14} />}
                    value={p.phone}
                    label={p.label}
                  />
                ))
              ) : (
                <p className="text-sm text-gray-400">No phone numbers added.</p>
              )}
            </InfoCard>
          </div>
        </div>
      </div>
    </>
  );
}
