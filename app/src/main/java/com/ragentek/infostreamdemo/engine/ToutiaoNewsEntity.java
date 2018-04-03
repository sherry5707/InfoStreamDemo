package com.ragentek.infostreamdemo.engine;

public class ToutiaoNewsEntity extends NewsEntity {
	public String source;
	public long ts;
	public String adId;
	public String adExtra;
	public long showAt;
	public int x;
	public int y;
	public String pic1Url;
	public String pic2Url;
	public String pic3Url;
	public String picBigUrl;
	
	public ToutiaoNewsEntity(String picUrl, String name, String url, String date, String docId, String source, long ts) {
		super(picUrl, name, url, date, docId);
		this.source = source;
		this.ts = ts;
		this.adId = null;
		this.adExtra = null;
		this.showAt = 0;
		this.x = 0;
		this.y = 0;
		this.pic1Url = null;
		this.pic2Url = null;
		this.pic3Url = null;
		this.picBigUrl = null;
	}
	
	public void setBigPicInfo(String picBigUrl) {
		this.picBigUrl = picBigUrl;
	}
	
	public void set3PicInfo(String pic1Url, String pic2Url, String pic3Url) {
		this.pic1Url = pic1Url;
		this.pic2Url = pic2Url;
		this.pic3Url = pic3Url;
	}
	
	public void set1PicInfo(String picUrl) {
		this.picUrl = picUrl;
	}

	public void setAdInfo(String adId, String adExtra) {
		this.adId = adId;
		this.adExtra = adExtra;
	}
}