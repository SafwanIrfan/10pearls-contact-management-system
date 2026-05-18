import api from "../../../services/api";

export const API_ENDPOINTS = {
  CONTACTS: (page: number, size: number) =>
    `/contacts/all?page=${page}&size=${size}`,
  CONTACTS_BY_ID: "/contacts/contact/:id",
  CONTACTS_CREATE: "/contacts/contact/add",
  CONTACTS_UPDATE: "/contacts/contact/update/:id",
  CONTACTS_DELETE: "/contacts/contact/delete/:id",
};

export const fetchPaginatedContacts = async (page: number, size: number) => {
  const endpoint = API_ENDPOINTS.CONTACTS(page, size);
  const response = await api.get(`${endpoint}`);

  if (response.status !== 200) {
    throw new Error("Failed to fetch contacts");
  }

  return response.data;
};
