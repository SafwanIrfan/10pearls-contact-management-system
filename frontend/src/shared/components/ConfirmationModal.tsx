// ─── ConfirmationModal.tsx ────────────────────────────────────────────────────

import Button from "./Button";

interface ConfirmationModalProps {
  title: string;
  message: React.ReactNode;
  confirmLabel?: string;
  cancelLabel?: string;
  variant?: "danger" | "primary";
  loading?: boolean;
  onConfirm: () => void;
  onCancel: () => void;
}

export const ConfirmationModal = ({
  title,
  message,
  confirmLabel = "Confirm",
  cancelLabel = "Cancel",
  variant = "danger",
  loading = false,
  onConfirm,
  onCancel,
}: ConfirmationModalProps) => {
  const primary = "bg-button hover:bg-button/90 text-white";
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div
        className="absolute inset-0 bg-black/30 backdrop-blur-sm"
        onClick={onCancel}
      />
      <div className="relative bg-white rounded-2xl shadow-xl p-6 w-full max-w-sm mx-4 flex flex-col gap-4 font-poppins">
        <div className="flex flex-col gap-1">
          <h3 className="text-base font-semibold text-heading">{title}</h3>
          <p className="text-sm text-gray-500">{message}</p>
        </div>
        <div className="flex items-center justify-end gap-2">
          <Button
            label={cancelLabel}
            variant="outline"
            onClick={onCancel}
            disabled={loading}
          />
          <Button
            variant={variant}
            label={loading ? "Please wait..." : confirmLabel}
            onClick={onConfirm}
            disabled={loading}
            className={`flex items-center gap-2 px-4 py-2.5 text-sm font-medium rounded-xl transition-all active:scale-95 disabled:opacity-50 disabled:pointer-events-none ${variant === "primary" ? primary : ""}`}
          />
        </div>
      </div>
    </div>
  );
};
