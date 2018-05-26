package com.pinyougou.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import sun.applet.Main;

import java.util.List;

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
}
