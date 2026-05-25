import ContactPageHeader from "../components/ContactPageHeader";
import SearchBar from "../components/SeachBar";
import ContactsTable from "../components/ContactsTable";
import { useState } from "react";
import { useContacts } from "../hooks/useContact";

const ContactsPage = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const { contacts, loading } = useContacts(page, pageSize, search);

  const handlePageSizeChange = (size: number) => {
    setPageSize(size);
    setPage(1);
  };

  return (
    <div className="flex-1 h-screen px-4 sm:px-6 py-4">
      <ContactPageHeader />
      <SearchBar onSearch={setSearch} />
      <ContactsTable
        contacts={contacts}
        loading={loading}
        page={page}
        setPage={setPage}
        pageSize={pageSize}
        onPageSizeChange={handlePageSizeChange}
      />
    </div>
  );
};
export default ContactsPage;
