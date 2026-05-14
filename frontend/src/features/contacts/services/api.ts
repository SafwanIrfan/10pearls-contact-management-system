import api from "../../../services/api";

export const API_ENDPOINTS = {
  CONTACTS: "/contacts/all",
  CONTACTS_BY_ID: "/contacts/contact/:id",
  CONTACTS_CREATE: "/contacts/contact/add",
  CONTACTS_UPDATE: "/contacts/contact/update/:id",
  CONTACTS_DELETE: "/contacts/contact/delete/:id",
};

export const fetchPaginatedContacts = async () => {
  const endpoint = API_ENDPOINTS.CONTACTS;
  const response = await api.get(`${endpoint}`);

  if (response.status !== 200) {
    throw new Error("Failed to fetch contacts");
  }

  return response.data;
};
