package com.parse.starter.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.BuildConfig;
import com.parse.starter.R;
import com.parse.starter.adapter.NewsAdapter;
import com.parse.starter.bean.NewsBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import material.danny_jiang.com.mcoypulltorefresh.listener.PullToRefreshListener;
import material.danny_jiang.com.mcoypulltorefresh.refresh.BaseRefreshLayout;
import material.danny_jiang.com.mcoypulltorefresh.refresh.RadarRefreshLayout;
import material.danny_jiang.com.pulltorefresh.widget.XListView;

/**
 * Created by axing on 16/7/27.
 */
public class ShowNewsActivity extends AppCompatActivity implements XListView.IXListViewListener {

    private ListView listView;
//    private XListView listView;
    private NewsAdapter adapter;
    private List<NewsBean> beans = new ArrayList<>();
    private RadarRefreshLayout radarRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        initView();

    }

    private void initView() {
//        listView = (XListView) findViewById(R.id.newsList);
//        listView.setPullRefreshEnable(true);
//        listView.setPullLoadEnable(true);
//        listView.setAutoLoadEnable(true);
//        listView.setXListViewListener(this);
//        listView.setRefreshTime(getTime());

        radarRefreshLayout = ((RadarRefreshLayout) findViewById(R.id.radarRefreshLayout_ShowNews));
        radarRefreshLayout.setHandler(new Handler());
        radarRefreshLayout.setPullToRefreshListener(new PullToRefreshListener() {
            @Override
            public void onStartRefresh(BaseRefreshLayout baseRefreshLayout) {
                Log.e("TAG", "onStartRefresh: ");
                beans.clear();
                getNetwork();
            }
        });

        listView = ((ListView) findViewById(R.id.newsList));

        adapter = new NewsAdapter(this, beans);
        listView.setAdapter(adapter);
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    private void getNetwork() {
        if (ParseUser.getCurrentUser() != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("News");

            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null && objects != null) {
                        Log.e("TAG", "done: 搜索成功");
                        if (objects.size() > 0) {
                            for (ParseObject parseObject : objects) {
                                JSONArray jsonArray = parseObject.getJSONArray("images");
                                Date createdTime = parseObject.getCreatedAt();
                                String address = parseObject.getString("address");
                                String username = parseObject.getString("username");
                                String text = parseObject.getString("text");

                                if (false) {
                                    Log.e("TAG", "done: jsonArray is " + jsonArray);
                                    Log.e("TAG", "done: createdTime is " + createdTime);
                                    Log.e("TAG", "done: address is " + address);
                                    Log.e("TAG", "done: username is " + username);
                                }

                                NewsBean bean = new NewsBean();

                                bean.setCreatedDate(createdTime);
                                bean.setAddress(address);
                                bean.setUserName(username);
                                bean.setText(text);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObj = jsonArray.getJSONObject(i);

                                        String url = jsonObj.getString("url");

                                        bean.addImage(url);

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                        Log.e("TAG", "done: e is " + e1.getMessage());
                                    }
                                }
                                beans.add(bean);
                            }
                            Log.e("TAG", "done: beans'size is " + beans.size());
                            refresh();
                        } else {
                            Log.e("TAG", "done: 找到0数据");
                        }
                    } else {
                        Log.e("TAG", "done: 搜索失败--" + e.getCode() + " : " + e.getMessage());
                    }
                }
            });
        }
    }

    private void refresh() {
        onLoadFinished();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
//            listView.autoRefresh();
            radarRefreshLayout.autoRefresh();
        }

    }

    @Override
    public void onRefresh() {
        Log.e("TAG", "onRefresh: ");
        beans.clear();
        getNetwork();
    }

    @Override
    public void onLoadMore() {

    }

    private void onLoadFinished() {
//        listView.stopRefresh();
//        listView.stopLoadMore();
//        listView.setRefreshTime(getTime());
        Log.e("TAG", "onLoadFinished: ");
        radarRefreshLayout.stopRefresh();
    }
}
