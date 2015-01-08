package org.coursera.capstone.client.core.entities;


import com.google.common.base.Objects;


public class CommentEntity {

	
	private String id;
	private String userToken;
	private String giftId;
	private int isTouched;
	private int isObscene;
	private int isInappropriate;
	
	public CommentEntity(){}
	public CommentEntity(String token,String giftId)
	{
		super();
		this.userToken=token;
		this.giftId=giftId;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	
	public int getIsTouched() {
		return isTouched;
	}

	public void setIsTouched(int isTouched) {
		this.isTouched = isTouched;
	}

	
	public int getIsObscene() {
		return isObscene;
	}

	public void setIsObscene(int isObscene) {
		this.isObscene = isObscene;
	}

	
	public int getIsInappropriate() {
		return isInappropriate;
	}

	public void setIsInappropriate(int isInappropriate) {
		this.isInappropriate = isInappropriate;
	}
	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(userToken,giftId);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CommentEntity) {
			CommentEntity other = (CommentEntity) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(userToken, other.userToken)
					&& giftId==other.giftId ;
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
	
}
