package org.gaia.be.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gaia.be.dao.handler.ListStringJacksonTypeHandler;

import java.util.List;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:24
 */
@Data
@TableName(value = "t_user_login", autoResultMap = true)
@EqualsAndHashCode(callSuper = true)
public class UserLoginPo extends BasePo {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 权限列表
     */
    @TableField(typeHandler = ListStringJacksonTypeHandler.class)
    private List<String> access;
}
