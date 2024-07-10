package org.gaia.be.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.gaia.be.dao.mapper.HttpExecuteLogMapper;
import org.gaia.be.model.param.HttpExecuteLogQueryParam;
import org.gaia.be.model.po.HttpExecuteLogPo;
import org.gaia.be.model.vo.HttpExecuteLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.winterframework.mybatis.plus.tool.PageTool;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:09
 */
@Service
public class HttpExecuteLogService {
    @Autowired
    private HttpExecuteLogMapper httpExecuteLogMapper;

    public HttpExecuteLogPo createApiExecuteLog(HttpExecuteLogPo httpExecuteLogPo) {
        httpExecuteLogMapper.insert(httpExecuteLogPo);
        return httpExecuteLogPo;
    }

    public boolean updateApiExecuteLogById(HttpExecuteLogPo httpExecuteLogPo) {
        return httpExecuteLogMapper.updateById(httpExecuteLogPo) > 0;
    }

    public Page<HttpExecuteLogVo> listApiExecuteLogByPage(HttpExecuteLogQueryParam params) {
        Page<HttpExecuteLogPo> page = Page.of(params.getPage(), params.getPageSize());
        LambdaQueryWrapper<HttpExecuteLogPo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(HttpExecuteLogPo::getExecutorId, params.getExecutorId());
        queryWrapper.orderByDesc(HttpExecuteLogPo::getId);
        Page<HttpExecuteLogPo> result = httpExecuteLogMapper.selectPage(page, queryWrapper);
        return PageTool.convert(result, HttpExecuteLogVo.class);
    }
}
