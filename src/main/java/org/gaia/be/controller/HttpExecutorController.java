package org.gaia.be.controller;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonParseException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.gaia.be.enums.CallTypeEnum;
import org.gaia.be.exceptions.BizException;
import org.gaia.be.model.dto.EventParam;
import org.gaia.be.model.param.HttpExecutorCreateParam;
import org.gaia.be.model.param.HttpExecutorExecuteParam;
import org.gaia.be.model.param.HttpExecutorQueryParam;
import org.gaia.be.model.param.HttpExecutorUpdateParam;
import org.gaia.be.model.po.HttpExecuteLogPo;
import org.gaia.be.model.vo.HttpExecutorVo;
import org.gaia.be.service.HttpExecuteLogService;
import org.gaia.be.service.HttpExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.winterframework.core.support.ApiResponse;
import org.winterframework.core.tool.JSONTool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:16
 */
@Slf4j
@RestController
@RequestMapping("/executor")
public class HttpExecutorController extends BaseController {
    @Autowired
    private HttpExecutorService httpExecutorService;
    @Autowired
    private HttpExecuteLogService httpExecuteLogService;

    @PostMapping("/list")
    public ApiResponse<Page<HttpExecutorVo>> listApiExecutors(@RequestBody HttpExecutorQueryParam query) {
        return ok(httpExecutorService.listApiEventByPage(query));
    }

    @GetMapping("/info")
    public ApiResponse<HttpExecutorVo> getInfo(@RequestParam Long id) {
        return ok(httpExecutorService.getApiEventById(id));
    }

    @PostMapping("/create")
    public ApiResponse<HttpExecutorVo> createEvent(@Valid @RequestBody HttpExecutorCreateParam params) {
        HttpExecutorVo httpExecutorVo = httpExecutorService.createApiEvent(params);
        return ok(httpExecutorVo);
    }

    @PostMapping("/update")
    public ApiResponse<HttpExecutorVo> updateEvent(@Valid @RequestBody HttpExecutorUpdateParam params) {
        HttpExecutorVo httpExecutorVo = httpExecutorService.updateApiEvent(params);
        return ok(httpExecutorVo);
    }

    @PostMapping("/delete")
    public ApiResponse<Boolean> deleteEvent(@RequestParam Long id) {
        boolean deleted = httpExecutorService.deleteApiEvent(id) > 0;
        return ok(deleted);
    }

    @PostMapping("/execute")
    public Object execute(@Valid @RequestBody HttpExecutorExecuteParam params) throws IOException {
        HttpExecutorVo httpExecutorVo = httpExecutorService.getApiEventById(params.getId());
        if (Objects.isNull(httpExecutorVo)) {
            throw new BizException("ApiExecutor不存在");
        }
        if (!Boolean.TRUE.equals(httpExecutorVo.getOnlineStatus())) {
            throw new BizException("ApiExecutor未上线");
        }

        for (EventParam httpParam : httpExecutorVo.getHttpParams()) {
            Object value = params.getParams().get(httpParam.getName());
            if (Boolean.TRUE.equals(httpParam.getRequired()) && Objects.isNull(value)) {
                throw new BizException("参数错误!");
            }
        }
        for (EventParam httpHeader : httpExecutorVo.getHttpHeaders()) {
            Object value = params.getHeaders().get(httpHeader.getName());
            if (Boolean.TRUE.equals(httpHeader.getRequired()) && Objects.isNull(value)) {
                throw new BizException("参数错误!");
            }
        }

        CallTypeEnum callTypeEnum = httpExecutorVo.getCallType();
        String url = null;
        if (callTypeEnum == CallTypeEnum.HTTP_CALL) {
            url = httpExecutorVo.getUrl();
        } else if (callTypeEnum == CallTypeEnum.MICRO_SERVICE) {
            url = httpExecutorVo.getServiceName() + httpExecutorVo.getUrl();
        }

        assert url != null;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .callTimeout(httpExecutorVo.getTimeout(), TimeUnit.SECONDS)
                .connectTimeout(httpExecutorVo.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(httpExecutorVo.getTimeout(), TimeUnit.SECONDS)
                .build();

        List<String> queryList = new ArrayList<>();
        if ("GET".equalsIgnoreCase(httpExecutorVo.getHttpMethod()) && !CollectionUtils.isEmpty(params.getParams())) {
            for (String s : params.getParams().keySet()) {
                queryList.add(s + "=" + params.getParams().get(s));
            }
            if (url.indexOf('?') < 0) {
                url += '?';
            }
            url += String.join("&", queryList);
        }

        Request.Builder requestBuilder = new Request.Builder().url(url);


        okhttp3.RequestBody requestBody = null;
        if (!"GET".equalsIgnoreCase(httpExecutorVo.getHttpMethod())) {
            String contentType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
            MediaType mediaType = MediaType.parse(contentType);
            if (!CollectionUtils.isEmpty(params.getHeaders())) {
                for (String s : params.getHeaders().keySet()) {
                    String v = String.valueOf(params.getHeaders().get(s));
                    if ("Content-Type".equalsIgnoreCase(s)) {
                        MediaType tmpMediaType = MediaType.parse(v);
                        if (tmpMediaType != null) {
                            mediaType = tmpMediaType;
                            contentType = s;
                        }
                    }
                    requestBuilder = requestBuilder.addHeader(s, v);
                }
            }
            if (contentType.contains(org.springframework.http.MediaType.APPLICATION_JSON_VALUE)) {
                String body = JSONTool.toJSONString(params.getParams());
                requestBody = okhttp3.RequestBody.create(body, mediaType);
            } else {
                String body = HttpUtil.toParams(params.getParams());
                requestBody = okhttp3.FormBody.create(body, mediaType);
            }
        } else {
            if (!CollectionUtils.isEmpty(params.getHeaders())) {
                for (String s : params.getHeaders().keySet()) {
                    String v = String.valueOf(params.getHeaders().get(s));
                    requestBuilder = requestBuilder.addHeader(s, v);
                }
            }
        }

        requestBuilder = requestBuilder.method(httpExecutorVo.getHttpMethod(), requestBody);

        HttpExecuteLogPo httpExecuteLogPo = new HttpExecuteLogPo();
        httpExecuteLogPo.setExecutorId(params.getId());
        httpExecuteLogPo.setUrl(httpExecutorVo.getUrl());
        httpExecuteLogPo.setHttpMethod(httpExecutorVo.getHttpMethod());
        httpExecuteLogPo.setHttpHeaders(JSONTool.toJSONString(params.getHeaders()));
        httpExecuteLogPo.setHttpParams(JSONTool.toJSONString(params.getParams()));
        httpExecuteLogPo = httpExecuteLogService.createApiExecuteLog(httpExecuteLogPo);
//        apiExecuteLogPo.setExecuteUser();
        long startTime = System.currentTimeMillis();
        try (Response response = okHttpClient.newCall(requestBuilder.build()).execute();) {
            ResponseBody responseBody = response.body();
            assert responseBody != null;
            String result = responseBody.string();

            httpExecuteLogPo.setExecuteResponse(result);
            httpExecuteLogPo.setExecuteResult(1);
            updateApiExecuteLog(httpExecuteLogPo, startTime);

            try {
                return JSONTool.parseObject(result, Map.class);
            } catch (RuntimeException e1) {
                if (e1.getCause() instanceof JsonParseException) {
                    log.error("解析json失败: {}", e1.getMessage());
                } else {
                    log.error("解析json失败: ", e1);
                }
                return result;
            }
        } catch (Exception e) {
            httpExecuteLogPo.setExecuteResult(2);
            httpExecuteLogPo.setExecuteResponse(e.getMessage());
            updateApiExecuteLog(httpExecuteLogPo, startTime);
            throw e;
        }
    }

    private void updateApiExecuteLog(HttpExecuteLogPo httpExecuteLogPo, long startTime) {
        httpExecuteLogPo.setDuration(System.currentTimeMillis() - startTime);
        httpExecuteLogService.updateApiExecuteLogById(httpExecuteLogPo);
    }
}
