package com.ragentek.infostreamdemo.baiduresponse;

import java.util.List;

/**
 * Created by dell on 2017/12/21.
 */

public class BaiduImageItem extends BaiduItem{
    public List<ImageUrl> smallImageList;
    public List<ImageUrl> imageList;
    public int colImageCount;
    public Source source;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("BaiduImageItem{")
                .append("id=" + id)
                .append(", title=" + title)
                .append(", updateTime=" + updateTime)
                .append(", isTop=" + isTop)
                .append(", recommend=" + recommend)
                .append(", type=" + type)
                .append(", catInfo=" + catInfo)
                .append(", smallImageList=" + smallImageList)
                .append(", imageList=" + imageList)
                .append(", detailUrl=" + detailUrl)
                .append(", source=" + source)
                .append(", colImageCount=" + colImageCount)
                .append("}");
        return sb.toString();
    }

    public class ImageUrl{
        public String imageUrl;
    }

    public class Source{
        public String name;
    }
}
