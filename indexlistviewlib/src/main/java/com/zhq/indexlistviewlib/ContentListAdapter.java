package com.zhq.indexlistviewlib;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.Set;

/**
 * IndexList的适配器
 * Created by zhanghq on 2016/5/21.
 */
final class ContentListAdapter extends BaseAdapter {

    private Context mContext;

    private List mDataSource;

    private IIndexGenerator mIndexGenerator;
    private IItemViewGenerator mItemViewGenerator;

    ContentListAdapter(Context context, List dataSource, IIndexGenerator indexGenerator, IItemViewGenerator viewGenerator) {
        mContext = context;
        mDataSource = dataSource;
        mIndexGenerator = indexGenerator;
        if (mIndexGenerator == null) {
            mIndexGenerator = new DefaultIndexGenerator(dataSource);
        }

        mItemViewGenerator = viewGenerator;
        if (mItemViewGenerator == null) {
            mItemViewGenerator = new DefaultViewGenerator(mContext);
        }
    }

    public void setData(List dataSource) {
        mDataSource = dataSource;
        if (mIndexGenerator != null) {
            mIndexGenerator.setData(dataSource);
        }
        notifyDataSetChanged();
    }

    public void setData(List dataSource, IIndexGenerator indexGenerator, IItemViewGenerator viewGenerator) {
        mDataSource = dataSource;

        mIndexGenerator = indexGenerator;
        if (mIndexGenerator == null) {
            mIndexGenerator = new DefaultIndexGenerator(dataSource);
        }

        mItemViewGenerator = viewGenerator;
        if (mItemViewGenerator == null) {
            mItemViewGenerator = new DefaultViewGenerator(mContext);
        }

        if (mIndexGenerator != null) {
            mIndexGenerator.setData(dataSource);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mIndexGenerator != null ? mIndexGenerator.getTotalCount() : 0;
    }

    @Override
    public Object getItem(int position) {
        String currentSection = getCurrentSection(position);
        List childList = (List) mIndexGenerator.getSortedDataSource().get(currentSection);
        int sectionIndex = (int) mIndexGenerator.getSectionIndexMap().get(currentSection);
        int index = position - sectionIndex - 1;
        if (index >= 0) {
            return childList.get(index);
        } else {
            return currentSection;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String currentSection = getCurrentSection(position);

        int sectionIndex = (int) mIndexGenerator.getSectionIndexMap().get(currentSection);
        if (position == sectionIndex) {
            convertView = mItemViewGenerator.genSectionView(currentSection, convertView);
        } else {
            List childList = (List) mIndexGenerator.getSortedDataSource().get(currentSection);
            Object item = childList.get(position - sectionIndex - 1);
            convertView = mItemViewGenerator.genItemView(item, convertView);
        }
        return convertView;
    }

    private String getCurrentSection(int position) {
        String currentSection = "";
        Set<String> keySet = mIndexGenerator.getSectionIndexMap().keySet();
        String[] keyArray = new String[keySet.size()];
        keySet.toArray(keyArray);
        for (int i = keyArray.length - 1; i >= 0; i--) {
            if (position >= (int) mIndexGenerator.getSectionIndexMap().get(keyArray[i])) {
                currentSection = keyArray[i];
//                sectionIndex = (int) mIndexGenerator.getSectionIndexMap().get(keyArray[i]);
//                if (position == (int) mIndexGenerator.getSectionIndexMap().get(keyArray[i])) {
//                    isSection = true;
//                }
                break;
            }
        }

        return currentSection;
    }
}
