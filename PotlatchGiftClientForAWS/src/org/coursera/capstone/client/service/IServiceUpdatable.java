package org.coursera.capstone.client.service;


import android.content.Context;
public interface IServiceUpdatable {

	void updateContent();
	void startUpdateService(Context context);
	void stopUpdateService(Context context);
}
