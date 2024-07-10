package org.gaia.be.model.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:09
 */
@Data
public class UserLoginVo {
    /**
     * 用户id
     */
    @JsonProperty("user_id")
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 权限列表
     */
    private List<String> access;
}
