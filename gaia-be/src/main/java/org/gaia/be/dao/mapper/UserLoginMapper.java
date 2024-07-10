package org.gaia.be.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.gaia.be.model.po.UserLoginPo;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:25
 */
@Mapper
public interface UserLoginMapper extends BaseMapper<UserLoginPo> {
}
