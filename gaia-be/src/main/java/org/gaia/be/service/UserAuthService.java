package org.gaia.be.service;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.gaia.be.constants.RedisConstants;
import org.gaia.be.dao.service.UserLoginDao;
import org.gaia.be.exceptions.BizException;
import org.gaia.be.model.param.UserLoginParam;
import org.gaia.be.model.po.UserLoginPo;
import org.gaia.be.model.vo.UserLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.winterframework.core.tool.JSONTool;
import org.winterframework.core.tool.StringTool;
import org.winterframework.data.redis.core.JedisTemplate;

/**
 * @author qinglin.liu
 * created at 2024/7/10 09:07
 */
@Slf4j
@Service
public class UserAuthService {
    @Autowired
    private JedisTemplate jedisTemplate;
    @Autowired
    private UserLoginDao userLoginDao;

    public String login(UserLoginParam param) {
        UserLoginPo userLoginPo = userLoginDao.selectByUserName(param.getUserName(), param.getPassword());
        if (userLoginPo == null) {
            throw new BizException("用户名或密码不正确！");
        }
        UserLoginVo userLoginVo = new UserLoginVo();
        userLoginVo.setUserId(userLoginVo.getUserId());
        userLoginVo.setUserName(userLoginPo.getUsername());
        userLoginVo.setAccess(userLoginPo.getAccess());
        userLoginVo.setAvatar(userLoginPo.getAvatar());
        String token = UUID.randomUUID().toString(true);
        String key = RedisConstants.AUTH + token;
        jedisTemplate.setex(key, 86400, JSONTool.toJSONString(userLoginVo));
        return token;
    }

    public UserLoginVo getLoginByToken(String token) {
        String value = jedisTemplate.get(RedisConstants.AUTH + token);
        if (StringTool.isBlank(value)) {
            return null;
        }
        return JSONTool.parseObject(value, UserLoginVo.class);
    }
}
