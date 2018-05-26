package com.pinyougou.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @version V1.0
 * @ClassName: ValidateActionJson
 * @Description: 分页对象
 * @Auther: Jianxiong Yi
 * @Date: 2018/5/25 22:19
 */
public class PageResult<T> implements Serializable {
    //总记录数
    private long total;
    //数据列表
    private List<T> rows;

    public PageResult(long total,List<T> rows) {
        this.rows = rows;
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public long getTotal() {
        return total;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
