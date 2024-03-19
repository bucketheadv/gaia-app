package org.gaia.be.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinglin.liu
 * created at 2024/3/19 14:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventParam {
    /**
     * 参数名
     */
    private String name;

    /**
     * 参数中文名
     */
    private String nameCN;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 是否必填
     */
    @Builder.Default
    private Boolean required = Boolean.FALSE;

    /**
     * 可选值，目前用于 type = select 情况下的下拉菜单
     */
    private String availableValues;

    /**
     * 默认值
     */
    private Object defaultValue;

    /**
     * 是否不可编辑
     */
    @Builder.Default
    private Boolean uneditable = Boolean.FALSE;
}
