package org.gaia.be.model.param;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.gaia.be.enums.CallTypeEnum;
import org.gaia.be.model.dto.EventParam;

import java.util.Date;
import java.util.List;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:03
 */
@Data
public class HttpExecutorCreateParam {
    /**
     * 名称
     */
    @NotBlank
    private String name;

    /**
     * 调用类型
     */
    @NotNull
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
    private List<EventParam> httpHeaders;

    /**
     * Http 请求参数
     */
    private List<EventParam> httpParams;

    /**
     * Http 请求体
     */
    private List<EventParam> httpBody;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 最后更新人
     */
    private String updatedBy;

    /**
     * 是否已删除
     */
    private Boolean deleted;

}
