export type UserType = "CANDIDATE" | "ENTERPRISE" | "ADMIN" | "";

const ACCESS_TOKEN_KEY = "xqh_access_token";
const REFRESH_TOKEN_KEY = "xqh_refresh_token";
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

export function getRefreshToken(): string {
  return localStorage.getItem(REFRESH_TOKEN_KEY) || "";
}

export function setRefreshToken(token: string): void {
  localStorage.setItem(REFRESH_TOKEN_KEY, token);
}

export function clearRefreshToken(): void {
  localStorage.removeItem(REFRESH_TOKEN_KEY);
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
  clearRefreshToken();
  clearUserType();
  clearUserName();
}
