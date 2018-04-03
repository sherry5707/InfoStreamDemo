package com.ragentek.infostreamdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ragentek.infostreamdemo.bean.BaiduChannelItem;
import com.ragentek.infostreamdemo.bean.BaiduChannelManage;
import com.ragentek.infostreamdemo.bean.ChannelItem;
import com.ragentek.infostreamdemo.bean.ChannelManage;
import com.ragentek.infostreamdemo.fragment.BaiduNewsFragment;
import com.ragentek.infostreamdemo.fragment.NewsFragment;
import com.ragentek.infostreamdemo.view.CategoryTabStrip;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 用户选择的新闻分类列表*/
	//private ArrayList<ChannelItem> userChannelList=new ArrayList<ChannelItem>();
	private ArrayList<BaiduChannelItem> userChannelList=new ArrayList<BaiduChannelItem>();
	/**fragments列表*/
	private ArrayList<Fragment> fragments=new ArrayList<Fragment>();

	
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;
	//private LinearLayout pagerTitleBar;
	
	private CategoryTabStrip tabs;
	private ImageView addTabs_icon;
	
	private BroadcastReceiver receiver;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerBroadrecevicer();
		tabs=(CategoryTabStrip)findViewById(R.id.category_strip);
		addTabs_icon=(ImageView)findViewById(R.id.icon_category);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		addTabs_icon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_channel = new  Intent(getApplicationContext(), ChannelActivity.class);
				startActivityForResult(intent_channel, CHANNELREQUEST);
				overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);			
			}
		});
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window Mywindow = this.getWindow();
			Mywindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			Mywindow.setStatusBarColor(getResources().getColor(R.color.statusbar_color));
		}

		requestAllPermissions();	
		//showNotification("Recommended to you");	
		initColumnData();
		mSectionsPagerAdapter=new SectionsPagerAdapter(getSupportFragmentManager(), this);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		tabs.setViewPager(mViewPager);
	}

	
	/** 获取Column栏目 数据*/
	private void initColumnData() {
		//userChannelList = ((ArrayList<ChannelItem>)ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
		userChannelList = ((ArrayList<BaiduChannelItem>) BaiduChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel());
		for(BaiduChannelItem i:userChannelList){
			Log.e("MainActivity","channel:"+i);
		}
	}
	
    @SuppressLint("NewApi")
	private void requestAllPermissions() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return;
		}
    	if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
    		requestPermissions(new String[] {Manifest.permission.READ_PHONE_STATE}, 0);
    	}
    }
    
	@SuppressLint("NewApi")
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 0) {
			((BaiduNewsFragment) mSectionsPagerAdapter.getItem(mViewPager.getCurrentItem())).retrieveNews();
		}
	}
	
	public void showNotification(String text) {
		NotificationManager nm = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("InfoStreamDemo");
		builder.setContentText(text);
		builder.setOngoing(true);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);  
		builder.setContentIntent(pendingIntent);  
		nm.notify(0, builder.build());  
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("onActivityResult", "");
		switch (requestCode) {
		case CHANNELREQUEST:
			if(resultCode == CHANNELRESULT){
				initColumnData();	//获取默认tab数据 
				mSectionsPagerAdapter.RefreshData();
				mViewPager.setAdapter(mSectionsPagerAdapter);
				tabs.setViewPager(mViewPager);
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
		private ArrayList<BaiduNewsFragment> fragmentList = new ArrayList<BaiduNewsFragment>();

		public SectionsPagerAdapter(FragmentManager fm, Context mContext) {
			super(fm);
			//NewsEngine engine = new ToutiaoEngine(context);
			//NewsEngine engine = ToutiaoEngine.GetInstance(context);
			for (int i=0; i<userChannelList.size(); i++) {
				BaiduNewsFragment frag=new BaiduNewsFragment();
				Bundle bundle=new Bundle();
				bundle.putString("channel", userChannelList.get(i).getChannelById());
				frag.setArguments(bundle);
				fragmentList.add(frag);
			}
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return userChannelList.get(position).getName();
		}
		
		public void RefreshData(){
			Log.e(TAG,"RefreshData");
			fragmentList.clear();
			for (int i=0; i<userChannelList.size(); i++) {
				BaiduNewsFragment frag=new BaiduNewsFragment();
				Bundle bundle=new Bundle();
				//bundle.putString("channel", channelList[i]);
				Log.e(TAG, "channel:"+userChannelList.get(i).getChannelById());
				bundle.putString("channel", userChannelList.get(i).getChannelById());
				frag.setArguments(bundle);		
				fragmentList.add(frag);
			}
			notifyDataSetChanged();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	//广播监听网络
	class NetworkChangeReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
			TelephonyManager mTelephony = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
			NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
			String type="";
			if (networkInfo != null && networkInfo.isAvailable()) {
				int netType = networkInfo.getType();
				int netSubtype = networkInfo.getSubtype();        //用来判断数据流量类型的：2g,3g什么的
				if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
																// mTelephony.isNetworkRoaming()  漫游，非漫游中
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {   //MOBILE
					if (netSubtype == TelephonyManager.NETWORK_TYPE_CDMA || netSubtype == TelephonyManager.NETWORK_TYPE_GPRS
							|| netSubtype == TelephonyManager.NETWORK_TYPE_EDGE) {
						type = "2g";
					} else if (netSubtype == TelephonyManager.NETWORK_TYPE_UMTS || netSubtype == TelephonyManager.NETWORK_TYPE_HSDPA
							|| netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_A || netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_0
							|| netSubtype == TelephonyManager.NETWORK_TYPE_EVDO_B) {
						type = "3g";
					} else if (netSubtype == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
						type = "4g";
					}

					Toast.makeText(MainActivity.this, "切换到" + type + "网络，请注意流量", Toast.LENGTH_SHORT).show();
				}
			} else if (networkInfo == null || !networkInfo.isAvailable()) {
				Toast.makeText(MainActivity.this, "没联网啊", Toast.LENGTH_SHORT).show();
			}
		}
	}


	private void registerBroadrecevicer() {
		//获取广播对象
		receiver = new NetworkChangeReceiver();
		//创建意图过滤器
		IntentFilter filter=new IntentFilter();
		//添加动作，监听网络
		filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		//filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(receiver,filter);
	}
}
