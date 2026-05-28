export interface SignInRequestDTO {
  identifier: string;
  password: string;
}

export interface SignUpRequestDTO {
  identifier: string;
  password: string;
}

export interface AuthResponseDTO {
  token: string;
}

export type DecodedToken = {
  expiration: number;
};
