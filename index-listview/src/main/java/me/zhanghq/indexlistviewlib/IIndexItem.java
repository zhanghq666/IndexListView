package me.zhanghq.indexlistviewlib;

/**
 * 使用{@link DefaultIndexGenerator}时，实体类须实现词接口
 * Created by zhanghq on 2016/5/23.
 */
public interface IIndexItem {

    /**
     * 分组依据字段
     * @return
     */
    String getKeyword();
}
