package org.coursera.capstone.client;

import org.coursera.capstone.client.entities.User;

public class UserManager {

private static UserManager instance;
private User anonymousUser;
private User authUser;
private boolean isAuthorized;

private UserManager(){
	anonymousUser=new User("tempuser","temp");
	isAuthorized=false;
	authUser=null;
}

public static UserManager getInstance(){
	if (instance==null)
	{
		instance=new UserManager();
		
	}
	return instance;
}

public User getAnonymousUser() {
	return anonymousUser;
}

public void setAnonymousUser(User anonymousUser) {
	this.anonymousUser = anonymousUser;
}

public User getAuthUser() {
	return authUser;
}

public void setAuthUser(User authUser) {
	this.authUser = authUser;
}

public boolean isAuthorized() {
	return isAuthorized;
}

public void setAuthorized(boolean isAuthorized) {
	this.isAuthorized = isAuthorized;
}
public void Authorize(User user)
{
	if (!user.getName().equals(anonymousUser.getName()))
	{
		//Write to database
		authUser=user;
		setAuthorized(true);
	}
}
public void UnAuthorize()
{
	authUser=null;
	setAuthorized(false);
}
}
