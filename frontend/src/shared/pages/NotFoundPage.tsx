import { useNavigate } from "react-router-dom";
import { Home } from "lucide-react";
import Button from "../components/Button";

export default function NotFoundPage() {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-bg flex flex-col items-center justify-center gap-4 font-poppins px-4">
      <p className="text-6xl sm:text-8xl font-bold text-red-500">404</p>
      <div className="text-center">
        <h1 className="text-xl font-semibold text-heading">Page not found</h1>
        <p className="text-sm text-gray-500 mt-1">
          The page you're looking for doesn't exist or has been moved.
        </p>
      </div>
      <Button
        icon={Home}
        label="Back to Home"
        variant="primary"
        onClick={() => navigate("/")}
        hideLabel={true}
      />
    </div>
  );
}
