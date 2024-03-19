package org.gaia.be.exceptions;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:14
 */
public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
