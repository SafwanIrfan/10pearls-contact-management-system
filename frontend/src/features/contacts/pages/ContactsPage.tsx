import ContactPageHeader from "../components/ContactPageHeader";
import SearchBar from "../components/SeachBar";
import ContactsTable from "../components/ContactsTable";
import React, { useState } from "react";
import { useContacts } from "../hooks/useContact";
import { useExportImport } from "../hooks/useExportImport";

const ContactsPage = () => {
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const { contacts, loading, refetch } = useContacts(page, pageSize, search);

  const {
    handleExport,
    handleImport,
    openFilePicker,
    fileInputRef,
    importing,
    exporting,
  } = useExportImport(refetch);


  const handlePageSizeChange = (size: number) => {
    setPageSize(size);
    setPage(1);
  };

  return (
    <div className="flex-1 h-screen px-4 sm:px-6 py-4">
      <ContactPageHeader
        onImport={handleImport}
        onExport={handleExport}
        openFilePicker={openFilePicker}
        fileInputRef={fileInputRef as React.RefObject<HTMLInputElement>}
        importing={importing}
        exporting={exporting}
      />
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
