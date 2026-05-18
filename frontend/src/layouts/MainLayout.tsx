import { Outlet } from "react-router-dom";
import Navbar from "../shared/components/Navbar";
import Sidebar from "../shared/components/Sidebar";
import { Toaster } from "react-hot-toast";

const MainLayout = () => {
  return (
    <div className="font-poppins h-screen flex flex-col">
      <Navbar />
      <div className="flex flex-1 overflow-hidden">
        <Sidebar />
        <main className="flex-1 overflow-y-auto p-4 bg-bg">
          <Toaster
            position="top-right"
            toastOptions={{ className: "font-poppins text-sm" }}
          />

          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default MainLayout;
