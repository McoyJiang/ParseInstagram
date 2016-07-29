package com.parse.starter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.parse.starter.R;
import com.parse.starter.bean.NewsBean;
import com.squareup.picasso.Picasso;

import java.util.List;

import material.danny_jiang.com.mcoyninegridimage.NineGridLayout;
import material.danny_jiang.com.mcoyninegridimage.NineGridLayoutAdapter;
import si.virag.fuzzydateformatter.FuzzyDateTimeFormatter;

/**
 * Created by axing on 16/7/27.
 */
public class NewsAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private List<NewsBean> beans;
    private Context context;

    public NewsAdapter(Context context, List<NewsBean> beans) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.beans = beans;
    }

    @Override
    public int getCount() {
        return beans == null ? 0 : beans.size();
    }

    @Override
    public Object getItem(int position) {
        return beans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsHolder holder = null;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_news_item, parent, false);
            holder = new NewsHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = ((NewsHolder) convertView.getTag());
        }

        NewsBean bean = beans.get(position);

        initHolder(holder, bean);

        return convertView;
    }

    private void initHolder(NewsHolder holder, NewsBean bean) {
        holder.userLabelView.setText(bean.getUserName());
        holder.newsTimeView.setText(FuzzyDateTimeFormatter.getTimeAgo(
                context, bean.getCreatedDate()));
        holder.newsAddressView.setText(bean.getAddress());
        holder.newsTextView.setText(bean.getText());

        NineGridLayoutAdapter<String> adapter = new NineGridLayoutAdapter<String>() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, String s) {
                Log.e("TAG", "onDisplayImage: s is " + s);
                Picasso.with(context).load(s).into(imageView);
            }
        };
        holder.nineGridLayout.setAdapter(adapter);
        holder.nineGridLayout.setDataList(bean.getAllImages());
    }

    class NewsHolder {
        public ImageView userLogoView;
        public TextView newsTextView;
        public TextView userLabelView;
        public NineGridLayout nineGridLayout;
        public TextView newsTimeView;
        public TextView newsAddressView;

        public NewsHolder(View convertView) {
            userLogoView = ((ImageView) convertView.findViewById(R.id.useLogo));
            newsTextView = ((TextView) convertView.findViewById(R.id.newsText));
            userLabelView = ((TextView) convertView.findViewById(R.id.userLabel));
            nineGridLayout = ((NineGridLayout) convertView.findViewById(R.id.newsImage));
            newsTimeView = ((TextView) convertView.findViewById(R.id.newsTime));
            newsAddressView = ((TextView) convertView.findViewById(R.id.newsAddress));
        }
    }
}
