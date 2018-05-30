package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.applet.Main;

import java.util.List;
import java.util.Map;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: TODO
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/24 10:41
 */
@RequestMapping("brand")
@RestController
public class BrandController {

    //设置最大超时时间10秒
    @Reference(timeout = 10000)
    private BrandService brandService;

    @GetMapping("findAll")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }

    @GetMapping("testPage")
    public List<TbBrand> testPage(Integer page,Integer rows){
        return brandService.findPage(page,rows).getRows();
    }

    /**
     * 分页查询所有列表
     * @param page 当前页号
     * @param rows 页面大小
     * @return
     */
    @GetMapping("findPage")
    public PageResult findPage(@RequestParam(name= "page",defaultValue = "1") Integer page,@RequestParam(name= "rows",defaultValue = "10")Integer rows) {
        return brandService.findPage(page, rows);
    }

    /**
     * 新增一条数据
     * @param tbBrand 品牌对象
     * @return 操作结果
     */
    @PostMapping("add")
    public Result add(@RequestBody TbBrand tbBrand) {
        try {
            brandService.add(tbBrand);
            return Result.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("新增失败");
    }

    /**
     * 根据id查询一条数据
     * @param id 品牌编号
     * @return 品牌对象
     */
    @GetMapping("findOne")
    public TbBrand findOne(Long id) {
        return brandService.findOne(id);
    }

    /**
     * 更新品牌
     * @param tbBrand 品牌对象
     * @return 操作结果
     */
    @PostMapping("update")
    public Result update(@RequestBody TbBrand tbBrand) {
        try {
            brandService.update(tbBrand);
            return Result.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("修改失败");
    }

    /**
     * 批量删除品牌
     * @param ids 品牌id数组
     * @return 操作结果
     */
    @RequestMapping("delete")
    public Result delete(Long[] ids) {
        try {
            brandService.deleteByIds(ids);
            return Result.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("删除失败");
    }

    @PostMapping("search")
    public PageResult search(@RequestParam(name="page",defaultValue = "1") Integer page,
                             @RequestParam(name="rows",defaultValue = "10") Integer rows,@RequestBody TbBrand tbBrand){
        PageResult pageResult = brandService.search(page,rows,tbBrand);
        return pageResult;
    }

    /**
     * 查询所有品牌
     */
    @GetMapping("findOptionList")
    public List<Map<String,Object>> findOptionList() {
        return brandService.findOptionList();
    }
}
