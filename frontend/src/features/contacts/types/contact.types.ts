export interface GetAllContactsResponse {
  data: ContactResponseDTO[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface Emails {
  id?: number;
  email: string;
  label: string;
}

export interface Phones {
  id?: number;
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
