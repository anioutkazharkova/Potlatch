package org.coursera.capstone.gift.repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.google.common.base.Objects;

@DynamoDBTable(tableName = "Chain")
public class GiftChainEntity {

	
	private String id;
	private int giftCount;
	private String name;
	
	@DynamoDBHashKey
	@DynamoDBAutoGeneratedKey
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

	@DynamoDBAttribute
	public int getGiftCount() {
		return this.giftCount;
	}

	public void setGiftCount(int count) {
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
	@DynamoDBAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
