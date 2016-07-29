package com.parse.starter.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by axing on 16/7/27.
 */
public class ShowNewsActivity extends AppCompatActivity {

    private ListView listView;
    private NewsAdapter adapter;
    private List<NewsBean> beans = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_news);

        listView = ((ListView) findViewById(R.id.newsList));

        adapter = new NewsAdapter(this, beans);

        listView.setAdapter(adapter);

        getNetwork();
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

                                if (BuildConfig.DEBUG) {
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

                        }
                    } else {
                        Log.e("TAG", "done: 搜索失败--" + e.getCode() + " : " + e.getMessage());
                    }
                }
            });
        }
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }
}
