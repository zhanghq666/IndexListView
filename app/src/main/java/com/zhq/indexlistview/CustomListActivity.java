package com.zhq.indexlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhq.indexlistview.bean.CustomItem;
import me.zhanghq.indexlistviewlib.HanziToPinyin;
import me.zhanghq.indexlistviewlib.IIndexGenerator;
import me.zhanghq.indexlistviewlib.IItemViewGenerator;
import me.zhanghq.indexlistviewlib.IndexListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanghq on 2016/7/16.
 */
public class CustomListActivity extends AppCompatActivity {

    private final int[] DRAWABLE_BM_ARRAY = new int[]{
            R.mipmap.bm_1, R.mipmap.bm_2, R.mipmap.bm_3, R.mipmap.bm_4, R.mipmap.bm_5
    };
    private final int[] DRAWABLE_CQ_ARRAY = new int[]{
            R.mipmap.cq_1, R.mipmap.cq_2, R.mipmap.cq_3, R.mipmap.cq_4
    };
    private final int[] DRAWABLE_DM_ARRAY = new int[]{
            R.mipmap.dm_1, R.mipmap.dm_2, R.mipmap.dm_3
    };
    private final int[] DRAWABLE_HSQ_ARRAY = new int[]{
            R.mipmap.hsq_1, R.mipmap.hsq_2, R.mipmap.hsq_3, R.mipmap.hsq_4, R.mipmap.hsq_5
    };
    private final int[] DRAWABLE_JM_ARRAY = new int[]{
            R.mipmap.jm_1, R.mipmap.jm_2, R.mipmap.jm_3, R.mipmap.jm_4
    };
    private final int[] DRAWABLE_SMY_ARRAY = new int[]{
            R.mipmap.smy_1, R.mipmap.smy_2, R.mipmap.smy_3, R.mipmap.smy_4, R.mipmap.smy_5, R.mipmap.smy_6
    };

    private IndexListView mListView;
    private IIndexGenerator<CustomItem> mIndexGenerator;
    private IItemViewGenerator mItemViewGenerator;
    private ArrayList mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        setTitle("自定义索引");

        mListView = (IndexListView) findViewById(R.id.lv_content);

        initFakeData();
        initGenerator();
        mListView.setData(mData, mIndexGenerator, mItemViewGenerator);
    }

    private void initFakeData() {
        mData = new ArrayList<CustomItem>();
        for (int i = 0; i < DRAWABLE_BM_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_BM_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("边牧");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "边牧", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
        for (int i = 0; i < DRAWABLE_CQ_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_CQ_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("柴犬");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "柴犬", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
        for (int i = 0; i < DRAWABLE_DM_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_DM_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("德牧");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "德牧", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
        for (int i = 0; i < DRAWABLE_HSQ_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_HSQ_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("哈士奇");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "哈士奇", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
        for (int i = 0; i < DRAWABLE_JM_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_JM_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("金毛");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "金毛", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
        for (int i = 0; i < DRAWABLE_SMY_ARRAY.length; i++) {
            int drawableSrc = DRAWABLE_SMY_ARRAY[i];
            CustomItem item = new CustomItem();
            item.setCategory("萨摩耶");
            item.setImgPath(drawableSrc);
            item.setTitle(String.format(" 我是%s%d号", "萨摩耶", i + 1));
            item.setDesc(item.getTitle() + item.getTitle());
            mData.add(item);
        }
    }


    private void initGenerator() {
        mIndexGenerator = new CustomIndexGenerator(mData);
        mItemViewGenerator = new CustomItemViewGenerator(this);
    }

    class CustomIndexGenerator implements IIndexGenerator<CustomItem> {
        private final String TAG = "CustomIndexGenerator";
        /**
         * 源数据源
         */
        private List<CustomItem> mOriginSource;

        /**
         * 分组并排序完毕的数据源
         */
        private Map<String, List<CustomItem>> mSortedSource;
        /**
         * 分组头项对应的下标
         */
        private Map<String, Integer> mSectionIndexMap;
        private List<String> mIndexList;

        CustomIndexGenerator(List<CustomItem> data) {
            setData(data);
        }

        @Override
        public void setData(List<CustomItem> data) {
            mOriginSource = data;
            if (mOriginSource == null || mOriginSource.size() <= 0) {
                return;
            }

            HashMap<String, List<CustomItem>> map = new HashMap<>();
            // 分组
            for (CustomItem item : mOriginSource) {
                String indexChar = item.getCategory();
                if (TextUtils.isEmpty(indexChar)) {
                    Log.w(TAG, String.format("Keyword:%s find no index char!", item.getCategory()));
                    continue;
                }
                if (map.keySet().contains(indexChar)) {
                    map.get(indexChar).add(item);
                } else {
                    List<CustomItem> list = new ArrayList<>();
                    list.add(item);
                    map.put(indexChar, list);
                }
            }
            // 排序
            ArrayList<Map.Entry<String, List<CustomItem>>> list = new ArrayList<>(map.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, List<CustomItem>>>() {

                @Override
                public int compare(Map.Entry<String, List<CustomItem>> firstMapEntry,
                                   Map.Entry<String, List<CustomItem>> secondMapEntry) {
                    return HanziToPinyin.getInstance().getPinyinFirstChar(firstMapEntry.getKey())
                            .compareTo(HanziToPinyin.getInstance().getPinyinFirstChar(secondMapEntry.getKey()));
                }
            });
            mSortedSource = new LinkedHashMap<>();
            for (Map.Entry<String, List<CustomItem>> item : list) {
                mSortedSource.put(item.getKey(), item.getValue());
            }

            // 计算组长位置
            int index = 0;
            mSectionIndexMap = new LinkedHashMap<>();
            for (String key : mSortedSource.keySet()) {
                List<CustomItem> childList = mSortedSource.get(key);
                mSectionIndexMap.put(key, index);
                index += childList.size() + 1;
            }

            mIndexList = new ArrayList<>(mSectionIndexMap.keySet());
        }

        @Override
        public Map<String, List<CustomItem>> getSortedDataSource() {
            return mSortedSource;
        }

        @Override
        public List<String> getIndexList() {
            return mIndexList;
        }

        @Override
        public Map<String, Integer> getSectionIndexMap() {
            return mSectionIndexMap;
        }

        @Override
        public int getTotalCount() {
            return (mOriginSource == null ? 0 : mOriginSource.size()) +
                    (mIndexList == null ? 0 : mIndexList.size());
        }
    }

    class CustomItemViewGenerator implements IItemViewGenerator {

        Context mContext;

        CustomItemViewGenerator(Context context) {
            mContext = context;
        }

        @Override
        public View genSectionView(String sectionName, View convertView) {
            SectionViewHolder viewHolder;
            if (convertView != null && convertView.getTag() instanceof SectionViewHolder) {
                viewHolder = (SectionViewHolder) convertView.getTag();
            } else {
                convertView = View.inflate(mContext, R.layout.item_custom_section, null);
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
                convertView = View.inflate(mContext, R.layout.item_custom_child, null);
                viewHolder = new ItemViewHolder();

                viewHolder.mPhotoIv = (ImageView) convertView.findViewById(R.id.iv_photo);
                viewHolder.mTitleTv = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.mDescTv = (TextView) convertView.findViewById(R.id.tv_desc);

                convertView.setTag(viewHolder);
            }

            if (item instanceof CustomItem) {
                CustomItem customItem = (CustomItem) item;
                viewHolder.mPhotoIv.setImageResource(customItem.getImgPath());
                viewHolder.mTitleTv.setText(customItem.getTitle());
                viewHolder.mDescTv.setText(customItem.getDesc());
            }
            return convertView;
        }


        class SectionViewHolder {
            TextView mSectionTv;
        }

        class ItemViewHolder {
            ImageView mPhotoIv;
            TextView mTitleTv;
            TextView mDescTv;
        }
    }
}
