package com.ragentek.infostreamdemo.baiduresponse;

import java.util.List;

/**
 * Created by dell on 2017/12/21.
 */

public class BaiduVedioItem extends BaiduItem{
    public String content;
    public SourceInfo sourceInfo;
    public String url;
    public String presentationType;
    public String duration;
    public String thumbUrl;
    public String thumbWidth;
    public String thumbHeight;
    public Source source;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("BaiduVedioItem{")
                .append("id=" + id)
                .append(", title=" + title)
                .append(", content=" + content)
                .append(", sourceInfo=" + sourceInfo)
                .append(", updateTime=" + updateTime)
                .append(", url=" + url)
                .append(", presentationType=" + presentationType)
                .append(", duration=" + duration)
                .append(", thumbUrl=" + thumbUrl)
                .append(", thumbWidth=" + thumbWidth)
                .append(", thumbHeight=" + thumbHeight)
                .append(", detailUrl=" + detailUrl)
                .append(", catInfo=" + catInfo)
                .append(", source=" + source)
                .append("}");
        return sb.toString();
    }

    public class SourceInfo{
        String id;
        String name;
    }

    public class Source{
        public String name;
    }
}
