package com.ragentek.infostreamdemo.fragment;

import com.ragentek.infostreamdemo.FullNews;
import com.ragentek.infostreamdemo.R;
import com.ragentek.infostreamdemo.engine.NewsEngine;
import com.ragentek.infostreamdemo.engine.ToutiaoEngine;
import com.ragentek.infostreamdemo.engine.ToutiaoNewsEntity;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ToutiaoNewsAdapter extends NewsAdapter {
	private int bigpicHeight = 0;
	private int normallHeight=0;
	private Activity activity;
	
	public ToutiaoNewsAdapter(Context context, NewsEngine engine, String channel) {
		super(context, engine, channel);
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		bigpicHeight = metrics.widthPixels * 9 / 16;
		normallHeight = metrics.widthPixels / 4;
	}

	private OnTouchListener itemTouchListener = new OnTouchListener() {
	    @SuppressLint("ClickableViewAccessibility")
		@Override
	    public boolean onTouch(View v, MotionEvent event) {
	    	final ToutiaoNewsEntity entity = ((ToutiaoViewHolder) v.getTag()).entity;
	        int x = (int)event.getX();
	        int y = (int)event.getY();
	        switch (event.getAction()) {
            	case MotionEvent.ACTION_DOWN:
            		entity.x = x;
            		entity.y = y;
            		break;
            	case MotionEvent.ACTION_UP:
    				Intent intent = new Intent(context, FullNews.class);
    				intent.putExtra("url", entity.url);
    				context.startActivity(intent);
    				if(activity!=null){
    					activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    				}
					new Thread(new Runnable() {
						@Override
						public void run() {
							if (entity.adId == null) {
								((ToutiaoEngine) engine).reportEvent(entity);
							} else {
								((ToutiaoEngine) engine).reportAd(entity, "click");
							}
						}
					}).start();
            		break;
            	case MotionEvent.ACTION_MOVE:
            		break;
	        }
	    	return true;
	    };
	};
	
	private void setImageView(ImageView view, String url) {
		if (url != null && !url.isEmpty()) {
			Picasso.with(context).load(url).into(view);
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	
	private void showImageView(ImageView view, String id) {
		if (id != null && !id.isEmpty()) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}
	
	private String formatDate(long ts) {
		long current = System.currentTimeMillis();
		long diff_sec = (current - ts) / 1000;
		long diff_min = diff_sec / 60;
		long diff_hour = diff_min / 60;
		if (diff_sec < 60) {
			return "刚刚";
		} else if (diff_min < 60) {
			return diff_min + "分钟前";
		} else if (diff_hour < 24) {
			return diff_hour + "小时前";
		} else {
			return "";
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ToutiaoViewHolder holder;
		final ToutiaoNewsEntity entity = (ToutiaoNewsEntity) newsList.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.toutiao_list_item, parent, false);
			holder = new ToutiaoViewHolder();
			holder.entity = entity;
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			holder.pic.getLayoutParams().height = normallHeight;

			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.bigpic = (ImageView) convertView.findViewById(R.id.bigpic);
			holder.bigpic.getLayoutParams().height = bigpicHeight;
			holder.pic1 = (ImageView) convertView.findViewById(R.id.pic1);
			holder.pic1.getLayoutParams().height = normallHeight;
			holder.pic2 = (ImageView) convertView.findViewById(R.id.pic2);
			holder.pic2.getLayoutParams().height = normallHeight;
			holder.pic3 = (ImageView) convertView.findViewById(R.id.pic3);
			holder.pic3.getLayoutParams().height = normallHeight;
			holder.ad = (ImageView) convertView.findViewById(R.id.ad);
			holder.source = (TextView) convertView.findViewById(R.id.source);
			holder.date = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
			convertView.setOnTouchListener(itemTouchListener);
		} else {
			holder = (ToutiaoViewHolder) convertView.getTag();
			holder.entity = entity;
		}
		
		setImageView(holder.pic, entity.picUrl);
		holder.title.setText(entity.name);
		setImageView(holder.bigpic, entity.picBigUrl);
		setImageView(holder.pic1, entity.pic1Url);
		setImageView(holder.pic2, entity.pic2Url);
		setImageView(holder.pic3, entity.pic3Url);
		showImageView(holder.ad, entity.adId);
		holder.source.setText(entity.source);
		holder.date.setText(formatDate(entity.ts));
		
		if (entity.adId != null && entity.showAt == 0) {
			entity.showAt = System.currentTimeMillis();
			new Thread(new Runnable(){
				@Override
				public void run() {
					((ToutiaoEngine) engine).reportAd(entity, "show");
			    }
			}).start();
		}
		
		return convertView;
	}

	public class ToutiaoViewHolder {
		ToutiaoNewsEntity entity;
		ImageView pic;
		TextView title;
		ImageView bigpic;
		ImageView pic1;
		ImageView pic2;
		ImageView pic3;
		ImageView ad;
		TextView source;
		TextView date;
    }
	
	public void setActivity(Activity activity){
		this.activity=activity;
	}
}
