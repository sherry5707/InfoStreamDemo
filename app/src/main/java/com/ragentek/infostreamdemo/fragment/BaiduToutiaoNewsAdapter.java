package com.ragentek.infostreamdemo.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
import com.ragentek.infostreamdemo.engine.ToutiaoEngine;
import com.ragentek.infostreamdemo.engine.ToutiaoNewsEntity;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BaiduToutiaoNewsAdapter extends BaiduNewsAdapter {
    private static final String TAG = "BaiduToutiaoNewsAdapter";
    private int bigpicHeight = 0;
    private int normallHeight = 0;
    private Activity activity;

    public BaiduToutiaoNewsAdapter(Context context, BaiduEngine engine, String channel) {
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
            final BaiduItem entity = ((ToutiaoViewHolder) v.getTag()).entity;
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //entity.x = x;
                    //entity.y = y;
                    break;
                case MotionEvent.ACTION_UP:
                    Intent intent = new Intent(context, FullNews.class);
                    intent.putExtra("url", entity.detailUrl);
                    context.startActivity(intent);
                    if (activity != null) {
                        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            /*if (entity.adId == null) {
								((BaiduEngine) engine).reportEvent(entity);
							} else {
								((BaiduEngine) engine).reportAd(entity, "click");
							}*/
                        }
                    }).start();
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        }

        ;
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

    // strTime要转换的String类型的时间
    // formatType时间格式
    // strTime的时间格式和formatType的时间格式必须相同
    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    // date要转换的date类型的时间
    public static long dateToLong(Date date) {
        return date.getTime();
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
        final BaiduItem entity = newsList.get(position);
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
            holder.pics.add(holder.pic1);
            holder.pics.add(holder.pic2);
            holder.pics.add(holder.pic3);
            holder.ad = (ImageView) convertView.findViewById(R.id.ad);
            holder.source = (TextView) convertView.findViewById(R.id.source);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            convertView.setTag(holder);
            convertView.setOnTouchListener(itemTouchListener);
        } else {
            holder = (ToutiaoViewHolder) convertView.getTag();
            holder.entity = entity;
        }
        if ("news".equals(entity.type)) {
            holder.title.setText(entity.title);
            if(((BaiduNewsItem) entity).images.size() == 1){
                setImageView(holder.pic, ((BaiduNewsItem) entity).images.get(0));
                setImageView(holder.bigpic, null);
                setImageView(holder.pic1, null);
                setImageView(holder.pic2, null);
                setImageView(holder.pic3, null);
            }else {
                setImageView(holder.pic, null);
                setImageView(holder.bigpic, null);
                for (int i = 0; i < ((BaiduNewsItem) entity).images.size(); i++) {
                    setImageView(holder.pics.get(i), ((BaiduNewsItem) entity).images.get(i));
                }
            }
            holder.source.setText(((BaiduNewsItem) entity).source);
        } else if ("image".equals(entity.type)) {
            String preUrl = "https:" + ((BaiduImageItem) entity).imageList.get(0).imageUrl;
            setImageView(holder.pic, preUrl);
            holder.title.setText(entity.title);
            for (int i = 0; i < ((BaiduImageItem) entity).smallImageList.size() - 1; i++) {
                String url = "https:" + ((BaiduImageItem) entity).smallImageList.get(i).imageUrl;
                setImageView(holder.pics.get(i), url);
            }
            setImageView(holder.bigpic, ((BaiduImageItem)entity).smallImageList.get(0).imageUrl);
            holder.source.setText(((BaiduImageItem) entity).source.name);
        } else if ("video".equals(entity.type)) {
            String url = "https:" + ((BaiduVedioItem) entity).thumbUrl;
            setImageView(holder.pic, url);
            setImageView(holder.bigpic, null);
            setImageView(holder.pic1, null);
            setImageView(holder.pic2, null);
            setImageView(holder.pic3, null);
            holder.title.setText(entity.title);
            holder.source.setText(((BaiduVedioItem)entity).source.name);
        }
        try {
            holder.date.setText(formatDate(stringToLong(entity.updateTime, "yyyy-MM-dd HH:mm:ss")));
        } catch (Exception e) {
            e.printStackTrace();
        }

/*		if (entity.adId != null && entity.showAt == 0) {
			entity.showAt = System.currentTimeMillis();
			new Thread(new Runnable(){
				@Override
				public void run() {
					((ToutiaoEngine) engine).reportAd(entity, "show");
			    }
			}).start();
		}*/

        return convertView;
    }

    public class ToutiaoViewHolder {
        BaiduItem entity;
        ImageView pic;
        TextView title;
        ImageView bigpic;
        ImageView pic1;
        ImageView pic2;
        ImageView pic3;
        ImageView ad;
        TextView source;
        TextView date;
        List<ImageView> pics = new ArrayList<>(3);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
