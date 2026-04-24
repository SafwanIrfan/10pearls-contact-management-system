import { createBrowserRouter } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import ContactsPage from "../features/contacts/pages/ContactsPage";


export const router = createBrowserRouter([

    {
        element: <MainLayout />,
        children: [
            { path: "/", element: <ContactsPage /> },
        ]
    }

])