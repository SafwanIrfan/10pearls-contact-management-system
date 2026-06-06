import { useRef, useState } from "react";
import {
  exportContacts,
  importContacts,
} from "../services/contactExportImportService";
import toast from "react-hot-toast";

export const useExportImport = (onImportSuccess: () => void) => {
  const [importing, setImporting] = useState(false);
  const [exporting, setExporting] = useState(false);
  const [showExportConfirm, setShowExportConfirm] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleExport = async () => {
    try {
      setExporting(true);
      await exportContacts();
      toast.success("Contacts exported successfully.");
    } catch {
      toast.error("Failed to export contacts. Please try again.");
    } finally {
      setExporting(false);
      setShowExportConfirm(false);
    }
  };

  const handleImport = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;

    // reset input so same file can be re-imported
    e.target.value = "";

    try {
      setImporting(true);
      await importContacts(file);
      toast.success("Contacts imported successfully.");
      onImportSuccess(); // refetch contacts table
    } catch {
      toast.error(
        "Failed to import contacts. Please check the file and try again.",
      );
    } finally {
      setImporting(false);
    }
  };

  const openFilePicker = () => fileInputRef.current?.click();

  return {
    handleExport,
    handleImport,
    openFilePicker,
    fileInputRef,
    importing,
    exporting,
    showExportConfirm,
    setShowExportConfirm,
  };
};
