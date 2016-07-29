package material.danny_jiang.com.mcoyninegridimage;

import android.content.Context;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by axing on 16/7/28.
 */
public abstract class NineGridLayoutAdapter<T> {

    public ImageView generateImageView(Context context) {
        GridImageView imageView = new GridImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return imageView;
    }

    public void onItemImageClick(Context context, int position, List<T> mDataList) {

    }

    protected abstract void onDisplayImage(Context context, ImageView childrenView, T t);

}
