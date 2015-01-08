package org.coursera.capstone.client.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class UpdateScheduler extends BroadcastReceiver {

	Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		context.sendBroadcast(new Intent("update_content"));
		//((IServiceUpdatable)context).updateContent();
		Log.d("service", "update");
		
	}

}
