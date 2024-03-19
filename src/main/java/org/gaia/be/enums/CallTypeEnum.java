package org.gaia.be.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinglin.liu
 * created at 2024/3/19 14:50
 */
@Getter
@AllArgsConstructor
public enum CallTypeEnum {
    HTTP_CALL(1, "普通Http请求"),
    MICRO_SERVICE(2, "微服务"),
    ;
    @EnumValue
    private final int code;

    private final String desc;

    public static final Map<Integer, CallTypeEnum> MAP = new HashMap<>();

    static {
        for (CallTypeEnum value : values()) {
            MAP.put(value.getCode(), value);
        }
    }

    public static CallTypeEnum parse(int code) {
        return MAP.get(code);
    }
}
