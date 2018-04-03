package com.ragentek.infostreamdemo.baiduresponse;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by dell on 2017/12/15.
 */

public class BaiduResponse {
    private static final String TAG = "BaiduResponse";
    private BaseResponse baseResponse;
    private String baiduid;
    private String contentInfo;
    private List<Advertisement> ads;
    private List<ResponseItem> items;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("BaiduResponse{")
                .append("baseResponse=" + baseResponse)
                .append(", baiduid=" + baiduid)
                .append(", contentInfo=" + contentInfo)
                .append(", ads=" + ads)
                .append(", items=" + items)
                .append("}");
        return sb.toString();
    }

    public List<ResponseItem> getResponseItems() {
        return items;
    }

    public class BaseResponse {
        int code;
        String msg;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("BaseResponse{")
                    .append("code=" + code)
                    .append(", msg=" + msg)
                    .append("}");
            return sb.toString();
        }
    }

    public class Advertisement {
        String width;
        String height;
    }

    public class ResponseItem {
        private String type;
        private String data;
        private String commentCounts;

        private BaiduItem newsItem;

        public BaiduItem getNewsItem() {
            if("news".equals(type)) {
                newsItem = new Gson().fromJson(data, BaiduNewsItem.class);
            }else if("image".equals(type)){
                newsItem = new Gson().fromJson(data, BaiduImageItem.class);
            }else if("video".equals(type)){
                newsItem = new Gson().fromJson(data, BaiduVedioItem.class);
            }
            newsItem.type = type;
            return newsItem;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("ResponseItem{")
                    .append("type=" + type)
                    .append(", data=" + data)
                    .append(", commentCounts=" + commentCounts)
                    .append("}");
            return sb.toString();
        }
    }

}
