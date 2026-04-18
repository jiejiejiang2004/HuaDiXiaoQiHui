package main.xiaoqihui.common.api;

import java.util.HashMap;
import java.util.Map;

public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;
    private final long timestamp;

    private ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(0, "success", data);
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> fail(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> page(long total, int pageNum, int pageSize, T list) {
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("total", total);
        pageData.put("pageNum", pageNum);
        pageData.put("pageSize", pageSize);
        long totalPages = pageSize <= 0 ? 0 : (long) Math.ceil((double) total / pageSize);
        pageData.put("totalPages", totalPages);
        pageData.put("list", list);
        @SuppressWarnings("unchecked")
        T data = (T) pageData;
        return success(data);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
