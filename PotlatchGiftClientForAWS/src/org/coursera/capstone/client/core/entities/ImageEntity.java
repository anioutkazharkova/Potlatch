package org.coursera.capstone.client.core.entities;


import com.google.common.base.Objects;


public class ImageEntity {

	
	private String id;
	
	private String name;
	private String url;
	private String giftId;
	
	private String contentType;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public ImageEntity(){}
	public ImageEntity(String name, String url,String giftId) {
		super();
		this.name = name;
		this.url = url;
		this.giftId=giftId;
		
	}
	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url,giftId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ImageEntity) {
			ImageEntity other = (ImageEntity) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url);
		} else {
			return false;
		}
	}
	
	public String getGiftId() {
		return giftId;
	}

	public void setGiftId(String giftId) {
		this.giftId = giftId;
	}
	
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
