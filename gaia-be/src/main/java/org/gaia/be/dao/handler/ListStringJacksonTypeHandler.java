package org.gaia.be.dao.handler;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

/**
 * @author qinglin.liu
 * @create 2023/9/14 15:37
 */
@MappedTypes({String.class})
@MappedJdbcTypes({JdbcType.VARCHAR})
public class ListStringJacksonTypeHandler extends ListJsonTypeHandler {
    public ListStringJacksonTypeHandler(Class<?> type) {
        super(String.class);
    }
}
