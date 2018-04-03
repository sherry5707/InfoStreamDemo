package com.ragentek.infostreamdemo.baiduparam;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.codec.digest.DigestUtils;

import static com.ragentek.infostreamdemo.engine.BaiduEngine.TOKEN;

/**
 * Created by dell on 2017/12/13.
 */

public class TestConnectionParam extends BaseRequestParam{
    String appsid;
    String signature;
    String timestamp;
    TestData data;

    public String getSignature(String timestamp,String data){
        Log.e("TestConnectionParma","加密前:"+timestamp+TOKEN+data);
        return DigestUtils.md5Hex(timestamp+TOKEN+data);
    }

    public TestConnectionParam(TestConnectionParam testConnection) {
        this.appsid = testConnection.appsid;
        this.signature = testConnection.signature;
        this.timestamp = testConnection.timestamp;
        this.data = testConnection.data;
    }
    public TestConnectionParam(String appsid, String timestamp) {
        this.appsid = appsid;
        this.timestamp = timestamp;
        this.data = new TestData();
        this.signature = getSignature(timestamp,data.getRequestParam());
    }

    public TestConnectionParam(String appsid, String signature, String timestamp, TestData data) {
        this.appsid = appsid;
        this.signature = signature;
        this.timestamp = timestamp;
        this.data = data;
    }

    @Override
    public boolean checkValid() {
        return true;
    }

    @Override
    public String getRequestParam() {
        return new Gson().toJson(new TestConnectionParam(this));
    }


    public class TestData extends BaseRequestParam{
        String message = "waimai";

        @Override
        public boolean checkValid() {
            return true;
        }

        @Override
        public String getRequestParam() {
            return new Gson().toJson(new TestData());
        }
    }
}
