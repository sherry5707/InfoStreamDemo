package com.ragentek.infostreamdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ragentek.infostreamdemo.R;
import com.ragentek.infostreamdemo.baiduresponse.BaiduNewsItem;
import com.ragentek.infostreamdemo.engine.BaiduEngine;
import com.ragentek.infostreamdemo.engine.NewsEngine;
import com.ragentek.infostreamdemo.engine.NewsEntity;
import com.ragentek.infostreamdemo.engine.ToutiaoEngine;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsFragment extends Fragment {
    private static final String TAG = "NewsFragment";
    private Context context = null;
    private SwipeRefreshLayout refresh_layout = null;
    private ListView listView = null;
    private NewsAdapter adapter = null;
    private NewsEngine engine = null;
    private String channel = null;

    private LinearLayout refreshlayout;
    private ImageView refresh;
    private Animation anim;

    private Activity activity;

    public NewsFragment() {
        super();
    }


    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        super.onAttach(activity);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.context = container.getContext();
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (ListView) rootView.findViewById(R.id.mListView);
        if (engine == null) {
            engine=ToutiaoEngine.GetInstance(context);
        }
        if (channel == null) {
            channel = this.getArguments().getString("channel");
        }
        adapter = engine.createNewsAdapter(channel);
        if (activity != null)
            adapter.setActivity(activity);

        TextView foot = new TextView(context);
        foot.setText("已经到底部,点击刷新按钮刷新");
        foot.setGravity(Gravity.CENTER);
        foot.setTextColor(getResources().getColor(R.color.darkgray));
        listView.addFooterView(foot);

        listView.setAdapter(adapter);
        refresh_layout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveNews();
            }
        });

        anim = AnimationUtils.loadAnimation(context, R.anim.info_rotate_refresh);
        refresh = (ImageView) rootView.findViewById(R.id.info_refresh);
        refreshlayout = (LinearLayout) rootView.findViewById(R.id.info_refresh_layout);
        refreshlayout.getBackground().setAlpha(100);
        refreshlayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                retrieveNews2();
            }
        });

        retrieveNews();
        return rootView;
    }

    public void retrieveNews() {
        refresh_layout.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<NewsEntity> newsList = engine.refreshData(adapter);
                if (newsList != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            refresh_layout.setRefreshing(false);
                            for (NewsEntity entity : newsList) {
                                adapter.addItem(entity);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                    });
                }
            }
        }).start();
    }

    public void retrieveNews2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(1);
                final ArrayList<NewsEntity> newsList = engine.refreshData(adapter);
                if (newsList != null) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            mHandler.sendEmptyMessage(2);
                            for (NewsEntity entity : newsList) {
                                adapter.addItem(entity);
                            }
                            adapter.notifyDataSetChanged();
                            listView.setSelection(0);
                        }
                    });
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                refresh.startAnimation(anim);
            }
            if (msg.what == 2) {
                refresh.clearAnimation();
            }
            super.handleMessage(msg);
        }

    };
}
