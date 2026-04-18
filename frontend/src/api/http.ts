import axios, { AxiosError, AxiosRequestConfig } from "axios";
import { ElMessage } from "element-plus";
import {
  clearAuth,
  getAccessToken,
  getRefreshToken,
  setAccessToken,
} from "@/utils/auth";

export const API_BASE_URL =
  process.env.VUE_APP_API_BASE_URL || "/recruit/api/v1";

export const API_ORIGIN = API_BASE_URL.replace("/recruit/api/v1", "");

const http = axios.create({
  baseURL: API_BASE_URL, // 此时 baseURL 为 "/recruit/api/v1"
  timeout: 10000,
});

let refreshingPromise: Promise<string> | null = null;

function toHandledError(
  source: unknown,
  fallbackMessage: string
): Error & {
  code?: number;
  handled: boolean;
  payload?: unknown;
} {
  const payload = source as { code?: number; message?: string } | undefined;
  const message = payload?.message || fallbackMessage;
  const error = new Error(message) as Error & {
    code?: number;
    handled: boolean;
    payload?: unknown;
  };
  error.name = "HandledHttpError";
  error.code = payload?.code;
  error.handled = true;
  error.payload = source;
  return error;
}

async function refreshAccessTokenIfNeeded(): Promise<string> {
  if (refreshingPromise) {
    return refreshingPromise;
  }
  const refreshToken = getRefreshToken();
  if (!refreshToken) {
    throw new Error("缺少 refreshToken");
  }
  refreshingPromise = axios
    .post(
      `${API_BASE_URL}/user/token/refresh`,
      { refreshToken },
      { timeout: 10000 }
    )
    .then((response) => {
      const payload = response.data;
      if (payload?.code !== 0 || !payload?.data?.accessToken) {
        throw new Error(payload?.message || "刷新 Token 失败");
      }
      const nextAccessToken = String(payload.data.accessToken);
      setAccessToken(nextAccessToken);
      return nextAccessToken;
    })
    .finally(() => {
      refreshingPromise = null;
    });
  return refreshingPromise;
}

http.interceptors.request.use((config) => {
  const token = getAccessToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    const payload = response.data;
    if (payload?.code !== 0) {
      ElMessage.error(payload?.message || "请求失败");
      return Promise.reject(toHandledError(payload, "请求失败"));
    }
    return payload.data;
  },
  async (error: AxiosError) => {
    const code = (error.response?.data as { code?: number } | undefined)?.code;
    const errorData = error.response?.data as { message?: string } | undefined;
    const originalRequest = (error.config || {}) as AxiosRequestConfig & {
      _retry?: boolean;
    };
    const requestUrl = String(originalRequest.url || "");

    if (
      code === 2001 &&
      !originalRequest._retry &&
      !requestUrl.includes("/user/token/refresh") &&
      getRefreshToken()
    ) {
      try {
        originalRequest._retry = true;
        const nextAccessToken = await refreshAccessTokenIfNeeded();
        originalRequest.headers = originalRequest.headers || {};
        originalRequest.headers.Authorization = `Bearer ${nextAccessToken}`;
        return http(originalRequest);
      } catch {
        clearAuth();
      }
    } else if (code === 2001) {
      clearAuth();
    }
    const handledError = toHandledError(
      errorData || { message: error.message },
      error.message || "网络异常"
    );
    ElMessage.error(handledError.message || "网络异常");
    return Promise.reject(handledError);
  }
);

export function resolveAssetUrl(url?: string): string {
  if (!url) {
    return "";
  }
  if (url.startsWith("http://") || url.startsWith("https://")) {
    return url;
  }
  return `${API_ORIGIN}${url}`;
}

export async function downloadBinaryFile(
  url: string,
  fileName?: string
): Promise<void> {
  let token = getAccessToken();
  if (!token && getRefreshToken()) {
    token = await refreshAccessTokenIfNeeded();
  }
  const request = async (accessToken: string) =>
    axios.get(`${API_ORIGIN}${url}`, {
      responseType: "blob",
      timeout: 20000,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

  let response;
  try {
    response = await request(token);
  } catch (error) {
    const axiosError = error as AxiosError;
    if (
      (axiosError.response?.status === 401 ||
        axiosError.response?.status === 403) &&
      getRefreshToken()
    ) {
      const nextAccessToken = await refreshAccessTokenIfNeeded();
      response = await request(nextAccessToken);
    } else {
      throw error;
    }
  }

  const blob = new Blob([response.data]);
  const objectUrl = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = objectUrl;
  link.download =
    fileName ||
    decodeURIComponent(
      response.headers["content-disposition"]
        ?.split("filename*=UTF-8''")
        ?.pop()
        ?.replace(/"/g, "") || "download"
    );
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(objectUrl);
}

export default http;
