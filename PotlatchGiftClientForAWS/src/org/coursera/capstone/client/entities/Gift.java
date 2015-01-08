package org.coursera.capstone.client.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class Gift implements Parcelable {

	private String giftText;
	private String giftTitle;
	private String giftImageUrl;
	private byte[] giftImage;
	private String userToken;
	private String chainId;
	private String id;
	private boolean isParent;
	private int giftTouchCount;
	private String date;
	private double latitude;
	private double longitude;
	private String userName;
	private String address;
	
	public Gift()
	{
		this.latitude=1000;
		this.longitude=1000;
	}
	public String getText() {
		return giftText;
	}
	public void setText(String text) {
		this.giftText = text;
	}
	public String getTitle() {
		return giftTitle;
	}
	public void setTitle(String title) {
		this.giftTitle = title;
	}
	public byte[] getGiftImage() {
		return giftImage;
	}
	public void setGiftImage(byte[] giftImage) {
		this.giftImage = giftImage;
	}
	public String getChainId() {
		return chainId;
	}
	public void setChainId(String chainId) {
		this.chainId = chainId;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public int getTouchCount() {
		return giftTouchCount;
	}
	public void setTouchCount(int touchCount) {
		this.giftTouchCount = touchCount;
	}
	public String getGiftImageUrl() {
		return giftImageUrl;
	}
	public void setGiftImageUrl(String giftImageUrl) {
		this.giftImageUrl = giftImageUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(giftText);
		dest.writeString(giftTitle);
		dest.writeInt(giftImage==null?0:giftImage.length);		
		dest.writeByteArray(giftImage);
		dest.writeString(giftImageUrl);
		dest.writeString(date);
		dest.writeInt(giftTouchCount);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		dest.writeInt(isParent()?1:0);
		dest.writeString(userName);
		dest.writeString(userToken);
		dest.writeString(id);
		dest.writeString(chainId);
		dest.writeString(address);
	}
	public static final Parcelable.Creator<Gift> CREATOR
    = new Parcelable.Creator<Gift>() {
public Gift createFromParcel(Parcel in) {
    return new Gift(in);
}

public Gift[] newArray(int size) {
    return new Gift[size];
}
};

private Gift(Parcel in) {
giftText=in.readString();
giftTitle=in.readString();
giftImage=new byte[in.readInt()];
in.readByteArray(giftImage);
giftImageUrl=in.readString();
date=in.readString();
giftTouchCount=in.readInt();
latitude=in.readDouble();
longitude=in.readDouble();
isParent=(in.readInt()==1?true:false);
userName=in.readString();
userToken=in.readString();
id=in.readString();
chainId=in.readString();
address=in.readString();

}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
}
