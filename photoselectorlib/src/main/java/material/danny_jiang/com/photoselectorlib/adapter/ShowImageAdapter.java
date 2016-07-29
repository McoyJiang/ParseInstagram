package material.danny_jiang.com.photoselectorlib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import material.danny_jiang.com.photoselectorlib.R;
import material.danny_jiang.com.photoselectorlib.bean.ImageItem;
import material.danny_jiang.com.photoselectorlib.utils.BitmapCache;

public class ShowImageAdapter extends BaseAdapter {

	public Map<String, String> mMap = new HashMap<String, String>();
	private TextCallback mTextcallback = null;
	private List<ImageItem> imageItemList;
	private Context mContext;
	private LayoutInflater inflater;
	private BitmapCache mCache;
	private Handler mHandler;
	private int mSelectTotal = 0;

	BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				}
			}
		}
	};

	public static interface TextCallback {
		public void onListen(int count);
	}

	public void setTextCallback(TextCallback listener) {
		mTextcallback = listener;
	}

	public ShowImageAdapter(Context context, List<ImageItem> imageItemList, Handler mHandler) {
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		this.imageItemList = imageItemList;

		mCache = new BitmapCache();
		this.mHandler = mHandler;
	}

	public int getCount() {
		return imageItemList == null ? 0 : imageItemList.size();
	}
	
	public Object getItem(int position) {
		return imageItemList.get(position);
	}
	
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ShowImageHolder holder;

		if (convertView == null) {
			holder = new ShowImageHolder();

			convertView = inflater.inflate(R.layout.show_image_item, parent, false);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			holder.selected = (ImageView) convertView.findViewById(R.id.isselected);
			holder.text = (TextView) convertView.findViewById(R.id.item_image_grid_text);

			convertView.setTag(holder);
		} else {
			holder = (ShowImageHolder) convertView.getTag();
		}

		final ImageItem item = imageItemList.get(position);

		holder.iv.setTag(item.imagePath);

		mCache.displayBmp(mContext, holder.iv, item.thumbnailPath, item.imagePath,callback);

		if (item.isSelected) {
			holder.selected.setImageResource(R.drawable.icon_data_select);  
			holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
		} else {
			holder.selected.setImageBitmap(null);
			holder.text.setBackgroundColor(0x00000000);
		}

		holder.iv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String path = imageItemList.get(position).imagePath;
				if (mSelectTotal < 9) {
					item.isSelected = !item.isSelected;
					if (item.isSelected) {
						holder.selected.setImageResource(R.drawable.icon_data_select);
						holder.text.setBackgroundResource(R.drawable.bgd_relatly_line);
						mSelectTotal++;
						if (mTextcallback != null)
							mTextcallback.onListen(mSelectTotal);
						mMap.put(path, path);
					} else if (!item.isSelected) {
						holder.selected.setImageBitmap(null);
						holder.text.setBackgroundColor(0x00000000);
						mSelectTotal--;
						if (mTextcallback != null)
							mTextcallback.onListen(mSelectTotal);
						mMap.remove(path);
					}
				} else if ( mSelectTotal >= 9) {
					if (item.isSelected == true) {
						item.isSelected = !item.isSelected;
						holder.selected.setImageBitmap(null);
						mSelectTotal--;
						mMap.remove(path);
					} else {
						Message message = Message.obtain(mHandler, 0);
						message.sendToTarget();
					}
				}
			}
		});
		return convertView;
	}

	class ShowImageHolder {
		public ImageView iv;
		public ImageView selected;
		public TextView text;
	}
}
