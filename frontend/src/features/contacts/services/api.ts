import api from "../../../services/api";
import type { ContactRequestDTO } from "../types/contact.types";

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

export const createContact = async (contactData: ContactRequestDTO) => {
  const endpoint = API_ENDPOINTS.CONTACTS_CREATE;
  const response = await api.post(`${endpoint}`, contactData);

  if (response.status !== 200) {
    throw new Error("Failed to add contacts");
  }

  return response.data;
};
