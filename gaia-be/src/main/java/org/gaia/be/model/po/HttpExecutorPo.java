package org.gaia.be.model.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.gaia.be.dao.handler.ListEventParamJacksonTypeHandler;
import org.gaia.be.enums.CallTypeEnum;
import org.gaia.be.model.dto.EventParam;

import java.util.List;

/**
 * @author qinglin.liu
 * created at 2024/3/19 14:48
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_http_executor", autoResultMap = true)
public class HttpExecutorPo extends BasePo {
    /**
     * 名称
     */
    private String name;

    /**
     * 调用 类型
     */
    private CallTypeEnum callType;

    /**
     * 服务名称，仅 eventType = 微服务 时，此项必填
     */
    private String serviceName;

    /**
     * 服务url，当eventType = 微服务时，此处填写 path
     */
    private String url;

    /**
     * http method
     */
    private String httpMethod;

    /**
     * 超时时间
     */
    private Long timeout;

    /**
     * 上线状态
     */
    private Boolean onlineStatus;

    /**
     * Header 参数
     */
    @TableField(typeHandler = ListEventParamJacksonTypeHandler.class)
    private List<EventParam> httpHeaders;

    /**
     * Http 请求参数
     */
    @TableField(typeHandler = ListEventParamJacksonTypeHandler.class)
    private List<EventParam> httpParams;

    /**
     * Http 请求体
     */
    @TableField(typeHandler = ListEventParamJacksonTypeHandler.class)
    private List<EventParam> httpBody;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 最后更新人
     */
    private String updatedBy;
}
