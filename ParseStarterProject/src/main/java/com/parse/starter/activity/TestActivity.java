package com.parse.starter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.starter.R;

import java.util.ArrayList;
import java.util.List;

import material.danny_jiang.com.mcoypulltorefresh.listener.PullToRefreshListener;
import material.danny_jiang.com.mcoypulltorefresh.refresh.BaseRefreshLayout;
import material.danny_jiang.com.mcoypulltorefresh.refresh.RadarRefreshLayout;

public class TestActivity extends AppCompatActivity {

    private ListView listView;
    private RadarRefreshLayout radarRefreshLayout;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        radarRefreshLayout = ((RadarRefreshLayout) findViewById(R.id.radarRefreshLayout));
        radarRefreshLayout.setHandler(mHandler);
        radarRefreshLayout.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onStartRefresh(BaseRefreshLayout baseRefreshLayout) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        radarRefreshLayout.stopRefresh();
                    }
                }, 3000);
            }
        });

        listView = ((ListView) findViewById(R.id.list_Test));;
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            strings.add("Android-" + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, strings);

        listView.setAdapter(adapter);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        radarRefreshLayout.autoRefresh();
    }
}
