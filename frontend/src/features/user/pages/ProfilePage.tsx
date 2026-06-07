// ─── ProfilePage.tsx ──────────────────────────────────────────────────────────

import { useState } from "react";
import { LogOut, KeyRound, UserCircle2 } from "lucide-react";
import { useProfile } from "../hooks/useProfile";
import { ChangePasswordModal } from "../components/ChangePasswordModal";
import { ConfirmationModal } from "../../../shared/components/ConfirmationModal";
import { logout } from "../../auth/services/authService";
import ActionButton from "../components/ActionButton";

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
          onConfirm={logout}
          onCancel={() => setShowLogoutModal(false)}
        />
      )}

      <div className="h-full flex flex-col px-4 py-6 font-poppins gap-6">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-semibold text-heading">Profile</h1>
          <p className="text-sm text-gray-500 mt-0.5">
            Manage your account settings.
          </p>
        </div>

        <div>
          {/* User Card */}
          <div className="bg-white rounded-2xl shadow-sm p-6 flex flex-col gap-4">
            <div className="flex flex-col sm:flex-row items-center justify-center gap-4 shrink-0">
              <div className="w-14 h-14 rounded-full bg-button/10 flex items-center justify-center shrink-0">
                <UserCircle2 size={28} className="text-button" />
              </div>
              <div className="min-w-0">
                <p className="text-sm text-gray-400 uppercase text-center sm:text-left tracking-wide mb-1">
                  {isEmail ? "Email" : "Phone"}
                </p>
                <p className="text-sm font-medium text-heading truncate">
                  {user?.sub ?? "—"}
                </p>
              </div>
            </div>
            <div className="flex flex-col sm:flex-row gap-4 mt-4">
              <ActionButton
                title="Change Password"
                onClick={() => setShowChangePassword(true)}
                icon={KeyRound}
                variant="primary"
              />
              <ActionButton
                title="Log Out"
                onClick={() => setShowLogoutModal(true)}
                icon={LogOut}
              />
            </div>
          </div>

          {/* Actions Card
          <div className="flex flex-col p-4 gap-4 bg-white rounded-2xl  shadow-sm">
            <ActionButton
              title="Change Password"
              onClick={() => setShowChangePassword(true)}
              icon={KeyRound}
              variant="primary"
            />
            <ActionButton
              title="Log Out"
              onClick={() => setShowLogoutModal(true)}
              icon={LogOut}
            />
          </div> */}
        </div>
      </div>
    </>
  );
}
