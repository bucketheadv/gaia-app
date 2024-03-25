package org.gaia.be.dao.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.gaia.be.model.dto.EventParam;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:08
 */
@MappedTypes({EventParam.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListEventParamJacksonTypeHandler extends ListJsonTypeHandler {
    public ListEventParamJacksonTypeHandler(Class<?> type) {
        super(EventParam.class);
    }
}
