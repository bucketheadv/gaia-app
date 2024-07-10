package org.gaia.be.controller;

import jakarta.validation.Valid;
import org.gaia.be.model.param.HttpExecuteLogQueryParam;
import org.gaia.be.service.HttpExecuteLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.winterframework.core.support.ApiResponse;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:11
 */
@RestController
@RequestMapping("/executeLog")
public class HttpExecuteLogController extends BaseController {
    @Autowired
    private HttpExecuteLogService httpExecuteLogService;

    @PostMapping("/list")
    public ApiResponse<?> listApiExecuteLog(@Valid @RequestBody HttpExecuteLogQueryParam query) {
        return ok(httpExecuteLogService.listApiExecuteLogByPage(query));
    }
}
