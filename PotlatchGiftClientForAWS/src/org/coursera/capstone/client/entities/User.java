package org.coursera.capstone.client.entities;

import org.coursera.capstone.client.core.Settings;

public class User {
	public enum UserRole{USER,ANONYMOUS};
	private String password;
	private String name;
	private UserRole role;
	private String userToken;
	private int giftCounts;
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role = role;
	}

	public User(){}
	public User(String name,String pass){
		this.name=name;
		this.password=pass;
		if (name.equals(Settings.anonymous))
		{
			this.role=UserRole.ANONYMOUS;
		}
		else
			this.role=UserRole.USER;
	}
	public int getGiftCounts() {
		return giftCounts;
	}
	public void setGiftCounts(int giftCounts) {
		this.giftCounts = giftCounts;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
}
