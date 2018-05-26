package com.pinyougou.sellergoods.service;

import com.pinyougou.Service.BaseService;
import com.pinyougou.pojo.TbBrand;

import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: TODO
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/24 10:30
 */
public interface BrandService extends BaseService<TbBrand> {
    /**
     * 查询所有品牌
     * @return 品牌集合
     */
    @Deprecated
    public List<TbBrand> queryAll();

    /**
     * 整合通过Mapper和分页助手测试
     */
    public List<TbBrand> testPage(Integer page, Integer rows);

}
