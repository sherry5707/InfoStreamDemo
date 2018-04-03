package com.ragentek.infostreamdemo.bean;

import android.database.SQLException;
import android.util.Log;

import com.ragentek.infostreamdemo.db.ChannelDao;
import com.ragentek.infostreamdemo.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaiduChannelManage {
    public static BaiduChannelManage channelManage;
    /**
     * 默认的用户选择频道列表
     */
    public static List<BaiduChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    public static List<BaiduChannelItem> defaultOtherChannels;
    private ChannelDao channelDao;
    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    static {
        defaultUserChannels = new ArrayList<BaiduChannelItem>();
        defaultOtherChannels = new ArrayList<BaiduChannelItem>();

        defaultUserChannels.add(new BaiduChannelItem(1, "娱乐", 1, 1));
        defaultUserChannels.add(new BaiduChannelItem(2, "体育", 2, 1));
        defaultUserChannels.add(new BaiduChannelItem(3, "图片", 3, 1));
        defaultUserChannels.add(new BaiduChannelItem(4, "IT", 4, 1));
        defaultUserChannels.add(new BaiduChannelItem(5, "手机", 5, 1));
        defaultUserChannels.add(new BaiduChannelItem(6, "财经", 6, 1));
        defaultUserChannels.add(new BaiduChannelItem(7, "汽车", 7, 1));
        defaultUserChannels.add(new BaiduChannelItem(8, "房产", 8, 1));

        defaultUserChannels.add(new BaiduChannelItem(9, "时尚", 1, 0));
        defaultUserChannels.add(new BaiduChannelItem(10, "文化", 2, 0));
        defaultOtherChannels.add(new BaiduChannelItem(11, "军事", 3, 0));
        defaultOtherChannels.add(new BaiduChannelItem(12, "科技", 4, 0));
        defaultOtherChannels.add(new BaiduChannelItem(13, "健康", 5, 0));
        defaultOtherChannels.add(new BaiduChannelItem(14, "母婴", 6, 0));
        defaultOtherChannels.add(new BaiduChannelItem(15, "社会", 7, 0));
        defaultOtherChannels.add(new BaiduChannelItem(16, "美食", 8, 0));
        defaultOtherChannels.add(new BaiduChannelItem(17, "家居", 9, 0));
        defaultOtherChannels.add(new BaiduChannelItem(18, "游戏", 10, 0));
        defaultOtherChannels.add(new BaiduChannelItem(19, "历史", 11, 0));
        defaultOtherChannels.add(new BaiduChannelItem(20, "时政", 12, 0));
        defaultOtherChannels.add(new BaiduChannelItem(21, "美女", 13, 0));
        defaultOtherChannels.add(new BaiduChannelItem(22, "搞笑", 14, 0));
        defaultOtherChannels.add(new BaiduChannelItem(23, "猎奇", 15, 0));
        defaultOtherChannels.add(new BaiduChannelItem(24, "旅游", 16, 0));
        defaultOtherChannels.add(new BaiduChannelItem(25, "动物", 17, 0));
        defaultOtherChannels.add(new BaiduChannelItem(26, "摄影", 18, 0));
        defaultOtherChannels.add(new BaiduChannelItem(27, "动漫", 19, 0));
        defaultOtherChannels.add(new BaiduChannelItem(28, "女人", 20, 0));
        defaultOtherChannels.add(new BaiduChannelItem(29, "生活", 21, 0));
        defaultOtherChannels.add(new BaiduChannelItem(30, "表演", 22, 0));
        defaultOtherChannels.add(new BaiduChannelItem(31, "音乐", 23, 0));
        defaultOtherChannels.add(new BaiduChannelItem(32, "影视周边", 24, 0));

    }

    private BaiduChannelManage(SQLHelper paramDBHelper) throws SQLException {
        if (channelDao == null)
            channelDao = new ChannelDao(paramDBHelper.getContext());
        // NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
        return;
    }

    /**
     * 初始化频道管理类
     *
     * @param dbHelper
     * @throws SQLException
     */
    public static BaiduChannelManage getManage(SQLHelper dbHelper) throws SQLException {
        if (channelManage == null)
            channelManage = new BaiduChannelManage(dbHelper);
        return channelManage;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        channelDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<BaiduChannelItem> getUserChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"1"});
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<BaiduChannelItem> list = new ArrayList<BaiduChannelItem>();
            for (int i = 0; i < count; i++) {
                BaiduChannelItem navigate = new BaiduChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        initDefaultChannel();
        return defaultUserChannels;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ChannelItem> getOtherChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"0"});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultOtherChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     *
     * @param userList
     */
    public void saveUserChannel(List<BaiduChannelItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            BaiduChannelItem channelItem = (BaiduChannelItem) userList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(Integer.valueOf(1));
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存其他频道到数据库
     *
     * @param otherList
     */
    public void saveOtherChannel(List<BaiduChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            BaiduChannelItem channelItem = (BaiduChannelItem) otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(Integer.valueOf(0));
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel() {
        Log.d("deleteAll", "deleteAll");
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveOtherChannel(defaultOtherChannels);
    }
}
