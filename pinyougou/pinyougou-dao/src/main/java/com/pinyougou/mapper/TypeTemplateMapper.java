package com.pinyougou.mapper;

import com.pinyougou.pojo.TbTypeTemplate;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TypeTemplateMapper extends Mapper<TbTypeTemplate> {

    /**
     * 查询所有类型模板[{id:10,text:手机}]
     * @return
     */
    List<Map<String,Object>> findTypeTemplateList();
}
