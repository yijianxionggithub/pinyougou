package com.pinyougou.mapper;

import com.pinyougou.pojo.TbSpecification;

import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface SpecificationMapper extends Mapper<TbSpecification> {

    /**
     * 查询所有规格 格式:{data:[{'id':27,'text':'网络'},{'id':32,'text':'机身内存'}]}
     * @return
     */
    List<Map<String,Object>> findOptionList();
}
