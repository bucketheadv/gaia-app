package org.gaia.be.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.gaia.be.exceptions.BizException;
import org.gaia.be.model.param.UserLoginParam;
import org.gaia.be.model.vo.UserLoginVo;
import org.gaia.be.service.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qinglin.liu
 * @create 2023/9/14 13:47
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody UserLoginParam params) {
        String token = userAuthService.login(params);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        return map;
    }

    @GetMapping("/getInfo")
    public UserLoginVo getUserInfo(@RequestParam String token) {
        if (StringUtils.isBlank(token)) {
            throw new BizException("Token不能为空!");
        }
        UserLoginVo userLoginVo = userAuthService.getLoginByToken(token);
        if (userLoginVo != null) {
            return userLoginVo;
        }
        throw new BizException("用户信息不存在！");
    }
}
