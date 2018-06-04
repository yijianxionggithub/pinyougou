package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.vo.Goods;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/goods")
@RestController
public class GoodsController {

    @Reference
    private GoodsService goodsService;

    @RequestMapping("/findAll")
    public List<TbGoods> findAll() {
        return goodsService.findAll();
    }

    @GetMapping("/findPage")
    public PageResult findPage(@RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        return goodsService.findPage(page, rows);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Goods goods) {
        try {
            //获得商家编号
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goods.getGoods().setSellerId(sellerId);
            goods.getGoods().setAuditStatus("0");//未申请审核

            goodsService.addGoods(goods);
            return Result.ok("增加成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("增加失败");
    }

    @GetMapping("/findOne")
    public Goods findOne(Long id) {
        return goodsService.findGoods(id);
    }

    /**
     * 修改商品信息
     * @param goods 商品基本信息,描述,sku
     */
    @PostMapping("/update")
    public Result update(@RequestBody Goods goods) {
        try {
            //防止篡改
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            TbGoods oldGood = goodsService.findOne(goods.getGoods().getId());

            if(!sellerId.equals(oldGood.getSellerId()) || !sellerId.equals(goods.getGoods().getSellerId())){
                return Result.fail("非法修改");
            }
            goodsService.updateGoods(goods);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    @GetMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            goodsService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    /**
     * 分页查询列表
     * @param goods 查询条件
     * @param page 页号
     * @param rows 每页大小
     * @return
     */
    @PostMapping("/search")
    public PageResult search(@RequestBody  TbGoods goods, @RequestParam(value = "page", defaultValue = "1")Integer page,
                               @RequestParam(value = "rows", defaultValue = "10")Integer rows) {
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);;
        return goodsService.search(page, rows, goods);
    }

    /**
     * 批量提交审核商品
     * @param ids 要审核的商品id
     * @param status 审核的状态
     */
    @GetMapping("/updateStatus")
    public Result updateStatus(Long[] ids,String status) {
        try {
            goodsService.updateStatus(ids,status);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok("修改失败");
    }

    /**
     * 上架下架商品
     * @param ids 要操作的商品编号
     * @param status 上架为'1',下架为'0'
     * @return 操作结果
     */
    @GetMapping("/updateMarketable")
    public Result updateMarketable(Long[] ids,String status) {
        try {
            goodsService.updateMarketable(ids,status);
            return Result.ok("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("操作失败");
    }

}
