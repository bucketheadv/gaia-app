package org.gaia.be.model.param;

import lombok.Data;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:18
 */
@Data
public class PassportInfoQueryParam {
    private String title;

    private int page = 1;

    private int pageSize = 20;
}
