package org.gaia.be.controller;

import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.winterframework.core.support.ApiResponse;
import org.winterframework.core.tool.DateTool;

/**
 * @author qinglin.liu
 * @create 2023/9/27 13:31
 */
@RestController
@RequestMapping("/timeTransfer")
public class TimeTransferController extends BaseController {
    @GetMapping("/toEpochMilli")
    public ApiResponse<Long> transferToTimestamp(@RequestParam String datetime, @RequestParam String timezone) {
        DateTime dateTime = DateTool.parseTimeZoneRetainFields(datetime, timezone);
        return ok(dateTime.getMillis());
    }

    @GetMapping("/toDatetime")
    public ApiResponse<String> transferToDateTime(@RequestParam Long timestamp, @RequestParam String timezone) {
        DateTime dateTime = new DateTime(timestamp, DateTool.getDateTimeZone(timezone));
        return ok(dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }
}
