package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: TODO
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/24 10:30
 */
public interface BrandService {

    /**
     * 查询所有品牌
     * @return 品牌集合
     */
    public List<TbBrand> queryAll();
}
