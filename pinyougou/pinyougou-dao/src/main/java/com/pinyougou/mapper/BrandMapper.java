package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: TODO
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/24 10:23
 */
public interface BrandMapper {

    /**
     * 查询所有品牌
     * @return
     */
    public List<TbBrand> queryAll();
}
