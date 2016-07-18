package com.zhq.indexlistviewlib;

import android.content.Context;
import android.view.View;
import android.widget.TextView;


/**
 * 默认的ItemView生成器
 * Created by zhanghq on 2016/5/26.
 */
final class DefaultViewGenerator implements IItemViewGenerator {

    private Context mContext;

    DefaultViewGenerator(Context context) {
        mContext = context;
    }

    @Override
    public View genSectionView(String sectionName, View convertView) {
        SectionViewHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof SectionViewHolder) {
            viewHolder = (SectionViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.item_section, null);
            viewHolder = new SectionViewHolder();

            viewHolder.mSectionTv = (TextView) convertView.findViewById(R.id.tv_section);

            convertView.setTag(viewHolder);
        }

        viewHolder.mSectionTv.setText(sectionName);

        return convertView;
    }

    @Override
    public View genItemView(Object item, View convertView) {
        ItemViewHolder viewHolder;
        if (convertView != null && convertView.getTag() instanceof ItemViewHolder) {
            viewHolder = (ItemViewHolder) convertView.getTag();
        } else {
            convertView = View.inflate(mContext, R.layout.item_child, null);
            viewHolder = new ItemViewHolder();

            viewHolder.mItemTv = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(viewHolder);
        }

        if (item instanceof IIndexItem) {
            viewHolder.mItemTv.setText(((IIndexItem) item).getKeyword());
        }
        return convertView;
    }

    class SectionViewHolder {
        TextView mSectionTv;
    }

    class ItemViewHolder {
        TextView mItemTv;
    }
}
