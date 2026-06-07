import api from "../../../services/api";

export const API_EXPORT_IMPORT_ENDPOINTS = {
  EXPORT: "/contacts/export",
  IMPORT: "/contacts/import",
};

// ── Export ──
export const exportContacts = async (): Promise<void> => {
  const response = await api.get(API_EXPORT_IMPORT_ENDPOINTS.EXPORT, {
    responseType: "blob",
  });

  const url = globalThis.URL.createObjectURL(new Blob([response.data]));
  const link = document.createElement("a");
  link.href = url;
  link.download = `contacts_${new Date().toISOString().split("T")[0]}.csv`;
  link.click();
  globalThis.URL.revokeObjectURL(url);
};

// ── Import ──
export const importContacts = async (file: File): Promise<void> => {
  const formData = new FormData();
  formData.append("file", file);

  await api.post(API_EXPORT_IMPORT_ENDPOINTS.IMPORT, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });
};
