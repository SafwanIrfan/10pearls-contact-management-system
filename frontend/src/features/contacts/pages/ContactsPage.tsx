import { Download, Upload, UserPlus } from "lucide-react";
import { useContacts } from "../hooks/useContact";
import ContactPageHeader from "../components/ContactPageHeader";
import SearchBar from "../components/SeachBar";

const ContactsPage = () => {
  const { contacts, loading, error, refetch } = useContacts();

  return (
    <div className="flex-1 h-screen px-4 sm:px-6 py-4 ">
      <ContactPageHeader />

      <SearchBar />
    </div>
  );
};
export default ContactsPage;
