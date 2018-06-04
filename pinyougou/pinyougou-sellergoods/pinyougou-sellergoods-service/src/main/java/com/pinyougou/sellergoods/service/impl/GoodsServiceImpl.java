package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service(interfaceClass = GoodsService.class)
@Transactional
public class GoodsServiceImpl extends BaseServiceImpl<TbGoods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsDescMapper goodsDescMapper;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private ItemCatMapper itemCatMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbGoods goods) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andNotEqualTo("isDelete","1");
        if(!StringUtils.isEmpty(goods.getGoodsName())){
            criteria.andLike("goodsName", "%" + goods.getGoodsName() + "%");
        }

        if(!StringUtils.isEmpty(goods.getAuditStatus())) {
            criteria.andEqualTo("auditStatus",goods.getAuditStatus());
        }

        if(!StringUtils.isEmpty(goods.getSellerId())) {
            criteria.andEqualTo("sellerId",goods.getSellerId());
        }

        List<TbGoods> list = goodsMapper.selectByExample(example);
        PageInfo<TbGoods> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void addGoods(Goods goods) {
        //保存商品信息
        add(goods.getGoods());

        //新增商品描述信息
        goods.getGoodsDesc().setGoodsId(goods.getGoods().getId());
        goodsDescMapper.insertSelective(goods.getGoodsDesc());

        setItemList(goods);

    }

    /**
     * 设置商品sku
     * @param goods
     */
    private void setItemList(Goods goods) {
        if ("1".equals(goods.getGoods().getIsEnableSpec())) {
            if(goods.getItemList() != null && goods.getItemList().size() > 0) {
                //新增sku
                for (TbItem item : goods.getItemList()) {
                    //拼接sku
                    //获得商品名称
                    String title = goods.getGoods().getGoodsName();
                    //判断是否启动规格

                    //获得规格选项
                    String itemSpec = item.getSpec();

                    //拼接sku
                    Map<String, Object> map = JSONArray.parseObject(itemSpec, Map.class);
                    Set<Map.Entry<String, Object>> entries = map.entrySet();
                    for (Map.Entry<String, Object> entry : entries) {
                        String value = entry.getValue().toString();
                        title += " " + value;
                    }

                    //设置sku标题
                    item.setTitle(title);
                    addItem(goods, item);

                    itemMapper.insertSelective(item);
                }
            }
        } else {
            TbItem item = new TbItem();
            item.setTitle(goods.getGoods().getGoodsName());
            item.setPrice(goods.getGoods().getPrice());
            item.setNum(9999);
            item.setStatus("0");
            item.setIsDefault("1");

            addItem(goods,item);
            itemMapper.insertSelective(item);

        }
    }

    @Override
    public Goods findGoods(Long id) {
        //查询商品基本信息
        Goods goods = new Goods();
        goods.setGoods(findOne(id));

        //查询商品描述
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);

        //查询商品sku
        TbItem item = new TbItem();
        item.setGoodsId(goods.getGoods().getId());
        List<TbItem> itemList = itemMapper.select(item);

        goods.setItemList(itemList);

        return goods;
    }

    /**
     * 修改商品信息
     * @param goods 商品基本信息,描述,sku
     */
    @Override
    public void updateGoods(Goods goods) {
        goods.getGoods().setAuditStatus("0");
        update(goods.getGoods());

        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

        //删除原有的sku
        TbItem item = new TbItem();
        item.setGoodsId(goods.getGoods().getId());
        itemMapper.delete(item);

        setItemList(goods);
    }

    /**
     * 批量提交审核商品
     * @param ids 要审核的商品id
     * @param status 审核的状态
     */
    @Override
    public void updateStatus(Long[] ids, String status) {
        //设置状态
        TbGoods goods = new TbGoods();
        goods.setAuditStatus(status);

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(goods,example);

        //修改sku状态为1
        if("2".equals(status)) {
            TbItem item = new TbItem();
            item.setStatus("1");
            Example itemExample = new Example(TbItem.class);
            itemExample.createCriteria().andIn("goodsId",Arrays.asList(ids));
            itemMapper.updateByExampleSelective(item,itemExample);
        }
    }

    @Override
    public void deleteGoodsByIds(Long[] ids) {
        TbGoods goods = new TbGoods();
        goods.setIsDelete("1");

        Example example = new Example(TbGoods.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(goods,example);
    }

    @Override
    public void updateMarketable(Long[] ids, String status) {
        TbGoods goods = new TbGoods();
        goods.setIsMarketable(status);

        //关闭商品
        if("0".equals(status)) {
            goods.setAuditStatus("4");
        }

        Example example = new Example(TbGoods.class);
        example.createCriteria().andIn("id",Arrays.asList(ids));
        goodsMapper.updateByExampleSelective(goods,example);
    }

    /**
     * 设置itme 的属性值
     *
     * @param goods
     * @param item
     */
    private void addItem(Goods goods, TbItem item) {

        //查询品牌
        TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
        item.setBrand(tbBrand.getName());

        //查询三级分类
        if(goods.getGoods().getCategory3Id() != null) {
            TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
            item.setCategory(itemCat.getName());
        }

        //设置分类id
        item.setCategoryid(goods.getGoods().getCategory3Id());

        //设置创建时间
        item.setCreateTime(new Date());

        //设置更新时间
        item.setUpdateTime(item.getCreateTime());

        //设置商品id,商品已保存,存在编号
        item.setGoodsId(goods.getGoods().getId());

        //设置商家id
        TbSeller tbSeller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
        item.setSellerId(tbSeller.getName());

        //设置图片
        String itemImages = goods.getGoodsDesc().getItemImages();
        List<Map> imageList = JSONArray.parseArray(itemImages, Map.class);
        if (imageList != null && imageList.size() > 0) {
            item.setImage(imageList.get(0).get("url").toString());
        }
    }
}
