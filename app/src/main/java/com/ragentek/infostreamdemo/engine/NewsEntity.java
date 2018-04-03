package com.ragentek.infostreamdemo.engine;

public class NewsEntity {
	public String picUrl;
	public String name;
	public String url;
	public String date;
	public String docId;
	
	public NewsEntity(String picUrl, String name, String url, String date, String docId) {
		this.picUrl = picUrl;
		this.name = name;
		this.url = url;
		this.date = date;
		this.docId = docId;
	}
}