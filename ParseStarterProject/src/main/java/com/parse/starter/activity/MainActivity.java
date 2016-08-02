package com.parse.starter.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseACL;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import material.danny_jiang.com.photoselectorlib.activity.ShowAlbumActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView userLogo;
    private TextView userPhone;
    private TextView userEmail;

    private KProgressHUD kProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        initViews();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("语音图片");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //4.5表示侧拉菜单的两种状态描述
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_menu, R.string.close_menu);
        drawerLayout.setDrawerListener(toggle);
        //让DrawerLayout和ToolBar状态同步
        toggle.syncState();

        navigationView = (NavigationView) findViewById(
                R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(MainActivity.this, "123", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(Gravity.LEFT);
                        break;
                }
                return true;
            }
        });
        View headerView = navigationView.getHeaderView(0);
        userLogo = (ImageView) headerView.findViewById(R.id.userCircleLogo);
        getUserLogo();
        userLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickLogo();
            }
        });

        userPhone = ((TextView) headerView.findViewById(R.id.userPhone));
        userEmail = ((TextView) headerView.findViewById(R.id.userEmail));

        if (ParseUser.getCurrentUser() != null) {
            userPhone.setText(ParseUser.getCurrentUser().getUsername());
            userEmail.setText(ParseUser.getCurrentUser().getEmail() == null
                    ? "点击设置邮箱": ParseUser.getCurrentUser().getEmail());
            userEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(v);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }


    private void showPickLogo() {
        Intent logo = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(logo, 1);
    }

    public void click(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.register:
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.showAll:
                intent.setClass(this, UserListActivity.class);
                startActivity(intent);
                break;
            case R.id.uploadLogo:
                uploadMultiImages();
                return;
            case R.id.userEmail:
                Toast.makeText(MainActivity.this, "设置邮箱", Toast.LENGTH_SHORT).show();
                break;
            case R.id.showMyNews:
                intent.setClass(this, ShowNewsActivity.class);
                startActivity(intent);
                break;
            case R.id.test:
                intent.setClass(this, TestActivity.class);
                startActivity(intent);
                break;
        }

    }

    private void uploadMultiImages() {
        Intent intent = new Intent(this, ShowAlbumActivity.class);

        startActivityForResult(intent, 5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5 && resultCode == RESULT_OK){
            List<String> selectedPhotos = data.getStringArrayListExtra("data");
            if (selectedPhotos != null && selectedPhotos.size() > 0) {
                ArrayList<ParseFile> files = new ArrayList<>();
                for (int i = 0; i < selectedPhotos.size(); i++) {
                    String path = selectedPhotos.get(i);
                    Log.e("TAG", "onActivityResult: " + path);
                    Bitmap bitmapImage = BitmapFactory.decodeFile(path);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] buffer = baos.toByteArray();
                    ParseFile parseFile = new ParseFile("Image" + i + ".jpg", buffer);

                    files.add(parseFile);
                }

                ParseObject news = new ParseObject("News");
                news.put("username", ParseUser.getCurrentUser().getUsername());
                news.put("images", files);

                kProgressHUD = KProgressHUD.create(this)
                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                        .setLabel("正在上传...")
                        .setCancellable(false)
                        .setAnimationSpeed(2)
                        .setDimAmount(0.5f)
                        .show();

                news.saveInBackground(new SaveCallback() {
                    @Override public void done(ParseException e) {
                        if (kProgressHUD != null && kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                        if (e == null) {
                            Log.e("TAG", "done: 上传多张图片成功");
                            Toast.makeText(MainActivity.this, "上传成功",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("TAG", "done: 上传多张图片失败--" + e.getCode());
                            Toast.makeText(MainActivity.this, "上传失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else if (resultCode == RESULT_OK && data != null) {
            final Uri uri = data.getData();
            Log.e("TAG", "onActivityResult: uri is " + uri.getPath());

            refreshUserLogo(uri);
        }
    }

    public void getUserLogo() {
        if (ParseUser.getCurrentUser() == null) {
            Log.e("TAG", "getUserLogo: 当前没有登录用户");
            return;
        } else {
            Log.e("TAG", "getUserLogo: 当前用户是 " +
                    ParseUser.getCurrentUser().getUsername());
        }

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.e("TAG", "done: 找到 " + object.getString("username"));
                    final ParseFile userlogo = (ParseFile) object.getParseFile("userlogo");
                    userlogo.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null && data.length > 0) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                userLogo.setImageBitmap(bitmap);
                            } else {
                                Log.e("TAG", "done: " + e.getCode());
                            }
                        }
                    });
                } else {
                    Log.e("TAG", "done: 未找到 " + e.getCode());
                }
            }
        });
    }

    public void refreshUserLogo(final Uri uri){
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    Log.e("TAG", "done: 找到 " + object.getString("username"));
                    updateImage(object.getObjectId(), uri);
                } else {
                    Log.e("TAG", "done: 未找到 " + e.getCode());
                    switch (e.getCode()) {
                        case ParseException.OBJECT_NOT_FOUND:
                            uploadImage(uri);
                            break;
                    }
                }
            }
        });
    }

    public void updateImage(String objectId, Uri uri){
        Toast.makeText(MainActivity.this, "开始上传", Toast.LENGTH_SHORT).show();

        final Bitmap bitmapImage;
        try {
            bitmapImage = getBitmapFormUri(this, uri);

            //Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.baby);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] buffer = baos.toByteArray();

            final ParseFile parseFile = new ParseFile("Image.jpg", buffer);

            final ParseACL parseACL = new ParseACL();
            parseACL.setPublicReadAccess(true);
            parseACL.setPublicWriteAccess(true);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserInfo");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        object.put("username", ParseUser.getCurrentUser().getUsername());
                        object.put("userlogo", parseFile);
                        object.setACL(parseACL);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(MainActivity.this, "上传成功",
                                            Toast.LENGTH_SHORT).show();
                                    userLogo.setImageBitmap(bitmapImage);
                                } else {
                                    Log.e("TAG", "done: updateImage : " + e.getCode());
                                }
                            }
                        });
                    } else {
                        Log.e("TAG", "done: updateImage : " + e.getCode());
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void uploadImage(Uri uri){
        try {
            Toast.makeText(MainActivity.this, "开始上传", Toast.LENGTH_SHORT).show();


            final Bitmap bitmapImage = getBitmapFormUri(this, uri);
            //Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            //Bitmap bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.baby);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] buffer = baos.toByteArray();

            ParseFile parseFile = new ParseFile("Image.jpg", buffer);

            final ParseACL parseACL = new ParseACL();
            parseACL.setPublicReadAccess(true);
            parseACL.setPublicWriteAccess(true);

            final ParseUser user = ParseUser.getCurrentUser();
            if (user != null && user.getUsername() != null) {
                ParseObject parseObject = new ParseObject("UserInfo");
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                parseObject.put("userlogo", parseFile);
                parseObject.setACL(parseACL);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(MainActivity.this, "上传成功",
                                Toast.LENGTH_SHORT).show();
                        userLogo.setImageBitmap(bitmapImage);
                    }
                });
            }

        } catch (Exception e) {
            Log.e("TAG", "onActivityResult: e is " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 4;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
