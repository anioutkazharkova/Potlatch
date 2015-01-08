package org.coursera.capstone.client.core;

import org.coursera.capstone.client.entities.User;
import org.coursera.capstone.client.oauth.SecuredRestBuilder;
import org.coursera.capstone.client.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

public class UserSvc {

	public static final String CLIENT_ID = "mobile";

	private static UserEntitySvcApi userSvc;	
	private static String server=Settings.SERVER;

	
	public static synchronized UserEntitySvcApi init(User userData) {
		String user=userData.getName();
		String pass=userData.getPassword();
SecuredRestBuilder builder= new SecuredRestBuilder()
.setLoginEndpoint(server + GiftEntitySvcApi.TOKEN_PATH)
.setUsername(user)
.setPassword(pass)
.setClientId(CLIENT_ID)
.setClient(
		new ApacheClient(new EasyHttpClient()))
.setEndpoint(server).setLogLevel(LogLevel.FULL);

userSvc =builder.build().create(UserEntitySvcApi.class);
return userSvc;
	}
}
