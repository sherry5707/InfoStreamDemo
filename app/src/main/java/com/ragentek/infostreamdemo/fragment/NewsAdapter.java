package com.ragentek.infostreamdemo.fragment;

import java.util.LinkedList;

import com.ragentek.infostreamdemo.FullNews;
import com.ragentek.infostreamdemo.R;
import com.ragentek.infostreamdemo.baiduresponse.BaiduNewsItem;
import com.ragentek.infostreamdemo.engine.NewsEngine;
import com.ragentek.infostreamdemo.engine.NewsEntity;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	protected Context context = null;
	protected NewsEngine engine = null;
	protected String channel = null;
	protected LinkedList<NewsEntity> newsList = new LinkedList<NewsEntity>();
	
	private Activity activity;
	
	public NewsAdapter(Context context, NewsEngine engine, String channel) {
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

	public void addItem(NewsEntity entity) {
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
	    	final NewsEntity entity = ((ViewHolder) v.getTag()).entity;
	        switch (event.getAction()) {
            	case MotionEvent.ACTION_DOWN:
            		break;
            	case MotionEvent.ACTION_UP:
    				Intent intent = new Intent(context, FullNews.class);
    				intent.putExtra("url", entity.url);
    				context.startActivity(intent);
    				if(activity!=null){
    					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    				}
            		break;
            	case MotionEvent.ACTION_MOVE:
            		break;
	        }
	    	return true;
	    };
	};
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		final NewsEntity entity = newsList.get(position);
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
		}
		
		if (entity.picUrl != null && !entity.picUrl.isEmpty()) {
			Picasso.with(context).load(entity.picUrl).into(holder.thumbnail);
			holder.thumbnail.setVisibility(View.VISIBLE);
		} else {
			holder.thumbnail.setVisibility(View.GONE);
		}
		holder.title.setText(entity.name);
		holder.date.setText(entity.date);
		
		return convertView;
	}
	
	public class ViewHolder {
		NewsEntity entity;
		ImageView thumbnail;
		TextView title;
		TextView date;
    }
}