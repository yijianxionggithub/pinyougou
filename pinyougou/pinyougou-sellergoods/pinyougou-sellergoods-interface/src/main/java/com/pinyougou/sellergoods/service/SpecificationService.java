package com.pinyougou.sellergoods.service;

import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.service.BaseService;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface SpecificationService extends BaseService<TbSpecification> {

    PageResult search(Integer page, Integer rows, TbSpecification specification);

    /**
     * 新增规格项
     * @param specification 规格和规格选项
     */
    void add(Specification specification);

    /**
     * 根据id查询规格和规格项
     * @return  规格和规格项
     */
    Specification findOne(Long id);

    /**
     * 修改规格
     * @param specification
     */
    void update(Specification specification);

    /**
     * 查询所有规格 格式:{data:[{'id':27,'text':'网络'},{'id':32,'text':'机身内存'}]}
     * @return
     */
    List<Map<String,Object>> findOptionList();

    /**
     * 批量删除
     * @param ids 要删除的id数组
     */
    void deleteByIds(Long[] ids);
}