import { useEffect, useState } from "react"
import type { getAllContactsResponse } from "../types/contact.types"
import { fetchPaginatedContacts } from "../services/api"

export const useContacts = () => {

    const [contacts, setContacts] = useState<getAllContactsResponse>({
        data: [],
        page: 1,
        size: 10,
        totalElements: 0,
        totalPages: 0,
    })
    const [loading, setLoading] = useState<boolean>(false)
    const [error, setError] = useState<string | null>(null)

    const fetchContacts = async () => {
        try {
            setLoading(true)
            const response = await fetchPaginatedContacts();
            setContacts(response);
            console.log(response);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        fetchContacts();
    }, [])

    return {
        contacts,
        loading,
        error,
        refetch: fetchContacts
    }
}