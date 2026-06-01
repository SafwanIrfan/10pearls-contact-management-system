export type DecodedUser = {
  sub: string;
  exp: number;
};

export type ChangePasswordRequestDTO = {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
};
