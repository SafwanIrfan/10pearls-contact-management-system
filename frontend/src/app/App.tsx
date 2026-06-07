import { Toaster } from "react-hot-toast";
import { RouterProvider } from "react-router-dom";
import { router } from "./routes";

const App = () => (
  <>
    <RouterProvider router={router} />
    <Toaster
      position="top-right"
      toastOptions={{ className: "font-poppins text-sm" }}
    />
  </>
);

export default App;
