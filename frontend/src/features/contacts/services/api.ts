import api from "../../../services/api";

export const API_ENDPOINTS = {
    CONTACTS: "/contacts",
    CONTACTS_BY_ID: "/contact/:id",
    CONTACTS_CREATE: "/contact/add",
    CONTACTS_UPDATE: "/contact/update/:id",
    CONTACTS_DELETE: "/contact/delete/:id",
}

export const fetchPaginatedContacts = async () => {
    const endpoint = API_ENDPOINTS.CONTACTS;
    const response = await api.get(`${endpoint}`);

    if (response.status !== 200) {
        throw new Error("Failed to fetch contacts");
    }

    return response.data;
}

