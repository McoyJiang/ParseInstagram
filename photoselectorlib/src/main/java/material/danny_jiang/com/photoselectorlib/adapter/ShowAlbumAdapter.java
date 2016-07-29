package material.danny_jiang.com.photoselectorlib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import material.danny_jiang.com.photoselectorlib.R;
import material.danny_jiang.com.photoselectorlib.bean.ImageBucket;
import material.danny_jiang.com.photoselectorlib.utils.BitmapCache;

/**
 * Created by axing on 16/7/28.
 */
public class ShowAlbumAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ImageBucket> list;
    private BitmapCache bitmapCache;

    BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        public void imageLoad(ImageView imageView, Bitmap bitmap,Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                }
            } else {
            }
        }
    };

    public ShowAlbumAdapter(Context context, List<ImageBucket> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;

        bitmapCache = new BitmapCache();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumHolder holder = null;

        if (convertView == null) {
            holder = new AlbumHolder();

            convertView = inflater.inflate(R.layout.show_album_item, parent, false);

            holder.iv = (ImageView) convertView.findViewById(R.id.image);
            holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);

            convertView.setTag(holder);
        } else {
            holder = ((AlbumHolder) convertView.getTag());
        }

        ImageBucket item = list.get(position);

        holder.count.setText("" + item.count);
        holder.name.setText(item.bucketName);
        holder.selected.setVisibility(View.GONE);
        if (item.imageList != null && item.imageList.size() > 0) {
            String thumbPath = item.imageList.get(0).thumbnailPath;
            String sourcePath = item.imageList.get(0).imagePath;
            holder.iv.setTag(sourcePath);
            bitmapCache.displayBmp(context, holder.iv, thumbPath,
                    sourcePath, callback);
        } else {
            holder.iv.setImageBitmap(null);
        }

        return convertView;
    }

    class AlbumHolder {
        private ImageView iv;
        private ImageView selected;
        private TextView name;
        private TextView count;
    }
}
