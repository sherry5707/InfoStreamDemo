package com.ragentek.infostreamdemo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/** 
 * ITEM的对应可序化队列属性
 *  */
public class BaiduChannelItem implements Serializable {
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
		id_channel.put(1, "1001");
		id_channel.put(2, "1002");
		id_channel.put(3, "1003");
		id_channel.put(4, "1004");
		id_channel.put(5, "1005");
		id_channel.put(6, "1006");
		id_channel.put(7, "1007");
		id_channel.put(8, "1008");
		id_channel.put(9, "1009");
		id_channel.put(10, "1011");
		id_channel.put(11, "1012");
		id_channel.put(12, "1013");
		id_channel.put(13, "1014");
		id_channel.put(14, "1015");
		id_channel.put(15, "1016");
		id_channel.put(16, "1017");
		id_channel.put(17, "1018");
		id_channel.put(18, "1019");
		id_channel.put(19, "1020");
		id_channel.put(20, "1021");
		id_channel.put(21, "1024");
		id_channel.put(22, "1025");
		id_channel.put(23, "1026");
		id_channel.put(24, "1027");
		id_channel.put(25, "1028");
		id_channel.put(26, "1030");
		id_channel.put(27, "1031");
		id_channel.put(28, "1032");
		id_channel.put(29, "1033");
		id_channel.put(30, "1034");
		id_channel.put(31, "1036");
		id_channel.put(32, "1037");

	}

	public BaiduChannelItem() {
	}

	public BaiduChannelItem(int id, String name, int orderId, int selected) {
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