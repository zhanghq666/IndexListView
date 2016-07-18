package com.zhq.indexlistviewlib;

import java.util.List;
import java.util.Map;

/**
 * 实现此接口完成自定义的分组、排序、索引操作
 * Created by zhanghq on 2016/5/23.
 */
public interface IIndexGenerator<T> {

    /**
     * 核心处理方法，在这里要完成数据的分组和排序
     * @param data
     */
    void setData(List<T> data);

    /**
     * 返回分组排序后的数据集合
     * @return
     */
    Map<String, List<T>> getSortedDataSource();

    /**
     * 返回索引字符串列表
     * @return
     */
    List<String> getIndexList();

    /**
     * 返回索引在ListView中的位置Map
     * @return
     */
    Map<String, Integer> getSectionIndexMap();

    /**
     * 返回所有条数（包括Section和Child）
     * @return
     */
    int getTotalCount();
}
