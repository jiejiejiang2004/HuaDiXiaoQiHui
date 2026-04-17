export type UserType = "CANDIDATE" | "ENTERPRISE" | "";

const ACCESS_TOKEN_KEY = "xqh_access_token";
const USER_TYPE_KEY = "xqh_user_type";
const USER_NAME_KEY = "xqh_user_name";

export function getAccessToken(): string {
  return localStorage.getItem(ACCESS_TOKEN_KEY) || "";
}

export function setAccessToken(token: string): void {
  localStorage.setItem(ACCESS_TOKEN_KEY, token);
}

export function clearAccessToken(): void {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
}

export function getUserType(): UserType {
  return (localStorage.getItem(USER_TYPE_KEY) as UserType) || "";
}

export function setUserType(userType: UserType): void {
  localStorage.setItem(USER_TYPE_KEY, userType);
}

export function clearUserType(): void {
  localStorage.removeItem(USER_TYPE_KEY);
}

export function getUserName(): string {
  return localStorage.getItem(USER_NAME_KEY) || "";
}

export function setUserName(name: string): void {
  localStorage.setItem(USER_NAME_KEY, name);
}

export function clearUserName(): void {
  localStorage.removeItem(USER_NAME_KEY);
}

export function clearAuth(): void {
  clearAccessToken();
  clearUserType();
  clearUserName();
}
