package com.pinyougou.Service;

import com.pinyougou.vo.PageResult;

import java.io.Serializable;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: 通用service接口
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/25 22:11
 */
public interface BaseService<T> {

    /**
     * 根据主键查询一个对象
     * @param id 主键
     * @return 一个对象
     */
    public T findOne(Serializable id);

    /**
     * 查询所有数据
     * @return 集合
     */
    public List<T> findAll();

    /**
     * 条件查询
     * @param t 条件对象
     * @return 集合
     */
    public List<T> findByWhere(T t);

    /**
     * 分页查询
     * @param page 当前页
     * @param rows 页面大小
     * @return 分页对象
     */
    public PageResult findPage(Integer page, Integer rows);

    /**
     * 分页加条件
     * @param page 当前页
     * @param rows 页面大小
     * @param t 条件
     * @return 分页对象
     */
    public PageResult findPage(Integer page, Integer rows, T t);

    /**
     * 选择性增加一个条数据
     * @param  t 插入的对象
     */
    public void add(T t);

    /**
     * 选择性修改一条数据
     * @param t 修改对象,带主键
     */
    public void update(T t);

    /**
     * 批量删除数据
     * @param ids 要删除的id数组
     */
    public void deleteByIds(Serializable[] ids);
}
