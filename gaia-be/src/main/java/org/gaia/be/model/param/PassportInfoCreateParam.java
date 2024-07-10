package org.gaia.be.model.param;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:17
 */
@Data
public class PassportInfoCreateParam {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    @NotBlank
    private String title;

    /**
     * 网站url
     */
    private String url;

    /**
     * 网站用户名
     */
    private String username;

    /**
     * 网站密码
     */
    private String password;

    /**
     * 备注
     */
    private String remark;

}
