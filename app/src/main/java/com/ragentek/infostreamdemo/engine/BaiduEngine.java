package com.ragentek.infostreamdemo.engine;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.Gson;
import com.ragentek.infostreamdemo.baiduparam.RequestParam;
import com.ragentek.infostreamdemo.baiduresponse.BaiduItem;
import com.ragentek.infostreamdemo.baiduresponse.BaiduResponse;
import com.ragentek.infostreamdemo.fragment.BaiduNewsAdapter;
import com.ragentek.infostreamdemo.fragment.BaiduToutiaoNewsAdapter;
import com.ragentek.infostreamdemo.fragment.NewsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class BaiduEngine extends NewsEngine {
    private static final String TAG = "BaiduEngine";
    private static final String TOKEN_URL = "http://open.snssdk.com/auth/access/device/";
    private static final String STREAM_URL = "https://cpu-openapi.baidu.com/api/v2/data/list";
    public static final String APPSID = "e2efff79";
    public static final String TOKEN = "c3a5437508fd8260b4ea08e55";
    private static final String EVENT_REPORT_URL = "http://open.snssdk.com/log/app_log_for_partner/v1/";
    private static final String AD_REPORT_URL = "http://open.snssdk.com/log/app_log_for_partner/v2/";

    private TelephonyManager tm = null;
    private ConnectivityManager cm = null;
    private Context context = null;
    private SharedPreferences sharedPreferences = null;
    private String token = null;
    private long token_expires = 0;
    private long min_hot_time = 0;

    public static final int TYPE_2G = 1;
    public static final int TYPE_3G = 2;
    public static final int TYPE_4G = 3;
    public static final int TYPE_WIFI = 4;

    private static BaiduEngine m_instance;
    private String apiString = "qingchengsp_api";            // "qingchengzm_api";	 // "qingchengsuoping";
    private String keyString = "d4aa6f6cdcaff892756c4ace7952a0c2";    //"71d0721cab8aa6f1d25e46c92179d8bc";	//"63f1777241cbee8d968414638554e76b";

    private Activity activity;
    private String channel;

    public BaiduEngine(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences("news", Context.MODE_PRIVATE);
        this.tm = (TelephonyManager) this.context.getSystemService(Context.TELEPHONY_SERVICE);
        this.cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        m_instance = this;
    }

    public static BaiduEngine GetInstance(Context mContext) {
        if (m_instance == null) {
            m_instance = new BaiduEngine(mContext);
        }
        return m_instance;
    }

    @Override
    public NewsAdapter createNewsAdapter(String channel) {
        return null;
    }

    @Override
    public BaiduNewsAdapter createBaiduNewsAdapter(String channel) {
        this.channel = channel;
        BaiduToutiaoNewsAdapter baiduNewsAdapter = new BaiduToutiaoNewsAdapter(context, this, channel);
        if (activity != null) {
            baiduNewsAdapter.setActivity(activity);
        }
        return baiduNewsAdapter;
    }

    private int getNetworkType() {
        int type = TYPE_WIFI;
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            switch (info.getSubtype()) {
                case TelephonyManager.NETWORK_TYPE_LTE:
                    type = TYPE_4G;
                    break;

                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    type = TYPE_3G;
                    break;
                default:
                    type = TYPE_2G;
                    break;
            }
        }
        return type;
    }

    @SuppressWarnings("deprecation")
    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        return ip;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTimeStamp() {
        return Long.toString(System.currentTimeMillis());
    }


    @Override
    public ArrayList<NewsEntity> refreshData(NewsAdapter adapter) {
        return null;
    }

    @Override
    public List<BaiduItem> refreshBaiduData(BaiduNewsAdapter adapter) {
        StringBuffer urlBuffer = new StringBuffer(STREAM_URL);
        //TestConnectionParam testConnectionParam = new TestConnectionParam(APPSID,Long.toString(System.currentTimeMillis()));
        RequestParam requestParam = new RequestParam(adapter.getChannel());
        Log.e(TAG, "requestParma:" + requestParam.getRequestParam());
        String result = sendHttpRequest(urlBuffer.toString(), requestParam.getRequestParam().getBytes());
        Log.e(TAG, "result:" + result);
        Gson gson = new Gson();
        BaiduResponse baiduResponse = gson.fromJson(result, BaiduResponse.class);
        Log.e(TAG, "baiduResponse:" + baiduResponse);
        List<BaiduResponse.ResponseItem> responseItems = null;
        if (baiduResponse != null) {
            responseItems = baiduResponse.getResponseItems();
        }
        List<BaiduItem> newsItems = new ArrayList<>();
        if (responseItems != null) {
            for (BaiduResponse.ResponseItem item : responseItems) {
                newsItems.add(item.getNewsItem());
                Log.e(TAG, "item:" + item.toString());
            }
        } else {
            Log.e(TAG, "items is null");
        }
        return newsItems;
    }

    private long getMinHotTime() {
        if (min_hot_time == 0) {
            min_hot_time = sharedPreferences.getLong("min_hot_time", System.currentTimeMillis() / 1000 - 10);
        }
        return min_hot_time;
    }

    private void updateMinHotTime(long time) {
        if (min_hot_time < time) {
            min_hot_time = time;
            sharedPreferences.edit().putLong("min_hot_time", this.min_hot_time).commit();
        }
    }

    private String getToken() {
        if (token == null) {
            token = sharedPreferences.getString("access_token", null);
            token_expires = sharedPreferences.getLong("token_expires", 0);
        }
        if (token_expires < System.currentTimeMillis() / 1000) {
            retrieveNewToken();
        }
        return token;
    }

    private void upateToken(String token, int expires) {
        this.token = token;
        this.token_expires = System.currentTimeMillis() / 1000 + expires;
        sharedPreferences.edit().putString("access_token", this.token).putLong("token_expires", this.token_expires).commit();
    }

    final private static char[] hexArray = "0123456789abcdef".toCharArray();

    private String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String getSecureKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = String.valueOf(Math.random());
        String key = keyString;
        String[] list = new String[3];
        list[0] = timestamp;
        list[1] = nonce;
        list[2] = key;
        Arrays.sort(list);

        String text = TextUtils.join("", list);
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] textBytes = text.getBytes("iso-8859-1");
        md.update(textBytes, 0, textBytes.length);
        byte[] sha1hash = md.digest();
        String signature = bytesToHex(sha1hash);

        StringBuffer sb = new StringBuffer();
        sb.append("timestamp=").append(timestamp).append("&nonce=").append(nonce).append("&signature=").append(signature).append("&partner=").append(apiString);
        return sb.toString();
    }

    public boolean retrieveNewToken() {
        StringBuffer urlBuffer = new StringBuffer(TOKEN_URL);
        StringBuffer sb = new StringBuffer();
        try {
            urlBuffer.append("?").append(getSecureKey());
            sb.append("os=").append("Android");
            sb.append("&os_version=").append(Build.VERSION.RELEASE);
            sb.append("&os_api=").append(Build.VERSION.SDK_INT);
            sb.append("&udid=").append(tm.getDeviceId());
            sb.append("&openudid=").append(Secure.getString(context.getContentResolver(), Secure.ANDROID_ID));
            sb.append("&device_model=").append(URLEncoder.encode(Build.MODEL, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String result = sendHttpRequest(urlBuffer.toString(), sb.toString().getBytes());
        if (result == null) {
            return false;
        }

        try {
            JSONObject root = new JSONObject(result);
            if (root.getInt("ret") == 0) {
                JSONObject data = root.getJSONObject("data");
                upateToken(data.getString("access_token"), data.getInt("expires_in"));
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String sendHttpRequest(String baseUrl, byte[] data) {
        HttpURLConnection c = null;
        try {
            URL url = new URL(baseUrl);
            c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("POST");
            c.setConnectTimeout(1000);
            c.setDoInput(true);
            c.setDoOutput(true);
            c.setUseCaches(false);
            c.setRequestProperty("Content-Type", "application/json");

            c.setRequestProperty("Content-Length", String.valueOf(data.length));
            OutputStream outputStream = c.getOutputStream();
            outputStream.write(data);

            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean reportEvent(NewsEntity entity) {
        StringBuffer urlBuffer = new StringBuffer(EVENT_REPORT_URL);
        StringBuffer sb = new StringBuffer();
        StringBuffer eventBuffer = new StringBuffer();
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            eventBuffer.append("[{\"category\":\"open\",\"tag\":\"go_detail\",\"label\":\"click_headline\",\"datetime\":\"").append(timeFormat.format(new Date())).append("\",\"value\":\"").append(entity.docId).append("\"}]");
            urlBuffer.append("?").append(getSecureKey());
            urlBuffer.append("&access_token=").append(getToken());
            sb.append("events=").append(URLEncoder.encode(eventBuffer.toString(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String result = sendHttpRequest(urlBuffer.toString(), sb.toString().getBytes());
        Log.d(TAG, "events:" + eventBuffer.toString());
        Log.d(TAG, "result:" + result);
        if (result == null) {
            return false;
        }

        try {
            JSONObject root = new JSONObject(result);
            if (root.getInt("ret") == 0) {
                return true;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean reportAd(ToutiaoNewsEntity entity, String type) {
        StringBuffer urlBuffer = new StringBuffer(AD_REPORT_URL);
        StringBuffer sb = new StringBuffer();
        StringBuffer eventBuffer = new StringBuffer();
        try {
            Long current = System.currentTimeMillis();
            eventBuffer.append("[{\"category\":\"open\",\"tag\":\"embeded_ad\",\"is_ad_event\":1,\"label\":\"").append(type).append("\"")
                    .append(",\"value\":").append(entity.adId).append(",\"log_extra\":").append(entity.adExtra)
                    .append(",\"nt\":").append(getNetworkType()).append(",\"client_ip\":\"").append(getLocalIpAddress()).append("\"")
                    .append(",\"client_at\":").append(Long.toString(current / 1000))
                    .append(",\"show_time\":").append(current - entity.showAt)
                    .append(",\"dx\":").append(entity.x).append(",\"dy\":").append(entity.y).append(",\"ux\":").append(entity.x).append(",\"uy\":").append(entity.y)
                    .append("}]");
            urlBuffer.append("?").append(getSecureKey());
            urlBuffer.append("&access_token=").append(getToken());
            sb.append("ua=").append(URLEncoder.encode(System.getProperty("http.agent"), "utf-8"));
            sb.append("&pdid=").append(tm.getDeviceId());
            sb.append("&device_type=").append("android_phone");
            sb.append("&events=").append(URLEncoder.encode(eventBuffer.toString(), "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String result = sendHttpRequest(urlBuffer.toString(), sb.toString().getBytes());
        Log.d(TAG, "events:" + eventBuffer.toString());
        Log.d(TAG, "result:" + result);
        if (result == null) {
            return false;
        }

        try {
            JSONObject root = new JSONObject(result);
            if (root.getInt("ret") == 0) {
                return true;
            }
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;
    }
}
