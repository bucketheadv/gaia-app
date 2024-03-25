package org.gaia.be.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.winterframework.crypto.annotation.EncryptField;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_passport_info")
public class PassportInfoPo extends BasePo {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 网站url
     */
    @EncryptField
    private String url;

    /**
     * 网站用户名
     */
    @EncryptField
    private String username;

    /**
     * 网站密码
     */
    @EncryptField
    private String password;

    /**
     * 备注
     */
    private String remark;
}
