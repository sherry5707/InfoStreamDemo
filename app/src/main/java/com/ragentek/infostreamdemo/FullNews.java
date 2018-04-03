package com.ragentek.infostreamdemo;

import com.ragentek.infostreamdemo.swipeback.SwipeBackActivity;
import com.ragentek.infostreamdemo.swipeback.SwipeBackLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


@SuppressLint("SetJavaScriptEnabled")
public class FullNews extends SwipeBackActivity {
	private WebView webview;
	private WebChromeClient chromeClient;

	private RelativeLayout top;

	private SwipeBackLayout mSwipeBackLayout;

	private BroadcastReceiver receiver;
	private LinearLayout refresh;
	private String refresh_url;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullnews);

		refresh=(LinearLayout)findViewById(R.id.nonet);

		mSwipeBackLayout = getSwipeBackLayout();
		//设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		//设置可以滑动的区域，推荐用屏幕像素的一半来指定
		mSwipeBackLayout.setEdgeSize(200);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window Mywindow = this.getWindow();
			Mywindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			Mywindow.setStatusBarColor(0xFFC4C4C4);
		}

		Bundle extras = getIntent().getExtras();
		final String url = extras.getString("url");
		refresh_url=url;
		refresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webview.reload();
			}
		});
		if(isNetworkConnected(FullNews.this)){
			refresh.setVisibility(View.GONE);
		}
		webview= (WebView) findViewById(R.id.webView);
		findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

		findViewById(R.id.buttonShare).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("text/plain"); // 分享的数据类型
				share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post"); // 分享的主题
				share.putExtra(Intent.EXTRA_TEXT, url); // 分享的内容

				startActivity(Intent.createChooser(share, "分享到")); // 选择对话框的标题
			}
		});

		top=(RelativeLayout)findViewById(R.id.top);

		final ProgressDialog progress = ProgressDialog.show(this, "Loading", "Please wait...", true, false);
		WebSettings settings = webview.getSettings();
		settings.setTextZoom(settings.getTextZoom() + 30);

		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);

		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(isNetworkConnected(FullNews.this)){
					Log.e("shouldOverrideUrlLoad"," ");

					Log.e("url", url);
					if (url.startsWith("http://")||url.startsWith("https://")) {
					/*String str="http://m.toutiao.com";
					String newUrl=url.replace(str, "http://open.toutiao.com");*/
						view.loadUrl(url);
					} else {
						try {
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.setData(Uri.parse(url));
							startActivity(intent);
							finish();
						} catch (Exception e) {
							Log.e("exception", e.getMessage()+e);
						}

					}
				}else {
					Toast.makeText(FullNews.this,"没联网啊",Toast.LENGTH_LONG);
				}
				refresh.setVisibility(View.GONE);
				return true;
			}

			public void onPageFinished(WebView view, String url) {
				if (FullNews.this != null && !FullNews.this.isFinishing()) {
					progress.dismiss();
				}
				/*webview.evaluateJavascript(
						"function deleteNode(name){try {var node = document.getElementsByClassName(name)[0].parentNode.parentNode; node.parentNode.removeChild(node);} catch(err){}} deleteNode('logo'); deleteNode('bonus-close');",
						new ValueCallback<String>() {
					@Override
					public void onReceiveValue(String s) {
					}
				});
				 *//**这个是为了加载网页内容*//*
                view.loadUrl("javascript:window.local_obj.showSource(document.getElementsByTagName('article')[0].innerText);"); */
				Log.e("onpagefinish","");
				super.onPageFinished(view, url);
			}

			public void onReceivedError(final WebView view, int errorCode, String description, String failingUrl) {
				//这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
				Log.e("errorCoed",errorCode+"");
				refresh.setVisibility(View.VISIBLE);
			}
		});

		webview.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				if (myCallback != null) {
					onHideCustomView();
					myCallback = null;
					return;
				}

				//Add the custom view to the view hierarchy
				FrameLayout decor=(FrameLayout) getWindow().getDecorView();
				myView = view;
				decor.addView(myView,new FrameLayout.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				//Stash the custom view callback
				myCallback = callback;

				chromeClient = this;

				// 分享栏隐藏
				top.setVisibility(View.GONE);
				hideSystemUI();
				//设置横屏
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				// 设置全屏
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);

				super.onShowCustomView(view, callback);
			}

			private View myView = null;
			private CustomViewCallback myCallback = null;

			@Override
			public void onHideCustomView() {
				long id = Thread.currentThread().getId();
				Log.v("WidgetChromeClient", "rong debug in hideCustom Ex: " + id);

				if (myView != null) {

					if (myCallback != null) {
						myCallback.onCustomViewHidden();
						myCallback = null;
					}
					//Remove the custom view
					FrameLayout decor=(FrameLayout)getWindow().getDecorView();
					decor.removeView(myView);
					myView = null;
				}

				// 分享栏显示
				top.setVisibility(View.VISIBLE);
				showSystemUI();

				// 设置竖屏
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

				super.onHideCustomView();
			}



		});

		webview.loadUrl(url);
	}

	private void hideSystemUI() {
		View decorView = getWindow().getDecorView();

		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

	}

	private void showSystemUI() {
		View decorView = getWindow().getDecorView();
		//显示navigationbar
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		//显示statusbar
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}

	// 网络状态
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);	//注意上下顺序不能变！！
	}

	@Override
	public void finish() {
		super.finish();
		try {
			webview.getClass().getMethod("onPause").invoke(webview,(Object[])null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
