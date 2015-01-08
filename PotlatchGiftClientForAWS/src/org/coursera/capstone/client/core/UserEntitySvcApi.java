package org.coursera.capstone.client.core;

import java.util.Collection;

import org.coursera.capstone.client.core.entities.UserEntity;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface UserEntitySvcApi {

	@GET("/user")
	public Collection<UserEntity> getUsersList();
	
	@GET("/user" + "/{id}")
	public UserEntity getUserById(@Path("id") String id);
	
	@POST("/user")
	public UserEntity addUser(@Body UserEntity v);
	@GET("/user/auth")
	public UserEntity getAuthorities();
	
	@POST("/user/gift_counts")
	public UserEntity addGiftToCount();
	
	@GET(value="/user/top")
	public Collection<UserEntity> getUsersTopList();
	
	//@GET("/user/exist")
	//public UserEntity getUserExist(@Query("name") String name,@Query("pass")String pass);
}
