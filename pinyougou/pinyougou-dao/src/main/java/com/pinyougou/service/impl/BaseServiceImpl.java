package com.pinyougou.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pinyougou.Service.BaseService;
import com.pinyougou.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: 通用service接口实现类
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/25 22:26
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {

    //从spring4.x开始引入泛型依赖注入
    @Autowired
    private Mapper<T> mapper;

    @Override
    public T findOne(Serializable id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> findAll() {
        return mapper.selectAll();
    }

    @Override
    public List<T> findByWhere(T t) {
        return mapper.select(t);
    }

    @Override
    public PageResult findPage(Integer page, Integer rows) {
        //设置分页参数
        PageHelper.startPage(page,rows);

        //查询所有
        List<T> list = mapper.selectAll();
        //创建分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public PageResult findPage(Integer page, Integer rows, T t) {
        //设置分页参数
        PageHelper.startPage(page,rows);

        //条件查询所有
        List<T> list = mapper.select(t);

        //创建分页信息对象
        PageInfo<T> pageInfo = new PageInfo<>(list);
        return new PageResult(pageInfo.getTotal(),pageInfo.getList());
    }

    @Override
    public void add(T t) {
        mapper.insertSelective(t);
    }

    @Override
    public void update(T t) {
        mapper.updateByPrimaryKeySelective(t);
    }

    @Override
    public void deleteByIds(Serializable[] ids) {
        if(ids != null && ids.length > 0) {
            for (Serializable id : ids) {
                mapper.deleteByPrimaryKey(id);
            }
        }
    }
}
