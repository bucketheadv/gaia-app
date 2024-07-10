package org.gaia.be.controller;

import org.winterframework.core.support.ApiResponse;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:33
 */
public class BaseController {
    public <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setCode(0);
        result.setData(data);
        return result;
    }

    public <T> ApiResponse<T> ok() {
        return ok(null);
    }

    public <T> ApiResponse<T> fail(int code, String message) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
