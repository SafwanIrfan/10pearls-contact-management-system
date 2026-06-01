import { createBrowserRouter } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import ContactsPage from "../features/contacts/pages/ContactsPage";
import CreateContactPage from "../features/contacts/pages/CreateContactsPage";
import ContactDetailsPage from "../features/contacts/pages/ContactDetailsPage";
import SignInPage from "../features/auth/pages/SignInPage";
import SignUpPage from "../features/auth/pages/SignUpPage";
import { ProtectedRoute } from "../features/auth/components/ProtectedRoute";
import ProfilePage from "../features/user/pages/ProfilePage";

export const router = createBrowserRouter([
  { path: "/auth/signin", element: <SignInPage /> },
  { path: "/auth/signup", element: <SignUpPage /> },

  {
    element: <ProtectedRoute />,
    children: [
      {
        element: <MainLayout />,
        children: [
          { path: "/", element: <ContactsPage /> },
          { path: "/contact/add", element: <CreateContactPage /> },
          { path: "/contact/:id", element: <ContactDetailsPage /> },
          { path: "/contact/update/:id", element: <CreateContactPage /> },
          { path: "/profile", element: <ProfilePage /> },
        ],
      },
    ],
  },
]);
