export interface getAllContactsResponse {
    data: ContactResponseDTO[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
}

export interface Emails {
    email: string;
    label: string;
}

export interface Phones {
    phone: string;
    label: string;
}

export interface ContactRequestDTO {
    firstName: string;
    lastName: string;
    title: string;
    emails: Emails[];
    phones: Phones[];
}

export interface ContactResponseDTO {
    id: string;
    firstName: string;
    lastName: string;
    title: string;
    emails: Emails[];
    phones: Phones[];
    createdAt: Date;
}

export interface updateContactRequest {
    firstName: string;
    lastName: string;
    title: string;
    email: Emails[];
    phone: Phones[];
}
