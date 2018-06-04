package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.mapper.TypeTemplateMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellergoods.service.TypeTemplateService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = TypeTemplateService.class)
public class TypeTemplateServiceImpl extends BaseServiceImpl<TbTypeTemplate> implements TypeTemplateService {

    @Autowired
    private TypeTemplateMapper typeTemplateMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbTypeTemplate typeTemplate) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbTypeTemplate.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(typeTemplate.getName())){
            criteria.andLike("name", "%" + typeTemplate.getName() + "%");
        }

        List<TbTypeTemplate> list = typeTemplateMapper.selectByExample(example);
        PageInfo<TbTypeTemplate> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public List<Map> findSpecList(Long id) {
        //查询模板id查询规格
        TbTypeTemplate typeTemplate = typeTemplateMapper.selectByPrimaryKey(id);

        //获得规格
        String specIds = typeTemplate.getSpecIds();
        //转成List
        List<Map> specList = JSONArray.parseArray(specIds.toString(), Map.class);

        for (Map map : specList) {
            TbSpecificationOption param = new TbSpecificationOption();
            param.setSpecId(Long.parseLong(map.get("id").toString()));
            //查询规格的所有规格选项
            List<TbSpecificationOption> options = specificationOptionMapper.select(param);
            map.put("options",options);
        }
        return specList;
    }

    @Override
    public List<Map<String,Object>> findTypeTemplateList() {
        return typeTemplateMapper.findTypeTemplateList();
    }
}
