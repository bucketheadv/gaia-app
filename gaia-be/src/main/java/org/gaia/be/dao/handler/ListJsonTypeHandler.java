package org.gaia.be.dao.handler;

import com.baomidou.mybatisplus.extension.handlers.AbstractJsonTypeHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.winterframework.core.support.ApplicationContextHolder;
import org.winterframework.core.tool.JSONTool;

import java.util.List;

/**
 * @author qinglin.liu
 * created at 2024/3/19 14:57
 */
public class ListJsonTypeHandler extends AbstractJsonTypeHandler<Object> {

    protected final Class<?> type;

    protected final ObjectMapper objectMapper;

    public ListJsonTypeHandler(Class<?> type) {
        this.type = type;
        ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        objectMapper = applicationContext.getBean(ObjectMapper.class);
    }

    @Override
    protected Object parse(String json) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String toJson(Object obj) {
        return JSONTool.toJSONString(obj);
    }
}
