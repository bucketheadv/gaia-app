package org.gaia.be.model.param;

import lombok.Data;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:25
 */
@Data
public class HttpExecutorQueryParam {
    private String name;

    private Integer callType;

    private int page = 1;

    private int pageSize = 20;
}
