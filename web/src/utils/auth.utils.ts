import { jwtDecode, JwtPayload } from "jwt-decode";
import { useUserStore } from "../store/user.store";

interface CustomJwtPayload extends JwtPayload {
  scope: Array<string> | string;
}

function saveToken(token: string) {
  const decodedToken = jwtDecode(token);
  if (decodedToken && decodedToken.exp) {
    const expirationTime = decodedToken.exp * 1000;
    localStorage.setItem("token", token);
    localStorage.setItem("jwtExp", expirationTime.toString());
  }
}

function isTokenExpired(): boolean {
  const expiration = localStorage.getItem("jwtExp");
  if (!expiration) {
    return true;
  }
  const now = Date.now();
  const isExpired = now > parseInt(expiration, 10);
  if (isExpired) removeToken();
  return isExpired;
}

function removeToken(): void {
  localStorage.removeItem("token");
  localStorage.removeItem("jwtExp");
}

function logout() {
  localStorage.removeItem("user");
  removeToken();
}

function getRoles(): Array<string> | string | undefined {
  const token = localStorage.getItem("token");
  if (token) {
    const decoded = jwtDecode(token) as CustomJwtPayload;
    return decoded.scope;
  } else {
    ("something went wrong.");
  }
}

export { saveToken, isTokenExpired, removeToken, getRoles, logout };
