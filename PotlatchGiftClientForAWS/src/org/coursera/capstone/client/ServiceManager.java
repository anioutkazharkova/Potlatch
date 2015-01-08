package org.coursera.capstone.client;

import org.coursera.capstone.client.core.GiftEntitySvcApi;
import org.coursera.capstone.client.core.GiftSvc;
import org.coursera.capstone.client.core.ImageEntitySvcApi;
import org.coursera.capstone.client.core.ImageSvc;
import org.coursera.capstone.client.core.UserEntitySvcApi;
import org.coursera.capstone.client.core.UserSvc;
import org.coursera.capstone.client.entities.User;

public class ServiceManager {

	private static ServiceManager instance;
	
	private ServiceManager()
	{
		
	}
	public static ServiceManager getInstance()
	{
		if (instance==null)
		{
			instance=new ServiceManager();
		}
		return instance;
	}
	public static UserEntitySvcApi getUserService(User user)
	{
		return UserSvc.init(user);
	}
	
	public static GiftEntitySvcApi getGiftService(User user)
	{
		return GiftSvc.init(user);
	}
	public static ImageEntitySvcApi getImageService(User user)
	{
		return ImageSvc.init(user);
	}
}
