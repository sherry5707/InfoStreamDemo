package com.ragentek.infostreamdemo.baiduresponse;

/**
 * Created by dell on 2017/12/21.
 */

public class BaiduItem {
    public String id;
    public String title;
    public String updateTime;
    public String isTop;
    public String recommend;
    public String detailUrl;
    public CatInfo catInfo;
    public String type;


    public class CatInfo{
        public String id;
        public String name;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("NewsItem{")
                .append("id=" + id)
                .append(", title=" + title)
                .append(", updateTime=" + updateTime)
                .append(", isTop=" + isTop)
                .append(", recommend=" + recommend)
                .append(", type=" + type)
                .append(", detailUrl=" + detailUrl)
                .append(", catInfo=" + catInfo)
                .append("}");
        return sb.toString();
    }
}
