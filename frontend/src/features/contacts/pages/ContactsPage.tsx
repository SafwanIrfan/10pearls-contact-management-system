import ContactPageHeader from "../components/ContactPageHeader";
import SearchBar from "../components/SeachBar";
import ContactsTable from "../components/ContactsTable";

const ContactsPage = () => {
  return (
    <div className="flex-1 h-screen px-4 sm:px-6 py-4 ">
      <ContactPageHeader />
      <SearchBar />
      <ContactsTable />
    </div>
  );
};
export default ContactsPage;
