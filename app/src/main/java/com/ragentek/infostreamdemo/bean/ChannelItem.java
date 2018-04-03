package com.ragentek.infostreamdemo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class ChannelItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6465237897027410019L;
	/** 
	 * 栏目对应ID
	 *  */ 
	public Integer id;
	/** 
	 * 栏目对应NAME
	 *  */
	public String name;
	/** 
	 * 栏目在整体中的排序顺序  rank
	 *  */
	public Integer orderId;
	/** 
	 * 栏目是否选中
	 *  */
	public Integer selected;
	/**
	 * 栏目对应频道
	 */
	//public String i_channel;
	private static Map<Integer, String> id_channel;
	static{
		id_channel=new HashMap<Integer, String>();
		id_channel.put(1, "__all__");
		id_channel.put(2, "news_hot");
		id_channel.put(3, "news_society");
		id_channel.put(4, "news_health");
		id_channel.put(5, "news_food");
		id_channel.put(6, "video");
		id_channel.put(7, "news_entertainment");
		id_channel.put(8, "news_tech");
		id_channel.put(9, "news_sports");
		id_channel.put(10, "news_military");
		id_channel.put(11, "news_finance");
		id_channel.put(12, "news_car");
		id_channel.put(13, "news_pet");
		id_channel.put(14, "news_culture"); 
		id_channel.put(15, "news_world");
		id_channel.put(16, "news_fashion");
		id_channel.put(17, "news_game");
		id_channel.put(18, "news_travel");
		id_channel.put(19, "news_history");
		id_channel.put(20, "news_discovery");
		id_channel.put(21, "news_regimen");
		id_channel.put(22, "news_baby");
		id_channel.put(23, "news_story");
		id_channel.put(24, "news_essay");

	}

	public ChannelItem() {
	}

	public ChannelItem(int id, String name, int orderId,int selected) {
		this.id = Integer.valueOf(id);
		this.name = name;
		this.orderId = Integer.valueOf(orderId);
		this.selected = Integer.valueOf(selected);
	}
	
	public int getId() {
		return this.id.intValue();
	}

	public String getName() {
		return this.name;
	}

	public int getOrderId() {
		return this.orderId.intValue();
	}

	public Integer getSelected() {
		return this.selected;
	}
	
	/**
	 * 
	 * @return channel的id
	 */
	public String getChannelById(){
		return id_channel.get(id);
	}
	
	

/*	public String getChannel() {
		return i_channel;
	}

	public void setChannel(String channel) {
		this.i_channel = channel;
	}*/

	public void setId(int paramInt) {
		this.id = Integer.valueOf(paramInt);
	}

	public void setName(String paramString) {
		this.name = paramString;
	}

	public void setOrderId(int paramInt) {
		this.orderId = Integer.valueOf(paramInt);
	}

	public void setSelected(Integer paramInteger) {
		this.selected = paramInteger;
	}

	public String toString() {
		return "ChannelItem [id=" + this.id + ", name=" + this.name
				+ ", selected=" + this.selected +"]";
	}
}