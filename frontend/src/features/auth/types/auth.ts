export interface SignInRequestDTO {
  identifier: string;
  password: string;
}

export interface SignUpRequestDTO {
  firstName: string;
  lastName: string;
  identifier: string;
  password: string;
}

export interface AuthResponseDTO {
  token: string;
}
