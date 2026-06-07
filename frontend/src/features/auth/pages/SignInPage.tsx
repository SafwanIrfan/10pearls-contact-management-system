import { Link } from "react-router-dom";
import type { SignInRequestDTO } from "../types/auth";
import { useAuth } from "../hooks/useAuth";
import Button from "../../../shared/components/Button";
import appLogo from "./../../../assets/LeadLy.svg";
import { useAuthForm } from "../hooks/useAuthForm";
import AuthFields from "../components/AuthFields";

const EMPTY_FORM: SignInRequestDTO = { identifier: "", password: "" };

export default function SignInPage() {
  const { handleSignIn, loading, error } = useAuth();
  const { form, errors, update, validate } =
    useAuthForm<SignInRequestDTO>(EMPTY_FORM);

  const handleSubmit = async () => {
    if (!validate()) return;
    await handleSignIn(form);
  };

  return (
    <div className="min-h-screen bg-bg flex items-center justify-center px-4 font-poppins">
      <div className="bg-white border border-gray-100 rounded-2xl shadow-sm w-full max-w-md">
        <div className="flex justify-center items-center p-4 bg-secondary rounded-t-2xl">
          <img src={appLogo} alt="appLogo" className="h-8 w-auto" />
        </div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            handleSubmit();
          }}
          className="p-8 flex flex-col gap-6"
        >
          {/* Header */}
          <div className="flex flex-col gap-1">
            <h1 className="text-xl font-semibold text-heading">Welcome back</h1>
            <p className="text-sm text-gray-500">
              Sign in to your account to continue.
            </p>
          </div>

          {error && (
            <div className="px-4 py-3 bg-red-50 border border-red-100 rounded-xl text-sm text-red-500">
              {error}
            </div>
          )}

          <AuthFields
            identifier={form.identifier}
            password={form.password}
            errors={errors}
            onIdentifierChange={(val) => update("identifier", val)}
            onPasswordChange={(val) => update("password", val)}
          />

          <Button
            type="submit"
            label={loading ? "Signing in..." : "Sign In"}
            variant="primary"
            onClick={handleSubmit}
            disabled={loading}
            className="w-full justify-center"
          />

          <p className="text-sm text-center text-gray-500">
            Don't have an account?{" "}
            <Link
              to="/auth/signup"
              className="text-button font-medium hover:underline"
            >
              Sign up
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
