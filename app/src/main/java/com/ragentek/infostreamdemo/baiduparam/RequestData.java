package com.ragentek.infostreamdemo.baiduparam;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.ragentek.infostreamdemo.AppApplication;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.ragentek.infostreamdemo.engine.BaiduEngine.APPSID;
import static com.ragentek.infostreamdemo.engine.BaiduEngine.getTimeStamp;

/**
 * Created by dell on 2017/12/14.
 */

public class RequestData extends BaseRequestParam{
    private static final String TAG = "RequestData";
    ContentParams contentParams;
    DeviceParam device;
    Network network;

    public RequestData(String channel){
        List<String> channelIds = new ArrayList<>();
        channelIds.add(channel);
        this.contentParams = new ContentParams("15","1","0",channelIds,getContentType(channel));
        //List<ContentTypeInfo> typeInfos = getContentTypeInfos(channel);
       // this.contentParams = new ContentParams(typeInfos,"15","1","0",channelIds);

        this.device = new DeviceParam();
        this.network = new Network();
    }

    public RequestData(ContentParams contentParams, DeviceParam device, Network network) {
        this.contentParams = contentParams;
        this.device = device;
        this.network = network;
    }
    public RequestData(RequestData data) {
        this.contentParams = data.contentParams;
        this.device = data.device;
        this.network = data.network;
    }

    @Override
    public boolean checkValid() {
        return true;
    }

    @Override
    public String getRequestParam() {
        return new Gson().toJson(new RequestData(this));
    }


    public class ContentParams {
        String pageSize;
        String pageIndex;
        String adCount;
        int contentType;
        List<String> catIds;
        //List<ContentTypeInfo> contentTypeInfos;

        private ContentParams(String pageSize, String pageIndex, String adCount, List<String> catIds) {
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.adCount = adCount;
            this.catIds = catIds;
            this.contentType = 0;
        }

        private ContentParams(String pageSize, String pageIndex, String adCount, List<String> catIds,int contentType) {
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.adCount = adCount;
            this.catIds = catIds;
            this.contentType = contentType;
        }

/*        private ContentParams( List<ContentTypeInfo> contentTypeInfos,String pageSize, String pageIndex, String adCount ,List<String> catIds) {
            this.pageSize = pageSize;
            this.pageIndex = pageIndex;
            this.adCount = adCount;
            this.contentTypeInfos = contentTypeInfos;
            this.catIds = catIds;
            this.contentType = 0;
        }*/
    }

/*    private class ContentTypeInfo{
        private String dataType;
        private String catIds;

        public ContentTypeInfo(String dataType, String catIds) {
            this.dataType = dataType;
            this.catIds = catIds;
        }
    }*/

    private class Network extends BaseRequestParam{
        String ipv4;
        int connectionType;
        int operatorType;

        public Network() {
            this.ipv4 = IPUtils.getIpAddress(AppApplication.getApp());
            this.connectionType = IPUtils.getNetworkType();
            this.operatorType = IPUtils.getOperators(AppApplication.getApp());
        }

        @Override
        public boolean checkValid() {
            return true;
        }

        @Override
        public String getRequestParam() {
            return new Gson().toJson(new Network());
        }
    }

    private class DeviceParam {
        String deviceType;
        String osType;
        String osVersion;
        String vendor;
        String model;
        Udid udid;

        public DeviceParam(){
            this.deviceType = "1";
            this.osType = "1";
            this.osVersion = Integer.toString(android.os.Build.VERSION.SDK_INT);;
            try {
                this.vendor = URLEncoder.encode(android.os.Build.MANUFACTURER,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            this.model = android.os.Build.MODEL;;
            this.udid = new Udid();
        }
        public DeviceParam(String deviceType, String osType, String osVersion, String vendor, String model, Udid udid) {
            this.deviceType = deviceType;
            this.osType = osType;
            this.osVersion = osVersion;
            this.vendor = vendor;
            this.model = model;
            this.udid = udid;
        }

        class Udid{
            String imei;
            String imeiMd5;

            public Udid(String imei, String imeiMd5) {
                this.imei = imei;
                this.imeiMd5 = imeiMd5;
            }

            @TargetApi(Build.VERSION_CODES.M)
            public Udid() {
                TelephonyManager telephonyManager = (TelephonyManager) AppApplication.getApp().getSystemService(Context.TELEPHONY_SERVICE);
                imei = telephonyManager.getDeviceId();
                Log.e(TAG,"imei:"+imei);
                if(imei!=null) {
                    imeiMd5 = stringToMD5(imei);
                }
            }

            /**
             * 将字符串转成MD5值
             * @param string 需要转换的字符串
             * @return 字符串的MD5值
             */
            public String stringToMD5(String string) {
                byte[] hash;

                try {
                    hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    return null;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return null;
                }

                StringBuilder hex = new StringBuilder(hash.length * 2);
                for (byte b : hash) {
                    if ((b & 0xFF) < 0x10)
                        hex.append("0");
                    hex.append(Integer.toHexString(b & 0xFF));
                }

                return hex.toString();
            }
        }
    }

    public int getContentType(String channel){
        if("1003".equals(channel)){
            return 1;
        }else if("1026".equals(channel)||"1034".equals(channel)||"1036".equals(channel)||"1037".equals(channel)){
            return 2;
        }else {
            return 0;
        }
    }
/*    public List<ContentTypeInfo> getContentTypeInfos(String channel){
        List<ContentTypeInfo> typeInfos = new ArrayList<>();
        if("1001".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1001"));
            typeInfos.add(new ContentTypeInfo("1","1001"));
            typeInfos.add(new ContentTypeInfo("2","1001"));
        }else if("1002".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1002"));
            typeInfos.add(new ContentTypeInfo("1","1002"));
            typeInfos.add(new ContentTypeInfo("2","1002"));
        }else if("1003".equals(channel)){
            typeInfos.add(new ContentTypeInfo("1","1003"));
        }else if("1004".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1004"));
        }else if("1005".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1005"));
        }else if("1006".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1006"));
            typeInfos.add(new ContentTypeInfo("1","1006"));
            typeInfos.add(new ContentTypeInfo("2","1006"));
        }else if("1007".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1007"));
            typeInfos.add(new ContentTypeInfo("1","1007"));
            typeInfos.add(new ContentTypeInfo("2","1007"));
        }else if("1008".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1008"));
            typeInfos.add(new ContentTypeInfo("1","1008"));
        }else if("1009".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1009"));
            typeInfos.add(new ContentTypeInfo("1","1009"));
            typeInfos.add(new ContentTypeInfo("2","1009"));
        }else if("1011".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1011"));
            typeInfos.add(new ContentTypeInfo("1","1011"));
            typeInfos.add(new ContentTypeInfo("2","1011"));
        }else if("1012".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1012"));
            typeInfos.add(new ContentTypeInfo("1","1012"));
            typeInfos.add(new ContentTypeInfo("2","1012"));
        }else if("1013".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1013"));
            typeInfos.add(new ContentTypeInfo("1","1013"));
            typeInfos.add(new ContentTypeInfo("2","1013"));
        }else if("1014".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1014"));
            typeInfos.add(new ContentTypeInfo("1","1014"));
            typeInfos.add(new ContentTypeInfo("2","1014"));
        }else if("1015".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1015"));
            typeInfos.add(new ContentTypeInfo("1","1015"));
            typeInfos.add(new ContentTypeInfo("2","1015"));
        }else if("1016".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1016"));
            typeInfos.add(new ContentTypeInfo("1","1016"));
            typeInfos.add(new ContentTypeInfo("2","1016"));
        }else if("1017".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1017"));
            typeInfos.add(new ContentTypeInfo("1","1017"));
            typeInfos.add(new ContentTypeInfo("2","1017"));
        }else if("1018".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1018"));
            typeInfos.add(new ContentTypeInfo("1","1018"));
        }else if("1019".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1019"));
            typeInfos.add(new ContentTypeInfo("1","1019"));
            typeInfos.add(new ContentTypeInfo("2","1019"));
        }else if("1020".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1020"));
            typeInfos.add(new ContentTypeInfo("1","1020"));
            typeInfos.add(new ContentTypeInfo("2","1020"));
        }else if("1021".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1021"));
            typeInfos.add(new ContentTypeInfo("1","1021"));
        }else if("1024".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1024"));
            typeInfos.add(new ContentTypeInfo("1","1024"));
        }else if("1025".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1025"));
            typeInfos.add(new ContentTypeInfo("1","1025"));
            typeInfos.add(new ContentTypeInfo("2","1025"));
        }else if("1026".equals(channel)){
            typeInfos.add(new ContentTypeInfo("2","1026"));
        }else if("1027".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1027"));
            typeInfos.add(new ContentTypeInfo("1","1027"));
            typeInfos.add(new ContentTypeInfo("2","1027"));
        }else if("1028".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1028"));
        }else if("1030".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1030"));
            typeInfos.add(new ContentTypeInfo("1","1030"));
        }else if("1031".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1031"));
            typeInfos.add(new ContentTypeInfo("1","1031"));
            typeInfos.add(new ContentTypeInfo("2","1031"));
        }else if("1032".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1032"));
            typeInfos.add(new ContentTypeInfo("1","1032"));
        }else if("1033".equals(channel)){
            typeInfos.add(new ContentTypeInfo("0","1033"));
            typeInfos.add(new ContentTypeInfo("1","1033"));
            typeInfos.add(new ContentTypeInfo("2","1033"));
        }else if("1034".equals(channel)){
            typeInfos.add(new ContentTypeInfo("2","1034"));
        }else if("1036".equals(channel)){
            typeInfos.add(new ContentTypeInfo("2","1036"));
        }else if("1037".equals(channel)){
            typeInfos.add(new ContentTypeInfo("2","1037"));
        }
        return typeInfos;
    }*/
}
