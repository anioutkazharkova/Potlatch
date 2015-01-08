package org.coursera.capstone.client.core.entities;

import java.util.UUID;


import com.google.common.base.Objects;


public class UserEntity {


	private String id;
	private String name;
	private String password;
	private String uuid;
	private int giftCounts;
	private String role;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	public void setPassword(String password)
	{
		this.password=password;
	}
	

	
	public void setGiftCounts(int count)
	{
		this.giftCounts=count;
	}

	public int getGiftCounts()
	{
		return this.giftCounts;
	}
	public void setUuid(String uuid)
	{
		this.uuid=uuid;
	}
	
	public String getUuid()
	{
		return this.uuid;
	}
	public UserEntity(){} 
	public UserEntity(String name,String password)
	{
		super();
		this.name=name;
		this.password=password;
		this.uuid=UUID.randomUUID().toString();
		if (name.equals("tempuser"))
		{
			this.role="anonymous";
		}
		else this.role="user";
	}
	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name,password);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserEntity) {
			UserEntity other = (UserEntity) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name);
		} else {
			return false;
		}
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
