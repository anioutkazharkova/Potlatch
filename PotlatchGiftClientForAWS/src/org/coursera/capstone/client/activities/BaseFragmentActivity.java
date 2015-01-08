package org.coursera.capstone.client.activities;

import org.coursera.capstone.client.ServiceManager;
import org.coursera.capstone.client.UserManager;
import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.ImageEntitySvcApi;
import org.coursera.capstone.client.core.UserEntitySvcApi;
import org.coursera.capstone.client.entities.User;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseFragmentActivity extends FragmentActivity {

	public UserManager userManager;
	public UserEntitySvcApi userService;
	public GiftEntitySvcApi giftService;
	public ImageEntitySvcApi imageService;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
		userManager=UserManager.getInstance();
		if (!userManager.isAuthorized())
		{
			initServices(userManager.getAnonymousUser());
		}
		else
		{
			initServices(userManager.getAuthUser());
		}
		
	}
	protected void initUserService(User user)
	{
		userService=ServiceManager.getUserService(user);
	}
	
	protected void initGiftService(User user)
	{
		giftService=ServiceManager.getGiftService(user);
	}
	protected void initImageService(User user)
	{
		imageService=ServiceManager.getImageService(user);
	}
	
	public void initServices(User user)
	{
		initUserService(user);
		initImageService(user);
		initGiftService(user);
		Log.d("init", user.getName());
	}
}
