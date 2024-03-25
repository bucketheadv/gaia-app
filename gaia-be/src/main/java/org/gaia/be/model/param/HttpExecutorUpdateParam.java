package org.gaia.be.model.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HttpExecutorUpdateParam extends HttpExecutorCreateParam {
    @NotNull
    private Long id;
}
