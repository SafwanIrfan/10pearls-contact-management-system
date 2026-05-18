import { createBrowserRouter } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import ContactsPage from "../features/contacts/pages/ContactsPage";
import CreateContactPage from "../features/contacts/pages/CreateContactsPage";

export const router = createBrowserRouter([
  {
    element: <MainLayout />,
    children: [
      { path: "/", element: <ContactsPage /> },
      { path: "/contact/add", element: <CreateContactPage /> },
    ],
  },
]);
