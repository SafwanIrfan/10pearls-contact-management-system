import ContactPageHeader from "../components/ContactPageHeader";
import SearchBar from "../components/SeachBar";
import ContactsTable from "../components/ContactsTable";
import React, { useState } from "react";
import { useContacts } from "../hooks/useContact";
import { useExportImport } from "../hooks/useExportImport";
import { ConfirmationModal } from "../../../shared/components/ConfirmationModal";

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
    showExportConfirm,
    setShowExportConfirm,
  } = useExportImport(refetch);

  const handlePageSizeChange = (size: number) => {
    setPageSize(size);
    setPage(1);
  };

  return (
    <div className="flex-1 h-screen px-4 sm:px-6 py-4">
      {showExportConfirm && (
        <ConfirmationModal
          title="Export Contacts"
          message="This will download all your contacts as a CSV file."
          confirmLabel="Export"
          variant="primary"
          loading={exporting}
          onConfirm={handleExport}
          onCancel={() => setShowExportConfirm(false)}
        />
      )}

      <ContactPageHeader
        onImport={handleImport}
        onExport={() => setShowExportConfirm(true)}
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
