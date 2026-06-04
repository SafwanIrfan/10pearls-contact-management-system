import api from "../../../services/api";
import type { ContactRequestDTO } from "../types/contact.types";

export const API_ENDPOINTS = {
  CONTACTS: (page?: number, size?: number, search?: string) => {
    const params = new URLSearchParams();
    if (page !== undefined) params.append("page", String(page));
    if (size !== undefined) params.append("size", String(size));
    if (search) params.append("search", encodeURIComponent(search));
    return `/contacts?${params.toString()}`;
  },
  CONTACTS_BY_ID: (id: number) => `/contacts/contact/${id}`,
  CONTACTS_CREATE: "/contacts/contact/add",
  CONTACTS_UPDATE: (id: number) => `/contacts/contact/update/${id}`,
  CONTACTS_DELETE: (id: number) => `/contacts/contact/delete/${id}`,
};

export const fetchPaginatedContacts = async (
  page?: number,
  size?: number,
  search?: string,
) => {
  const endpoint = API_ENDPOINTS.CONTACTS(page, size, search);
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

export const fetchContactById = async (id: number) => {
  const endpoint = API_ENDPOINTS.CONTACTS_BY_ID(id);
  const response = await api.get(`${endpoint}`);

  if (response.status !== 200) {
    throw new Error("Failed to fetch contact with id: " + id);
  }

  return response.data;
};

export const updateContact = async (id: number, payload: ContactRequestDTO) => {
  const response = await api.put(API_ENDPOINTS.CONTACTS_UPDATE(id), payload);
  return response.data;
};

export const deleteContact = async (id: number) => {
  const response = await api.delete(API_ENDPOINTS.CONTACTS_DELETE(id));
  return response.data;
};
