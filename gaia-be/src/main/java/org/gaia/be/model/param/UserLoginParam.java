package org.gaia.be.model.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:11
 */
@Data
public class UserLoginParam {
    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
