package material.danny_jiang.com.mcoyninegridimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by axing on 16/7/27.
 */
public class NineGridLayout<T> extends ViewGroup {

    private List<T> mDataList;  //9宫格中需要显示的数据源

    private int mGridSize;      //9宫格中每一个单元的大小

    private int mGap;           //9宫格中单元格之间的间隙

    private int mColumnCount;    //9宫格的列数

    private int mRowCount;       //9宫格的行数

    public NineGridLayout(Context context) {
        this(context, null);
    }

    public NineGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NineGridImageView);
        this.mGap = (int) typedArray.getDimension(R.styleable.NineGridImageView_imgGap, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "NineGridLayout--onMeasure: ");

        //定义高度，初始值为0
        int totalHeight = 0;
        //获取父视图传递的剩余总宽度
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        //减去左填充和右填充获取可用的宽度
        int availableWidth = totalWidth - getPaddingLeft() - getPaddingRight();

        if (mDataList != null && mDataList.size() > 0) { //数据源长度大于0， 9宫格中有数据

            // 先计算出每个单元格的大小
            mGridSize = (availableWidth - mGap * (mColumnCount - 1)) / mColumnCount;
            Log.e("TAG", "onMeasure: mColumnCount is " + mColumnCount + " mRowCount is " + mRowCount);
            Log.e("TAG", "onMeasure: mGridSize is " + mGridSize);

            //根据单元格的大小、行数、行间隙，计算出总共需要的高度
            totalHeight = mGridSize * mRowCount + mGap * (mRowCount - 1) + getPaddingTop() + getPaddingBottom();

        } else { //数据源中没有数据，则使用父视图传递下来的宽高

            totalHeight = MeasureSpec.getSize(heightMeasureSpec);

        }

        Log.e("TAG", "NineaGridLayout onMeasure: totalWidht is " + totalWidth + " totalHeight is " + totalHeight);
        setMeasuredDimension(totalWidth, totalHeight);
    }

    public void setDataList(List<T> mDataList) {
        //如果数据源为null， 或者长度为0，则不显示NineGridLayout
        if (mDataList == null || mDataList.size() == 0) {
            setVisibility(GONE);
            return;
        } else {
            this.mDataList = mDataList;
            setVisibility(VISIBLE);
        }

        //获取9宫格的参数：行数和列数
        int[] gridParam = calculateGridParam(mDataList.size());
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];

        /**
         * 先移除所有的ImageView，然后根据数据源的长度添加相应数量的ImageView
         */
        //removeAllViews();
        for (int i = 0; i < mDataList.size(); i++) {
            ImageView imageView = getImageView(i);
            addView(imageView, generateDefaultLayoutParams());
            if (mAdapter != null) {
                mAdapter.onDisplayImage(getContext(), imageView, mDataList.get(i));
            }
        }

        //刷新UI界面
        requestLayout();
    }

    public void setAdapter(NineGridLayoutAdapter<T> mAdapter) {
        this.mAdapter = mAdapter;
    }

    private NineGridLayoutAdapter<T> mAdapter;

    private ImageView getImageView(final int position) {
        if (mAdapter != null) {
            ImageView imageView = mAdapter.generateImageView(getContext());
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapter.onItemImageClick(getContext(), position, mDataList);
                }
            });
            return imageView;
        } else {
            Log.e("NineGirdImageView", "Your must set a NineGridImageViewAdapter for NineGirdImageView");
            return null;
        }
    }

    /**
     * 设置 9宫格参数
     *
     * @param imagesSize 图片数量
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    private int[] calculateGridParam(int imagesSize) {
        Log.e("TAG", "calculateGridParam: imageSize is " + imagesSize);
        int[] gridparam = new int[2];

        if (imagesSize > 9) {
            imagesSize = 9; //最多显示9个ImageView
        }
        gridparam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);

        gridparam[1] = 3; //固定有3列

        return gridparam;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.e("TAG", "NineGridLayout--onLayout: ");
        layoutChildView();
    }

    private void layoutChildView() {
        if (mDataList == null || mDataList.size() == 0) {
            return;
        }

        int childCount = mDataList.size();  //根据数据源的长度，获取子view的个数

        for (int i = 0; i < childCount; i++) {
            ImageView childrenView = (ImageView) getChildAt(i);
            int rowNum = i / mColumnCount;
            int columnNum = i % mColumnCount;
            int left = (mGridSize + mGap) * columnNum + getPaddingLeft();
            int top = (mGridSize + mGap) * rowNum + getPaddingTop();
            int right = left + mGridSize;
            int bottom = top + mGridSize;

            Log.e("TAG", "layoutChildView: i is " + i);
            Log.e("TAG", "left is " + left);
            Log.e("TAG", "top is " + top);
            Log.e("TAG", "right is " + right);
            Log.e("TAG", "bottom is " + bottom);

            childrenView.layout(left, top, right, bottom);
        }
    }
}
