package com.parse.starter.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.starter.R;
import com.parse.starter.adapter.UserListAdapter;
import com.parse.starter.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity {

    ArrayList<UserInfo> usernames;

    RecyclerView userList;
    UserListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        usernames = new ArrayList<UserInfo>();

        userList = (RecyclerView) findViewById(R.id.userList);
        adapter = new UserListAdapter(this, usernames);
        userList.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));
        userList.setAdapter(adapter);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        if (ParseUser.getCurrentUser() != null) {
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        }
        query.addAscendingOrder("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    Log.e("TAG", "done: find all user successful size is " + objects.size());
                    if (objects.size() > 0) {
                        for (ParseUser user : objects) {
                            UserInfo userInfo = new UserInfo();
                            userInfo.setUserName(user.getUsername());
                            usernames.add(userInfo);
                        }

                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("TAG", "done: find all user failed");
                }
            }
        });

        /**userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = usernames.get(position).getUserName();

                Intent i = new Intent(UserListActivity.this, UserDetailActivity.class);
                i.putExtra("username", s);

                startActivity(i);
            }
        });*/
    }
}
