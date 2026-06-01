// ─── useContactById.ts ────────────────────────────────────────────────────────

import { useEffect, useState } from "react";
import type { ContactResponseDTO } from "../types/contact.types";
import { fetchContactById } from "../services/api";

export const useContactById = (id: number | null) => {
  const [contact, setContact] = useState<ContactResponseDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    if (id === null) return;
    const fetch = async () => {
      try {
        setLoading(true);
        const res = await fetchContactById(id);
        setContact(res);
      } catch {
        setError(true);
      } finally {
        setLoading(false);
      }
    };
    fetch();
  }, [id]);

  return { contact, loading, error };
};
