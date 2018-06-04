package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;

import java.util.List;
import java.util.Map;

public interface TypeTemplateService extends BaseService<TbTypeTemplate> {

    PageResult search(Integer page, Integer rows, TbTypeTemplate typeTemplate);

    /**
     * 在类型模板根据规格编号查询规格和对应的规格项
     * @param id 类型模板的id
     * @return   {"id":10,"text":"网络","options":[{id,specName},{id,specName}]}
     */
    List<Map> findSpecList(Long id);

    /**
     * 查询所有分类模板
     * @return
     */
    List<Map<String,Object>> findTypeTemplateList();
}