package org.gaia.be.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import org.gaia.be.model.param.PassportInfoCreateParam;
import org.gaia.be.model.param.PassportInfoQueryParam;
import org.gaia.be.model.param.PassportInfoUpdateParam;
import org.gaia.be.model.vo.PassportInfoVo;
import org.gaia.be.service.PassportInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.winterframework.core.support.ApiResponse;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:15
 */
@RestController
@RequestMapping("/passportInfo")
public class PassportInfoController extends BaseController {
    @Autowired
    private PassportInfoService passportInfoService;

    @PostMapping("/list")
    public ApiResponse<Page<PassportInfoVo>> listPassportInfo(@RequestBody PassportInfoQueryParam params) {
        Page<PassportInfoVo> page = passportInfoService.listPassportInfoByPage(params);
        return ok(page);
    }

    @GetMapping("/info")
    public ApiResponse<PassportInfoVo> getPassportInfo(@RequestParam Long id) {
        PassportInfoVo passportInfoVo = passportInfoService.getPassportInfoById(id);
        return ok(passportInfoVo);
    }

    @PostMapping("/create")
    public ApiResponse<PassportInfoVo> createPassportInfo(@Valid @RequestBody PassportInfoCreateParam params) {
        params.setUserId(1L);
        PassportInfoVo passportInfoVo = passportInfoService.createPassportInfo(params);
        return ok(passportInfoVo);
    }

    @PostMapping("/update")
    public ApiResponse<PassportInfoVo> updatePassportInfo(@Valid @RequestBody PassportInfoUpdateParam params) {
        int num = passportInfoService.updateById(params);
        if (num > 0) {
            return ok();
        }
        return fail(1, "更新失败!");
    }

    @PostMapping("/delete")
    public ApiResponse<?> deletePassportInfo(@RequestParam Long id) {
        passportInfoService.deleteById(id);
        return ok();
    }
}
