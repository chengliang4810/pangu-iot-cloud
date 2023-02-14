package com.pangu.common.mybatis.core.page;

import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表格分页数据对象
 *
 * @author chengliang4810
 */

@Data
@NoArgsConstructor
public class TableDataInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 数据
     * rows = 分页数据
     * total = 总记录数
     */
    private Map<String, Object> data = new HashMap<>(3);

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, long total) {
        this.data.put("rows", list);
        this.data.put("total", total);
    }

    public static <T> TableDataInfo<T> build(IPage<T> page) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.data.put("rows", page.getRecords());
        rspData.data.put("total", page.getTotal());
        return rspData;
    }

    public static <T> TableDataInfo<T> build(List<T> list) {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        rspData.data.put("rows", list);
        rspData.data.put("total", list.size());
        return rspData;
    }

    public static <T> TableDataInfo<T> build() {
        TableDataInfo<T> rspData = new TableDataInfo<>();
        rspData.setCode(HttpStatus.HTTP_OK);
        rspData.setMsg("查询成功");
        return rspData;
    }

    /**
     * 设置列表数据
     *
     * @param list 列表
     */
    public void setRows(List<T> list){
        this.data.put("rows", list);
    }

    /**
     * 设置总条数
     *
     * @param size 大小
     */
    public void setTotal(int size) {
        this.data.put("total", size);
    }

    /**
     * 设置总条数
     *
     * @param size 大小
     */
    public void setTotal(long size) {
        this.data.put("total", size);
    }
}
