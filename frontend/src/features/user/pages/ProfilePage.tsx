// ─── ProfilePage.tsx ──────────────────────────────────────────────────────────

import { useState } from "react";
import { LogOut, KeyRound, UserCircle2 } from "lucide-react";
import { useProfile } from "../hooks/useProfile";
import { ChangePasswordModal } from "../components/ChangePasswordModal";
import { ConfirmationModal } from "../../../shared/components/ConfirmationModal";
import { logout } from "../../auth/services/authService";

export default function ProfilePage() {
  const { user, loading, handleChangePassword } = useProfile();

  const [showChangePassword, setShowChangePassword] = useState(false);
  const [showLogoutModal, setShowLogoutModal] = useState(false);

  const isEmail = user?.sub?.includes("@");

  return (
    <>
      {showChangePassword && (
        <ChangePasswordModal
          loading={loading}
          onCancel={() => setShowChangePassword(false)}
          onSubmit={async (payload) => {
            await handleChangePassword(payload, () =>
              setShowChangePassword(false),
            );
          }}
        />
      )}

      {showLogoutModal && (
        <ConfirmationModal
          title="Log Out"
          message="Are you sure you want to log out?"
          confirmLabel="Log Out"
          variant="primary"
          onConfirm={logout}
          onCancel={() => setShowLogoutModal(false)}
        />
      )}

      <div className="h-full flex flex-col px-4 py-6 font-poppins gap-6">
        {/* Header */}
        <div>
          <h1 className="text-xl font-semibold text-heading">Profile</h1>
          <p className="text-xs text-gray-500 mt-0.5">
            Manage your account settings.
          </p>
        </div>

        <div className="flex flex-col gap-4 max-w-md">
          {/* User Card */}
          <div className="bg-white border border-gray-100 rounded-2xl shadow-sm p-6 flex items-center gap-4">
            <div className="w-14 h-14 rounded-full bg-button/10 flex items-center justify-center shrink-0">
              <UserCircle2 size={28} className="text-button" />
            </div>
            <div className="min-w-0">
              <p className="text-xs text-gray-400 uppercase tracking-wide mb-1">
                {isEmail ? "Email" : "Phone"}
              </p>
              <p className="text-sm font-medium text-heading truncate">
                {user?.sub ?? "—"}
              </p>
            </div>
          </div>

          {/* Actions Card */}
          <div className="bg-white border border-gray-100 rounded-2xl shadow-sm divide-y divide-gray-100">
            <button
              onClick={() => setShowChangePassword(true)}
              className="w-full flex items-center gap-3 px-5 py-4 text-sm font-medium text-text hover:bg-gray-50 transition-all rounded-t-2xl"
            >
              <KeyRound size={16} className="text-gray-400" />
              Change Password
            </button>
            <button
              onClick={() => setShowLogoutModal(true)}
              className="w-full flex items-center gap-3 px-5 py-4 text-sm font-medium text-red-400 hover:bg-red-50 transition-all rounded-b-2xl"
            >
              <LogOut size={16} />
              Log Out
            </button>
          </div>
        </div>
      </div>
    </>
  );
}
