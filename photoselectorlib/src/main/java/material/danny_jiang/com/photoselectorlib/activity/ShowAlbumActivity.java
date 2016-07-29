package material.danny_jiang.com.photoselectorlib.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.Serializable;
import java.util.List;

import material.danny_jiang.com.photoselectorlib.R;
import material.danny_jiang.com.photoselectorlib.adapter.ShowAlbumAdapter;
import material.danny_jiang.com.photoselectorlib.bean.ImageBucket;
import material.danny_jiang.com.photoselectorlib.utils.AlbumHelper;

public class ShowAlbumActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE_LIST = "imagelist";

    private AlbumHelper mHelper;

    private List<ImageBucket> imageBucketList;
    private GridView albumGridView;
    private ShowAlbumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_thumb);

        mHelper = AlbumHelper.getHelper();
        mHelper.init(getApplicationContext());

        initData();

        initViews();
    }

    private void initData() {
        imageBucketList = mHelper.getImagesBucketList(false);
    }

    private void initViews() {
        albumGridView = ((GridView) findViewById(R.id.albumGridView));

        adapter = new ShowAlbumAdapter(this, imageBucketList);

        albumGridView.setAdapter(adapter);

        albumGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 * 通知适配器，绑定的数据发生了改变，应当刷新视图
                 */
                Intent intent = new Intent(ShowAlbumActivity.this, ShowImageActivity.class);
                intent.putExtra(EXTRA_IMAGE_LIST, (Serializable) imageBucketList.get(position).imageList);
                startActivityForResult(intent, 3);
                //finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG", "ShowAlbum onActivityResult: resultCode is " + resultCode + " data is " + data);
        setResult(resultCode, data);
        finish();
        if (requestCode == 1 && resultCode == RESULT_OK) {

        }
    }
}
