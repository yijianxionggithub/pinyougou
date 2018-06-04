package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbGoods;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;

public interface GoodsService extends BaseService<TbGoods> {

    PageResult search(Integer page, Integer rows, TbGoods goods);

    /**
     * 新增商品信息
     * @param goods  商品信息
     */
    void addGoods(Goods goods);

    /**
     * 根据id查询商品
     * @param id 商品编号
     * @return 商品goods
     */
    Goods findGoods(Long id);

    /**
     * 修改商品信息
     * @param goods 商品基本信息,描述,sku
     */
    void updateGoods(Goods goods);

    /**
     * 批量提交审核商品
     * @param ids 要审核的商品id
     * @param status 审核的状态
     */
    void updateStatus(Long[] ids, String status);

    /**
     * 将isDelete 设置为1
     * @param ids 删除的商品编号
     */
    void deleteGoodsByIds(Long[] ids);

    /**
     * 上架下架商品
     * @param ids 要操作的商品编号
     * @param status 上架为'1',下架为'0'
     */
    void updateMarketable(Long[] ids, String status);
}