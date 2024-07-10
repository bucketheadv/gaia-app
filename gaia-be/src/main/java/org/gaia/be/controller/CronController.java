package org.gaia.be.controller;

import lombok.extern.slf4j.Slf4j;
import org.gaia.be.exceptions.BizException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.winterframework.core.support.ApiResponse;
import org.winterframework.core.tool.CronTool;

import java.text.ParseException;
import java.util.List;

/**
 * @author qinglin.liu
 * @create 2023/9/26 19:38
 */
@Slf4j
@RestController
@RequestMapping("/cron")
public class CronController extends BaseController {
    @GetMapping("/getNextValidTimeAfterNow")
    public ApiResponse<?> getNextValidTimeAfterNow(@RequestParam String cron,
                                                   @RequestParam(defaultValue = "10") Integer count) throws Exception {
        List<String> values;
        try {
            values = CronTool.getNextValidTimeAfter(cron, count);
        } catch (ParseException e) {
            log.error("", e);
            throw new BizException(e.getMessage());
        }
        return ok(values);
    }
}
