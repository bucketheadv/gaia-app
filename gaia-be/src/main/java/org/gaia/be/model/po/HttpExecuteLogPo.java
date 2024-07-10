package org.gaia.be.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_http_execute_log")
public class HttpExecuteLogPo extends BasePo {
    /**
     * 执行器id
     */
    private Long executorId;

    /**
     * url
     */
    private String url;

    /**
     * Http Method
     */
    private String httpMethod;

    /**
     * HttpHeader参数
     */
    private String httpHeaders;

    /**
     * HttpParams参数
     */
    private String httpParams;

    /**
     * HttpBody参数
     */
    private String httpBody;

    /**
     * 耗时/毫秒
     */
    private Long duration;

    /**
     * 执行人
     */
    private String executeUser;

    /**
     * 执行结果 0 未知 1 成功 2 失败
     */
    private Integer executeResult;

    /**
     * 执行返回结果
     */
    private String executeResponse;

}
