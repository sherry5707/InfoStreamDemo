package com.ragentek.infostreamdemo.baiduresponse;

import java.util.List;

/**
 * Created by dell on 2017/12/15.
 */

public class BaiduNewsItem extends BaiduItem{
    public List<String> images;
    public String source;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("NewsItem{")
                .append("id=" + id)
                .append(", title=" + title)
                .append(", images=" + images)
                .append(", updateTime=" + updateTime)
                .append(", isTop=" + isTop)
                .append(", recommend=" + recommend)
                .append(", detailUrl=" + detailUrl)
                .append(", catInfo=" + catInfo)
                .append(", source=" + source)
                .append("}");
        return sb.toString();
    }

    public BaiduNewsItem(String id, String title, List<String> images, String updateTime, String isTop, String recommend, String detailUrl, CatInfo catInfo, String source) {
        this.id = id;
        this.title = title;
        this.images = images;
        this.updateTime = updateTime;
        this.isTop = isTop;
        this.recommend = recommend;
        this.detailUrl = detailUrl;
        this.catInfo = catInfo;
        this.source = source;
    }
}

