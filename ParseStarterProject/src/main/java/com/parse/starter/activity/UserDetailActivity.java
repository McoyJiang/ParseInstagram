package com.parse.starter.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.starter.R;

import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        imageView = ((ImageView) findViewById(R.id.userImage));

        String username = getIntent().getStringExtra("username");

        setTitle(username + "'s detail");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("UserInfo");
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    Log.e("TAG", "done: find successful--" + objects.size());
                    if (objects.size() > 0) {
                        for (ParseObject object : objects) {
                            ParseFile parseFile = (ParseFile) object.get("userlogo");

                            parseFile.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {
                                        Log.e("TAG", "done: download iamge succeed!");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(
                                                data, 0, data.length);

                                        imageView.setImageBitmap(bitmap);
                                    } else {
                                        Log.e("TAG", "done: download iamge failed--" + e.getMessage());
                                    }
                                }
                            });
                        }
                    }
                } else {
                    Log.e("TAG", "done: find failed--" + e.getMessage());
                }
            }
        });
    }
}
