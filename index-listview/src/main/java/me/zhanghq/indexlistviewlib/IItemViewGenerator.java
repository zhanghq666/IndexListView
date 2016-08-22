package me.zhanghq.indexlistviewlib;

import android.view.View;

/**
 * 继承此接口实现自定义的组头视图、子项视图的绘制赋值
 * Created by zhanghq on 2016/5/26.
 */
public interface IItemViewGenerator {

    /**
     * 生成组视图
     * @param sectionName   对应位置的组名
     * @param convertView
     * @return
     */
    View genSectionView(String sectionName, View convertView);

    /**
     * 生成子项视图
     * @param item          对应位置的子项实例
     * @param convertView
     * @return
     */
    View genItemView(Object item, View convertView);
}
