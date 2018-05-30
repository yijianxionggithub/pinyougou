package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.fabric.xmlrpc.base.Param;
import com.pinyougou.mapper.SpecificationMapper;
import com.pinyougou.mapper.SpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.sellergoods.service.SpecificationOptionService;
import com.pinyougou.sellergoods.service.SpecificationService;
import com.pinyougou.service.impl.BaseServiceImpl;
import com.pinyougou.vo.PageResult;
import com.pinyougou.vo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SpecificationService.class)
public class SpecificationServiceImpl extends BaseServiceImpl<TbSpecification> implements SpecificationService {

    @Autowired
    private SpecificationMapper specificationMapper;

    @Autowired
    private SpecificationOptionMapper specificationOptionMapper;

    @Override
    public PageResult search(Integer page, Integer rows, TbSpecification specification) {
        PageHelper.startPage(page, rows);

        Example example = new Example(TbSpecification.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(specification.getSpecName())){
            criteria.andLike("specName", "%" + specification.getSpecName() + "%");
        }

        List<TbSpecification> list = specificationMapper.selectByExample(example);
        PageInfo<TbSpecification> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public void add(Specification specification) {
        //新增规格
       specificationMapper.insertSelective(specification.getSpecification());

        //新增规格选项
        for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {
            tbSpecificationOption.setSpecId(specification.getSpecification().getId());
            specificationOptionMapper.insertSelective(tbSpecificationOption);
        }
    }

    /**
     * 根据id查询规格和规格项
     * @return  规格和规格项
     */
    @Override
    public Specification findOne(Long id) {
        Specification specification = new Specification();
        //查询规格
        specification.setSpecification(specificationMapper.selectByPrimaryKey(id));

        //查询对应规格项,规格在规格选项标中,以普通字段的形式存在,并没有外键

        TbSpecificationOption param = new TbSpecificationOption();
        param.setSpecId(id);
        List<TbSpecificationOption> specifiactionOptionList = specificationOptionMapper.select(param);
        specification.setSpecificationOptionList(specifiactionOptionList);
        return specification;
    }

    /**
     * 修改规格
     * @param specification
     */
    @Override
    public void update(Specification specification) {
        //修改规格
        super.update(specification.getSpecification());

        //删除该规格选项
        TbSpecificationOption param = new TbSpecificationOption();
        param.setSpecId(specification.getSpecification().getId());
        specificationOptionMapper.delete(param);

        if(specification.getSpecificationOptionList() != null && specification.getSpecificationOptionList().size() > 0) {
            //修改规格选项
            for (TbSpecificationOption tbSpecificationOption : specification.getSpecificationOptionList()) {

                tbSpecificationOption.setSpecId(specification.getSpecification().getId());
                specificationOptionMapper.insertSelective(tbSpecificationOption);
            }
        }
    }

    /**
     * 删除规格和规格选项
     * @param ids 要删除的id数组
     */
    @Override
    public void deleteByIds(Long[] ids) {
        //删除规格
        super.deleteByIds(ids);

        //批量删除规格项
        Example example = new Example(TbSpecificationOption.class);
        example.createCriteria().andIn("specId", Arrays.asList(ids));
        specificationOptionMapper.deleteByExample(example);
    }

    /**
     * 查询所有规格 格式:{data:[{'id':27,'text':'网络'},{'id':32,'text':'机身内存'}]}
     * @return
     */
    @Override
    public List<Map<String, Object>> findOptionList() {
        return specificationMapper.findOptionList();
    }
}
