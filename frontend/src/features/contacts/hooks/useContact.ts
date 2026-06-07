import { useEffect, useState } from "react";
import type { GetAllContactsResponse } from "../types/contact.types";
import { fetchPaginatedContacts } from "../services/api";

export const useContacts = (
  page?: number,
  pageSize?: number,
  search?: string,
) => {
  const [contacts, setContacts] = useState<GetAllContactsResponse>({
    data: [],
    page: 1,
    size: 10,
    totalElements: 0,
    totalPages: 0,
  });
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const [collapsed, setCollapsed] = useState(false);

  useEffect(() => {
    const handleResize = () => setCollapsed(window.innerWidth < 768);
    handleResize();
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const fetchContacts = async () => {
    try {
      setError(null);
      setLoading(true);
      const response = await fetchPaginatedContacts(page!, pageSize!, search);
      setContacts(response);
      console.log(response);
    } catch (error: any) {
      const message = error?.response?.data?.message;
      setError(message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (page === undefined || pageSize === undefined) return;
    fetchContacts();
  }, [page, pageSize, search]);

  return {
    contacts,
    loading,
    error,
    refetch: fetchContacts,
    collapsed,
    setCollapsed,
  };
};
