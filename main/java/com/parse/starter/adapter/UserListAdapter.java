package com.parse.starter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.starter.R;
import com.parse.starter.bean.UserInfo;

import java.util.List;

/**
 * Created by axing on 16/7/21.
 */
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater inflater;
    private Context context;
    private List<UserInfo> list;

    public UserListAdapter(Context context, List<UserInfo> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_list_item, parent, false);
        return new UserListHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserListHolder newHolder = (UserListHolder) holder;

        final UserInfo userInfo = list.get(position);

        newHolder.userName.setText(userInfo.getUserName());
        newHolder.userLogo.setImageResource(R.drawable.leaf);

        ParseQuery<ParseObject> info = new ParseQuery<ParseObject>("UserInfo");
        info.whereEqualTo("username", userInfo.getUserName());
        info.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {
                    ParseObject object = objects.get(0);
                    final ParseFile parseFile = (ParseFile) object.get("userlogo");
                    Log.e("TAG", "done: file url is " + parseFile.getUrl());

                    parseFile.getDataInBackground(
                            new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Log.e("TAG", "done: download iamge succeed!---" + userInfo.getUserName());
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                                data, 0, data.length);

                                        newHolder.userLogo.setImageBitmap(bitmap);
                                    } else {
                                        Log.e("TAG", "done: download iamge failed--" + e.getMessage());
                                    }
                                }
                            },
                            new ProgressCallback() {
                                @Override
                                public void done(Integer percentDone) {
                                    Log.e("TAG", "done: percentDone is " + percentDone);
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class UserListHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public ImageView userLogo;

        public UserListHolder(View itemView) {
            super(itemView);

            userName = ((TextView) itemView.findViewById(R.id.username));
            userLogo = ((ImageView) itemView.findViewById(R.id.userlogo));
        }
    }
}
