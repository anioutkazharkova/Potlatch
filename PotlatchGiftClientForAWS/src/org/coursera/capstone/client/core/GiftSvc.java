package org.coursera.capstone.client.core;

import org.coursera.capstone.client.entities.User;
import org.coursera.capstone.client.oauth.SecuredRestBuilder;
import org.coursera.capstone.client.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;

public class GiftSvc {

	public static final String CLIENT_ID = "mobile";

	private static GiftEntitySvcApi giftSvc;	
	private static String server=Settings.SERVER;

	
	public static synchronized GiftEntitySvcApi init(User userData) {
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

giftSvc =builder.build().create(GiftEntitySvcApi.class);
return giftSvc;
	}
}
