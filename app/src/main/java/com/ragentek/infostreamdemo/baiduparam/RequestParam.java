package com.ragentek.infostreamdemo.baiduparam;

import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;

import static com.ragentek.infostreamdemo.engine.BaiduEngine.APPSID;
import static com.ragentek.infostreamdemo.engine.BaiduEngine.TOKEN;
import static com.ragentek.infostreamdemo.engine.BaiduEngine.getTimeStamp;

/**
 * Created by dell on 2017/12/13.
 */

public class RequestParam extends BaseRequestParam {
    String appsid;
    String timestamp;
    RequestData data;
    String signature;

    /**
     * @param timestamp
     * @param data
     * @return
     */
    public String getSignature(String timestamp, String data) {
        return DigestUtils.md5Hex(timestamp + TOKEN + data);
    }

    public RequestParam(String channel) {
        this.appsid = APPSID;
        this.timestamp = getTimeStamp();
        this.data = new RequestData(channel);
        this.signature = getSignature(timestamp, data.getRequestParam());
    }

    public RequestParam(RequestParam requestParam) {
        this.appsid = requestParam.appsid;
        this.timestamp = requestParam.timestamp;
        this.data = requestParam.data;
        this.signature = requestParam.signature;
    }

    @Override
    public boolean checkValid() {
        return true;
    }

    @Override
    public String getRequestParam() {
        RequestParam requestParam = new RequestParam(RequestParam.this);
        return new Gson().toJson(requestParam);
    }
}
