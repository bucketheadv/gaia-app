package org.gaia.be.dao.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.gaia.be.dao.mapper.UserLoginMapper;
import org.gaia.be.model.po.UserLoginPo;
import org.springframework.stereotype.Service;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:26
 */
@Service
public class UserLoginDao extends ServiceImpl<UserLoginMapper, UserLoginPo> {
    public UserLoginPo selectByUserName(String userName, String password) {
        LambdaQueryWrapper<UserLoginPo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLoginPo::getUsername, userName)
                .eq(UserLoginPo::getPassword, password);
        return baseMapper.selectOne(queryWrapper);
    }
}
