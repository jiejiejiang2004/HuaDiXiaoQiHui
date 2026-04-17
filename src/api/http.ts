import axios from "axios";
import { ElMessage } from "element-plus";
import { clearAuth, getAccessToken } from "@/utils/auth";

export const API_BASE_URL =
  process.env.VUE_APP_API_BASE_URL || "http://localhost:8084/recruit/api/v1";
export const API_ORIGIN = API_BASE_URL.replace("/recruit/api/v1", "");

const http = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
});

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
      return Promise.reject(payload);
    }
    return payload.data;
  },
  (error) => {
    const code = error?.response?.data?.code;
    if (code === 2001) {
      clearAuth();
    }
    ElMessage.error(
      error?.response?.data?.message || error.message || "网络异常"
    );
    return Promise.reject(error);
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

export default http;
