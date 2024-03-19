package org.gaia.be.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:10
 */
@Data
public class HttpExecuteLogQueryParam {
    @NotNull
    private Long executorId;

    private int page = 1;

    private int pageSize = 10;
}
