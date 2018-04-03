package com.ragentek.infostreamdemo.engine;

import com.ragentek.infostreamdemo.baiduresponse.BaiduItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduNewsItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduResponse;
import com.ragentek.infostreamdemo.fragment.BaiduNewsAdapter;
import com.ragentek.infostreamdemo.fragment.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsEngine {
	public abstract ArrayList<NewsEntity> refreshData(NewsAdapter adapter);
	public abstract List<BaiduItem> refreshBaiduData(BaiduNewsAdapter adapter);
	public abstract NewsAdapter createNewsAdapter(String channel);
	public abstract BaiduNewsAdapter createBaiduNewsAdapter(String channel);
}
