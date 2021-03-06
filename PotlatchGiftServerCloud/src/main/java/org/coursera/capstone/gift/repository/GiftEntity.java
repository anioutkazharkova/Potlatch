package org.coursera.capstone.gift.repository;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.common.base.Objects;

@DynamoDBTable(tableName = "Gift")
public class GiftEntity {

	
	private String id;
	private String text;
	private String title;
	private String imageUrl;
	private String userToken;
	private String userName;
	private String chainId;
	private int isParent;
	private int touchCount;
	private String date;
	private double lat;
	private double lng;
	private String address;
	
	public GiftEntity(){}
	public GiftEntity(String text,String title, String imageUrl,String userToken,String chainId)
	{
		super();
		this.text=text;
		this.title=title;
		this.imageUrl=imageUrl;
		this.userToken=userToken;
		this.chainId=chainId;
		this.lat=1000;
		this.lng=1000;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date now = new Date();
		this.date=dateFormat.format(now);
	}
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public void setText(String text)
	{
		this.text=text;
	}
	@DynamoDBAttribute
	public String getText()
	{
		return this.text;
	}
	@DynamoDBAttribute
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@DynamoDBAttribute
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@DynamoDBAttribute
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	@DynamoDBAttribute
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	@DynamoDBAttribute
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	@DynamoDBAttribute
	public int getTouchCount()
	{
		return this.touchCount;
	}
	public void setTouchCount(int touchCount)
	{
		this.touchCount=touchCount;
	}
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(title,text,imageUrl,userToken,chainId);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GiftEntity) {
			GiftEntity other = (GiftEntity) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(title, other.title)
					&& Objects.equal(text, other.text)
					&& Objects.equal(imageUrl, other.imageUrl)
					&& Objects.equal(userToken, other.userToken)
					&& chainId == other.chainId;
		} else {
			return false;
		}
	}
	@DynamoDBAttribute
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@DynamoDBAttribute
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	@DynamoDBAttribute
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	@DynamoDBAttribute
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@DynamoDBAttribute
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
