package org.gaia.be.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.gaia.be.dao.PassportInfoMapper;
import org.gaia.be.model.param.PassportInfoCreateParam;
import org.gaia.be.model.param.PassportInfoQueryParam;
import org.gaia.be.model.param.PassportInfoUpdateParam;
import org.gaia.be.model.po.PassportInfoPo;
import org.gaia.be.model.vo.PassportInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.winterframework.core.tool.BeanTool;
import org.winterframework.mybatis.plus.tool.PageTool;

/**
 * @author qinglin.liu
 * created at 2024/3/19 16:16
 */
@Service
public class PassportInfoService {
    @Autowired
    private PassportInfoMapper passportInfoMapper;

    public PassportInfoVo createPassportInfo(PassportInfoCreateParam params) {
        PassportInfoPo passportInfoPo = BeanTool.copyAs(params, PassportInfoPo.class);
        passportInfoMapper.insert(passportInfoPo);
        return BeanTool.copyAs(passportInfoPo, PassportInfoVo.class);
    }

    public int updateById(PassportInfoUpdateParam params) {
        PassportInfoPo passportInfoPo = BeanTool.copyAs(params, PassportInfoPo.class);
        return passportInfoMapper.updateById(passportInfoPo);
    }

    public int deleteById(Long id) {
        return passportInfoMapper.deleteById(id);
    }

    public Page<PassportInfoVo> listPassportInfoByPage(PassportInfoQueryParam query) {
        Page<PassportInfoPo> page = Page.of(query.getPage(), query.getPageSize());
        LambdaQueryWrapper<PassportInfoPo> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(query.getTitle())) {
            lambdaQueryWrapper.like(PassportInfoPo::getTitle, query.getTitle());
        }
        lambdaQueryWrapper.orderByDesc(PassportInfoPo::getId);
        Page<PassportInfoPo> result = passportInfoMapper.selectPage(page, lambdaQueryWrapper);
        return PageTool.convert(result, PassportInfoVo.class);
    }

    public PassportInfoVo getPassportInfoById(Long id) {
        PassportInfoPo passportInfoPo = passportInfoMapper.selectById(id);
        if (passportInfoPo == null) {
            return null;
        }
        return BeanTool.copyAs(passportInfoPo, PassportInfoVo.class);
    }
}
