package com.ragentek.infostreamdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ragentek.infostreamdemo.FullNews;
import com.ragentek.infostreamdemo.R;
import com.ragentek.infostreamdemo.baiduresponse.BaiduImageItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduNewsItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduVedioItem;
import com.ragentek.infostreamdemo.engine.BaiduEngine;
import com.ragentek.infostreamdemo.engine.NewsEngine;
import com.ragentek.infostreamdemo.engine.NewsEntity;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;

public class BaiduNewsAdapter extends BaseAdapter {
    private static final String TAG = "BaiduNewsAdapter";
    protected Context context = null;
    protected BaiduEngine engine = null;
    protected String channel = null;
    protected LinkedList<BaiduItem> newsList = new LinkedList<BaiduItem>();

    private Activity activity;

    public BaiduNewsAdapter(Context context, BaiduEngine engine, String channel) {
        this.context = context;
        this.engine = engine;
        this.channel = channel;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    public void addItem(BaiduItem entity) {
        newsList.addFirst(entity);
    }

    @Override
    public Object getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getChannel() {
        return channel;
    }

    private OnTouchListener itemTouchListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final BaiduItem entity = ((ViewHolder) v.getTag()).entity;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    Intent intent = new Intent(context, FullNews.class);
                    intent.putExtra("url", entity.detailUrl);
                    context.startActivity(intent);
                    if (activity != null) {
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        }

        ;
    };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        BaiduItem entity = (BaiduItem)newsList.get(position);
        Log.e(TAG,"entity:"+entity);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();
            holder.entity = entity;
            holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
            convertView.setOnTouchListener(itemTouchListener);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.entity = entity;
        }
        if("image".equals(entity.type)){
            Log.e(TAG,"imageItem:"+((BaiduImageItem)entity).imageList.size()+",url:"+((BaiduImageItem)entity).imageList.get(0).imageUrl);
            if (((BaiduImageItem) entity).imageList != null && !((BaiduImageItem) entity).imageList.get(0).imageUrl.isEmpty()) {
                String url = "https:"+((BaiduImageItem) entity).imageList.get(0).imageUrl;
                Picasso.with(context).load(url).into(holder.thumbnail);
                holder.thumbnail.setVisibility(View.VISIBLE);
            } else {
                holder.thumbnail.setVisibility(View.GONE);
            }
        }else if("news".equals(entity.type)){
            if (((BaiduNewsItem)entity).images.get(0) != null && !((BaiduNewsItem) entity).images.get(0).isEmpty()) {
                Picasso.with(context).load(((BaiduNewsItem) entity).images.get(0)).into(holder.thumbnail);
                holder.thumbnail.setVisibility(View.VISIBLE);
            } else {
                holder.thumbnail.setVisibility(View.GONE);
            }
        }else if("video".equals(entity.type)){
            if (((BaiduVedioItem)entity).thumbUrl!= null && !((BaiduVedioItem) entity).thumbUrl.isEmpty()) {
                String url = "https:"+((BaiduVedioItem) entity).thumbUrl;
                Picasso.with(context).load(url).into(holder.thumbnail);
                holder.thumbnail.setVisibility(View.VISIBLE);
            } else {
                holder.thumbnail.setVisibility(View.GONE);
            }
        }

        holder.title.setText(entity.title);
        holder.date.setText(entity.updateTime);

        return convertView;
    }

    public class ViewHolder {
        BaiduItem entity;
        ImageView thumbnail;
        TextView title;
        TextView date;
    }
}