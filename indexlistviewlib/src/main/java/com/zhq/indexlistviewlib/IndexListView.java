package com.zhq.indexlistviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

/**
 * 带索引条的ListView
 * Created by zhanghq on 2016/5/21.
 */
public final class IndexListView extends FrameLayout implements IndexView.OnIndexerItemClickListener {

    public enum IndexLayoutMode {
        /**
         * 宽松
         */
        LOOSE,
        /**
         * 紧凑
         */
        COMPACT
    }

    private static final String TAG = "IndexListView";

    private Context mContext;
    /**
     * 索引弹窗
     */
    private PopupWindow mPopupWindow;
    /**
     * 是否弹出索引弹窗
     */
    private boolean ifShowPopWindow;
    /**
     * 索引弹窗文字颜色
     */
    private int mPopTextColor;
    /**
     * 索引弹窗背景颜色
     */
    private int mPopBackgroundColor;
    /**
     * 索引弹窗文字大小
     */
    private float mPopTextSize;

    /**
     * 索引条文字大小
     */
    private float mIndexTextSize;
    /**
     * 索引条文字颜色
     */
    private int mIndexTextColorNormal;
    /**
     * 索引条文字按压颜色
     */
    private int mIndexTextColorPressed;
    /**
     * 索引条布局模式
     */
    private IndexLayoutMode mIndexLayoutMode;

    private IIndexGenerator mIndexGenerator;

    private TextView mPopupText;
    private IndexView mIndexView;
    private ListView mContentLv;

    private ContentListAdapter mListAdapter;

    private OnItemClickListener mOnItemClickListener;

    public IndexListView(Context context) {
        this(context, null);
    }

    public IndexListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        mContext = context;
        initAttr(attrs);
        initView();
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.IndexListView);
        ifShowPopWindow = typedArray.getBoolean(R.styleable.IndexListView_if_show_pop, false);
        mPopTextColor = typedArray.getColor(R.styleable.IndexListView_pop_text_color, 0xFF00FF00);
        mPopTextSize = typedArray.getDimensionPixelSize(R.styleable.IndexListView_pop_text_size, 20);
        mPopBackgroundColor = typedArray.getColor(R.styleable.IndexListView_pop_background_color, 0xA0757575);

        mIndexTextSize = typedArray.getDimensionPixelSize(R.styleable.IndexListView_index_text_size, 12);
        mIndexTextColorNormal = typedArray.getColor(R.styleable.IndexListView_index_text_color, Color.GRAY);
        mIndexTextColorPressed = typedArray.getColor(R.styleable.IndexListView_index_text_color_pressed, Color.DKGRAY);
        mIndexLayoutMode = typedArray.getInt(R.styleable.IndexListView_index_layout_mode, 0) == 0 ? IndexLayoutMode.LOOSE : IndexLayoutMode.COMPACT;
        typedArray.recycle();
    }

    private void initView() {
        View rootView = View.inflate(mContext, R.layout.layout_indexer_list_view, this);
        android.view.ViewGroup.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rootView.setLayoutParams(layoutParams);

        mIndexView = (IndexView) rootView.findViewById(R.id.index_indexer);
        mContentLv = (ListView) rootView.findViewById(R.id.index_list_view);
        mContentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null && mListAdapter != null) {
                    mOnItemClickListener.onItemClick(mListAdapter.getItem(position));
                }
            }
        });

        mIndexView.setTextSize(mIndexTextSize);
        mIndexView.setTextColorNormal(mIndexTextColorNormal);
        mIndexView.setTextColorPressed(mIndexTextColorPressed);
        mIndexView.setLayoutMode(mIndexLayoutMode);
        mIndexView.setOnItemClickListener(this);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List data) {
        setData(data, null, null);
    }

    public void setData(List data, IIndexGenerator indexGenerator, IItemViewGenerator viewGenerator) {
        if (indexGenerator == null) {
            indexGenerator = new DefaultIndexGenerator(data);
        }
        mIndexGenerator = indexGenerator;
        indexGenerator.setData(data);

        if (viewGenerator == null) {
            viewGenerator = new DefaultViewGenerator(mContext);
        }

        if (mListAdapter == null) {
            mListAdapter = new ContentListAdapter(mContext, data, indexGenerator, viewGenerator);
            mContentLv.setAdapter(mListAdapter);
        } else {
            mListAdapter.setData(data);
        }
        String[] array = (String[]) indexGenerator.getIndexList().toArray(new String[indexGenerator.getIndexList().size()]);
        try {
            mIndexView.setIndexerChars(array);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopup(String text) {
        if (!ifShowPopWindow)
            return;
        if (mPopupWindow == null) {
            mPopupText = new TextView(mContext);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            mPopupText.setLayoutParams(layoutParams);
            mPopupText.setTextColor(mPopTextColor);
            mPopupText.setTypeface(Typeface.MONOSPACE);
            mPopupText.setTextSize(mPopTextSize);
            mPopupText.setGravity(Gravity.CENTER);
            mPopupText.setBackgroundColor(mPopBackgroundColor);
            mPopupText.setPadding(20, 0, 20, 0);

            mPopupWindow = new PopupWindow(mPopupText, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
//        int width = (int) mPopupText.getPaint().measureText(text);
//        mPopupWindow.setWidth(width * 1.5);

        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(this, Gravity.CENTER, 0, 0);
        }
    }

    public void dismissPopup() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onIndexerItemPress(String s) {
        showPopup(s);
        mContentLv.setSelection((Integer) mIndexGenerator.getSectionIndexMap().get(s));
    }

    @Override
    public void onIndexerItemRelease() {
        dismissPopup();
    }

    @Override
    protected void onDetachedFromWindow() {
        dismissPopup();
        super.onDetachedFromWindow();
    }

    public interface OnItemClickListener {
        void onItemClick(Object item);
    }
}
