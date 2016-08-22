package me.zhanghq.indexlistviewlib;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 默认的索引生成器
 * 以{@link IIndexItem} getKeywords()的结果，依据其拼音首字母进行分组排序
 * Created by zhanghq on 2016/5/23.
 */
final class DefaultIndexGenerator implements IIndexGenerator<IIndexItem> {
    private static final String TAG = "DefaultIndexGenerator";
    /**
     * 源数据源
     */
    private List<IIndexItem> mOriginSource;

    /**
     * 分组并排序完毕的数据源
     */
    private Map<String, List<IIndexItem>> mSortedSource;
    /**
     * 分组头项对应的下标
     */
    private Map<String, Integer> mSectionIndexMap;
    private List<String> mIndexList;

    public DefaultIndexGenerator(List<IIndexItem> data) {
        setData(data);
    }

    @Override
    public void setData(List<IIndexItem> data) {
        mOriginSource = data;
        if (mOriginSource == null || mOriginSource.size() <= 0) {
            return;
        }

        HashMap<String, List<IIndexItem>> map = new HashMap<>();
        // 分组
        for (IIndexItem item : mOriginSource) {
            String indexChar = HanziToPinyin.getInstance().getPinyinFirstChar(item.getKeyword());
            if (TextUtils.isEmpty(indexChar)) {
                Log.w(TAG, String.format("Keyword:%s find no index char!", item.getKeyword()));
                continue;
            }
            if (map.keySet().contains(indexChar)) {
                map.get(indexChar).add(item);
            } else {
                List<IIndexItem> list = new ArrayList<>();
                list.add(item);
                map.put(indexChar, list);
            }
        }
        // 排序
//        ArrayList<Map.Entry<String, List<IIndexItem>>> list = new ArrayList<>(map.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<String, List<IIndexItem>>>() {
//
//            @Override
//            public int compare(Map.Entry<String,List<IIndexItem>> firstMapEntry,
//                    Map.Entry<String,List<IIndexItem>> secondMapEntry) {
//                return firstMapEntry.getKey().compareTo(secondMapEntry.getKey());
//            }
//        });
        mSortedSource = new TreeMap<>(map);
//        for (Map.Entry<String, List<IIndexItem>> item: list){
//            mSortedSource.put(item.getKey(), item.getValue());
//        }

        // 计算组长位置
        int index = 0;
        mSectionIndexMap = new TreeMap<>();
        for (String key : mSortedSource.keySet()) {
            List<IIndexItem> childList = mSortedSource.get(key);
            mSectionIndexMap.put(key, index);
            index += childList.size() + 1;
        }

        mIndexList = new ArrayList<>(mSectionIndexMap.keySet());
    }

    @Override
    public Map<String, List<IIndexItem>> getSortedDataSource() {
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
