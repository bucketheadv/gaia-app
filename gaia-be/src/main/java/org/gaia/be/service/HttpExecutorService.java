package org.gaia.be.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.gaia.be.dao.mapper.HttpExecutorMapper;
import org.gaia.be.enums.CallTypeEnum;
import org.gaia.be.model.param.HttpExecutorCreateParam;
import org.gaia.be.model.param.HttpExecutorQueryParam;
import org.gaia.be.model.param.HttpExecutorUpdateParam;
import org.gaia.be.model.po.HttpExecutorPo;
import org.gaia.be.model.vo.HttpExecutorVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.winterframework.core.tool.BeanTool;
import org.winterframework.mybatis.plus.tool.PageTool;

import java.util.Objects;

/**
 * @author qinglin.liu
 * created at 2024/3/19 15:24
 */
@Service
public class HttpExecutorService {
    @Autowired
    private HttpExecutorMapper httpExecutorMapper;

    public Page<HttpExecutorVo> listApiEventByPage(HttpExecutorQueryParam query) {
        Page<HttpExecutorPo> page = Page.of(query.getPage(), query.getPageSize());
        LambdaQueryWrapper<HttpExecutorPo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(query.getName())) {
            lambdaQueryWrapper.like(HttpExecutorPo::getName, query.getName());
        }
        if (!Objects.isNull(query.getCallType())) {
            lambdaQueryWrapper.eq(HttpExecutorPo::getCallType, CallTypeEnum.parse(query.getCallType()));
        }
        lambdaQueryWrapper.orderByDesc(HttpExecutorPo::getId);

        Page<HttpExecutorPo> pageResult = httpExecutorMapper.selectPage(page, lambdaQueryWrapper);
        return PageTool.convert(pageResult, HttpExecutorVo.class);
    }

    public HttpExecutorVo getApiEventById(Long id) {
        HttpExecutorPo httpExecutorPO = httpExecutorMapper.selectById(id);
        return BeanTool.copyAs(httpExecutorPO, HttpExecutorVo.class);
    }

    public HttpExecutorVo createApiEvent(HttpExecutorCreateParam params) {
        HttpExecutorPo httpExecutorPO = BeanTool.copyAs(params, HttpExecutorPo.class);
        httpExecutorMapper.insert(httpExecutorPO);
        return BeanTool.copyAs(httpExecutorPO, HttpExecutorVo.class);
    }

    public HttpExecutorVo updateApiEvent(HttpExecutorUpdateParam params) {
        HttpExecutorPo httpExecutorPO = BeanTool.copyAs(params, HttpExecutorPo.class);
        int count = httpExecutorMapper.updateById(httpExecutorPO);
        if (count > 0) {
            return BeanTool.copyAs(httpExecutorPO, HttpExecutorVo.class);
        }
        return null;
    }

    public int deleteApiEvent(Long id) {
        return httpExecutorMapper.deleteById(id);
    }
}
