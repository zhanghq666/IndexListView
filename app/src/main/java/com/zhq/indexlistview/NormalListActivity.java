package com.zhq.indexlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhq.indexlistviewlib.IIndexItem;
import com.zhq.indexlistviewlib.IndexListView;

import java.util.ArrayList;

/**
 * Created by zhanghq on 2016/7/16.
 */
public class NormalListActivity extends AppCompatActivity {

    private IndexListView mListView;

    String[] fakeData = new String[] {"习近平", "李克强","温家宝", "贾庆林","王岐山", "司马义艾买提",
            "萧景琰", "霓凰","梅长苏", "萧景桓","蒙挚", "萧景宣","飞流", "吴磊","高鑫", "陈龙",
            "萧景睿", "程皓枫","言豫津", "郭晓然","夏冬", "张龄心","谢玉", "刘奕君","宫羽", "周奇奇",
            "莅阳公主","张琰琰", "梁帝","丁勇岱", "夏江","王永泉","静妃","刘敏涛","言阙","王劲松",
            "秦般若", "王鸥","穆青", "张晓谦","蔺晨", "靳东","黎纲", "王宏","甄平", "赵一龙",
            "言皇后","方晓莉", "越贵妃","杨雨婷", "卓鼎风","刘昊明","卓青遥","言杰","卓夫人","刘姝晨"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_list);
        setTitle("普通索引");

        mListView = (IndexListView) findViewById(R.id.lv_content);
        ArrayList data = new ArrayList<IIndexItem>();
        for (final String str : fakeData) {
            IIndexItem item = new IIndexItem() {
                @Override
                public String getKeyword() {
                    return str;
                }
            };
            data.add(item);
        }
        mListView.setData(data);
    }
}
