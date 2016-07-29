package material.danny_jiang.com.photoselectorlib.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import material.danny_jiang.com.photoselectorlib.R;
import material.danny_jiang.com.photoselectorlib.adapter.ShowImageAdapter;
import material.danny_jiang.com.photoselectorlib.bean.ImageItem;
import material.danny_jiang.com.photoselectorlib.utils.AlbumHelper;

public class ShowImageActivity extends AppCompatActivity implements View.OnClickListener {

    private List<ImageItem> mDataList;

    private AlbumHelper mHelper;

    private GridView mGridView;
    private ShowImageAdapter adapter;
    private Button mFinishBtn;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(ShowImageActivity.this, "最多选择9张图片", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        mHelper = AlbumHelper.getHelper();
        mHelper.init(getApplicationContext());

        initData();

        initViews();
    }

    private void initViews() {
        mFinishBtn = ((Button) findViewById(R.id.bt));
        mFinishBtn.setOnClickListener(this);

        mGridView = (GridView) findViewById(R.id.gridview);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        adapter = new ShowImageAdapter(this, mDataList, mHandler);

        mGridView.setAdapter(adapter);

        adapter.setTextCallback(new ShowImageAdapter.TextCallback() {
            public void onListen(int count) {
                mFinishBtn.setText("完成" + "(" + count + ")");
            }
        });
    }

    private void initData() {
        mDataList = (List<ImageItem>) getIntent().getSerializableExtra(ShowAlbumActivity.EXTRA_IMAGE_LIST);
    }

    public void back(View view) {
        Intent intent = new Intent();
        intent.putExtra("aaa", "bbb");
        setResult(2, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        ArrayList<String> list = new ArrayList<String>();
        Collection<String> c = adapter.mMap.values();
        Iterator<String> it = c.iterator();
        for (; it.hasNext();) {
            list.add(it.next());
        }

        Intent intent = new Intent();
        intent.putStringArrayListExtra("data", list);

        setResult(RESULT_OK, intent);
        finish();
    }
}
