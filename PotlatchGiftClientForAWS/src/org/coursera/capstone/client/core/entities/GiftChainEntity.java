package org.coursera.capstone.client.core.entities;


import com.google.common.base.Objects;


public class GiftChainEntity {

	
	private String id;
	private int giftCount;
	private String name;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public GiftChainEntity(){}
	
	public GiftChainEntity(String name)
	{
		super();
		this.giftCount=0;
		this.name=name;
	}

	
	public int getGiftCount() {
		return this.giftCount;
	}

	public void setCount(int count) {
		this.giftCount = count;
	}
	
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(giftCount);
	}

	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GiftChainEntity) {
			GiftChainEntity other = (GiftChainEntity) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name);
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
