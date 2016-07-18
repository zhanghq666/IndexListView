package com.zhq.indexlistview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mNormalListBtn;
    private Button mCustomListBtn;
    private Button mCompactListBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNormalListBtn = (Button) findViewById(R.id.btn_normal_list);
        mCustomListBtn = (Button) findViewById(R.id.btn_custom_list);
        mCompactListBtn = (Button) findViewById(R.id.btn_compact_list);

        mNormalListBtn.setOnClickListener(this);
        mCustomListBtn.setOnClickListener(this);
        mCompactListBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_normal_list:
                intent.setClass(this, NormalListActivity.class);
                break;
            case R.id.btn_custom_list:
                intent.setClass(this, CustomListActivity.class);
                break;
            case R.id.btn_compact_list:
                intent.setClass(this, CompactListActivity.class);
                break;
        }
        startActivity(intent);
    }
}
