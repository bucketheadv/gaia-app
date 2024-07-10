package org.gaia.be.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HttpExecutorExecuteParam {
    @NotNull
    private Long id;

    private Map<String, Object> params;

    private Map<String, Object> headers;

    private Map<String, Object> body;
}
