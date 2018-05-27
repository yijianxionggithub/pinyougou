package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.BrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: TODO
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/24 10:31
 */
@Service(interfaceClass = BrandService.class)
public class BrandServiceImpl extends BaseServiceImpl<TbBrand> implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    @Override
    @Deprecated
    public List<TbBrand> queryAll() {
        return brandMapper.queryAll();
    }

    @Override
    public List<TbBrand> testPage(Integer page, Integer rows) {
        PageHelper.startPage(page,rows);
        return brandMapper.selectAll();
    }

    @Override
    public PageResult search(Integer page, Integer rows, TbBrand tbBrand) {
        //设置分页查询
        PageHelper.startPage(page,rows);

        //创建Example
        Example example = new Example(TbBrand.class);
        Example.Criteria criteria = example.createCriteria();
        //封装查询条件
        if(StringUtils.isNotBlank(tbBrand.getName())) {
            criteria.andLike("name","%" + tbBrand.getName() +"%");
        }

        if(StringUtils.isNotBlank(tbBrand.getFirstChar())) {
            criteria.andEqualTo("firstChar",tbBrand.getFirstChar());
        }

        //条件查询
        List<TbBrand> tbBrandList = brandMapper.selectByExample(example);

        //创建分页信息对象
        PageInfo pageInfo = new PageInfo(tbBrandList);

        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }
}
